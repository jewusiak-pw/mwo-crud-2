package pl.edu.pw.mwotest.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UpdatableConcertDataUpdateRequest {
    private String name;

    private ZonedDateTime date;

    private BigDecimal price;
}
