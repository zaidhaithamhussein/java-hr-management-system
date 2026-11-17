package hrsystem;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService implements ReportGenerator {
	@Override
	public void exportMonthlyPayrollCSV(List<Payroll> payrolls, String month, String outFile) {
		try (PrintWriter out = new PrintWriter(outFile)) {
			out.println("employeeId,month,netSalary,taxWithheld,processedDate");
			for (Payroll p : payrolls) {
				if (p != null && month.equals(p.getMonth())) {
					out.println(String.join(",", p.getEmployeeId(), p.getMonth(), String.valueOf(p.getNetSalary()),
							String.valueOf(p.getTaxWithheld()), p.getProcessedDate()));
				}
			}
		} catch (Exception ex) {
			System.out.println("Failed to export: " + ex.getMessage());
		}
	}

	@Override
	public void printDeptSalaryTotals(List<Payroll> payrolls, List<Employee> emps, List<Department> depts,
			String month) {
		Map<String, Integer> empDept = emps.stream()
				.collect(Collectors.toMap(Employee::getEmployeeId, Employee::getDepartmentId, (a, b) -> a));
		Map<Integer, Double> totals = new HashMap<>();
		for (Payroll p : payrolls) {
			if (p != null && month.equals(p.getMonth())) {
				Integer d = empDept.get(p.getEmployeeId());
				if (d != null)
					totals.merge(d, p.getNetSalary(), Double::sum);
			}
		}
		System.out.println("\nDepartment-wise Net Salary Totals (" + month + "):");
		for (Department d : depts) {
			System.out.printf(" - %s (%d): %.2f%n", d.getDepartmentName(), d.getDepartmentId(),
					totals.getOrDefault(d.getDepartmentId(), 0.0));
		}
	}

	@Override
	public void printTaxSummary(List<Payroll> payrolls, String month) {
		double sum = payrolls.stream().filter(p -> p != null && month.equals(p.getMonth()))
				.mapToDouble(Payroll::getTaxWithheld).sum();
		System.out.println("Total Tax Withheld (" + month + "): " + String.format("%.2f", sum));
	}
}