package lk.icbt.dental.model.strategy;

import lk.icbt.dental.model.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public BigDecimal calculateTotal(BigDecimal consultationFee, BigDecimal treatmentFee) {
        if (consultationFee == null || treatmentFee == null) {
            throw new ValidationException("Consultation fee and treatment fee are required.");
        }
        if (consultationFee.signum() < 0 || treatmentFee.signum() < 0) {
            throw new ValidationException("Bill values cannot be negative.");
        }
        return consultationFee.add(treatmentFee).setScale(2, RoundingMode.HALF_UP);
    }
}
