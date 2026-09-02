package lk.icbt.dental.model.strategy;

import java.math.BigDecimal;

public interface BillingStrategy {
    BigDecimal calculateTotal(BigDecimal consultationFee, BigDecimal treatmentFee);
}
