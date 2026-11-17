package hrsystem;

public abstract class Employee implements Payable, Authenticatable, UpdatableContact {
	protected String employeeId;
	protected String fullName;
	protected String dateOfBirth;
	protected Gender gender;
	protected String email;
	protected String phoneNumber;
	protected String hireDate;
	protected String jobTitle;
	protected int departmentId;
	protected EmploymentStatus status;
	protected String lastPromotionDate;
	protected String password;

	public Employee(String employeeId, String fullName, String dateOfBirth, Gender gender, String email,
			String phoneNumber, String hireDate, String jobTitle, int departmentId, EmploymentStatus status,
			String lastPromotionDate, String password) {
		this.employeeId = employeeId;
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.hireDate = hireDate;
		this.jobTitle = jobTitle;
		this.departmentId = departmentId;
		this.status = status;
		this.lastPromotionDate = lastPromotionDate;
		this.password = password;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public EmploymentStatus getStatus() {
		return status;
	}

	public abstract double calculateSalary();

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setPhoneNumber(String phone) {
		this.phoneNumber = phone;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getLoginId() {
		return employeeId;
	}

	@Override
	public boolean checkPassword(String raw) {
		return password != null && password.equals(raw);
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getHireDate() {
		return hireDate;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getLastPromotionDate() {
		return lastPromotionDate;
	}

	public String getPassword() {
		return password;
	}

	public void setDepartmentId(int d) {
		this.departmentId = d;
	}

	public void setStatus(EmploymentStatus s) {
		this.status = s;
	}
}
