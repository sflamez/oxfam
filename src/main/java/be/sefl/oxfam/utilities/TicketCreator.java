package be.sefl.oxfam.utilities;

import java.io.Serializable;
import java.util.List;

import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.object.Article;
import be.sefl.oxfam.object.Order;

/**
 * @author sefl
 */
public class TicketCreator implements Serializable {
	
	private static final long serialVersionUID = -7148808555170211272L;
	
	public static String createTicket(Order order, boolean isTotal) {
		StringBuffer ticket = new StringBuffer();
		
		if (isTotal) {
			ticket.append(generateTotalHeader());
		} else {
			ticket.append(generateHeader());
		}
		
		ticket.append(StringFormatter.createStarLine());

		/** Begin listing Articles. */
		List<Article> articles = order.getArticles();
		for (int i = 0; i < articles.size(); i++) {
			ticket.append(generateArticleLine(articles.get(i), order.getCount(articles.get(i))));
		}
		ticket.append(Constants.NEWLINE);

		/** Begin listing Extras. */
		double[] extras = order.getExtra();
		for (int i = 0; i < extras.length; i++) {
			if (extras[i] != 0.0) {
				ticket.append(generateExtraLine(order.getExtraS(i), extras[i]));
			}
		}
		ticket.append(Constants.NEWLINE);

		/** Begin listing Summary. */
		ticket.append(generateSummary(order));

		if (isTotal) {
			ticket.append(generateTotalFooter(order.getTotals()));
		} else {
			ticket.append(generateFooter());
		}

		return ticket.toString();
	}
	
	private static String generateHeader() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(StringFormatter.center(Constants.HEADER_NAME));
		buffer.append(StringFormatter.center(Constants.HEADER_ADDRESS_1));
		buffer.append(StringFormatter.center(Constants.HEADER_ADDRESS_2));
		buffer.append(StringFormatter.center(Constants.HEADER_TEL));
		buffer.append(Constants.NEWLINE);

		buffer.append(StringFormatter.center(HelpMethods.currentDateAndTimeToString()));
		buffer.append(Constants.NEWLINE);

