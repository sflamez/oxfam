package be.sefl.oxfam.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.sefl.oxfam.border.ClickableTitledBorder;
import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.constants.EuroLabel;
import be.sefl.oxfam.object.Article;
import be.sefl.oxfam.object.Category;
import be.sefl.oxfam.object.Order;
import be.sefl.oxfam.panel.OxfamJPanel;
import be.sefl.oxfam.utilities.Database;
import be.sefl.oxfam.utilities.HelpMethods;
import be.sefl.oxfam.utilities.Printer;

/**
 * @author sefl
 */
public class OxfamFrame extends MainFrame implements ActionListener {

	private static final long serialVersionUID = 7743524114756854696L;

	private static final Logger logger = LoggerFactory.getLogger("OxfamFrame");
	
	// ---------- Panels ----------\\
	private static JPanel mainPanel;
	private static JPanel[] helpPanelsArti, helpPanelsAmni, helpPanelsGiftsOut,	helpPanelsGiftsIn, helpPanelsReduc;
	private static JPanel[] helpPanelReductionProduct6;
	private static JPanel[] helpPanelReductionProduct21;
	private static JPanel[][] helpPanels; // weergave leeggoed
	private static OxfamJPanel[] panels;

	// ---------- Buttons ----------\\
	private static JButton OKButton = new JButton("Totaal klant");
	private static JButton resetButton = new JButton("Klant wissen");
	private static JButton totalButton = new JButton("Dagtotaal");

	// ---------- Labels ----------\\
	private static JLabel[][] prodLabels;
	private static JLabel[][] priceLabels;
	private static JLabel totalAmountLabel, totalLabel, emptyMin, emptyPlus;
	private static JLabel artisanaat, amnicef, giftsOut, giftsIn, reducBons;
	private static JLabel reductionProduct6;
	private static JLabel reductionProduct21;
	private static final JLabel startLabel = new JLabel("Startbedrag: ");
	private static final JLabel kassaLabel = new JLabel("Nu in kassa: ");
	private static final JLabel soldLabel = new JLabel("Al verkocht: ");
	private static JLabel startAmountLabel, inKassaLabel, aldreadySoldLabel;

	// ---------- TextFields ----------\\
	private static JTextField[] emptyBack;
	private static JTextField[][] sellCount;
	private static JTextField artiTxt, amniTxt, giftsOutTxt, giftsInTxt, reducTxt;
	private static JTextField reductionProduct6Txt;
	private static JTextField reductionProduct21Txt;

	
	// ---------- Variables ----------\\
	private static boolean programStart, collapseCategories;
	private static OxfamFrame oxfamFrame;
	private static List<Category> categories;
	private static double startAmount, kassaAmount, alreadySoldAmount;
	private static Order order, totalOrder;
	private static Category emptyBottlesCategory;
	private static int nbrOfArticles;

	private static Database database;

	public OxfamFrame(String title) {
		super(title, null, true);

		this.addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				StopFrame stopFrame = new StopFrame(oxfamFrame);
				stopFrame.setVisible(true);
				enabled(false);
			}

