package com.currencyconverter.service;

import com.currencyconverter.model.ConversionRequest;
import com.currencyconverter.model.ConversionResponse;
import com.currencyconverter.model.ExchangeRateResponse;

public interface CurrencyService {
    
    /**
     * Fetches the latest exchange rates for the specified base currency.
     *
     * @param baseCurrency The base currency code (e.g., USD)
     * @return ExchangeRateResponse containing the latest exchange rates
     */
    ExchangeRateResponse getExchangeRates(String baseCurrency);
    
    /**
     * Converts an amount from one currency to another.
     *
     * @param request The conversion request containing source currency, target currency, and amount
     * @return ConversionResponse containing the converted amount
     */
    ConversionResponse convertCurrency(ConversionRequest request);
}
