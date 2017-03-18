package be.sefl.oxfam.main;

import javax.swing.JFrame;
import javax.swing.UIManager;

import jpos.util.JposPropertiesConst;
import be.sefl.oxfam.frame.StartFrame;

public class Oxfam {
	public static void main(String[] args) {
		System.setProperty(JposPropertiesConst.JPOS_POPULATOR_FILE_PROP_NAME, "c:\\jpos.xml");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.err.println(e.getStackTrace());
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