			public void windowOpened(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}
		});

		database = new Database();

		try {
			categories = database.getCategories();
			for (Category category : categories) {
				if (category.isLeeggoed()) {
					emptyBottlesCategory = category;
				}
				List<Article> articles = database.getArticles(category);
				category.addArticles(articles);
				nbrOfArticles += articles.size();
			}
		} catch (Exception e) {
			logger.error("Problem reading from DB", e);
		}

		order = new Order();
		totalOrder = new Order();

		prodLabels = new JLabel[categories.size()][];
		priceLabels = new JLabel[categories.size()][];
		emptyBack = new JTextField[emptyBottlesCategory.getArticleCount()];
		sellCount = new JTextField[categories.size()][];
		panels = new OxfamJPanel[categories.size() + 1];
		helpPanels = new JPanel[categories.size()][2];
		helpPanelsArti = new JPanel[2];
		helpPanelsAmni = new JPanel[2];
		helpPanelsGiftsOut = new JPanel[2];
		helpPanelsGiftsIn = new JPanel[2];
		helpPanelsReduc = new JPanel[2];
		helpPanelReductionProduct6 = new JPanel[2];
		helpPanelReductionProduct21 = new JPanel[2];

		createArtikelPanels();

		// Create the main panel to contain the sub panels.
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Add the select and display panels to the main panel.
		for (int i = 0; i < panels.length; i++) {
			mainPanel.add(panels[i]);
		}

		JScrollPane scroller = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setPreferredSize(new Dimension(550, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300));
		scroller.getVerticalScrollBar().setUnitIncrement(40);

		addComponent(scroller);
		addComponent(createTotalPanel());
		addComponent(createStartBedragPanel());

		// Add buttons
		OKButton.addActionListener(this);
		resetButton.addActionListener(this);
		totalButton.addActionListener(this);
		addControl(OKButton);
		addControl(resetButton);
		addControl(totalButton);
	}

	private static void createAndShowGUI() {
		// Create instance of OxfamFrame
		oxfamFrame = new OxfamFrame("Oxfam Wereldwinkel");
		oxfamFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		oxfamFrame.pack();
		oxfamFrame.setLocation();
		oxfamFrame.setVisible(true);
	}

	public static void init(double amount, boolean collapse) {
		programStart = true;
		collapseCategories = collapse;
		kassaAmount = startAmount = amount;

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void createArtikelPanels() {
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			List<Article> articles = category.getArticles();
			int aantalArtikels = articles.size();

			panels[i] = new OxfamJPanel(new GridLayout(1, 2), this);
			panels[i].setBorder(BorderFactory.createCompoundBorder(new ClickableTitledBorder(categories.get(i).getName()), BorderFactory.createEmptyBorder(0, 5, 0, 5))); // T,L,B,R

			// speciale regeling voor leeggoed
			if (category.isLeeggoed()) {
				helpPanels[i][0] = new JPanel(new GridLayout(aantalArtikels + 1, 1));
				helpPanels[i][1] = new JPanel(new GridLayout(aantalArtikels + 1, 3));
			} else {
				helpPanels[i][0] = new JPanel(new GridLayout(aantalArtikels, 1));
				helpPanels[i][1] = new JPanel(new GridLayout(aantalArtikels, 2));
			}

			prodLabels[i] = new JLabel[aantalArtikels];
			priceLabels[i] = new JLabel[aantalArtikels];
			sellCount[i] = new JTextField[aantalArtikels];

			// extra labels voor het leeggoed
			if (category.isLeeggoed()) {
				emptyMin = new JLabel("Terug");
				emptyPlus = new JLabel("Betaald");
				emptyMin.setHorizontalAlignment(JLabel.CENTER);
				emptyPlus.setHorizontalAlignment(JLabel.CENTER);
				helpPanels[i][0].add(new JLabel());
				helpPanels[i][1].add(new JLabel());
				helpPanels[i][1].add(emptyPlus);
				helpPanels[i][1].add(emptyMin);
			}

			// anders: voeg produktlijnen toe
			for (int j = 0; j < aantalArtikels; j++) {
				Color color = (j % 2 == 0) ? Color.BLACK : Color.BLUE;
				prodLabels[i][j] = new JLabel(articles.get(j).getDescription());
				prodLabels[i][j].setForeground(color);
				prodLabels[i][j].setFont(new Font("Tahoma", 0, 12));
				prodLabels[i][j].setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
				helpPanels[i][0].add(prodLabels[i][j]);
				priceLabels[i][j] = new JLabel(HelpMethods.toAmount(articles.get(j).getPrice()) + Constants.EURO);
				priceLabels[i][j].setForeground(color);
				priceLabels[i][j].setFont(new Font("Tahoma", 0, 12));
				priceLabels[i][j].setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
				priceLabels[i][j].setHorizontalAlignment(JLabel.RIGHT);
				helpPanels[i][1].add(priceLabels[i][j]);
				sellCount[i][j] = new JTextField();
				sellCount[i][j].setName(i + "-" + j);
				sellCount[i][j].setColumns(5);
				sellCount[i][j].setHorizontalAlignment(JTextField.RIGHT);
				sellCount[i][j].addFocusListener(new FocusListener() {
					public void focusGained(FocusEvent fe) {
					}

					public void focusLost(FocusEvent fe) {
						updateTotalLabel();
					}
				});
				helpPanels[i][1].add(sellCount[i][j]);

				// speciaal geval: leeggoed moet kunnen "verkocht" én teruggebracht worden.
				if (category.isLeeggoed()) {
					emptyBack[j] = new JTextField();
					emptyBack[j].setName(i + "-" + j);
					emptyBack[j].setColumns(5);
					emptyBack[j].setHorizontalAlignment(JTextField.RIGHT);
					emptyBack[j].addFocusListener(new FocusListener() {
						public void focusGained(FocusEvent fe) {
						}

						public void focusLost(FocusEvent fe) {
							updateTotalLabel();
						}
					});
					helpPanels[i][1].add(emptyBack[j]);
				}

				panels[i].add(helpPanels[i][0]);
				panels[i].add(helpPanels[i][1]);

				if (collapseCategories) {
					panels[i].closePanel();
				}
			}
		}

		// speciale regeling: artisanaat, geschenkbon (in & out), kortingsbon
		panels[categories.size()] = new OxfamJPanel(new GridLayout(7, 2), this);
		panels[categories.size()].setBorder(BorderFactory.createCompoundBorder(new ClickableTitledBorder("Artisanaat + bonnen"), BorderFactory.createEmptyBorder(0, 5, 0, 5))); // T,L,B,R

		artisanaat = new JLabel("Totaal artisanaat:");
		artisanaat.setFont(new Font("Tahoma", 0, 12));
		amnicef = new JLabel("Totaal Amnesty & UNICEF:");
		amnicef.setFont(new Font("Tahoma", 0, 12));
		giftsOut = new JLabel("Verkochte geschenkbonnen:");
		giftsOut.setFont(new Font("Tahoma", 0, 12));
		giftsIn = new JLabel("Ontvangen geschenkbonnen:");
		giftsIn.setFont(new Font("Tahoma", 0, 12));
		reducBons = new JLabel("Ontvangen kortingsbonnen:");
		reducBons.setFont(new Font("Tahoma", 0, 12));
		reductionProduct6 = new JLabel("Kortingsproduct (6% BTW):");
		reductionProduct6.setFont(new Font("Tahoma", 0, 12));
		reductionProduct21 = new JLabel("Kortingsproduct (21% BTW):");
		reductionProduct21.setFont(new Font("Tahoma", 0, 12));

		artiTxt = new JTextField();
		artiTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		amniTxt = new JTextField();
		amniTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		giftsOutTxt = new JTextField();
		giftsOutTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		giftsInTxt = new JTextField();
		giftsInTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		reducTxt = new JTextField();
		reducTxt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		reductionProduct6Txt = new JTextField();
		reductionProduct6Txt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});
		reductionProduct21Txt = new JTextField();
		reductionProduct21Txt.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe) {
			}

			public void focusLost(FocusEvent fe) {
				updateTotalLabel();
			}
		});

		helpPanelsArti[0] = new JPanel(new GridLayout(1, 1));
		helpPanelsArti[1] = new JPanel(new GridLayout(1, 2));
		helpPanelsAmni[0] = new JPanel(new GridLayout(1, 1));
		helpPanelsAmni[1] = new JPanel(new GridLayout(1, 2));
		helpPanelsGiftsOut[0] = new JPanel(new GridLayout(1, 1));
		helpPanelsGiftsOut[1] = new JPanel(new GridLayout(1, 2));
		helpPanelsGiftsIn[0] = new JPanel(new GridLayout(1, 1));
		helpPanelsGiftsIn[1] = new JPanel(new GridLayout(1, 2));
		helpPanelsReduc[0] = new JPanel(new GridLayout(1, 1));
		helpPanelsReduc[1] = new JPanel(new GridLayout(1, 2));
		helpPanelReductionProduct6[0] = new JPanel(new GridLayout(1, 1));
		helpPanelReductionProduct6[1] = new JPanel(new GridLayout(1, 2));
		helpPanelReductionProduct21[0] = new JPanel(new GridLayout(1, 1));
		helpPanelReductionProduct21[1] = new JPanel(new GridLayout(1, 2));

		helpPanelsArti[0].add(artisanaat);
		helpPanelsArti[1].add(artiTxt);
		helpPanelsArti[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelsAmni[0].add(amnicef);
		helpPanelsAmni[1].add(amniTxt);
		helpPanelsAmni[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelsGiftsOut[0].add(giftsOut);
		helpPanelsGiftsOut[1].add(giftsOutTxt);
		helpPanelsGiftsOut[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelsGiftsIn[0].add(giftsIn);
		helpPanelsGiftsIn[1].add(giftsInTxt);
		helpPanelsGiftsIn[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelsReduc[0].add(reducBons);
		helpPanelsReduc[1].add(reducTxt);
		helpPanelsReduc[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelReductionProduct6[0].add(reductionProduct6);
		helpPanelReductionProduct6[1].add(reductionProduct6Txt);
		helpPanelReductionProduct6[1].add(new EuroLabel(JLabel.LEFT));
		helpPanelReductionProduct21[0].add(reductionProduct21);
		helpPanelReductionProduct21[1].add(reductionProduct21Txt);
		helpPanelReductionProduct21[1].add(new EuroLabel(JLabel.LEFT));

		panels[categories.size()].add(helpPanelsArti[0]);
		panels[categories.size()].add(helpPanelsArti[1]);
		panels[categories.size()].add(helpPanelsAmni[0]);
		panels[categories.size()].add(helpPanelsAmni[1]);
		panels[categories.size()].add(helpPanelsGiftsOut[0]);
		panels[categories.size()].add(helpPanelsGiftsOut[1]);
		panels[categories.size()].add(helpPanelsGiftsIn[0]);
		panels[categories.size()].add(helpPanelsGiftsIn[1]);
		panels[categories.size()].add(helpPanelsReduc[0]);
		panels[categories.size()].add(helpPanelsReduc[1]);
		panels[categories.size()].add(helpPanelReductionProduct6[0]);
		panels[categories.size()].add(helpPanelReductionProduct6[1]);
		panels[categories.size()].add(helpPanelReductionProduct21[0]);
		panels[categories.size()].add(helpPanelReductionProduct21[1]);

		if (collapseCategories) {
			panels[categories.size()].closePanel();
		}
	}

	private JPanel createTotalPanel() {
		// Weergave van totale som voor deze bestelling
		JPanel totalPanel = new JPanel();
		totalPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Totaal"), BorderFactory.createEmptyBorder(0, 5, 0, 5))); // T,L,B,R

		totalLabel = new JLabel("Voorlopig totaal:");
		totalLabel.setFont(new Font("Tahoma", 0, 12));
		totalAmountLabel = new JLabel("0,00");
		totalAmountLabel.setFont(new Font("Tahoma", 0, 12));
		totalPanel.add(totalLabel);
		totalPanel.add(totalAmountLabel);
		totalPanel.add(new EuroLabel());

		return totalPanel;
	}

	private JPanel createStartBedragPanel() {
		JPanel startBedragPanel = new JPanel(new GridLayout(3, 2));
		startBedragPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Kassa"), BorderFactory.createEmptyBorder(0, 5, 0, 5))); // T,L,B,R

		startLabel.setHorizontalAlignment(JLabel.RIGHT);
		startLabel.setFont(new Font("Tahoma", 0, 12));
		startAmountLabel = new JLabel(HelpMethods.toAmount(startAmount) + Constants.EURO);
		startAmountLabel.setHorizontalAlignment(JLabel.LEFT);
		startAmountLabel.setFont(new Font("Tahoma", 0, 12));

		kassaLabel.setHorizontalAlignment(JLabel.RIGHT);
		kassaLabel.setFont(new Font("Tahoma", 0, 12));
		inKassaLabel = new JLabel(HelpMethods.toAmount(kassaAmount) + Constants.EURO);
		inKassaLabel.setHorizontalAlignment(JLabel.LEFT);
		inKassaLabel.setFont(new Font("Tahoma", 0, 12));

		soldLabel.setHorizontalAlignment(JLabel.RIGHT);
		soldLabel.setFont(new Font("Tahoma", 0, 12));
		aldreadySoldLabel = new JLabel(HelpMethods.toAmount(kassaAmount - startAmount) + Constants.EURO);
		aldreadySoldLabel.setHorizontalAlignment(JLabel.LEFT);
		aldreadySoldLabel.setFont(new Font("Tahoma", 0, 12));

		soldLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		aldreadySoldLabel.setFont(new Font("Tahoma", Font.BOLD, 12));

		startBedragPanel.add(soldLabel);
		startBedragPanel.add(aldreadySoldLabel);
		startBedragPanel.add(kassaLabel);
		startBedragPanel.add(inKassaLabel);
		startBedragPanel.add(startLabel);
		startBedragPanel.add(startAmountLabel);

		return startBedragPanel;
	}

	public void reset() {
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			List<Article> articles = category.getArticles();
			int aantalArtikels = articles.size();
			for (int j = 0; j < aantalArtikels; j++) {
				sellCount[i][j].setText("");
				if (category.isLeeggoed()) {
					emptyBack[j].setText("");
				}
			}
			artiTxt.setText("");
			amniTxt.setText("");
			giftsOutTxt.setText("");
			giftsInTxt.setText("");
			reducTxt.setText("");
			reductionProduct6Txt.setText("");
			reductionProduct21Txt.setText("");
		}

		order.reset();
		updateTotalLabel();
	}

	private double getTotal() {
		double tmpTotal = 0;

		boolean setBorderRed;

		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			List<Article> articles = category.getArticles();

			setBorderRed = false;

			for (int j = 0; j < articles.size(); j++) {
				int count = 0;
				try {
					count = Integer.parseInt(sellCount[i][j].getText());
				} catch (NumberFormatException nfe) {
					sellCount[i][j].setText("");
				}
				tmpTotal += count * articles.get(j).getPrice();

				if (count != 0) {
					setBorderRed = true;
				}

				// verwerking teruggebracht leeggoed
				if (category.isLeeggoed()) {
					count = 0;
					try {
						count = Integer.parseInt(emptyBack[j].getText());
					} catch (NumberFormatException nfe) {
						emptyBack[j].setText("");
					}
					tmpTotal -= count * articles.get(j).getPrice();

					if (count != 0) {
						setBorderRed = true;
					}
				}
			}

			if (setBorderRed) {
				setBorderColor(panels[i], Color.RED);
			} else {
				setBorderColor(panels[i], null);
			}
		}

		setBorderRed = false;

		double amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(artiTxt.getText(), true);
		} catch (NumberFormatException nfe) {
			artiTxt.setText("");
		}
		tmpTotal += amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(amniTxt.getText(), true);
		} catch (NumberFormatException nfe) {
			amniTxt.setText("");
		}
		tmpTotal += amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(giftsOutTxt.getText(), true);
		} catch (NumberFormatException nfe) {
			giftsOutTxt.setText("");
		}
		tmpTotal += amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(giftsInTxt.getText(), true);
		} catch (NumberFormatException nfe) {
			giftsInTxt.setText("");
		}
		tmpTotal -= amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(reducTxt.getText(), true);
		} catch (NumberFormatException nfe) {
			reducTxt.setText("");
		}
		tmpTotal -= amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(reductionProduct6Txt.getText(), true);
		} catch (NumberFormatException nfe) {
			reductionProduct6Txt.setText("");
		}
		tmpTotal += amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		amount = 0.0;
		try {
			amount = HelpMethods.parseStringToDouble(reductionProduct21Txt.getText(), true);
		} catch (NumberFormatException nfe) {
			reductionProduct21Txt.setText("");
		}
		tmpTotal += amount;
		if (amount != 0.0) {
			setBorderRed = true;
		}

		if (setBorderRed) {
			setBorderColor(panels[categories.size()], Color.RED);
		} else {
			setBorderColor(panels[categories.size()], null);
		}

		return tmpTotal;
	}

	private void setBorderColor(OxfamJPanel panel, Color color) {
		((ClickableTitledBorder) ((CompoundBorder) panel.getBorder()).getOutsideBorder()).setTitleColor(color);
	}

	private void updateTotalLabel() {
		totalAmountLabel.setText(HelpMethods.toAmount(getTotal()));
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == OKButton) {
			order.reset();

			for (int i = 0; i < categories.size(); i++) {
				Category category = categories.get(i);
				List<Article> articles = category.getArticles();

				for (int j = 0; j < articles.size(); j++) {
					int count = 0;
					try {
						count = Integer.parseInt(sellCount[i][j].getText());
					} catch (NumberFormatException nfe) {
						sellCount[i][j].setText("");
					}
					order.addArticle(articles.get(j), count);

					// verwerking teruggebracht leeggoed
					if (category.isLeeggoed()) {
						count = 0;
						try {
							count = Integer.parseInt(emptyBack[j].getText());
						} catch (NumberFormatException nfe) {
							emptyBack[j].setText("");
						}
						order.addArticle(articles.get(j), -count);
					}
				}
			}

			double amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(artiTxt.getText(), true);
			} catch (NumberFormatException nfe) {
				artiTxt.setText("");
			}
			order.addExtra(0, amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(amniTxt.getText(),	true);
			} catch (NumberFormatException nfe) {
				amniTxt.setText("");
			}
			order.addExtra(1, amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(giftsOutTxt.getText(), true);
			} catch (NumberFormatException nfe) {
				giftsOutTxt.setText("");
			}
			order.addExtra(2, amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(giftsInTxt.getText(), true);
			} catch (NumberFormatException nfe) {
				giftsInTxt.setText("");
			}
			order.addExtra(3, -amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(reducTxt.getText(), true);
			} catch (NumberFormatException nfe) {
				reducTxt.setText("");
			}
			order.addExtra(4, -amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(reductionProduct6Txt.getText(), true);
			} catch (NumberFormatException nfe) {
				reductionProduct6Txt.setText("");
			}
			order.addExtra(5, amount);

			amount = 0.0;
			try {
				amount = HelpMethods.parseStringToDouble(reductionProduct21Txt.getText(), true);
			} catch (NumberFormatException nfe) {
				reductionProduct21Txt.setText("");
			}
			order.addExtra(6, amount);

			ConfirmOrderFrame bbf = new ConfirmOrderFrame(order, this);
			bbf.setVisible(true);
			enabled(false);
		}

		else if (e.getSource() == resetButton) {
			reset();
		}

		else if (e.getSource() == totalButton) {
			new ConfirmExitFrame(this).setVisible(true);
			enabled(false);
		}
	}

	public static void endDay() {
		writeTotal();
		totalLabel.setText("Dagopbrengst: ");
		totalAmountLabel.setText(HelpMethods.toAmount(totalOrder.getTotal()));
		totalOrder.reset();
		OKButton.setEnabled(false);
		resetButton.setEnabled(false);
		totalButton.setEnabled(false);
	}

	public static void process(Order order, boolean bancontact) {
		logger.info("Processing order");
		try {
			database.processOrderInDB(order);
		} catch (Exception e) {
			logger.error("Failed to process order in DB", e);
		}
		logger.info("Order successfully processed in DB");

		alreadySoldAmount += order.getTotal();

		if (!bancontact) {
			kassaAmount = HelpMethods.round(kassaAmount + order.getTotal());
			inKassaLabel.setText(HelpMethods.toAmount(kassaAmount) + Constants.EURO);
		}

		aldreadySoldLabel.setText(alreadySoldAmount + Constants.EURO);

		totalOrder.add(order);
		logger.info("Order added to dayTotal");

		writeBackUp(totalOrder);
		logger.info("Wrote backup");
		programStart = false;

		oxfamFrame.reset();
	}
	
	public static int getNbrOfArticles() {
		return nbrOfArticles;
	}

	private static void writeTotal() {
		String fileName = "Totaal (" + HelpMethods.currentDateAndTimeToString() + ").txt";

		try {
			File file = new File(fileName);
			Printer.printDayTotalToFile(totalOrder, file);
		} catch (Exception e) {
			logger.error("Fout bij schrijven naar file", e);
		}
	}

	private static void writeBackUp(Order order) {
		File oldBackup = new File("Backup.txt");
		if (programStart) {
			oldBackup.renameTo(new File("Backup (" + HelpMethods.currentDateAndTimeToString() + ").txt"));
			oldBackup = new File("Backup.txt");
		}
		
		Printer.printToFile(order, oldBackup);
	}

}
