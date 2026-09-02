package lk.icbt.dental.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppointmentNumberGeneratorTest {
    @Test
    void shouldGenerateNumberWithDateAndFourDigitSuffix() {
        String number = AppointmentNumberGenerator.generate(LocalDate.of(2026, 5, 20));
        assertTrue(number.matches("APT-20260520-[0-9]{4}"));
    }
}
