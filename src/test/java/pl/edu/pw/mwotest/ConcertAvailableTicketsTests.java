package pl.edu.pw.mwotest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pw.mwotest.models.Concert;
import pl.edu.pw.mwotest.models.TicketSale;
import pl.edu.pw.mwotest.models.TicketSaleRequest;
import pl.edu.pw.mwotest.services.ConcertService;
import pl.edu.pw.mwotest.services.SalesService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

@SpringBootTest
public class ConcertAvailableTicketsTests {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private SalesService salesService;

    @Test
    void testCalculationOfRemainingTickets() {
        // given
        var concert = concertService.createConcert(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var sale1 = salesService.sellTickets(TicketSaleRequest.builder().concertId(concert.getId()).numberOfTickets(10).clientAddress(pl.edu.pw.mwotest.models.Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("e@email.pl").build());
        var sale2 = salesService.sellTickets(TicketSaleRequest.builder().concertId(concert.getId()).numberOfTickets(10).clientAddress(pl.edu.pw.mwotest.models.Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("e@email.pl").build());
        var sale3 = salesService.sellTickets(TicketSaleRequest.builder().concertId(concert.getId()).numberOfTickets(22).clientAddress(pl.edu.pw.mwotest.models.Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("eeee@email.pl").build());

        // when
        var concert2 = concertService.getConcertById(concert.getId());

        // then
        Assertions.assertEquals(concert.getInitialTicketsNumber() - Stream.of(sale1, sale2, sale3).mapToInt(TicketSale::getNumberOfTickets).sum(),
                concert2.getAvailableTickets());
    }
}
