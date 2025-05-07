package ru.netology.sender;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageSenderImplTest {

    @Mock
    private GeoService geoService;

    @Mock
    private LocalizationService localizationService;

    @InjectMocks
    private MessageSenderImpl messageSender;

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void send_ShouldReturnCorrectMessage(Map<String, String> headers,
                                         String expectedIp,
                                         Country country,
                                         String expectedMessage) {
        // Arrange
        if (expectedIp != null) {
            Location mockLocation = new Location(null, country, null, 0);
            when(geoService.byIp(eq(expectedIp))).thenReturn(mockLocation);
            when(localizationService.locale(eq(country))).thenReturn(expectedMessage);
        } else {
            when(localizationService.locale(eq(Country.USA))).thenReturn(expectedMessage);
        }

        // Act
        String result = messageSender.send(headers);

        // Assert
        assertEquals(expectedMessage, result);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                // Россия
                Arguments.of(
                        createHeaders("172.123.12.19"), // Используем метод-помощник
                        "172.123.12.19",
                        Country.RUSSIA,
                        "Добро пожаловать"
                ),
                // США
                Arguments.of(
                        createHeaders("96.44.183.149"),
                        "96.44.183.149",
                        Country.USA,
                        "Welcome"
                ),
                // Пустой IP
                Arguments.of(
                        createHeaders(""),
                        null,
                        null,
                        "Welcome"
                ),
                // Германия (другая страна)
                Arguments.of(
                        createHeaders("212.23.61.41"),
                        "212.23.61.41",
                        Country.GERMANY,
                        "Welcome"
                )
        );
    }

    // Метод-помощник для создания Map в Java 8
    private static Map<String, String> createHeaders(String ip) {
        Map<String, String> headers = new HashMap<>();
        if (ip != null) {
            headers.put("x-real-ip", ip);
        }
        return headers;
    }
}