package be.sefl.oxfam.utilities;

import be.sefl.oxfam.object.Order;

import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger("PrinterThread");

	private Socket socket;

    public PrinterThread(Socket socket) {
        super("PrinterThread");
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            Order order = (Order) inputStream.readObject();
            logger.info("Received an order:");
            logger.info(order.toString());
            logger.info("Trying to print order...");
            Printer.print(order);
            inputStream.close();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
