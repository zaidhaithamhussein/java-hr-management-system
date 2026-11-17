package hrsystem;

import java.util.*;
import java.util.stream.Collectors;

public class HRFacade {
	private final FileHandler repo = new FileHandler();
	private final ReportService report = new ReportService();

	private List<Employee> full = new ArrayList<>(), part = new ArrayList<>(), cont = new ArrayList<>();
	private List<Department> depts = new ArrayList<>();
	private List<LeaveRequest> leaves = new ArrayList<>();
	private List<Payroll> payrolls = new ArrayList<>();
	private Map<String, Employee> byId = new HashMap<>();

	private static <T> List<T> nn(List<T> x) {
		return (x != null) ? x : new ArrayList<>();
	}

	private String ftPath, ptPath, ctPath, deptPath, leavePath, payrollPath;

	public void loadAll(String ft, String pt, String ct, String dept, String leave, String payroll) {
		this.ftPath = ft;
		this.ptPath = pt;
		this.ctPath = ct;
		this.deptPath = dept;
		this.leavePath = leave;
		this.payrollPath = payroll;

		full = nn(repo.loadFullTime(ft));
		part = nn(repo.loadPartTime(pt));
		cont = nn(repo.loadContract(ct));
		depts = nn(repo.loadDepartments(dept));
		leaves = nn(repo.loadLeaves(leave));
		payrolls = nn(repo.loadPayrolls(payroll));

		byId = allEmployees().stream()
				.collect(java.util.stream.Collectors.toMap(Employee::getEmployeeId, e -> e, (a, b) -> a));
	}

	private void rebuildIndex() {
		byId = allEmployees().stream().collect(Collectors.toMap(Employee::getEmployeeId, e -> e, (a, b) -> a));
	}

	public List<Employee> allEmployees() {
		List<Employee> all = new ArrayList<>(full.size() + part.size() + cont.size());
		all.addAll(full);
		all.addAll(part);
		all.addAll(cont);
		return all;
	}

	public Employee login(String id, String pw) {
		Employee e = byId.get(id);
		return (e != null && e.checkPassword(pw)) ? e : null;
	}

	public boolean hasEmployee(String id) {
		return id != null && byId.containsKey(id);
	}

	public void updateMyContacts(Employee e, String phone, String email, String pw) {
		if (phone != null && !phone.isEmpty())
			e.setPhoneNumber(phone);
		if (email != null && !email.isEmpty())
			e.setEmail(email);
		if (pw != null && !pw.isEmpty())
			e.setPassword(pw);
		persistEmployee(e);
	}

	public LeaveRequest requestLeave(Employee me, LeaveType type, String start, String end) {
		String id = generateLeaveId(me.getEmployeeId(), start);
		LeaveRequest lr = new LeaveRequest(id, me.getEmployeeId(), type, start, end, ApprovalStatus.PENDING, "");
		leaves.add(lr);
		repo.saveLeaves(leavePath, leaves);
		return lr;
	}

	private String generateLeaveId(String empId, String start) {
		try {
			String[] p = start.split("-");
			int y = Integer.parseInt(p[0]);
			int m = Integer.parseInt(p[1]);
			int d = Integer.parseInt(p[2]);
			return empId + "-" + y + "-" + m + "-" + d;
		} catch (Exception ex) {
			return empId + "-" + start.trim();
		}
	}

	public boolean createEmployee(Employee e) {
		if (e == null || e.getEmployeeId() == null || e.getEmployeeId().isEmpty())
			return false;
		if (hasEmployee(e.getEmployeeId()))
			return false;

		if (e instanceof FullTimeEmployee) {
			full.add(e);
			repo.saveFullTime(ftPath, full);
		} else if (e instanceof PartTimeEmployee) {
			part.add(e);
			repo.savePartTime(ptPath, part);
		} else {
			cont.add(e);
			repo.saveContract(ctPath, cont);
		}
		rebuildIndex();
		return true;
	}

	public void promoteEmployee(Employee e, String newTitle, String promoDate) {
		new FieldWriter().promote(e, newTitle, promoDate);
		persistEmployee(e);
	}

