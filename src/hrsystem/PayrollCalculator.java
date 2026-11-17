package hrsystem;

public class PayrollCalculator {
	private TaxStrategy taxStrategy;

	public PayrollCalculator(TaxStrategy taxStrategy) {
		this.taxStrategy = taxStrategy;
	}

	public void setTaxStrategy(TaxStrategy taxStrategy) {
		this.taxStrategy = taxStrategy;
	}

	
	public double[] compute(double base, double bonuses, double deductions) {
		double gross = Math.max(0, base + bonuses - Math.max(0, deductions));
		double tax = (taxStrategy == null) ? 0.0 : taxStrategy.tax(gross);
		double net = Math.max(0, gross - tax);
		return new double[] { net, tax };
	}
}