package pl.edu.pw.mwotest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pw.mwotest.exceptions.ValidationFailedException;
import pl.edu.pw.mwotest.models.Address;
import pl.edu.pw.mwotest.services.ValidationService;

public class ValidationsTests {

    @Nested
    @SpringBootTest
    class ValidationServiceIntegrationTest {

        @Autowired
        private ValidationService validationService;

        @ParameterizedTest
        @ValueSource(strings = {"1", "THIS IS 36 CHARS AAAAAAAAAAAAAAAAAAA", ""})
        @NullSource
        void invalidFirstLineAddressTest(String firstLine) {
            Address address = Address.builder().firstLine(firstLine).zipCode("00-000").city("asd").build();
            Assertions.assertThatThrownBy(() -> validationService.validate(address)).isInstanceOf(ValidationFailedException.class).hasMessageContaining("1st line");
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "THIS IS 36 CHARS AAAAAAAAAAAAAAAAAAA", ""})
        @NullSource
        void invalidCityAddressTest(String city) {
            Address address = Address.builder().firstLine("firstLine").zipCode("00-000").city(city).build();
            Assertions.assertThatThrownBy(() -> validationService.validate(address)).isInstanceOf(ValidationFailedException.class).hasMessageContaining("City");
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "THIS IS 13 CH", ""})
        @NullSource
        void invalidZipcodeAddressTest(String zip) {
            Address address = Address.builder().firstLine("firstLine").zipCode(zip).city("city").build();
            Assertions.assertThatThrownBy(() -> validationService.validate(address)).isInstanceOf(ValidationFailedException.class).hasMessageContaining("Zip");
        }

        @ParameterizedTest
        @ValueSource(strings = {"22", "35353535353535353535353535353353535", "Czerniakowska 100"})
        void validFirstLine(String firstLine) {
            Address address = Address.builder().firstLine(firstLine).zipCode("00-100").city("city").build();
            validationService.validate(address);
            assert true;
        }

        @ParameterizedTest
        @ValueSource(strings = {"22", "35353535353535353535353535353353535", "Warszawa"})
        void validCity(String city) {
            Address address = Address.builder().firstLine("first").zipCode("00-100").city(city).build();
            validationService.validate(address);
            assert true;
        }

        @ParameterizedTest
        @CsvFileSource(files = "src/test/resources/zipcodes.csv")
        void validZip(String zip) {
            Address address = Address.builder().firstLine("first").zipCode(zip).city("city").build();
            validationService.validate(address);
            assert true;
        }
    }
}
