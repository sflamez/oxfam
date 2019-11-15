package be.sefl.oxfam.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.frame.OxfamFrame;
import be.sefl.oxfam.utilities.HelpMethods;

/**
 * @author sefl
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 4011575444843477144L;

	private static final String[] extraS = {"Artisanaat: ","Amnesty & UNICEF: ","Verkochte geschenkbonnen: ","Ontvangen geschenkbonnen: ","Ontvangen kortingsbonnen: ","Kortingsproduct (6% BTW): ","Kortingsproduct (21% BTW): "};
	
	//---------- Variables ----------\\
	private int[] count;
	private List<Article> articles;
	private double[] extra;
	private double amountToReturn;
	private double amountPaid;
	private boolean bancontact;
	private double amountPaidWithBancontact;
	private boolean payconiq;
	private double amountPaidWithPayconiq;

	public Order() {
		count = new int[OxfamFrame.getNbrOfArticles()];
		articles = new ArrayList<Article>();
		extra = new double[extraS.length];
	}
	
	public Order(Order b) {
		count = new int[b.size()];
		articles = new ArrayList<Article>(b.size());
		for (int i = 0; i < b.size(); i++) {
			Article tmp = b.getArticles().get(i);
			articles.add(tmp);
			count[i] = b.getCount(tmp);
		}
		extra = new double[extraS.length];
		for (int i = 0; i < extra.length; i++) {
			extra[i] = b.getExtra(i);
		}
		amountPaid = b.getAmountPaid();
		amountToReturn = b.getAmountToReturn();
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public int getCount(Article a) {
		return count[articles.indexOf(a)];
	}
	
	public double getExtra(int i) {
		return extra[i];
	}
	
	public double[] getExtra() {
		return extra;
	}
	
	public String getExtraS(int i) {
		return extraS[i];
	}
	
	public String[] getExtraS() {
		return extraS;
	}
	
	public double[] getTotals() {
		double[] total = new double[3];
		for (int i = 0; i < articles.size(); i++) {
			Article tmp = articles.get(i);
			if (tmp.getBtw() == 0) {
				total[0] += count[i] * tmp.getPrice();
			} else if (tmp.getBtw() == 6) {
				total[1] += count[i] * tmp.getPrice();
			} else if (tmp.getBtw() == 21) {
				total[2] += count[i] * tmp.getPrice();
			}
		}
		
		total[2] += extra[0];		//Artisanaat = 21% BTW
		total[0] += extra[1];		//Amnesty & UNICEF = 0% BTW
		total[0] += extra[2];		//Geschenkbonnen = 0% BTW
		total[0] += extra[3];		//Geschenkbonnen = 0% BTW
		total[0] += extra[4];		//Kortingsbonnen = 0% BTW
		total[1] += extra[5];		//Kortingsproduct 6% BTW
		total[2] += extra[6];		//Kortingsproduct 21% BTW
		
		for (int i = 0; i < 3; i++) {
			total[i] = HelpMethods.round(total[i]);
		}
		return total;
	}
	
	public double getAmountPaid() {
		return amountPaid;
	}
	
	public double getAmountToReturn() {
		return amountToReturn;
	}
	
	public double getTotal() {
		double total = 0.0;
		for (int i=0; i<articles.size(); i++) {
			Article tmp = articles.get(i);
			total += count[i]*tmp.getPrice();
		}

		for (int j=0; j<extra.length; j++) {
			total += extra[j];
		}
		
		return HelpMethods.round(total);
	}
	
	public void add(Order order) {
		for (int i=0; i<order.size(); i++) {
			Article tmp = order.getArticles().get(i);
			if (!articles.contains(tmp)) {
				articles.add(tmp);
			}
			this.count[articles.indexOf(tmp)] += order.getCount(tmp);
		}
		
		for (int i=0; i<extra.length; i++) {
			extra[i] = HelpMethods.round(extra[i] + order.getExtra(i));
		}

		if (order.isPaidWithBancontact()) {
			amountPaidWithBancontact += order.getTotal();
		} else if (order.isPaidWithPayconiq()) {
			amountPaidWithPayconiq += order.getTotal();
		}
	}
	
	public void addArticle(Article art, int size) {
		if (size != 0) {
			if (!articles.contains(art)) {
				articles.add(art);
			}
			count[articles.indexOf(art)] += size;
		}
	}
	
	public void addExtra(int i, double amount) {
		extra[i] += amount;
	}
	
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	public void setAmountToReturn(double amountToReturn) {
		this.amountToReturn = amountToReturn;
	}
	
	public void paidWithBancontact() {
		bancontact = true;
	}

	public boolean isPaidWithBancontact() {
		return bancontact;
	}

	public double getAmountPaidWithBancontact() {
		return amountPaidWithBancontact;
	}

	public void paidWithPayconiq() {
		payconiq = true;
	}

	public boolean isPaidWithPayconiq() {
		return payconiq;
	}

	public double getAmountPaidWithPayconiq() {
		return amountPaidWithPayconiq;
	}

	public boolean isPaidCash() {
		return !(bancontact || payconiq);
	}

	public int size() {
		return articles.size();
	}

	public String toString() {
		String ret = "";
		for (int i=0; i<articles.size(); i++) {
			Article tmp = articles.get(i);
			ret += count[i] + "x\t" + tmp.getDescription() + ", " + HelpMethods.toAmount(tmp.getPrice()) + Constants.EURO + Constants.NEWLINE;
		}
		ret +=  Constants.NEWLINE;
		for (int i = 0; i < extra.length; i++) {
			ret += extraS[i] + HelpMethods.toAmount(extra[i]) + Constants.EURO;
			ret += Constants.NEWLINE;
		}
		
		return ret;
	}

}