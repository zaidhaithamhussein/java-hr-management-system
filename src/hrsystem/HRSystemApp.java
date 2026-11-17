package hrsystem;

import java.util.*;
import java.util.stream.Collectors;

public class HRSystemApp {

	private static final String FT_FILE = "employees_full_time.txt";
	private static final String PT_FILE = "employees_part_time.txt";
	private static final String CT_FILE = "employees_contract.txt";
	private static final String DEPT_FILE = "departments.txt";
	private static final String LEAVE_FILE = "leaves.txt";
	private static final String PAYROLL_FILE = "payroll.txt";

	public static void main(String[] args) {
		HRFacade facade = new HRFacade();
		facade.loadAll(FT_FILE, PT_FILE, CT_FILE, DEPT_FILE, LEAVE_FILE, PAYROLL_FILE);

		System.out.println("=== DATA LOADED ===");
		System.out.println("Employees (all): " + facade.allEmployees().size());
		System.out.println("Departments    : " + facade.getDepartments().size());
		System.out.println("Leaves         : " + facade.getLeaves().size());
		System.out.println("Payroll rows   : " + facade.getPayrolls().size());

		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("\n=== LOGIN MENU ===");
			System.out.println("1) Login with Employee ID");
			System.out.println("0) Exit");
			System.out.print("Choose: ");
			String c = in.nextLine().trim();
			if ("0".equals(c)) {
				System.out.println("Bye.");
				break;
			}
			if (!"1".equals(c)) {
				System.out.println("Invalid.");
				continue;
			}

			// Login
			System.out.print("Employee ID: ");
			String id = in.nextLine().trim();
			System.out.print("Password  : ");
			String pw = in.nextLine().trim();

			Employee me = facade.login(id, pw);
			if (me == null) {
				System.out.println("Invalid credentials.");
				continue;
			}
			System.out.println("Welcome, " + me.getFullName() + "!");

			String title = me.getJobTitle() == null ? "" : me.getJobTitle().toUpperCase();
			if (title.contains("HR")) {
				hrPortal(in, facade, me);
			} else if (title.contains("MANAGER")) {
				managerPortal(in, facade, me);
			} else {
				employeePortal(in, facade, me);
			}
		}
		in.close();
	}

	private static void employeePortal(Scanner in, HRFacade facade, Employee me) {
		while (true) {
			System.out.println("\n=== EMPLOYEE PORTAL (" + me.getEmployeeId() + ") ===");
			System.out.println("1) View my profile");
			System.out.println("2) Update my phone/email/password");
			System.out.println("3) View my payroll rows");
			System.out.println("4) Request a leave");
			System.out.println("0) Logout");
			System.out.print("Choose: ");
			String c = in.nextLine().trim();
			if ("0".equals(c))
				break;

			switch (c) {
			case "1" -> showMyProfile(facade, me);
			case "2" -> updateOwnContactsUI(in, facade, me);
			case "3" -> viewMyPayroll(facade, me);
			case "4" -> {
				LeaveType lt = askLeaveType(in);
				System.out.print("Start date (YYYY-MM-DD): ");
				String s = in.nextLine().trim();
				System.out.print("End date (YYYY-MM-DD)  : ");
				String e = in.nextLine().trim();
				LeaveRequest lr = facade.requestLeave(me, lt, s, e);
				System.out.println("Leave requested: " + lr.getLeaveId() + " | Status: " + lr.getApprovalStatus());
			}
			default -> System.out.println("Invalid.");
			}
		}
	}

	private static void showMyProfile(HRFacade facade, Employee me) {
		Department d = facade.getDepartments().stream().filter(x -> x.getDepartmentId() == me.getDepartmentId())
				.findFirst().orElse(null);
		System.out.println("\n--- My Profile ---");
		System.out.println("ID: " + me.getEmployeeId());
		System.out.println("Name: " + me.getFullName());
		System.out.println("Dept: " + (d == null ? me.getDepartmentId() : d.getDepartmentName()));
		System.out.println("Status: " + me.getStatus());
		System.out.println("Monthly Pay: " + me.calculateSalary());
	}

	private static void viewMyPayroll(HRFacade facade, Employee me) {
		List<Payroll> mine = facade.getPayrolls().stream().filter(p -> me.getEmployeeId().equals(p.getEmployeeId()))
				.collect(Collectors.toList());
		System.out.println("\n--- My Payroll ---");
		if (mine.isEmpty()) {
			System.out.println("(none)");
		} else {
			for (Payroll p : mine) {
				System.out.println(p.getMonth() + " | Net: " + p.getNetSalary());
			}
		}
	}

	private static LeaveType askLeaveType(Scanner in) {
		System.out.print("LeaveType (SICK/VACATION/MATERNITY/PATERNITY/UNPAID): ");
		try {
			return LeaveType.valueOf(in.nextLine().trim().toUpperCase());
		} catch (Exception ex) {
			return LeaveType.SICK;
		}
	}

	private static void hrPortal(Scanner in, HRFacade facade, Employee me) {
		while (true) {
			System.out.println("\n=== HR PORTAL ===");
			System.out.println("1) Create New Employee");
			System.out.println("2) Update Employee Dept/Status");
			System.out.println("3) Process Monthly Payroll (Strategy)");
			System.out.println("4) Promote Employee");
			System.out.println("5) Update MY phone/email/password");
			System.out.println("0) Back");
			System.out.print("Choose: ");
			String c = in.nextLine().trim();
			if ("0".equals(c))
				break;

			switch (c) {
			case "1" -> createEmployeeUI(in, facade);
			case "2" -> updateEmployeeUI(in, facade);
			case "3" -> {
				System.out.print("Month tag (e.g., Jul-25): ");
				String month = in.nextLine().trim();
				facade.processMonthlyPayrollForMonth(month);
			}
			case "4" -> promoteEmployeeUI(in, facade);
			case "5" -> updateOwnContactsUI(in, facade, me);
			default -> System.out.println("Invalid.");
			}
		}
	}

	private static void createEmployeeUI(Scanner in, HRFacade facade) {
		System.out.print("Type (FT/PT/CT): ");
		String type = in.nextLine().trim().toUpperCase();

		System.out.print("Employee ID: ");
		String id = in.nextLine().trim();

		if (facade.hasEmployee(id)) {
			System.out.println("❌ Cannot create: employee with ID '" + id + "' already exists.");
			return;
		}

		System.out.print("Full name: ");
		String fullName = in.nextLine().trim();

		System.out.print("Date of birth (YYYY-MM-DD): ");
		String dob = in.nextLine().trim();

		System.out.print("Gender (MALE/FEMALE): ");
		Gender gender = parseGender(in.nextLine().trim());

		System.out.print("Email: ");
		String email = in.nextLine().trim();

		System.out.print("Phone number: ");
		String phone = in.nextLine().trim();

		System.out.print("Hire date (YYYY-MM-DD): ");
		String hireDate = in.nextLine().trim();

		System.out.print("Job title: ");
		String jobTitle = in.nextLine().trim();

		System.out.print("Department ID (int): ");
		int deptId = parseInt(in.nextLine().trim(), 0);

		System.out.print("Status (ACTIVE/ON_LEAVE/TERMINATED): ");
		EmploymentStatus status = parseStatus(in.nextLine().trim());

		System.out.print("Last promotion date (YYYY-MM-DD or blank): ");
		String lastPromo = in.nextLine().trim();

		System.out.print("Password: ");
		String password = in.nextLine().trim();

		Employee e = null;
		switch (type) {
		case "FT" -> {
			System.out.print("Salary (double): ");
			double salary = parseDouble(in.nextLine().trim(), 0.0);
			e = new FullTimeEmployee(id, fullName, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
					lastPromo, password, salary);
		}
		case "PT" -> {
			System.out.print("Working hours per month (int): ");
			int hours = parseInt(in.nextLine().trim(), 0);
			System.out.print("Hourly rate (double): ");
			double rate = parseDouble(in.nextLine().trim(), 0.0);
			e = new PartTimeEmployee(id, fullName, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
					lastPromo, password, hours, rate);
		}
		case "CT" -> {
			System.out.print("Contract duration (months, int): ");
			int months = parseInt(in.nextLine().trim(), 1);
			System.out.print("Overall payment (double): ");
			double overall = parseDouble(in.nextLine().trim(), 0.0);
			e = new ContractEmployee(id, fullName, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
					lastPromo, password, months, overall);
		}
		default -> {
			System.out.println("Unknown type. Use FT/PT/CT.");
			return;
		}
		}

		boolean created = facade.createEmployee(e);
		if (!created) {
			System.out.println("Cannot create: employee with ID '" + id + "' already exists or input invalid.");
		} else {
			System.out.println("Employee created & saved.");
		}
	}

	private static void updateEmployeeUI(Scanner in, HRFacade facade) {
		System.out.print("Employee ID: ");
		String id = in.nextLine().trim();
		Employee e = facade.allEmployees().stream().filter(x -> x.getEmployeeId().equals(id)).findFirst().orElse(null);
		if (e == null) {
			System.out.println("Not found.");
			return;
		}

		System.out.print("New DeptId (blank=keep): ");
		String dep = in.nextLine().trim();
		if (!dep.isEmpty()) {
			try {
				e.setDepartmentId(Integer.parseInt(dep));
			} catch (NumberFormatException ex) {
				System.out.println("Invalid DeptId. Keeping current.");
			}
		}

		EmploymentStatus chosen = null;
		while (true) {
			System.out.println("New Status (blank=keep):");
			System.out.println("  1) ACTIVE");
			System.out.println("  2) ON_LEAVE");
			System.out.println("  3) TERMINATED");
			System.out.print("Choose 1-3 (or blank): ");
			String choice = in.nextLine().trim();

			if (choice.isEmpty())
				break;
			if ("1".equals(choice)) {
				chosen = EmploymentStatus.ACTIVE;
				break;
			}
			if ("2".equals(choice)) {
				chosen = EmploymentStatus.ON_LEAVE;
				break;
			}
			if ("3".equals(choice)) {
				chosen = EmploymentStatus.TERMINATED;
				break;
			}

			System.out.println("Invalid selection; please type 1, 2, 3, or blank.");
		}
		if (chosen != null)
			e.setStatus(chosen);

		facade.persistEmployee(e);

		System.out.println("Updated & saved.");
	}

	private static void promoteEmployeeUI(Scanner in, HRFacade facade) {
		System.out.print("Employee ID: ");
		String id = in.nextLine().trim();
		Employee e = facade.allEmployees().stream().filter(x -> x.getEmployeeId().equals(id)).findFirst().orElse(null);
		if (e == null) {
			System.out.println("Not found.");
			return;
		}

		System.out.print("New Job Title: ");
		String jt = in.nextLine().trim();
		System.out.print("Promotion Date (YYYY-MM-DD): ");
		String pd = in.nextLine().trim();
		facade.promoteEmployee(e, jt, pd);
		System.out.println("Promoted.");
	}

	private static void managerPortal(Scanner in, HRFacade facade, Employee me) {
		while (true) {
			System.out.println("\n=== MANAGER PORTAL ===");
			System.out.println("1) Department-wise salary totals");
			System.out.println("2) Tax summary");
			System.out.println("3) Show first 10 payroll rows");
			System.out.println("4) View PENDING leave requests");
			System.out.println("5) Decide on a leave");
			System.out.println("6) Update MY phone/email/password");
			System.out.print("0) Back\nChoose: ");
			String c = in.nextLine().trim();
			if ("0".equals(c))
				break;

			switch (c) {
			case "1" -> {
				System.out.print("Month tag (e.g., 2025-09): ");
				String m = in.nextLine().trim();
				facade.printDeptSalaryTotals(m);
			}
			case "2" -> {
				System.out.print("Month tag (e.g., 2025-09): ");
				String m = in.nextLine().trim();
				facade.printTaxSummary(m);
			}
			case "3" -> showPayrollLogs(facade);
			case "4" -> listPendingLeaves(facade);
			case "5" -> decideLeaveUI(in, facade);
			case "6" -> updateOwnContactsUI(in, facade, me);
			default -> System.out.println("Invalid.");
			}
		}
	}

	private static void showPayrollLogs(HRFacade facade) {
		System.out.println("\n--- Payroll Logs (first 10) ---");
		List<Payroll> ps = facade.getPayrolls();
		for (int i = 0; i < Math.min(10, ps.size()); i++) {
			Payroll p = ps.get(i);
			System.out.println(p.getEmployeeId() + " | " + p.getMonth() + " | Net: " + p.getNetSalary());
		}
	}

	private static void listPendingLeaves(HRFacade facade) {
		System.out.println("\n--- Pending Leave Requests ---");
		List<LeaveRequest> pending = facade.getPendingLeaves();
		if (pending.isEmpty()) {
			System.out.println("(none)");
			return;
		}
		for (LeaveRequest lr : pending) {
			System.out.printf("%s | Emp: %s | %s | %s → %s | Status=%s%n", lr.getLeaveId(), lr.getEmployeeId(),
					lr.getLeaveType(), lr.getStartDate(), lr.getEndDate(), lr.getApprovalStatus());
		}
	}

	private static void decideLeaveUI(Scanner in, HRFacade facade) {
		System.out.print("Leave ID: ");
		String id = in.nextLine().trim();

		LeaveRequest match = facade.getLeaves().stream().filter(l -> id.equals(l.getLeaveId())).findFirst()
				.orElse(null);

		if (match == null) {
			System.out.println("Not found.");
			return;
		}

		System.out.println("Current: " + match.getApprovalStatus() + " | Emp: " + match.getEmployeeId() + " | "
				+ match.getLeaveType() + " | " + match.getStartDate() + " → " + match.getEndDate());

		System.out.print("Decision (APPROVED / REJECTED / PENDING): ");
		ApprovalStatus s = parseApprovalStatus(in.nextLine());
		if (s == null) {
			System.out.println("Invalid decision. Use APPROVED/REJECTED/PENDING.");
			return;
		}

		boolean ok = facade.setLeaveStatus(id, s);
		System.out.println(ok ? ("Leave " + id + " set to " + s + " and saved.") : "Failed to update leave.");
	}

	private static void updateOwnContactsUI(Scanner in, HRFacade facade, Employee me) {
		System.out.print("New phone (blank=keep): ");
		String ph = in.nextLine().trim();
		System.out.print("New email (blank=keep): ");
		String em = in.nextLine().trim();
		System.out.print("New password (blank=keep): ");
		String pw = in.nextLine().trim();
		facade.updateMyContacts(me, ph, em, pw);
		System.out.println("Updated & saved.");
	}

	private static int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return def;
		}
	}

	private static double parseDouble(String s, double def) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return def;
		}
	}

	private static Gender parseGender(String s) {
		try {
			return Gender.valueOf(s.trim().toUpperCase());
		} catch (Exception e) {
			return Gender.MALE;
		}
	}

	private static EmploymentStatus parseStatus(String s) {
		try {
			return EmploymentStatus.valueOf(s.trim().toUpperCase());
		} catch (Exception e) {
			return EmploymentStatus.ACTIVE;
		}
	}

	private static ApprovalStatus parseApprovalStatus(String s) {
		try {
			return ApprovalStatus.valueOf(s.trim().toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}
}