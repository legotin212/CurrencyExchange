package org.dto;

import org.model.Currency;

public record ExchangeResponseDTO(Currency baseCurrency, Currency targetCurrency, double rate, double amount, double convertedAmount) {
}
