package hrsystem;

public class Payroll {
	private String payrollId;
	private String employeeId;
	private String month;
	private double baseSalary;
	private double bonuses;
	private double deductions;
	private double netSalary;
	private double taxWithheld;
	private String processedDate;

	public Payroll(String payrollId, String employeeId, String month, double baseSalary, double bonuses,
			double deductions, double netSalary, double taxWithheld, String processedDate) {
		this.payrollId = payrollId;
		this.employeeId = employeeId;
		this.month = month;
		this.baseSalary = baseSalary;
		this.bonuses = bonuses;
		this.deductions = deductions;
		this.netSalary = netSalary;
		this.taxWithheld = taxWithheld;
		this.processedDate = processedDate;
	}

	public double getNetSalary() {
		return netSalary;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getMonth() {
		return month;
	}

	public String getPayrollId() {
		return payrollId;
	}

	public double getBaseSalary() {
		return baseSalary;
	}

	public double getBonuses() {
		return bonuses;
	}

	public double getDeductions() {
		return deductions;
	}

	public double getTaxWithheld() {
		return taxWithheld;
	}

	public String getProcessedDate() {
		return processedDate;
	}

}