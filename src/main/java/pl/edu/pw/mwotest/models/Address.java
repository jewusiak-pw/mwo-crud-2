package pl.edu.pw.mwotest.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Size(min = 2, max = 35, message = "1st line must be between 2 and 35 characters.")
    @NotNull(message = "1st line cannot be null.")
    private String firstLine;
    private String secondLine;
    @Size(min = 2, max = 12, message = "Zip code must be between 2 and 12 characters.")
    @NotNull(message = "Zip code cannot be null.")
    private String zipCode;
    @Size(min = 2, max = 35, message = "City must be between 2 and 35 characters.")
    @NotNull(message = "City cannot be null.")
    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(firstLine, address.firstLine) && Objects.equals(secondLine, address.secondLine) && Objects.equals(zipCode, address.zipCode) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstLine, secondLine, zipCode, city);
    }
}
