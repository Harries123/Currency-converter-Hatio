package com.currencyconverter.service;

import com.currencyconverter.client.ExchangeRateClient;
import com.currencyconverter.exception.ExternalApiException;
import com.currencyconverter.model.ConversionRequest;
import com.currencyconverter.model.ConversionResponse;
import com.currencyconverter.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final ExchangeRateClient exchangeRateClient;

    @Autowired
    public CurrencyServiceImpl(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    @Override
    public ExchangeRateResponse getExchangeRates(String baseCurrency) {
        try {
            ExchangeRateResponse response = exchangeRateClient.getLatestRates(baseCurrency);

            // Validate the response
            if (response == null || response.getRates() == null || response.getRates().isEmpty()) {
                throw new ExternalApiException("Failed to fetch exchange rates. The response is empty or invalid.");
            }

            return response;
        } catch (Exception e) {
            throw new ExternalApiException("Error fetching exchange rates: " + e.getMessage(), e);
        }
    }

    @Override
    public ConversionResponse convertCurrency(ConversionRequest request) {
        // Fetch exchange rates for the source currency
        ExchangeRateResponse exchangeRateResponse = getExchangeRates(request.getFrom());
        
        Map<String, Double> rates = exchangeRateResponse.getRates();
        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Exchange rates data is not available.");
        }

        // Get the target exchange rate
        Double targetRate = rates.get(request.getTo());
        if (targetRate == null) {
            throw new IllegalArgumentException("Invalid target currency code: " + request.getTo());
        }

        // Calculate the converted amount
        double convertedAmount = request.getAmount() * targetRate;

        // Build the response
        return ConversionResponse.builder()
                .from(request.getFrom())
                .to(request.getTo())
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .build();
    }
}
