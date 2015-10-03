package be.sefl.oxfam.frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import be.sefl.oxfam.constants.Constants;
import be.sefl.oxfam.object.Order;
import be.sefl.oxfam.utilities.HelpMethods;

/**
 * @author sefl
 */
public class ChangeFrame extends MainFrame {

	private static final long serialVersionUID = -5967535826021367075L;

	//------------ Labels ------------\\
//	private JLabel amountToReturnLabel;

	//---------- Textfields ----------\\
	private JTextField textFieldPaid;

	//---------- Buttons ----------\\
	private JButton OK = new JButton("Bereken");
	private JButton nOK = new JButton("Terug");

	//---------- Variables ----------\\
	private double amountToPay;
	private double amountPaid;
//	private double amountToReturn;
	private Order order;


	public ChangeFrame(Order o, MainFrame parent) {
		super("Hoeveel teruggeven?", parent, false);

		this.setTextPanelDimension(2,2);

		this.order = o;
		this.amountToPay = this.order.getTotal();
		this.textFieldPaid = new JTextField();
		
		JLabel toPayLabel = new JLabel("Te betalen: ");
		toPayLabel.setFont(new Font("Tahoma",0,12));
		this.addComponent(toPayLabel);
		JLabel amountToPayLabel = new JLabel(HelpMethods.toAmount(amountToPay)+Constants.EURO);
		amountToPayLabel.setFont(new Font("Tahoma",0,12));
		this.addComponent(amountToPayLabel);
		JLabel paidLabel = new JLabel("Betaald bedrag: ");
		paidLabel.setFont(new Font("Tahoma",0,12));
		this.addComponent(paidLabel);
		this.addComponent(textFieldPaid);

//		JLabel toReturnLabel = new JLabel("Terug te geven: ");
//		toReturnLabel.setFont(new Font("Tahoma",0,12));
//		this.addComponent(toReturnLabel);
//		amountToReturnLabel = new JLabel();
//		amountToReturnLabel.setFont(new Font("Tahoma",0,12));
//		this.addComponent(amountToReturnLabel);
		
		OK.addActionListener(new ActionListener() {
/*			public void actionPerformed(ActionEvent e) {
				amountPaid = HelpMethods.parseStringToDouble(textFieldPaid.getText(),true);
				textFieldPaid.setText(HelpMethods.toAmount(amountPaid)+Constants.EURO);
				amountToReturn = HelpMethods.round(amountPaid-amountToPay);
				amountToReturnLabel.setFont(new Font("Tahoma",Font.BOLD,12));
				amountToReturnLabel.setText(HelpMethods.toAmount(amountToReturn)+Constants.EURO);
				order.setAmountPaid(amountPaid);
				order.setAmountToReturn(amountToReturn);
			}*/
			public void actionPerformed(ActionEvent e) {
				amountPaid = HelpMethods.parseStringToDouble(textFieldPaid.getText(),true);
				order.setAmountPaid(amountPaid);
				order.setAmountToReturn(HelpMethods.round(amountPaid-amountToPay));
				frame.parent = new ConfirmOrderFrame(order, frame.parent.parent);
				frame.enableParent(true);
				frame.remove();
			}
		});
		nOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.enableParent(true);
				frame.remove();
			}
		});

		this.addControl(OK);
		this.addControl(nOK);
		
		this.pack();
		
		this.setLocation();
	}
}
