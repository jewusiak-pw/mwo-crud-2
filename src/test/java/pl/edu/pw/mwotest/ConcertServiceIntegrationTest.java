package pl.edu.pw.mwotest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pw.mwotest.models.Address;
import pl.edu.pw.mwotest.models.Concert;
import pl.edu.pw.mwotest.models.TicketSaleRequest;
import pl.edu.pw.mwotest.models.UpdatableConcertDataUpdateRequest;
import pl.edu.pw.mwotest.repositories.ConcertRepository;
import pl.edu.pw.mwotest.repositories.TicketSaleRepository;
import pl.edu.pw.mwotest.services.ConcertService;
import pl.edu.pw.mwotest.services.SalesService;
import pl.edu.pw.mwotest.utils.ComparationUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

@SpringBootTest
@Slf4j
public class ConcertServiceIntegrationTest {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private SalesService salesService;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private TicketSaleRepository ticketSaleRepository;

    @ParameterizedTest
    @MethodSource("concertProvider")
    void concertCreatedSuccessfully(Concert concert) {
        log.info("Testing concert: {}", concert.getName());
        // given parameter above

        // when
        var createdConcert = concertService.createConcert(concert);

        // then
        Assertions.assertTrue(ComparationUtils.compareConcertFields(concert, createdConcert), "The created concert should be equal to the expected concert");
    }


    static Stream<Arguments> concertProvider() {
        return Stream.of(
                Arguments.of(Concert.builder().name("1name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build()),
                Arguments.of(Concert.builder().name("2adfasdfgsfsa").date(ZonedDateTime.now().plusDays(1)).initialTicketsNumber(40).price(BigDecimal.valueOf(100)).build()),
                Arguments.of(Concert.builder().name("3as").date(ZonedDateTime.now().plusDays(2)).initialTicketsNumber(35).price(BigDecimal.valueOf(482913067245183756201482374928013726.23)).build()),
                Arguments.of(Concert.builder().name("4fsadafdsadfsfsdafsd").date(ZonedDateTime.now().plusDays(3)).initialTicketsNumber(400000).price(BigDecimal.valueOf(1023213.0)).build()),
                Arguments.of(Concert.builder().name("5dsfdfsasf").date(ZonedDateTime.now().plusDays(4)).initialTicketsNumber(Integer.MAX_VALUE).price(BigDecimal.valueOf(10.1230)).build()),
                Arguments.of(Concert.builder().name("6afssdfa").date(ZonedDateTime.now().plusDays(5)).initialTicketsNumber(1).price(BigDecimal.valueOf(1444440.0)).build()),
                Arguments.of(Concert.builder().name("7sadffsa").date(ZonedDateTime.now().plusDays(6)).initialTicketsNumber(1000).price(BigDecimal.valueOf(66610.0)).build()),
                Arguments.of(Concert.builder().name("8afsdd").date(ZonedDateTime.now().plusDays(7)).initialTicketsNumber(51).price(BigDecimal.valueOf(110.0)).build())
        );
    }


    @Test
    void deleteConcertSuccessfully() {
        // given
        var concert = concertService.createConcert(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var cid = concertRepository.save(concert).getId();

        // when
        concertService.deleteConcert(cid);

        // then
        Assertions.assertFalse(concertRepository.findById(cid).isPresent(), "The concert should not be present in the database");
    }

    @Test
    void updateConcertSuccessfully() {
        // given
        var concert = concertRepository.save(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());
        var request = UpdatableConcertDataUpdateRequest.builder().name("new name").date(concert.getDate().plusDays(1)).build();
        var expectedConcert = Concert.builder().id(concert.getId()).name(request.getName()).date(concert.getDate().plusDays(1)).initialTicketsNumber(concert.getInitialTicketsNumber()).price(concert.getPrice()).build();

        // when
        concertService.updateConcert(concert.getId(), request);

        // then
        Assertions.assertTrue(ComparationUtils.compareConcertFields(expectedConcert, concertRepository.findById(concert.getId()).get()), "The updated concert should be equal to the expected concert");
    }

    @Test
    void readConcertSuccessfully() {
        // given
        var concert = concertRepository.save(Concert.builder().name("name").date(ZonedDateTime.now()).initialTicketsNumber(50).price(BigDecimal.valueOf(10.0)).build());

        // when
        var readConcert = concertService.getConcertById(concert.getId());

        // then
        Assertions.assertTrue(ComparationUtils.compareConcertFields(concert, readConcert), "The read concert should be equal to the expected concert");
    }
}
