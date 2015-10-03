package be.sefl.oxfam.panel;

import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import be.sefl.oxfam.border.ClickableTitledBorder;
import be.sefl.oxfam.frame.MainFrame;

/**
 * @author sefl
 */
public class OxfamJPanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 8217982183309628113L;

	private boolean isOpen = true;

	private OxfamJPanel backup;

	private MainFrame parent;

	public OxfamJPanel(MainFrame parent) {
		super();
		this.addMouseListener(this);
		this.parent = parent;
	}

	public OxfamJPanel(LayoutManager layout, MainFrame parent) {
		super(layout);
		this.addMouseListener(this);
		this.parent = parent;
	}

	public void mouseClicked(MouseEvent e) {
		if (!(this.getBorder() instanceof CompoundBorder)) {
			return;
		} else {
			CompoundBorder bComp = (CompoundBorder) this.getBorder();
			if (!(bComp.getOutsideBorder() instanceof ClickableTitledBorder)) {
				return;
			} else {
				Point p = e.getPoint();
				ClickableTitledBorder bClick = (ClickableTitledBorder) bComp.getOutsideBorder();

				if (bClick.isPointInTitle(this, p)) {
					if (isOpen) {
						this.closePanel();
					} else {
						this.openPanel();
					}
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void openPanel() {
		this.refill();
		isOpen = true;
		parent.pack();
	}

	public void closePanel() {
		backup = (OxfamJPanel) this.clone();
		this.removeAll();
		isOpen = false;
		parent.pack();
	}

	public Object clone() {
		OxfamJPanel clone = new OxfamJPanel(this.parent);

		clone.setBorder(this.getBorder());
		clone.setLayout(this.getLayout());

		while (this.getComponentCount() > 0) {
			clone.add(this.getComponent(0));
		}
		
		return clone;
	}

	public void refill() {
		while (backup.getComponentCount() > 0) {
			this.add(backup.getComponent(0));
		}
	}
}