	private static class FieldWriter extends Employee {
		FieldWriter() {
			super("", "", "", Gender.MALE, "", "", "", "", 0, EmploymentStatus.ACTIVE, "", "");
		}

		void promote(Employee e, String title, String date) {
			e.jobTitle = title;
			e.lastPromotionDate = date;
		}

		@Override
		public double calculateSalary() {
			return 0;
		}
	}

	public void persistEmployee(Employee e) {
		if (e instanceof FullTimeEmployee) {
			repo.saveFullTime(ftPath, full);
		} else if (e instanceof PartTimeEmployee) {
			repo.savePartTime(ptPath, part);
		} else if (e instanceof ContractEmployee) {
			repo.saveContract(ctPath, cont);
		}
		rebuildIndex();
	}

	public void exportMonthlyPayrollCSV(String month, String outFile) {
		report.exportMonthlyPayrollCSV(payrolls, month, outFile);
	}

	public void printDeptSalaryTotals(String month) {
		report.printDeptSalaryTotals(payrolls, allEmployees(), depts, month);
	}

	public void printTaxSummary(String month) {
		report.printTaxSummary(payrolls, month);
	}

	public void processMonthlyPayrollForMonth(String monthTag) {
		System.out.println("\n[Strategy] Processing payroll for " + monthTag + " ...");

		List<Payroll> ps = (payrolls != null) ? payrolls : new ArrayList<>();
		List<Employee> all = allEmployees();

		List<Payroll> monthRows = new ArrayList<>();
		for (Payroll p : ps) {
			if (p != null && monthTag != null && monthTag.equals(p.getMonth())) {
				monthRows.add(p);
			}
		}

		if (monthRows.isEmpty()) {
			System.out.println("[Strategy] No payroll rows for " + monthTag + ".");
			System.out.println("[Strategy] Done.");
			return;
		}

		Map<String, Employee> idToEmp = new HashMap<>();
		if (all != null) {
			for (Employee e : all) {
				if (e != null && e.getEmployeeId() != null) {
					idToEmp.put(e.getEmployeeId(), e);
				}
			}
		}

		for (Payroll p : monthRows) {
			String empId = p.getEmployeeId();
			Employee e = idToEmp.get(empId);
			if (e == null) {
				System.out.println(" - " + empId + " (missing employee) skipped.");
				continue;
			}

			TaxStrategy strategy = (e instanceof FullTimeEmployee) ? new StandardTaxStrategy()
					: (e instanceof PartTimeEmployee) ? new PartTimeTaxStrategy() : new ContractTaxStrategy();

			double base = e.calculateSalary();
			double bonuses = 0.0;
			double deductions = 0.0;

			PayrollCalculator calc = new PayrollCalculator(strategy);
			double[] result = calc.compute(base, bonuses, deductions);
			double net = result[0], tax = result[1];

			System.out.printf(" - %s (%s): base=%.2f tax=%.2f net=%.2f%n", empId, e.getClass().getSimpleName(), base,
					tax, net);
		}

		System.out.println("[Strategy] Done.");
	}

	public List<LeaveRequest> getPendingLeaves() {
		List<LeaveRequest> out = new ArrayList<>();
		for (LeaveRequest lr : leaves) {
			if (lr.getApprovalStatus() == ApprovalStatus.PENDING)
				out.add(lr);
		}
		return out;
	}

	public boolean setLeaveStatus(String leaveId, ApprovalStatus status) {
		if (leaveId == null || status == null)
			return false;
		for (LeaveRequest lr : leaves) {
			if (leaveId.equals(lr.getLeaveId())) {
				lr.setApprovalStatus(status);
				repo.saveLeaves(leavePath, leaves);
				return true;
			}
		}
		return false;
	}

	public List<Department> getDepartments() {
		return depts;
	}

	public List<LeaveRequest> getLeaves() {
		return leaves;
	}

	public List<Payroll> getPayrolls() {
		return payrolls;
	}
}