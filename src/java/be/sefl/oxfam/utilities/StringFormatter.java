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

		String spaces = "";
		
		for (int i=0; i<nbrOfSpaces; i++) {
			spaces += ' ';
		}
		
		return spaces + s + Constants.NEWLINE;
	}
	
	/** Returns a String of length MAX_COUNT. */
	public static String formatCount(int count) {
		String countString = String.valueOf(count) + "x ";
		
		while (countString.length() < Constants.MAX_COUNT) {
			countString = ' ' + countString;
		}
		
		return countString;
	}
	
	/** Returns a String of length MAX_DESCRIPTION. */
	public static String formatDescription(String description) {
		while (description.length() < Constants.MAX_DESCRIPTION) {
			description += ' ';
		}
		
		return description;
	}
	
	/** Returns a String of length MAX_PRICE. */
	public static String formatUnitPrice(String price) {
		while (price.length() < Constants.MAX_CHARS - Constants.MAX_PRICE - Constants.MAX_COUNT) {
			price += ' ';
		}
		
		return price;
	}
	
	/** Returns a String of length MAX_PRICE. */
	public static String formatTotalPrice(String price) {
		while (price.length() < Constants.MAX_PRICE) {
			price = ' ' + price;
		}
		
		return price;
	}
	
	/** Returns a String of length MAX_PRICE. */
	public static String formatTotalAmount(String totalAmount) {
		while (totalAmount.length() < (Constants.MAX_CHARS - Constants.LABEL_TOTAL.length())) {
			totalAmount = ' ' + totalAmount;
		}
		
		return totalAmount;
	}
	
	/** Returns a String of length MAX_PRICE. */
	public static String formatPaidAmount(String paidAmount) {
		while (paidAmount.length() < (Constants.MAX_CHARS - Constants.LABEL_PAID.length())) {
			paidAmount = ' ' + paidAmount;
		}
		
		return paidAmount;
	}
	
	/** Returns a String of length MAX_PRICE. */
	public static String formatReturnAmount(String returnAmount) {
		while (returnAmount.length() < (Constants.MAX_CHARS - Constants.LABEL_RETURN.length())) {
			returnAmount = ' ' + returnAmount;
		}
		
		return returnAmount;
	}
	
	/** Returns a String of MAX_CHAR stars.</p>
	 */
	public static String createStarLine() {
		String starLine = "";
		for (int i=0; i<Constants.MAX_CHARS; i++) {
			starLine += '*';
		}
		return starLine + Constants.NEWLINE;
	}

}
