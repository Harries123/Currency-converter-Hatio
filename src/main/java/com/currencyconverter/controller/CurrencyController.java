package com.currencyconverter.controller;

import com.currencyconverter.model.ConversionRequest;
import com.currencyconverter.model.ConversionResponse;
import com.currencyconverter.model.ExchangeRateResponse;
import com.currencyconverter.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Currency API", description = "Endpoints for currency conversion and exchange rates")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/rates")
    @Operation(summary = "Get exchange rates", description = "Fetches the latest exchange rates for the specified base currency")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rates"),
            @ApiResponse(responseCode = "400", description = "Invalid base currency"),
            @ApiResponse(responseCode = "503", description = "External API unavailable")
    })
    public ResponseEntity<ExchangeRateResponse> getExchangeRates(@RequestParam(defaultValue = "USD") String base) {
        ExchangeRateResponse response = currencyService.getExchangeRates(base);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert currency", description = "Converts an amount from one currency to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully converted currency"),
            @ApiResponse(responseCode = "400", description = "Invalid request or currency"),
            @ApiResponse(responseCode = "503", description = "External API unavailable")
    })
    public ResponseEntity<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        ConversionResponse response = currencyService.convertCurrency(request);
        return ResponseEntity.ok(response);
    }
}