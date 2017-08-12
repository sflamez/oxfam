package be.sefl.oxfam.utilities;

import be.sefl.oxfam.object.Order;

import java.io.ObjectInputStream;
import java.net.Socket;

public class PrinterThread extends Thread {
    private Socket socket;

    public PrinterThread(Socket socket) {
        super("PrinterThread");
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            Order order = (Order) inputStream.readObject();
            System.out.println("Received an order:");
            System.out.println(order);
            System.out.println("Trying to print order...");
            Printer.print(order);
            inputStream.close();
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
