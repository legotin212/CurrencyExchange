package org.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exception.NotFoundException;
import org.exception.WrongArgumentsException;
import org.model.ExchangeRate;
import org.service.ExchangeRateServiceImpl;
import org.service.ExchangeRateService;
import org.servlet.abstractServlet.PatchServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends PatchServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.length() == 7) {

            String baseCurrencyCode = pathInfo.substring(1, 4).toUpperCase();
            String targetCurrencyCode = pathInfo.substring(4, 7).toUpperCase();

            Optional<ExchangeRate> exchangeRateDTO = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRateDTO.isPresent()) {
                objectMapper.writeValue(resp.getWriter(), exchangeRateDTO.get());
            }
            throw  new NotFoundException("Exchange rate not found");
        }
        throw new WrongArgumentsException("No currency code in request");
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String pathInfo = req.getPathInfo();
        String body = req.getReader().readLine();
        String r = body.substring(5);


        String baseCurrencyCode = pathInfo.substring(1, 4).toUpperCase();
        String targetCurrencyCode = pathInfo.substring(4, 7).toUpperCase();
        double exchangeRate = Double.parseDouble(r);

        exchangeRateService.update(baseCurrencyCode, targetCurrencyCode, exchangeRate);

        Optional<org.model.ExchangeRate> exchangeRateDTO = exchangeRateService.getExchangeRate(baseCurrencyCode, targetCurrencyCode);

        objectMapper.writeValue(resp.getWriter(), exchangeRateDTO);

    }


}
