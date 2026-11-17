package hrsystem;

public class PartTimeEmployee extends Employee {
	private int workingHoursPerMonth;
	private double hourlyRate;

	public PartTimeEmployee(String employeeId, String fullName, String dateOfBirth, Gender gender, String email,
			String phoneNumber, String hireDate, String jobTitle, int departmentId, EmploymentStatus status,
			String lastPromotionDate, String password, int workingHoursPerMonth, double hourlyRate) {
		super(employeeId, fullName, dateOfBirth, gender, email, phoneNumber, hireDate, jobTitle, departmentId, status,
				lastPromotionDate, password);
		this.workingHoursPerMonth = workingHoursPerMonth;
		this.hourlyRate = hourlyRate;
	}

	@Override
	public double calculateSalary() {
		return workingHoursPerMonth * hourlyRate;
	}

	public int getWorkingHoursPerMonth() {
		return workingHoursPerMonth;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}
}