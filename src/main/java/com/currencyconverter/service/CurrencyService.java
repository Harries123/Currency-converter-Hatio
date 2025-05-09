package com.currencyconverter.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrencyService {

    @Value("${currency.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getExchangeRates(String base) {
        try {
            String url = apiUrl + base.toUpperCase();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("rates")) {
                throw new IllegalStateException("Invalid API response");
            }

            // Safe casting with proper validation
            Object ratesObject = response.get("rates");
            if (!(ratesObject instanceof Map)) {
                throw new IllegalStateException("Unexpected rates format");
            }

            return (Map<String, Double>) ratesObject;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("API Error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Network Error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    public double convert(String from, String to, double amount) {
        Map<String, Double> rates = getExchangeRates(from);
        Double targetRate = rates.get(to.toUpperCase());

        if (targetRate == null) {
            throw new IllegalArgumentException("Invalid currency code: " + to);
        }

        return amount * targetRate;
    }
}