		return buffer.toString();
	}

	private static String generateTotalHeader() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(StringFormatter.center(Constants.TOTAL_HEADER_1));
		buffer.append(StringFormatter.center(Constants.TOTAL_HEADER_2 + HelpMethods.currentDateToString() + Constants.TOTAL_HEADER_3 + HelpMethods.currentTimeToString()));
		buffer.append(Constants.NEWLINE);

		buffer.append(StringFormatter.createStarLine());
		buffer.append(Constants.NEWLINE);
		buffer.append(Constants.NEWLINE);

		return buffer.toString();
	}

	private static String generateFooter() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(Constants.NEWLINE);

		String goodbyeText = "";
		int nbrOfStars = (Constants.MAX_CHARS - (Constants.FOOTER_GOODBYE.length() + 2)) / 2;
		int nbrOfExtraStars = (Constants.MAX_CHARS - (Constants.FOOTER_GOODBYE.length() + 2)) % 2;
		for (int i = 0; i < nbrOfStars; i++) {
			goodbyeText += '*';
		}

		goodbyeText += '*' + Constants.FOOTER_GOODBYE + '*';

		for (int i = 0; i < nbrOfStars + nbrOfExtraStars; i++) {
			goodbyeText += '*';
		}

		buffer.append(StringFormatter.createStarLine());
		buffer.append(StringFormatter.center(goodbyeText));
		buffer.append(StringFormatter.createStarLine());

		return buffer.toString();
	}

	private static String generateTotalFooter(double[] totalsByPercentage) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(Constants.NEWLINE);
		buffer.append(Constants.NEWLINE);

		for (int i = 0; i < totalsByPercentage.length; i++) {
			buffer.append(generateTotalLine(i, totalsByPercentage[i]));
		}

		buffer.append(Constants.NEWLINE);
		buffer.append(StringFormatter.createStarLine());
		buffer.append(Constants.NEWLINE);

		return buffer.toString();
	}

	private static String generateSummary(Order order) {
		StringBuffer buffer = new StringBuffer(Constants.MAX_CHARS);

		buffer.append(StringFormatter.createAmountLine(order.getTotal(), Constants.LABEL_TOTAL));

		if (order.getAmountPaidWithBancontact() != 0) {
			buffer.append(StringFormatter.createAmountLine(order.getAmountPaidWithBancontact(), Constants.LABEL_PAID_BY_BANCONTACT));
		}

		if (order.getAmountPaidWithPayconiq() != 0) {
			buffer.append(StringFormatter.createAmountLine(order.getAmountPaidWithPayconiq(), Constants.LABEL_PAID_BY_PAYCONIQ));
		}

		if (order.getAmountPaid() != 0 && order.getAmountToReturn() != 0) {
			buffer.append(StringFormatter.createAmountLine(order.getAmountPaid(), Constants.LABEL_PAID));
			buffer.append(StringFormatter.createAmountLine(order.getAmountToReturn(), Constants.LABEL_RETURN));
		}

		return buffer.toString();
	}

	private static String generateArticleLine(Article article, int count) {
		StringBuffer line = new StringBuffer(Constants.MAX_CHARS);

		String name = article.getShortDescription();
		double unitPrice = article.getPrice();

		line.append(StringFormatter.formatDescription(name));

		if (count == 1) {
			line.append(StringFormatter.formatTotalPrice(HelpMethods.toAmount(unitPrice)));
		} else {
			line.append(Constants.NEWLINE);
			line.append(StringFormatter.formatCount(count));
			line.append(StringFormatter.formatUnitPrice(HelpMethods.toAmount(unitPrice)));
			line.append(StringFormatter.formatTotalPrice(HelpMethods.toAmount(count * unitPrice)));
		}
		line.append(Constants.NEWLINE);

		return line.toString();
	}

	private static String generateExtraLine(String extraName, double extraAmount) {
		StringBuffer line = new StringBuffer(Constants.MAX_CHARS);

		line.append(StringFormatter.formatDescription(extraName));
		line.append(StringFormatter.formatTotalPrice(HelpMethods.toAmount(extraAmount)));
		line.append(Constants.NEWLINE);

		return line.toString();
	}

	private static String generateTotalLine(int totalIndex, double amount) {
		StringBuffer buffer = new StringBuffer(Constants.MAX_CHARS);

		String description = Constants.TOTAL_FOOTER_DESCRIPTIONS[totalIndex];
		String totalAmount = HelpMethods.toAmount(amount);
		String totalInfo = totalAmount + Constants.EURO;

		String btwAmount = HelpMethods.calcBTW(Constants.TOTAL_FOOTER_PERCENTAGES[totalIndex], amount);
		String btwInfo = " (" + btwAmount + Constants.EURO + " BTW)";

		int maxDescriptionLength;

		buffer.append(description);

		/** BTW 0% ? Only show description + totalAmount. */
		if (totalIndex == 0) {
			maxDescriptionLength = Constants.MAX_CHARS - totalInfo.length();

			/** Align right. */
			for (int i = 0; i < maxDescriptionLength - description.length(); i++) {
				buffer.append(' ');
			}

			buffer.append(totalInfo);
		} else {
			/** BTW 6% or 21% ? Show description + totalAmount + BTW. */
			maxDescriptionLength = Constants.MAX_CHARS - totalInfo.length()	- Constants.MAX_BTW;

			/** Align right. */
			for (int i = 0; i < maxDescriptionLength - description.length(); i++) {
				buffer.append(' ');
			}

			buffer.append(totalInfo);

			/** Align right. */
			for (int i = 0; i < Constants.MAX_BTW - btwInfo.length(); i++) {
				buffer.append(' ');
			}

			buffer.append(btwInfo);
		}

		buffer.append(Constants.NEWLINE);

		return buffer.toString();
	}

}
