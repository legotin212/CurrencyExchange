package org.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.exception.NotFoundException;
import org.exception.WrongArgumentsException;
import org.model.Currency;
import org.service.CurrencyService;
import org.service.CurrencyServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService  = CurrencyServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String currencyCode = pathInfo.substring(1).toUpperCase();
            Optional<Currency> currency = currencyService.getCurrencyByCode(currencyCode);
            if (currency.isPresent()) {
                objectMapper.writeValue(response.getWriter(), currency.get());
            }
            throw new NotFoundException("Currency not found");
        }
        throw new WrongArgumentsException("Wrong currency code");

    }

}
