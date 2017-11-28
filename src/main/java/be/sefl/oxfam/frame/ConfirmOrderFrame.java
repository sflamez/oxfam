package be.sefl.oxfam.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.sefl.oxfam.border.ClickableTitledBorder;
import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.object.Article;
import be.sefl.oxfam.object.Order;
import be.sefl.oxfam.utilities.HelpMethods;
import be.sefl.oxfam.utilities.Printer;

/**
 * @author sefl
 */
public class ConfirmOrderFrame extends MainFrame {

	private static final long serialVersionUID = -2837528567624834445L;
	
    private static final Logger logger = LoggerFactory.getLogger("ConfirmOrderFrame");

	//---------- Panels ----------\\
	private JPanel arts;
	private JPanel extras;
	private JPanel total;
	
	//---------- Buttons ----------\\
	private JButton OK = new JButton("Bevestig");
	private JButton nOK = new JButton("Wijzig");
	private JButton change = new JButton("Teruggeven");
	private JButton print = new JButton("Print");

	//---------- Variables ----------\\
	private List<Article> articles;
	private double[] extra;
	private String[] extraS;
	private Order order;
	private int countExtras;

	public ConfirmOrderFrame(Order orderToConfirm, MainFrame parent) {
		super("Bevestig bestelling", parent, true);

		this.order = orderToConfirm;

		this.articles = order.getArticles();
		this.extra = order.getExtra();
		this.extraS = order.getExtraS();
		
		this.arts = new JPanel(new GridLayout(articles.size(), 2));
		this.arts.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Voeding"), 
			BorderFactory.createEmptyBorder(0,5,0,5)));		//T,L,B,R
		
		for (int i=0; i<extra.length; i++) {
			if (extra[i] != 0) countExtras++;
		}

		this.extras = new JPanel(new GridLayout(countExtras,1));
		this.extras.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Artisanaat en bonnen"), 
			BorderFactory.createEmptyBorder(0,5,0,5)));		//T,L,B,R
		
		this.total = new JPanel(new GridLayout(1,1));

		/** Show amount paid and amount to return if available. */
		if (orderToConfirm.getAmountToReturn() != 0.0) {
			total = new JPanel(new GridLayout(1,3));
			
		}

		this.total.setBorder(BorderFactory.createCompoundBorder(
				new ClickableTitledBorder("Totaal"), 
			BorderFactory.createEmptyBorder(0,5,0,5)));		//T,L,B,R

		//Panel for articles
		Article article = null;
		for (int i=0; i<articles.size(); i++) {
			article = articles.get(i);
    		Color color = (i%2 == 0) ? Color.BLACK : Color.BLUE;
			JLabel artLabel = new JLabel(order.getCount(article) + "x " + article.getDescription() + ", " + HelpMethods.toAmount(article.getPrice()) + Constants.EURO);
			artLabel.setForeground(color);
			artLabel.setFont(new Font("Tahoma",0,12));
			arts.add(artLabel);
			JLabel totalLabel = new JLabel("= " + HelpMethods.toAmount(order.getCount(article) * article.getPrice()) + Constants.EURO);
			totalLabel.setForeground(color);
			totalLabel.setFont(new Font("Tahoma",0,12));
			totalLabel.setHorizontalAlignment(JLabel.RIGHT);
			arts.add(totalLabel);
		}

		//Panel for artisanaat, coupons, etc.
		for (int i=0; i<extra.length; i++) {
			if (extra[i] == 0) continue;
			JLabel extraLabel = new JLabel(extraS[i]);
			extraLabel.setFont(new Font("Tahoma",0,12));
			extras.add(extraLabel);
			JLabel amountLabel = new JLabel(HelpMethods.toAmount(extra[i])+Constants.EURO);
			amountLabel.setFont(new Font("Tahoma",0,12));
			amountLabel.setHorizontalAlignment(JLabel.RIGHT);
			extras.add(amountLabel);
		}
		
		JLabel totalLabel = new JLabel("Totaal: "+HelpMethods.toAmount(orderToConfirm.getTotal())+Constants.EURO);
		totalLabel.setFont(new Font("Tahoma",0,12));
		total.add(totalLabel);

		/** Show amount paid and amount to return if available. */
		if (orderToConfirm.getAmountToReturn() != 0.0) {
			JLabel paidLabel = new JLabel("Betaald: "+HelpMethods.toAmount(orderToConfirm.getAmountPaid())+Constants.EURO);
			paidLabel.setFont(new Font("Tahoma",0,12));
			total.add(paidLabel);

			JLabel returnLabel = new JLabel("Terug: "+HelpMethods.toAmount(orderToConfirm.getAmountToReturn())+Constants.EURO);
			returnLabel.setFont(new Font("Tahoma",Font.BOLD,12));
			total.add(returnLabel);
		}
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				OxfamFrame.process(order);
				enableParent(true);
				frame.remove();
			}
		});
		nOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// Reset order, want na de wijzigingen wordt er opnieuw op 'Process'
				// gedrukt en wordt deze order dus opnieuw aangemaakt.
				order.reset();
				enableParent(true);
				frame.remove();
			}
		});
		change.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				frame.enabled(false);
				ChangeFrame cFrame = new ChangeFrame(order, frame);
				cFrame.setVisible(true);
			}
		});
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				    logger.info("Trying to print order");
				    Printer.print(order);
				} catch (Exception e) {
					logger.error("Failed to print ticket", e);
					frame.enabled(false);
				    new ExceptionFrame("Failed to print ticket", e, frame, true).setVisible(true);
				}
			}
		});

		this.addControl(OK);
		this.addControl(nOK);
		this.addControl(change);
		this.addControl(print);

		// How many articles can we show without using a scrollbar? -> numArts
		int rest = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -150 -196 -27;
		int numArts = (int) Math.floor(rest/15.0);

		if (articles.size() == 0 && countExtras == 0) {
			enableParent(true);
			frame.remove();
		}
		
		if (articles.size() != 0) {
			if (articles.size() > numArts) {
				//Put panel arts into a scrollPane.
				JScrollPane scrollArticles = new JScrollPane(arts,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				scrollArticles.setPreferredSize(new Dimension(450,(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() -150 -196));
				scrollArticles.setBorder(null);
				scrollArticles.getVerticalScrollBar().setUnitIncrement(40);
				this.addComponent(scrollArticles);
			}
			else this.addComponent(arts);
		}

		if (countExtras != 0) {
			this.addComponent(extras);
		}

		this.addComponent(total);
		this.pack();
		this.setLocation();
	}
}