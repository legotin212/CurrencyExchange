package org.service;

import org.dto.CurrencyDTO;
import org.model.Currency;
import org.dao.CurrencyDAO;
import org.dao.CurrencyDAOImpl;
import java.util.List;
import java.util.Optional;

public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDAO currencyDAO;
    private static CurrencyServiceImpl instance;

    public static synchronized CurrencyServiceImpl getInstance(){
        if (instance == null) {
            instance = new CurrencyServiceImpl(CurrencyDAOImpl.getInstance());
        }
        return instance;
    }

    public CurrencyServiceImpl(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }


    @Override
    public List<Currency> getAllCurrency() {
        return currencyDAO.findAll();
    }


    @Override
    public Optional<Currency> getCurrencyByCode(String currencyCode) {
        return currencyDAO.findByCode(currencyCode);
    }

    @Override
    public void saveCurrency(CurrencyDTO currencyDTO) {
        currencyDAO.save(currencyDTO);
    }

}
