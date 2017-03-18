package be.sefl.oxfam.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * @author sefl
 */
public class ExceptionFrame extends MainFrame {
	
	private static final long serialVersionUID = -8136481598030050052L;

	/** Label. */
	private JLabel exceptionLabel;

	/** Button. */
	private JButton okButton = new JButton("OK");

	public ExceptionFrame(String title, String exception, MainFrame parent, boolean boxes) {
		super(title, parent, boxes);
		
		exceptionLabel = new JLabel(exception);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		this.addComponent(exceptionLabel);
		this.addControl(okButton);

		this.pack();
		
		this.setLocation();
	}
}
