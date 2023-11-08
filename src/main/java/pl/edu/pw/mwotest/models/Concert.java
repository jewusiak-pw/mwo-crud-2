package pl.edu.pw.mwotest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.jdbc.repository.query.Query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Concert {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Concert name cannot be null")
    private String name;

    @NotNull(message = "Concert date cannot be null")
    private ZonedDateTime date;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "initialTicketsNumber cannot be null")
    @Min(value = 0, message = "initialTicketsNumber cannot be negative")
    private Integer initialTicketsNumber;

    @Formula("select initialTicketsNumber - coalesce(sum(ts.numberOfTickets), 0) from TicketSale ts where ts.concert_id = id")
    @Setter(AccessLevel.NONE)
    private Integer availableTickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concert concert = (Concert) o;
        return Objects.equals(id, concert.id) && Objects.equals(name, concert.name) && Objects.equals(date, concert.date) && Objects.equals(price, concert.price) && Objects.equals(initialTicketsNumber, concert.initialTicketsNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, price, initialTicketsNumber);
    }
}
