package hrsystem;

public class ContractTaxStrategy implements TaxStrategy {
	private final double rate;

	public ContractTaxStrategy() {
		this(0.05);
	}

	public ContractTaxStrategy(double rate) {
		this.rate = rate;
	}

	@Override
	public double tax(double gross) {
		return Math.max(0, gross) * rate;
	}
}