package hrsystem;

public class FullTimeEmployee extends Employee {
	private double salary;

	public FullTimeEmployee(String employeeId, String fullName, String dateOfBirth, Gender gender, String email,
			String phoneNumber, String hireDate, String jobTitle, int departmentId, EmploymentStatus status,
			String lastPromotionDate, String password, double salary) {
		super(employeeId, fullName, dateOfBirth, gender, email, phoneNumber, hireDate, jobTitle, departmentId, status,
				lastPromotionDate, password);
		this.salary = salary;
	}

	@Override
	public double calculateSalary() {
		return salary;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double s) {
		this.salary = s;
	}

}