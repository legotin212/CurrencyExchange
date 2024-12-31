package org.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dto.ExchangeResponseDTO;
import org.exception.WrongArgumentsException;
import org.service.ExchangeService;
import org.service.ExchangeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class Exchange extends HttpServlet {
    private final ExchangeService exchangeService = ExchangeServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        double param = Double.parseDouble(req.getParameter("amount"));
        BigDecimal amount = BigDecimal.valueOf(param);
        if(baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new WrongArgumentsException("Enter target currency code");
        }

        ExchangeResponseDTO exchangeResponseDTO = exchangeService.exchange(baseCurrencyCode, targetCurrencyCode, amount);
        objectMapper.writeValue(resp.getWriter(), exchangeResponseDTO);
    }
}
