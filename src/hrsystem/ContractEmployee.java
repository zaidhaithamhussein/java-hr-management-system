package hrsystem;

public class ContractEmployee extends Employee {
	private int durationMonths;
	private double overallPayment;

	public ContractEmployee(String employeeId, String fullName, String dateOfBirth, Gender gender, String email,
			String phoneNumber, String hireDate, String jobTitle, int departmentId, EmploymentStatus status,
			String lastPromotionDate, String password, int durationMonths, double overallPayment) {
		super(employeeId, fullName, dateOfBirth, gender, email, phoneNumber, hireDate, jobTitle, departmentId, status,
				lastPromotionDate, password);
		this.durationMonths = durationMonths;
		this.overallPayment = overallPayment;
	}

	@Override
	public double calculateSalary() {
		return overallPayment / durationMonths;
	}

	public int getDurationMonths() {
		return durationMonths;
	}

	public double getOverallPayment() {
		return overallPayment;
	}
}