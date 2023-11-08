package pl.edu.pw.mwotest.models;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UpdatableTicketSaleDetailsUpdateRequest {
    private String clientEmail;

    private Address clientAddress;
}
