package hrsystem;

public class Department {
	private int departmentId;
	private String departmentName;
	private String headOfDepartmentId;
	private double budget;
	private String location;

	public Department(int departmentId, String departmentName, String headOfDepartmentId, double budget,
			String location) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.headOfDepartmentId = headOfDepartmentId;
		this.budget = budget;
		this.location = location;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public String getHeadOfDepartmentId() {
		return headOfDepartmentId;
	}

	public double getBudget() {
		return budget;
	}

	public String getLocation() {
		return location;
	}

}