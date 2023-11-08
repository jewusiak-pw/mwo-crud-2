package pl.edu.pw.mwotest.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketSaleRequest {
    private Integer concertId;

    private String clientEmail;

    private Address clientAddress;

    private Integer numberOfTickets;
}
