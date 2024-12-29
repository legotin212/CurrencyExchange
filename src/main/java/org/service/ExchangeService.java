package org.service;

import org.dto.ExchangeResponseDTO;

public interface ExchangeService {
    ExchangeResponseDTO exchange(String baseCurrencyCode, String TargetCurrencyCode, double amount);
}
