package com.currencyconverter.controller;

import com.currencyconverter.model.CurrencyConversionRequest;
import com.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyConverterController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getRates(@RequestParam(defaultValue = "USD") String base) {
        return ResponseEntity.ok(currencyService.getExchangeRates(base));
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convert(@RequestBody CurrencyConversionRequest request) {
        double convertedAmount = currencyService.convert(request.getFrom(), request.getTo(), request.getAmount());
        return ResponseEntity.ok(Map.of(
                "from", request.getFrom(),
                "to", request.getTo(),
                "amount", request.getAmount(),
                "convertedAmount", convertedAmount
        ));
    }
}
