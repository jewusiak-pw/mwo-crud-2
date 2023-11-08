package pl.edu.pw.mwotest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.mwotest.exceptions.ConcertNotFoundException;
import pl.edu.pw.mwotest.models.Concert;
import pl.edu.pw.mwotest.models.UpdatableConcertDataUpdateRequest;
import pl.edu.pw.mwotest.repositories.ConcertRepository;
import pl.edu.pw.mwotest.repositories.TicketSaleRepository;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ValidationService validationService;

    public Concert createConcert(Concert concert) {
        validationService.validate(concert);
        return concertRepository.save(concert);
    }

    public Concert getConcertById(int id) {
        return concertRepository.findById(id).orElseThrow(ConcertNotFoundException::new);
    }

    public Concert decreaseInitialTicketsNumber(int concertId, int ticketAmount) {
        var concertToUpdate = getConcertById(concertId);
        concertToUpdate.setInitialTicketsNumber(concertToUpdate.getInitialTicketsNumber() - ticketAmount);
        return concertRepository.save(concertToUpdate);
    }

    public void deleteConcert(int id) {
        var concertToDelete = getConcertById(id);
        concertRepository.delete(concertToDelete);
    }

    public Concert updateConcert(int cid, UpdatableConcertDataUpdateRequest request) {
        var concert = getConcertById(cid);
        if (request.getName() != null) {
            concert.setName(request.getName());
        }
        if (request.getDate() != null) {
            concert.setDate(request.getDate());
        }
        if (request.getPrice() != null) {
            concert.setPrice(request.getPrice());
        }
        validationService.validate(concert);
        return concertRepository.save(concert);
    }
}
