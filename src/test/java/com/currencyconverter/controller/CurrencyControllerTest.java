package com.currencyconverter.controller;

import com.currencyconverter.exception.ExternalApiException;
import com.currencyconverter.exception.InvalidCurrencyException;
import com.currencyconverter.model.ConversionRequest;
import com.currencyconverter.model.ConversionResponse;
import com.currencyconverter.model.ExchangeRateResponse;
import com.currencyconverter.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExchangeRateResponse exchangeRateResponse;
    private ConversionResponse conversionResponse;

    @BeforeEach
    void setUp() {
        // Set up exchange rate response
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);
        rates.put("GBP", 0.75);
        rates.put("JPY", 110.0);

        exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setBase("USD");
        exchangeRateResponse.setRates(rates);
        exchangeRateResponse.setTimestamp(System.currentTimeMillis() / 1000);
        exchangeRateResponse.setDisclaimer("Test disclaimer");
        exchangeRateResponse.setLicense("Test license");

        // Set up conversion response
        conversionResponse = ConversionResponse.builder()
                .from("USD")
                .to("EUR")
                .amount(100.0)
                .convertedAmount(85.0)
                .build();
    }

    @Test
    void getExchangeRates_DefaultBase_ReturnsRates() throws Exception {
        when(currencyService.getExchangeRates("USD")).thenReturn(exchangeRateResponse);

        mockMvc.perform(get("/api/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("USD"))
                .andExpect(jsonPath("$.rates.EUR").value(0.85))
                .andExpect(jsonPath("$.rates.GBP").value(0.75))
                .andExpect(jsonPath("$.rates.JPY").value(110.0));
    }

    @Test
    void getExchangeRates_CustomBase_ReturnsRates() throws Exception {
        exchangeRateResponse.setBase("EUR");
        when(currencyService.getExchangeRates("EUR")).thenReturn(exchangeRateResponse);

        mockMvc.perform(get("/api/rates?base=EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("EUR"));
    }

    @Test
    void getExchangeRates_ExternalApiException_ReturnsServiceUnavailable() throws Exception {
        when(currencyService.getExchangeRates("USD"))
                .thenThrow(new ExternalApiException("External API unavailable"));

        mockMvc.perform(get("/api/rates"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message").value("External API unavailable"));
    }

    @Test
    void convertCurrency_ValidRequest_ReturnsConversion() throws Exception {
        ConversionRequest request = new ConversionRequest("USD", "EUR", 100.0);
        
        when(currencyService.convertCurrency(any(ConversionRequest.class))).thenReturn(conversionResponse);

        mockMvc.perform(post("/api/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("EUR"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.convertedAmount").value(85.0));
    }

    @Test
    void convertCurrency_InvalidCurrency_ReturnsBadRequest() throws Exception {
        ConversionRequest request = new ConversionRequest("USD", "XYZ", 100.0);
        
        when(currencyService.convertCurrency(any(ConversionRequest.class)))
                .thenThrow(new InvalidCurrencyException("Invalid target currency: XYZ"));

        mockMvc.perform(post("/api/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid target currency: XYZ"));
    }

    @Test
    void convertCurrency_InvalidRequest_ReturnsBadRequest() throws Exception {
        ConversionRequest request = new ConversionRequest("", "", -100.0);

        mockMvc.perform(post("/api/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}