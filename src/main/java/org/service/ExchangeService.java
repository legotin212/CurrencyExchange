package org.service;

import org.dto.ExchangeResponseDTO;

import java.math.BigDecimal;

public interface ExchangeService {
    ExchangeResponseDTO exchange(String baseCurrencyCode, String TargetCurrencyCode, BigDecimal amount);
}
