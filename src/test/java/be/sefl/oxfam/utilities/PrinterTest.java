package be.sefl.oxfam.utilities;

import static org.junit.Assert.assertTrue;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.junit.Ignore;
import org.junit.Test;

import be.sefl.oxfam.object.Order;

public class PrinterTest {

	@Test
	public void testPrinterCapabilities() throws Exception {
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

		System.out.println(printService.getName());
		assertTrue(printService.isDocFlavorSupported(DocFlavor.INPUT_STREAM.AUTOSENSE));
	}

	@Ignore
	@Test
	public void testPrint() throws Exception {
		Order order = new Order();
		Printer.print(order);
	}

}
