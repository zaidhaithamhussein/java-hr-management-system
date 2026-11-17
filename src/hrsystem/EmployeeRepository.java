package hrsystem;

import java.util.List;

public interface EmployeeRepository {
	List<Employee> loadFullTime(String path);

	List<Employee> loadPartTime(String path);

	List<Employee> loadContract(String path);

	void saveFullTime(String path, List<Employee> employees);

	void savePartTime(String path, List<Employee> employees);

	void saveContract(String path, List<Employee> employees);
}