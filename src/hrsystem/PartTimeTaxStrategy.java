package hrsystem;

public class PartTimeTaxStrategy implements TaxStrategy {
	private final double rate;

	public PartTimeTaxStrategy() {
		this(0.08);
	}

	public PartTimeTaxStrategy(double rate) {
		this.rate = rate;
	}

	@Override
	public double tax(double gross) {
		return Math.max(0, gross) * rate;
	}
}