package org.dto;

import org.model.Currency;

public record ExchangeRateDTO(int id,Currency baseCurrency, Currency targetCurrency, double rate ) {
}
