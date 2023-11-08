package pl.edu.pw.mwotest.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.mwotest.exceptions.NotEnoughTicketsAvailableException;
import pl.edu.pw.mwotest.models.TicketSale;
import pl.edu.pw.mwotest.models.TicketSaleRequest;
import pl.edu.pw.mwotest.models.UpdatableTicketSaleDetailsUpdateRequest;
import pl.edu.pw.mwotest.repositories.TicketSaleRepository;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final TicketSaleRepository ticketSaleRepository;
    private final ConcertService concertService;
    private final ValidationService validationService;

    public TicketSale sellTickets(TicketSaleRequest request) {
        var concert = concertService.getConcertById(request.getConcertId());

        if (concert.getAvailableTickets() < request.getNumberOfTickets()) {
            throw new NotEnoughTicketsAvailableException();
        }

        var ticketSale = TicketSale.builder().concert(concert).numberOfTickets(request.getNumberOfTickets()).clientAddress(request.getClientAddress()).clientEmail(request.getClientEmail()).build();

        validationService.validate(ticketSale);
        return ticketSaleRepository.save(ticketSale);
    }

    public void deleteTicketSale(int id) {
        var ticketSaleToDelete = getTicketSaleById(id);
        ticketSaleRepository.delete(ticketSaleToDelete);
    }

    public TicketSale getTicketSaleById(int id) {
        return ticketSaleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ticket sale with id " + id + " not found"));
    }

    public TicketSale updateTicket(int tid, UpdatableTicketSaleDetailsUpdateRequest request){
        var ticketSale = getTicketSaleById(tid);
        if (request.getClientAddress() != null) {
            if (request.getClientAddress().getFirstLine() != null) {
                ticketSale.getClientAddress().setFirstLine(request.getClientAddress().getFirstLine());
            }
            if (request.getClientAddress().getSecondLine() != null) {
                ticketSale.getClientAddress().setSecondLine(request.getClientAddress().getSecondLine());
            }
            if (request.getClientAddress().getZipCode() != null) {
                ticketSale.getClientAddress().setZipCode(request.getClientAddress().getZipCode());
            }
            if (request.getClientAddress().getCity() != null) {
                ticketSale.getClientAddress().setCity(request.getClientAddress().getCity());
            }
        }
        if (request.getClientEmail() != null) {
            ticketSale.setClientEmail(request.getClientEmail());
        }
        validationService.validate(ticketSale);
        return ticketSaleRepository.save(ticketSale);
    }

}
