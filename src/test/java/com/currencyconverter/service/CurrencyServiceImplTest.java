package com.currencyconverter.service;

import com.currencyconverter.client.ExchangeRateClient;
import com.currencyconverter.exception.InvalidCurrencyException;
import com.currencyconverter.model.ConversionRequest;
import com.currencyconverter.model.ConversionResponse;
import com.currencyconverter.model.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private ExchangeRateResponse mockResponse;

    @BeforeEach
    void setUp() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.85);
        rates.put("GBP", 0.75);
        rates.put("JPY", 110.0);

        mockResponse = new ExchangeRateResponse();
        mockResponse.setBase("USD");
        mockResponse.setRates(rates);
        mockResponse.setTimestamp(System.currentTimeMillis() / 1000);
        mockResponse.setDisclaimer("Mock disclaimer");
        mockResponse.setLicense("Mock license");
    }
    
    @Test
    void getExchangeRates_ReturnsExchangeRateResponse() {
        // Arrange
        when(exchangeRateClient.getLatestRates("USD")).thenReturn(mockResponse);

        // Act
        ExchangeRateResponse result = currencyService.getExchangeRates("USD");

        // Assert
        assertNotNull(result);
        assertEquals("USD", result.getBase());
        assertNotNull(result.getRates());
        assertEquals(3, result.getRates().size());
        verify(exchangeRateClient, times(1)).getLatestRates("USD");
    }

    @Test
    void convertCurrency_ValidRequest_ReturnsConversionResponse() {
        // Arrange
        ConversionRequest request = new ConversionRequest("USD", "EUR", 100.0);
        when(exchangeRateClient.getLatestRates("USD")).thenReturn(mockResponse);

        // Act
        ConversionResponse result = currencyService.convertCurrency(request);

        // Assert
        assertNotNull(result);
        assertEquals("USD", result.getFrom());
        assertEquals("EUR", result.getTo());
        assertEquals(100.0, result.getAmount());
        assertEquals(85.0, result.getConvertedAmount());
    }

    @Test
    void convertCurrency_InvalidCurrency_ThrowsInvalidCurrencyException() {
        // Arrange
        ConversionRequest request = new ConversionRequest("USD", "XYZ", 100.0);
        when(exchangeRateClient.getLatestRates("USD")).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(InvalidCurrencyException.class, () -> {
            currencyService.convertCurrency(request);
        });
    }

    @Test
    void convertCurrency_CaseSensitivity_WorksWithDifferentCase() {
        // Arrange
        ConversionRequest request = new ConversionRequest("usd", "eur", 100.0);
        when(exchangeRateClient.getLatestRates("USD")).thenReturn(mockResponse);

        // Act
        ConversionResponse result = currencyService.convertCurrency(request);

        // Assert
        assertNotNull(result);
        assertEquals("USD", result.getFrom());
        assertEquals("EUR", result.getTo());
        assertEquals(100.0, result.getAmount());
        assertEquals(85.0, result.getConvertedAmount());
    }
}