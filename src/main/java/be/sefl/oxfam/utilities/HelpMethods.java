package be.sefl.oxfam.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sefl
 */
public final class HelpMethods {

	/**
	 * <p>Zet gegeven String (zowel met decimale komma als met decimaal punt)
	 * om naar een double, eventueel afgerond tot 2 cijfers na de komma.</p>
	 */
	public static Double parseStringToDouble(String s, boolean round) throws NumberFormatException {
	    String doubleString = s.replace(',', '.');
		return round? round(Double.parseDouble(doubleString)) : Double.parseDouble(doubleString);
	}
	
	/**
	 * <p>Rond een gegeven double af tot op 2 cijfers na de komma.</p>
	 */
	public static Double round(double d) {
		return java.lang.Math.round(d*100.0)/100.0;
	}
	
	/**
	 * <p>Wrap een gegeven double in een String met 2 cijfers na de komma.</p>
	 */
	public static String toAmount(double d) {
		double tmpDouble = round(d);
		String tmpDoubleString = tmpDouble + "";
		if (java.lang.Math.round(tmpDouble*100.0)%10 == 0.0) {
			tmpDoubleString += '0';
		}
		return tmpDoubleString.replace('.', ',');
	}
	
	/**
	 * <p>Geef de huidige datum + tijd terug als String.</p>
	 */
	public static String currentDateAndTimeToString() {
    	String currentDateAndTimeAsString = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date(System.currentTimeMillis()));
    	return currentDateAndTimeAsString.replace(':', 'u');
	}
	
	/**
	 * <p>Geef de huidige datum terug als String.</p>
	 */
	public static String currentDateToString() {
    	return new SimpleDateFormat("dd-MM-yyyy").format(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * <p>Geef de huidige tijd terug als String.</p>
	 */
	public static String currentTimeToString() {
    	return new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));
	}
	
	/**
	 * <p>Berekent BTW van percentage % op bedrag amount.</p> 
	 */
	public static String calcBTW(int percentage, double amount) {
		return toAmount(amount/100*percentage);
	}

}
