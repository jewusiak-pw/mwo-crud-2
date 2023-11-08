package pl.edu.pw.mwotest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.mwotest.exceptions.ValidationFailedException;
import pl.edu.pw.mwotest.models.Concert;
import pl.edu.pw.mwotest.repositories.ConcertRepository;
import pl.edu.pw.mwotest.repositories.TicketSaleRepository;
import pl.edu.pw.mwotest.services.ConcertService;
import pl.edu.pw.mwotest.services.SalesService;
import pl.edu.pw.mwotest.services.ValidationService;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;


public class ConcertServiceUnitTest {
    private ConcertRepository concertRepository;

    private ConcertService concertService;

    private ValidationService validationService;

    @BeforeEach
    void beforeEach() {
        concertRepository = Mockito.mock(ConcertRepository.class);
        validationService = Mockito.mock(ValidationService.class);
        concertService = Mockito.spy(new ConcertService(concertRepository, validationService));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 20, 30})
    void sellTicketsToConcertSuccessful(Integer numberOfTickets){
        // given
        var initialNumberOfTickets = 50;
        Concert concert = Concert.builder().name("Conc name").date(ZonedDateTime.now().plusDays(30)).price(new BigDecimal(10)).initialTicketsNumber(initialNumberOfTickets).build();
        Mockito.when(concertRepository.findById(Mockito.any())).thenReturn(Optional.of(concert));

        // when
        concertService.decreaseInitialTicketsNumber(1, numberOfTickets);

        // then
        Assertions.assertEquals(initialNumberOfTickets-numberOfTickets, concert.getInitialTicketsNumber());
    }

    @Test
    void createConcert_ValidInputs_CreatesSuccessfully() {
        // given
        var concert = Concert.builder().name("Conc name").date(ZonedDateTime.now().plusDays(30)).price(new BigDecimal(10)).initialTicketsNumber(50).build();
        var concertExpected = Concert.builder().id(1).name("Conc name").date(ZonedDateTime.now().plusDays(30)).price(new BigDecimal(10)).initialTicketsNumber(50).build();
        Mockito.when(concertRepository.save(Mockito.any())).thenReturn(concertExpected);

        // when
        var createdConcert = concertService.createConcert(concert);

        // then
        Mockito.verify(concertRepository).save(concert);
        Assertions.assertEquals(concertExpected, createdConcert, "The created concert should be equal to the expected concert");
    }

    @Test
    void onValidationException_createConcert_ThrowsException() {
        // given
        var concert = Concert.builder().name("Conc name").date(ZonedDateTime.now().plusDays(30)).price(new BigDecimal(10)).initialTicketsNumber(50).build();
        Mockito.doThrow(ValidationFailedException.class).when(validationService).validate(concert);

        // when / then
        Assertions.assertThrows(ValidationFailedException.class, () -> concertService.createConcert(concert));
        Mockito.verify(concertRepository, Mockito.never()).save(concert);
    }
}
