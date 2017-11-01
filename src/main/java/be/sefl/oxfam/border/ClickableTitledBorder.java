package be.sefl.oxfam.border;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * @author Flamooze
 */
public class ClickableTitledBorder extends TitledBorder {

	private static final long serialVersionUID = 156226294943644957L;

	private Point textLoc = new Point();

	public ClickableTitledBorder(String title) {
		super(title);
	}

	public boolean isPointInTitle(JComponent jc, Point p) {
		Font font = getTitleFont();
		FontMetrics fm = jc.getFontMetrics(font);

		textLoc.y = 1;
		textLoc.x = 10;

		int stringWidth = SwingUtilities.computeStringWidth(fm, getTitle());
		int stringHeight = fm.getHeight();

		return (p.getX() >= textLoc.x && p.getX() <= (textLoc.x + stringWidth) &&
				p.getY() >= textLoc.y && p.getY() <= (textLoc.y + stringHeight));
	}

}
