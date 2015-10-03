package be.sefl.oxfam.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

/**
 * @author sefl
 */
public class HelpMethods_TEST extends TestCase {

	public void testParseStringToDouble() {
		// Test parsing without rounding.
		assertEquals(0.00, HelpMethods.parseStringToDouble("0", false));
		assertEquals(1.00, HelpMethods.parseStringToDouble("1", false));
		assertEquals(0.56, HelpMethods.parseStringToDouble("0.56", false));
		assertEquals(0.561, HelpMethods.parseStringToDouble("0.561", false));
		assertEquals(0.569, HelpMethods.parseStringToDouble("0.569", false));
		assertEquals(100.00, HelpMethods.parseStringToDouble("100", false));
		assertEquals(1.2345, HelpMethods.parseStringToDouble("1.2345", false));
		assertEquals(1.235, HelpMethods.parseStringToDouble("1.235", false));

		// Test parsing with rounding.
		assertEquals(0.00, HelpMethods.parseStringToDouble("0", true));
		assertEquals(1.00, HelpMethods.parseStringToDouble("1", true));
		assertEquals(0.56, HelpMethods.parseStringToDouble("0.56", true));
		assertEquals(0.56, HelpMethods.parseStringToDouble("0.561", true));
		assertEquals(0.57, HelpMethods.parseStringToDouble("0.569", true));
		assertEquals(100.00, HelpMethods.parseStringToDouble("100", true));
		assertEquals(1.23, HelpMethods.parseStringToDouble("1.2345", true));
		assertEquals(1.24, HelpMethods.parseStringToDouble("1.235", true));
	}

	public void testRound() {
		assertEquals(0.00, HelpMethods.round(0));
		assertEquals(1.00, HelpMethods.round(1));
		assertEquals(0.56, HelpMethods.round(0.56));
		assertEquals(0.56, HelpMethods.round(0.561));
		assertEquals(0.57, HelpMethods.round(0.569));
		assertEquals(100.00, HelpMethods.round(100));
		assertEquals(1.23, HelpMethods.round(1.2345));
		assertEquals(1.24, HelpMethods.round(1.235));
	}

	public void testToAmount() {
		assertEquals("0,00", HelpMethods.toAmount(0));
		assertEquals("1,00", HelpMethods.toAmount(1));
		assertEquals("0,56", HelpMethods.toAmount(0.56));
		assertEquals("0,56", HelpMethods.toAmount(0.561));
		assertEquals("0,57", HelpMethods.toAmount(0.569));
		assertEquals("100,00", HelpMethods.toAmount(100));
		assertEquals("1,23", HelpMethods.toAmount(1.2345));
		assertEquals("1,24", HelpMethods.toAmount(1.235));
	}

	public void testCurrentDateAndTimeToString() {
		Calendar calendar = GregorianCalendar.getInstance();
		
		int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		int month = calendar.get(GregorianCalendar.MONTH) + 1;
		int year = calendar.get(GregorianCalendar.YEAR);
		
		int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
		int minute = calendar.get(GregorianCalendar.MINUTE);

		String expectedDateTime = String.format("%02d-%02d-%4d %02du%02d", day, month, year, hour, minute);
		assertEquals(expectedDateTime, HelpMethods.currentDateAndTimeToString());
	}

	public void testCurrentDateToString() {
		Calendar calendar = GregorianCalendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		String expectedDate = String.format("%02d-%02d-%4d", day, month, year);
		assertEquals(expectedDate, HelpMethods.currentDateToString());
	}

	public void testCurrentTimeToString() {
		Calendar calendar = GregorianCalendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String expectedTime = String.format("%02d:%02d", hour, minute);
		assertEquals(expectedTime, HelpMethods.currentTimeToString());
	}

	public void testCalcBTW() {
		assertEquals("6,00", HelpMethods.calcBTW(6, 100));
		assertEquals("97,68", HelpMethods.calcBTW(21, 465.16));
	}

}
