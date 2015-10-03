package be.sefl.oxfam.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;


/**
 * @author sefl
 */
public class ConfirmExitFrame extends MainFrame {

	private static final long serialVersionUID = 3776027741501100471L;

	//---------- Buttons ----------\\
	private JButton OK = new JButton("Ja");
	private JButton nOK = new JButton("Nee");

	public ConfirmExitFrame(MainFrame parent) {
		super("Bevestig afsluiten", parent, false);
		
		this.addComponent(new JLabel("Ben je zeker dat je je winkelmoment wil afsluiten?")); 
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableParent(true);
				OxfamFrame.endDay();
				frame.remove();
			}
		});
		nOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableParent(true);
				frame.remove();
			}
		});

		this.addControl(OK);
		this.addControl(nOK);
		
		this.pack();
		
		this.setLocation();
	}
}