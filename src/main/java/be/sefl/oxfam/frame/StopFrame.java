package be.sefl.oxfam.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author sefl
 */
public class StopFrame extends MainFrame {

	private static final long serialVersionUID = 2592747571912856026L;

	//---------- Buttons ----------\\
	private JButton OK = new JButton("Ja");
	private JButton nOK = new JButton("Nee");

	public StopFrame(MainFrame parent) {
		super("Bevestig afsluiten", parent, false);

		this.addComponent(new JLabel("Ben je zeker dat je het programma wil afsluiten?"));
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
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