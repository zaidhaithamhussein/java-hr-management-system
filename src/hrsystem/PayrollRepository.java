package hrsystem;

import java.util.List;

public interface PayrollRepository {
	List<Payroll> loadPayrolls(String path);

	void savePayrolls(String path, List<Payroll> payrolls); 
}