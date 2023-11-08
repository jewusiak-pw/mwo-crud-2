package pl.edu.pw.mwotest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pw.mwotest.exceptions.NotEnoughTicketsAvailableException;
import pl.edu.pw.mwotest.models.*;
import pl.edu.pw.mwotest.repositories.ConcertRepository;
import pl.edu.pw.mwotest.repositories.TicketSaleRepository;
import pl.edu.pw.mwotest.services.ConcertService;
import pl.edu.pw.mwotest.services.SalesService;
import pl.edu.pw.mwotest.utils.ComparationUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@SpringBootTest
public class SalesServiceIntegrationTest {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private SalesService salesService;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private TicketSaleRepository ticketSaleRepository;

    @Test
    void ticketSaleSuccessful() {
        // given
        var concert = concertRepository.save(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var saleRequest = TicketSaleRequest.builder().concertId(concert.getId()).numberOfTickets(10).clientAddress(Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa")
                .build()).clientEmail("email@p.pl").build();
        var expectedSale = TicketSale.builder().concert(concert).numberOfTickets(10).clientAddress(Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build())
                .clientEmail("email@p.pl").concert(concert).build();

        // when
        var sid = salesService.sellTickets(saleRequest).getId();

        // then
        var sale = ticketSaleRepository.findById(sid).get();
        Assertions.assertTrue(ComparationUtils.compareSaleFields(expectedSale, sale), "The created sale should be equal to the expected sale");
        Assertions.assertEquals(concert.getInitialTicketsNumber() - saleRequest.getNumberOfTickets(), sale.getConcert().getAvailableTickets());
    }

    @Test
    void sellTickets_shouldThrowNotEnoughTicketsException() {
        // given
        var concert = concertService.createConcert(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        TicketSaleRequest request = TicketSaleRequest.builder().concertId(concert.getId()).numberOfTickets(51).clientAddress(Address.builder().firstLine("1st").secondLine("2nd")
                .zipCode("00-001").city("WArszawa").build()).clientEmail("email@email.pl").build();

        // when / then
        Assertions.assertThrows(NotEnoughTicketsAvailableException.class, () -> salesService.sellTickets(request));
    }

    @Test
    void ticketSaleIsFetchedWithConcert_AvailableTickets_ClientAddress() {
        // given
        var concert1 = concertService.createConcert(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var sale1 = salesService.sellTickets(TicketSaleRequest.builder().concertId(concert1.getId()).numberOfTickets(10).clientAddress(Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("email@email.pl").build());

        // when
        var sale2 = salesService.getTicketSaleById(sale1.getId());

        // then
        Assertions.assertTrue(ComparationUtils.compareSaleFields(sale1, sale2), "The fetched sale should be equal to the created sale");
        Assertions.assertTrue(ComparationUtils.compareConcertFields(concert1, sale2.getConcert()), "The fetched concert should be equal to the created concert");
        Assertions.assertEquals(40, sale2.getConcert().getAvailableTickets(), "The fetched available tickets should be equal to the created available tickets");
    }

    @Test
    void ticketSaleUpdateSuccessful() {
        // given
        var concert = concertRepository.save(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var sale = ticketSaleRepository.save(TicketSale.builder().concert(concert).numberOfTickets(10).clientAddress(Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("email@email.pl").build());
        var request = UpdatableTicketSaleDetailsUpdateRequest.builder().clientAddress(Address.builder().firstLine("new 1st").secondLine("new 2nd").zipCode("00-002").build()).clientEmail("email2@pl.pl").build();
        var expectedSale = TicketSale.builder().id(sale.getId()).concert(concert).numberOfTickets(10).clientAddress(Address.builder().firstLine("new 1st").secondLine("new 2nd").zipCode("00-002").city("WArszawa").build()).clientEmail("email2@pl.pl").build();

        // when
        salesService.updateTicket(sale.getId(), request);

        // then
        var updatedSale = ticketSaleRepository.findById(sale.getId()).get();
        Assertions.assertTrue(ComparationUtils.compareSaleFields(expectedSale, updatedSale), "The updated sale should be equal to the expected sale");
    }

    @Test
    void deleteTicketSaleTest() {
        // given
        var concert = concertRepository.save(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var sale = ticketSaleRepository.save(TicketSale.builder().concert(concert).numberOfTickets(10).clientAddress(Address.builder().firstLine("1st").secondLine("2nd").zipCode("00-001").city("WArszawa").build()).clientEmail("email@email.pl").build());

        // when
        salesService.deleteTicketSale(sale.getId());

        // then
        Assertions.assertFalse(ticketSaleRepository.findById(sale.getId()).isPresent(), "The sale should not be present in the database");
        Assertions.assertEquals(50, concertRepository.findById(concert.getId()).get().getAvailableTickets(), "The concert should have 50 available tickets");
    }
}
