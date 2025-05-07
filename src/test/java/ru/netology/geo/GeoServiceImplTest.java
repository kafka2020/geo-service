package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GeoServiceImplTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    void byIp_ShouldReturnCorrectLocation(String ip, Location expected) {
        GeoService geoService = new GeoServiceImpl();

        Location result = geoService.byIp(ip);

        if (expected == null) {
            assertNull(result);
        } else {
            assertNotNull(result);
            assertEquals(expected.getCity(), result.getCity());
            assertEquals(expected.getCountry(), result.getCountry());
            assertEquals(expected.getStreet(), result.getStreet());
            assertEquals(expected.getBuiling(), result.getBuiling());
        }
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("127.0.0.1",
                        new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11",
                        new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149",
                        new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.1.2.3",
                        new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.1.2.3",
                        new Location("New York", Country.USA, null, 0)),
                Arguments.of("10.20.30.40", null)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "23.453532, 33.34445",
            "0, 0",
            "-32.32444, 9.3242424",
            "4433, 999980"
    })
    void byCoordinates_ShouldThrowRuntimeException(double latitude, double longitude) {
        GeoService geoService = new GeoServiceImpl();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> geoService.byCoordinates(latitude, longitude)
        );

        assertEquals("Not implemented", exception.getMessage());
    }
}