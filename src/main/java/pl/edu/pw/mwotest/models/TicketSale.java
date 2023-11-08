package pl.edu.pw.mwotest.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @ManyToOne(optional = false)
    @NotNull(message = "Concert has to be assigned to a ticket sale.")
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Email(message = "ClientEmail must be a valid email.")
    private String clientEmail;

    @Embedded
    @NotNull(message = "Client address cannot be null.")
    private Address clientAddress;

    @NotNull(message = "Ticket number cannot be null")
    @Min(value = 0, message = "Number of tickets cannot be negative")
    private Integer numberOfTickets;
}
