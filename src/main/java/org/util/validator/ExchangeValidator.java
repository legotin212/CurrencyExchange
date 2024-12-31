package org.util.validator;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeValidator {
    public Optional<String> validate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {

        if (!baseCurrencyCode.matches("[A-Z]{3}")) {
            return Optional.of("Base currency code must consist of 3 uppercase Latin letters.");
        }
        if (!targetCurrencyCode.matches("[A-Z]{3}")) {
            return Optional.of("Target currency code must consist of 3 uppercase Latin letters.");
        }

        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            return Optional.of("Base currency code and target currency code cannot be the same.");
        }
        if (rate.equals(BigDecimal.ZERO) || rate.compareTo(BigDecimal.ZERO) < 0) {
            return Optional.of("Exchange rate must be a positive number.");
        }
        return Optional.empty();
    }
}
