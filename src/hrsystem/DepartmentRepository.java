package hrsystem;

import java.util.List;

public interface DepartmentRepository {
	List<Department> loadDepartments(String path);

	void saveDepartments(String path, List<Department> departments);
}