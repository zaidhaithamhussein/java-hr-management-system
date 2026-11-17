package hrsystem;

import java.util.List;

public interface ReportGenerator {
	void exportMonthlyPayrollCSV(List<Payroll> payrolls, String month, String outFile);

	void printDeptSalaryTotals(List<Payroll> payrolls, List<Employee> employees, List<Department> depts, String month);

	void printTaxSummary(List<Payroll> payrolls, String month);
}