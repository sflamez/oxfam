package be.sefl.oxfam.constants;

/**
 * @author sefl
 */
public class Constants {
	
	/** Printing constants. */
	public static final String SERVER_PROPERTY = "runsAsOxfamServer";
	public static final int SERVER_PORT = 9999;
	public static final String EURO = " €";
	public static final String ESC = ((char) 0x1b) + "";
	public static final String LF = ((char) 0x0a) + "";
	public static final String SPACES = "                                                                      ";

	public static final int MAX_CHARS = 48;
	public static final int MAX_COUNT = 32;
	public static final int MAX_DESCRIPTION = 40;
	public static final int MAX_PRICE = 8;
	public static final int MAX_BTW = 16;

	public static final String HEADER_NAME = "Oxfam Wereldwinkel Wevelgem";
	public static final String HEADER_ADDRESS_1 = "Peperstraat 2";
	public static final String HEADER_ADDRESS_2 = "8560 GULLEGEM";
	public static final String HEADER_TEL = "0474/47.96.67";
	
	public static final String TOTAL_HEADER_1 = "Verkoopmoment afgesloten";
	public static final String TOTAL_HEADER_2 = "op ";
	public static final String TOTAL_HEADER_3 = " om ";

	public static final String[] TOTAL_FOOTER_DESCRIPTIONS = {"0% BTW", "6% BTW", "21% BTW"};
	public static final int[] TOTAL_FOOTER_PERCENTAGES = {0, 6, 21};
	public static final String FOOTER_STAR_LINE = "*******************************";
	public static final String FOOTER_GOODBYE = "*** Bedankt voor uw bezoek! ***";


	public static final String LABEL_TOTAL = "TOTAAL";
	public static final String LABEL_PAID = "Betaald";
	public static final String LABEL_PAID_BY_BANCONTACT = "Betaald met bancontact";
	public static final String LABEL_RETURN = "Terug";
	
	public static final String NEWLINE = "\r\n";

}