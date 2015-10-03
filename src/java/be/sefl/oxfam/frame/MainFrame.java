package be.sefl.oxfam.frame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author sefl
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -3226522373263645027L;

	//-------- MainFrame --------\\
	protected MainFrame frame;
	protected MainFrame parent;
	
	//---------- Panels ----------\\
	private JPanel mainPanel;
	private JPanel textPanel;
	private JPanel controlPanel;

	public MainFrame(String title, MainFrame parent, boolean boxes) {
		super(title);
		
		this.addWindowListener(new WindowListener() {
		    public void windowClosing(WindowEvent e) {
				enableParent(true);
				remove();
		    }
  		    public void windowOpened(WindowEvent e) {}
		    public void windowClosed(WindowEvent e) {}
		    public void windowIconified(WindowEvent e) {}
		    public void windowDeiconified(WindowEvent e) {}
		    public void windowActivated(WindowEvent e) {}
		    public void windowDeactivated(WindowEvent e) {}
		});

		this.frame = this;
		this.parent = parent;

		//Create the main panel to contain the two sub panels
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

		//Panel for text
		textPanel = new JPanel();
		if (boxes) {
			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
		}
		textPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		//Panel for controls
		controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));

		//Add the text and control panels to the main panel
		mainPanel.add(textPanel);
		mainPanel.add(controlPanel);
		
		this.setContentPane(mainPanel);
	}
	
	protected void setLocation() {
		//Center the window.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		
		frame.setLocation((screenSize.width-frameSize.width)/2,(screenSize.height-frameSize.height)/2);
	}
	
	public void addControl(JButton button) {
		this.controlPanel.add(button);
	}

	public void addComponent(JComponent component) {
		this.textPanel.add(component);
	}

	public void setTextPanelDimension(int rows, int columns) {
		this.textPanel.setLayout(new GridLayout(rows, columns));
	}
	
	protected void enabled(boolean enabled) {
		this.setVisible(enabled);
	}

	protected void enableParent(boolean enabled) {
		if (parent != null) {
			parent.enabled(enabled);
		}
	}

	protected void remove() {
		this.dispose();
	}
}
