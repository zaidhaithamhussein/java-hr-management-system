package hrsystem;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler
		implements EmployeeRepository, DepartmentRepository, LeaveRepository, PayrollRepository, LeaveDecisionService {

	@Override
	public List<Employee> loadFullTime(String path) {
		List<Employee> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t");
				list.add(EmployeeFactory.createFullTime(parts));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Employee> loadPartTime(String path) {
		List<Employee> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t");
				list.add(EmployeeFactory.createPartTime(parts));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Employee> loadContract(String path) {
		List<Employee> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t");
				list.add(EmployeeFactory.createContract(parts));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Department> loadDepartments(String path) {
		List<Department> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t");
				list.add(new Department(Integer.parseInt(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3]),
						parts[4]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<LeaveRequest> loadLeaves(String path) {
		List<LeaveRequest> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t", -1);
				list.add(new LeaveRequest(parts[0], parts[1], LeaveType.valueOf(parts[2].trim().toUpperCase()),
						parts[3], parts[4], ApprovalStatus.valueOf(parts[5].trim().toUpperCase()), parts[6]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Payroll> loadPayrolls(String path) {
		List<Payroll> list = new ArrayList<>();
		try (Scanner sc = new Scanner(new File(path))) {
			if (sc.hasNextLine())
				sc.nextLine();
			while (sc.hasNextLine()) {
				String[] parts = sc.nextLine().split("\t");
				list.add(new Payroll(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
						Double.parseDouble(parts[4]), Double.parseDouble(parts[5]), Double.parseDouble(parts[6]),
						Double.parseDouble(parts[7]), parts[8]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void setApprovalStatus(LeaveRequest lr, ApprovalStatus status) {
		lr.setApprovalStatus(status);
	}

	@Override
	public void saveFullTime(String path, List<Employee> employees) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tsalary");
			for (Employee e : employees) {
				if (e instanceof FullTimeEmployee f) {
					out.println(String.join("\t", e.getEmployeeId(), e.getFullName(), e.getDateOfBirth(),
							e.getGender().name(), e.getEmail(), e.getPhoneNumber(), e.getHireDate(), e.getJobTitle(),
							"", String.valueOf(e.getDepartmentId()), e.getStatus().name(), e.getLastPromotionDate(),
							e.getPassword(), String.valueOf(f.getSalary())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void savePartTime(String path, List<Employee> employees) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\thoursPerMonth\thourlyRate");
			for (Employee e : employees) {
				if (e instanceof PartTimeEmployee p) {
					out.println(String.join("\t", e.getEmployeeId(), e.getFullName(), e.getDateOfBirth(),
							e.getGender().name(), e.getEmail(), e.getPhoneNumber(), e.getHireDate(), e.getJobTitle(),
							"", String.valueOf(e.getDepartmentId()), e.getStatus().name(), e.getLastPromotionDate(),
							e.getPassword(), String.valueOf(p.getWorkingHoursPerMonth()),
							String.valueOf(p.getHourlyRate())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveContract(String path, List<Employee> employees) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tdurationMonths\toverallPayment");
			for (Employee e : employees) {
				if (e instanceof ContractEmployee c) {
					out.println(String.join("\t", e.getEmployeeId(), e.getFullName(), e.getDateOfBirth(),
							e.getGender().name(), e.getEmail(), e.getPhoneNumber(), e.getHireDate(), e.getJobTitle(),
							"", String.valueOf(e.getDepartmentId()), e.getStatus().name(), e.getLastPromotionDate(),
							e.getPassword(), String.valueOf(c.getDurationMonths()),
							String.valueOf(c.getOverallPayment())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveDepartments(String path, List<Department> departments) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println("departmentId\tdepartmentName\theadOfDepartmentId\tbudget\tlocation");
			for (Department d : departments) {
				out.println(String.join("\t", String.valueOf(d.getDepartmentId()), d.getDepartmentName(),
						d.getHeadOfDepartmentId(), String.valueOf(d.getBudget()), d.getLocation()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// LeaveRepository
	@Override
	public void saveLeaves(String path, List<LeaveRequest> leaves) {
		try (PrintWriter out = new PrintWriter(path)) {
			out.println("leaveId\temployeeId\tleaveType\tstartDate\tendDate\tapprovalStatus\tapproverId");
			for (LeaveRequest l : leaves) {
				out.println(String.join("\t", l.getLeaveId(), l.getEmployeeId(), l.getLeaveType().name(),
						l.getStartDate(), l.getEndDate(), l.getApprovalStatus().name(),
						l.getApproverId() == null ? "" : l.getApproverId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void savePayrolls(String path, List<Payroll> list) {
		try (java.io.PrintWriter out = new java.io.PrintWriter(path)) {
			out.println(
					"payroll_id\temployee_id\tmonth\tbase_salary\tbonuses\tdeductions\ttax_withheld\tnet_salary\tprocessed_date");
			for (Payroll p : list) {
				out.println(String.join("\t", p.getPayrollId(), p.getEmployeeId(), p.getMonth(),
						String.valueOf(p.getBaseSalary()), String.valueOf(p.getBonuses()),
						String.valueOf(p.getDeductions()), String.valueOf(p.getTaxWithheld()),
						String.valueOf(p.getNetSalary()), p.getProcessedDate()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}