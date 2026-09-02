package lk.icbt.dental.model.strategy;

import lk.icbt.dental.model.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardBillingStrategyTest {
    private final StandardBillingStrategy strategy = new StandardBillingStrategy();

    @Test
    void shouldAddConsultationAndTreatmentFees() {
        BigDecimal total = strategy.calculateTotal(
                new BigDecimal("2500.00"), new BigDecimal("7500.00"));

        assertEquals(new BigDecimal("10000.00"), total);
    }

    @Test
    void shouldRejectNegativeFees() {
        assertThrows(ValidationException.class, () -> strategy.calculateTotal(
                new BigDecimal("-1.00"), new BigDecimal("5000.00")));
    }
}
