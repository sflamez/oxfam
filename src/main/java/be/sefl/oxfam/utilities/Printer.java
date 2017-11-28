package be.sefl.oxfam.utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.sefl.oxfam.object.Order;

/**
 * @author sefl
 */
public class Printer {

    private static final Logger logger = LoggerFactory.getLogger("Printer");

    private static PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

    public static void setPrinter(PrintService printer) {
        printService = printer;
    }

    public static void printToFile(Order order, File file) {
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(TicketCreator.createTicket(order, false));
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            logger.error("Failed to print order to file", ioe);
        }
    }

    public static void printDayTotalToFile(Order totalOrder, File file) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.write(TicketCreator.createTicket(totalOrder, true));
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            logger.error("Failed to print day total to file", ioe);
        }
    }

    public static void print(Order order) throws Exception {
        if (printService == null) {
            logger.error("No printService available");
            throw new RuntimeException("No ticket printer available.");
        }

        DocPrintJob printJob = printService.createPrintJob();
        byte[] ticket = TicketCreator.createTicket(order, false).getBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream(ticket.length + 3);
        out.write(ticket);
        // Add cut command.
        out.write(new byte[]{27, 100, 3});
        Doc doc = new SimpleDoc(out.toByteArray(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        printJob.print(doc, new HashPrintRequestAttributeSet());
    }

}
