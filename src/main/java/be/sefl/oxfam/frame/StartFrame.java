package be.sefl.oxfam.frame;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import be.sefl.oxfam.utilities.HelpMethods;
import be.sefl.oxfam.utilities.PrintServer;

/**
 * @author sefl
 */
public class StartFrame extends MainFrame {

	private static final long serialVersionUID = -8806543956467615918L;

	/** Textfield */
	private JTextField textField;

	/** Checkboxes with selection for collapsing categories. */
	private ButtonGroup collapseCategories;
	private JRadioButton collapseCategoriesNo;
	private JRadioButton collapseCategoriesYes;

	private JCheckBox isServer;

	/** Buttons */
	private JButton OK = new JButton("Start");
	private JButton nOK = new JButton("Stop");

	/** Variables */
	private double startBedrag;

	public StartFrame() {
		super("Bedrag in kassa", null, false);
		
		this.addWindowListener(new WindowListener() {
		    public void windowClosing(WindowEvent e) {
				System.exit(0);
		    }
  		    public void windowOpened(WindowEvent e) {}
		    public void windowClosed(WindowEvent e) {}
		    public void windowIconified(WindowEvent e) {}
		    public void windowDeiconified(WindowEvent e) {}
		    public void windowActivated(WindowEvent e) {}
		    public void windowDeactivated(WindowEvent e) {}
		});

		this.setTextPanelDimension(3,2);
		JLabel startLabel = new JLabel("      Startbedrag:");
		startLabel.setFont(new Font("Tahoma",0,12));

		this.addComponent(startLabel);
		this.addComponent(textField = new JTextField());
		
		JLabel collapseLabel = new JLabel("Categorieën dicht?");
		collapseLabel.setFont(new Font("Tahoma",0,12));

		this.addComponent(collapseLabel);
		
		JPanel choice = new JPanel(new GridLayout(1,2));
		collapseCategories = new ButtonGroup();
		collapseCategories.add(collapseCategoriesYes = new JRadioButton("Ja"));
		collapseCategories.add(collapseCategoriesNo = new JRadioButton("Nee"));
		choice.add(collapseCategoriesYes);
		choice.add(collapseCategoriesNo);
		
		this.addComponent(choice);

		JLabel printServerLabel = new JLabel("Op PC met printer?");
		printServerLabel.setFont(new Font("Tahoma", 0, 12));
		this.addComponent(printServerLabel);
		this.addComponent(this.isServer = new JCheckBox());

		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().equals("")) {
					startBedrag = HelpMethods.parseStringToDouble(textField.getText(),true);
				} else {
					startBedrag = 0.0;
				}
				System.setProperty("runsAsOxfamServer", String.valueOf(isServer.isSelected()));
				if (isServer.isSelected()) {
					new PrintServer().execute();
				}

				javax.swing.SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		                OxfamFrame.init(startBedrag, collapseCategoriesYes.isSelected());
		            }
		        });
				frame.remove();
			}
		});
		nOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.addControl(OK);
		this.addControl(nOK);
		
		this.pack();
		
		this.setLocation();
	}
}