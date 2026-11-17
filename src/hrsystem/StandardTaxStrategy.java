package hrsystem;

public class StandardTaxStrategy implements TaxStrategy {
	private final double rate;

	public StandardTaxStrategy() {
		this(0.12);
	}

	public StandardTaxStrategy(double rate) {
		this.rate = rate;
	}

	@Override
	public double tax(double gross) {
		return Math.max(0, gross) * rate;
	}
}