package be.sefl.oxfam.utilities;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintServer extends SwingWorker<Object, Object> {
	
	private static final Logger logger = LoggerFactory.getLogger("PrintServer");

	protected Object doInBackground() throws Exception {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			while (true) {
				(new PrinterThread(serverSocket.accept())).start();
				Thread.sleep(1000L);
			}
		} catch (IOException e) {
			logger.error("Failed to open up server socket on port 9999");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}
}
