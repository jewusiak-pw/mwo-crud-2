package pl.edu.pw.mwotest.utils;

import pl.edu.pw.mwotest.models.Concert;
import pl.edu.pw.mwotest.models.TicketSale;

public class ComparationUtils {
    public static boolean compareSaleFields(TicketSale sale1, TicketSale sale2) {
        return sale1 == sale2 || sale1 != null && sale2 != null && sale1.getNumberOfTickets().equals(sale2.getNumberOfTickets()) && sale1.getClientAddress().equals(sale2.getClientAddress()) && sale1.getClientEmail().equals(sale2.getClientEmail()) && sale1.getConcert().getId().equals(sale2.getConcert().getId());
    }

    public static boolean compareConcertFields(Concert concert1, Concert concert2) {
        return concert1 == concert2 || concert1 != null && concert2 != null && concert1.getName().equals(concert2.getName()) && concert1.getDate().toEpochSecond() == concert2.getDate().toEpochSecond() && concert1.getInitialTicketsNumber().equals(concert2.getInitialTicketsNumber()) && concert1.getPrice().compareTo(concert2.getPrice()) == 0;
    }
}
