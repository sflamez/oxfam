package be.sefl.oxfam.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jpos.JposConst;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.object.Article;
import be.sefl.oxfam.object.Order;

/**
 * @author sefl
 */
public class Printer {

	private static POSPrinter printer = new POSPrinter();

	public static void printToFile(Order order, File file) {
		try {
			FileWriter writer = new FileWriter(file, false);
			writer.write(TicketCreator.createTicket(order, false));
			writer.flush();
			writer.close();
		} catch (IOException ioe) {
			// nada
		}
	}

	public static void printDayTotalToFile(Order totalOrder, File file) {
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(TicketCreator.createTicket(totalOrder, true));
			writer.flush();
			writer.close();
		} catch (IOException ioe) {
			// nada
		}
	}

	public static void print(Order order) {
		if (printer == null) {
			throw new RuntimeException("No ticket printer available.");
		}

		try {
			printer.open("Star TSP100 Cutter (TSP143)_1");
			printer.claim(1);
			printer.setDeviceEnabled(true);
			printer.setAsyncMode(true);

			// Set map mode to metric - all dimensions specified in 1/100mm
			// units. (1 unit = 1/100mm)
			printer.setMapMode(POSPrinterConst.PTR_MM_METRIC);

			// Begin a transaction:
			// Transaction mode causes all output to be buffered. Once
			// transaction mode is terminated, the buffered data is
			// outputted to the printer in one shot - increased reliability.
			printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_TRANSACTION);

			printTicket(order);

			// the ESC + "|100fP" control code causes the printer to execute a
			// paper cut after feeding to the cutter position
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|100fP");

			// terminate the transaction causing all of the above buffered data
			// to be sent to the printer
			printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT, POSPrinterConst.PTR_TP_NORMAL);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the printer object
			try {
				while (printer.getState() != JposConst.JPOS_S_IDLE) {
					Thread.sleep(0);
				}
				printer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static void printTicket(Order order) throws Exception {
		printHeader();

		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.createStarLine() + Constants.LF);

		/** Begin listing Articles. */
		List<Article> articles = order.getArticles();
		for (int i = 0; i < articles.size(); i++) {
			printArticleLine(articles.get(i), order.getCount(articles.get(i)));
		}
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

		/** Begin listing Extras. */
		double[] extras = order.getExtra();
		for (int i = 0; i < extras.length; i++) {
			if (extras[i] != 0.0) {
				printExtraLine(order.getExtraS(i), extras[i]);
			}
		}
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

		/** Begin listing Summary. */
		printSummary(order.getTotal(), order.getAmountPaid(), order .getAmountToReturn());

		printFooter();
	}

	private static void printHeader() throws Exception {
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.ESC + "|bC" + Constants.HEADER_NAME + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.HEADER_ADDRESS_1 + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.HEADER_ADDRESS_2 + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.HEADER_TEL + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + HelpMethods.currentDateAndTimeToString() + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
	}

	private static void printFooter() throws Exception {
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.FOOTER_STAR_LINE + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.FOOTER_GOODBYE + Constants.LF);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.ESC + "|cA" + Constants.FOOTER_STAR_LINE + Constants.LF);

		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
	}

	private static void printSummary(double totalAmount, double paidAmount, double returnAmount) throws Exception {
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LABEL_TOTAL);
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatTotalAmount(HelpMethods.toAmount(totalAmount)));
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

		if (paidAmount != 0 && returnAmount != 0) {
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LABEL_PAID);
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatPaidAmount(HelpMethods.toAmount(paidAmount)));
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);

			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LABEL_RETURN);
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatReturnAmount(HelpMethods.toAmount(returnAmount)));
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
		}
	}

	private static void printArticleLine(Article article, int count) throws Exception {
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatDescription(article.getShortDescription()));

		if (count == 1) {
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatTotalPrice(HelpMethods.toAmount(article.getPrice())));
		} else {
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatCount(count));
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatUnitPrice(HelpMethods.toAmount(article.getPrice())));
			printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatTotalPrice(HelpMethods.toAmount(count * article.getPrice())));
		}
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
	}

	private static void printExtraLine(String extraName, double extraAmount) throws Exception {
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatDescription(extraName));
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, StringFormatter.formatTotalPrice(HelpMethods.toAmount(extraAmount)));
		printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, Constants.LF);
	}

}