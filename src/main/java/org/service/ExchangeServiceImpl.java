package org.service;

import org.dao.CurrencyDAO;
import org.dao.CurrencyDAOImpl;
import org.dao.ExchangeRatesDAO;
import org.dao.ExchangeRatesDAOImpl;
import org.dto.ExchangeResponseDTO;
import org.exception.WrongArgumentsException;
import org.model.Currency;
import org.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRatesDAO exchangeRatesDAO;
    private final CurrencyDAO currencyDAO;
    private static ExchangeServiceImpl instance;



    public static synchronized ExchangeServiceImpl getInstance(){
        if (instance == null) {
            instance = new ExchangeServiceImpl(ExchangeRatesDAOImpl.getInstance(), CurrencyDAOImpl.getInstance())  ;
        }
        return instance;
    }

    private ExchangeServiceImpl(ExchangeRatesDAO exchangeRatesDAO, CurrencyDAO currencyDAO) {
        this.exchangeRatesDAO = exchangeRatesDAO;
        this.currencyDAO = currencyDAO;
    }

    @Override
    public ExchangeResponseDTO exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {

        Optional<ExchangeRate> rateDTO = exchangeRatesDAO.get(baseCurrencyCode, targetCurrencyCode);
        if(rateDTO.isPresent()){
            BigDecimal rate = rateDTO.get().getRate();
            BigDecimal convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);
        }

        Optional<ExchangeRate> reverseRateDTO = exchangeRatesDAO.get(targetCurrencyCode, baseCurrencyCode);
        if(reverseRateDTO.isPresent()){
            BigDecimal rate = reverseRate(reverseRateDTO.get().getRate());
            BigDecimal convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);
        }

        Optional<ExchangeRate> baseCurrencyToUSD = exchangeRatesDAO.get("USD", baseCurrencyCode );
        Optional<ExchangeRate> targetCurrencyToUSD = exchangeRatesDAO.get("USD", targetCurrencyCode);
        if(baseCurrencyToUSD.isPresent() && targetCurrencyToUSD.isPresent()){
            BigDecimal rate = crossRateExchangeRate(baseCurrencyToUSD.get().getRate(), targetCurrencyToUSD.get().getRate());
            BigDecimal convertedAmount = exchange(amount, rate);
            return createResponseDTO( baseCurrencyCode,  targetCurrencyCode,  amount, rate, convertedAmount);
        }

        throw new WrongArgumentsException("No available exchange rate found for " + baseCurrencyCode + " and " + targetCurrencyCode);
    }

    private ExchangeResponseDTO createResponseDTO(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount, BigDecimal rate, BigDecimal convertedAmount){
        Currency baseCurrency = currencyDAO.findByCode(baseCurrencyCode).get();
        Currency targetCurrency = currencyDAO.findByCode(targetCurrencyCode).get();

        return new ExchangeResponseDTO(
                baseCurrency,
                targetCurrency,
                rate.setScale(2,BigDecimal.ROUND_HALF_UP),
                amount.setScale(2,BigDecimal.ROUND_HALF_UP),
                convertedAmount.setScale(2,BigDecimal.ROUND_HALF_UP));
    }

    private BigDecimal crossRateExchangeRate(BigDecimal baseCurrencyToUSDRate, BigDecimal targetCurrencyToUSDRate) {
       return targetCurrencyToUSDRate.divide(baseCurrencyToUSDRate, 10, RoundingMode.HALF_UP);
    }

    private BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate);
    }

    private BigDecimal reverseRate(BigDecimal rate){
        BigDecimal one = new BigDecimal(1);
        return one.divide(rate, 10, RoundingMode.HALF_UP);
    }

}
