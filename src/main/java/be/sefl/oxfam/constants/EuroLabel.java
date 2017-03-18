package be.sefl.oxfam.constants;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * @author sefl
 */
public class EuroLabel extends JLabel {

	private static final long serialVersionUID = 2262104213408206609L;

	public EuroLabel() {
		super(Constants.EURO);
		setFont(new Font("Tahoma",0,12));
	}

	public EuroLabel(int horizontalAlignment) {
		super(Constants.EURO, horizontalAlignment);
		setFont(new Font("Tahoma",0,12));
	}
}
