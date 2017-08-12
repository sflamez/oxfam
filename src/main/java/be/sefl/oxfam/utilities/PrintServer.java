package be.sefl.oxfam.utilities;

import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.SwingWorker;

public class PrintServer extends SwingWorker {

    protected Object doInBackground() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            while(true) {
                (new PrinterThread(serverSocket.accept())).start();
                Thread.sleep(1000L);
            }

            return null;
        } catch (IOException e) {
            System.err.println("Failed to open up server socket on port 9999");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
