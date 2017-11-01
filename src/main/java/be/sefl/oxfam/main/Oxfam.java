package be.sefl.oxfam.main;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jpos.util.JposPropertiesConst;
import be.sefl.oxfam.frame.StartFrame;

public class Oxfam {

	private static final Logger logger = LoggerFactory.getLogger("Oxfam");

	public static void main(String[] args) {
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "c:\\jpos.xml");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			logger.error("Failed to set look and feel", e);
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                StartFrame frame = new StartFrame();
				frame.setVisible(true);
            }
        });
	}
}
