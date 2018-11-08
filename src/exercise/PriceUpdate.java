package exercise;

import java.text.DecimalFormat;

public class PriceUpdate {

	private final String companyName;
	private final double price;

	public PriceUpdate(String companyName, double price) {
		this.companyName = companyName;
		this.price = price;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public double getPrice() {
		return this.price;
	}

	@Override
	public String toString() {
		return companyName + " - " + price;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Please implement this method

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof PriceUpdate)) {
			return false;
		}

		PriceUpdate pu = (PriceUpdate) obj;

		String otherCompanyName = pu.getCompanyName();
		if (!this.companyName.equals(otherCompanyName))
			return false;

		if (this.price - pu.getPrice() >= 0.01)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		
		// This implementation ensures that those objects 
		// that are equal have the same hash code.
		
		int charSum =0;
		char[] chArr = companyName.toCharArray();
		
		for( char c: chArr )
			charSum += (int) c;
		
		int priceDigitSum =0;
		
		DecimalFormat df = new DecimalFormat("0.00");

		String priceStr = df.format(price);
		char[] priceStrArr = priceStr.toCharArray();
		for( char c: priceStrArr ) {
			priceDigitSum+=(int)c;
		}
		
		return ((31*priceDigitSum + charSum)%(Integer.MAX_VALUE));
	}
}
