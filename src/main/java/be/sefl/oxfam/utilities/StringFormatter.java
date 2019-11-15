/*
 * Created on 29-dec-2006
 */
package be.sefl.oxfam.utilities;

import be.sefl.oxfam.constants.Constants;

/**
 * @author Flamooze
 */
public class StringFormatter {

	public static String center(String s) {
		int nbrOfSpaces = (Constants.MAX_CHARS - s.length()) / 2;
		return padd(s, s.length() + nbrOfSpaces).concat(Constants.NEWLINE);
	}

	/**
	 * Returns a String of length MAX_COUNT.
	 */
	public static String formatCount(int count) {
		return padd(count + "x ", Constants.MAX_COUNT);
	}

	/**
	 * Returns a String of length MAX_DESCRIPTION.
	 */
	public static String formatDescription(String description) {
		return add(description, Constants.MAX_DESCRIPTION);
	}

	/**
	 * Returns a String of length MAX_PRICE.
	 */
	public static String formatUnitPrice(String price) {
		return add(price, Constants.MAX_CHARS - Constants.MAX_PRICE - Constants.MAX_COUNT);
	}

	/**
	 * Returns a String of length MAX_PRICE.
	 */
	public static String formatTotalPrice(String price) {
		return padd(price, Constants.MAX_PRICE);
	}

	/**
	 * Prepends the string passed as param with spaces up to maxLength.
	 */
	private static String padd(String s, int maxLength) {
		StringBuilder builder = new StringBuilder(s);
		while (builder.length() < maxLength) {
			builder.insert(0, ' ');
		}
		return builder.toString();
	}

	/**
	 * Adds spaces to the string passed as param up to maxLength.
	 */
	private static String add(String s, int maxLength) {
		StringBuilder builder = new StringBuilder(s);
		while (builder.length() < maxLength) {
			builder.append(' ');
		}
		return builder.toString();
	}

	/**
	 * Returns a formatted line for the specified amount and it's corresponding label.
	 */
	public static String createAmountLine(double amount, String label) {
		return new StringBuilder(label)
				.append(padd(HelpMethods.toAmount(amount), Constants.MAX_CHARS - label.length()))
				.append(Constants.NEWLINE)
				.toString();
	}

	/**
	 * Returns a String of MAX_CHAR stars.</p>
	 */
	public static String createStarLine() {
		String starLine = "";
		for (int i = 0; i < Constants.MAX_CHARS; i++) {
			starLine += '*';
		}
		return starLine + Constants.NEWLINE;
	}

}
