package org.dto;

import org.model.Currency;

import java.math.BigDecimal;

public record ExchangeResponseDTO(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
}
