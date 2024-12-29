package org.dao;

import org.dto.CurrencyDTO;
import org.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {
    List<Currency> findAll();
    void save(CurrencyDTO dto);
    Optional<Currency> findByCode(String code);


}