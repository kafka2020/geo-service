package ru.netology.i18n;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationServiceImplTest {

    @ParameterizedTest
    @EnumSource(Country.class)
    void locale_ShouldReturnCorrectMessageForAllCountries(Country country) {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String result = localizationService.locale(country);

        if (country == Country.RUSSIA) {
            assertEquals("Добро пожаловать", result);
        } else {
            assertEquals("Welcome", result);
        }
    }
}