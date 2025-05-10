package com.currencyconverter.client;

import com.currencyconverter.exception.ExternalApiException;
import com.currencyconverter.model.ExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class ExchangeRateClient {

    private final WebClient webClient;

    @Autowired
    public ExchangeRateClient(WebClient.Builder webClientBuilder, 
                              @Value("${openexchangerates.api.url}") String apiUrl, 
                              @Value("${openexchangerates.app.id}") String apiKey) {
        this.webClient = webClientBuilder
                .baseUrl(apiUrl + "/" + apiKey)
                .build();
    }

    public ExchangeRateResponse getLatestRates(String baseCurrency) {
        try {
            // Default base to USD if not provided
            if (baseCurrency == null || baseCurrency.isEmpty()) {
                baseCurrency = "USD";
            }

            return webClient.get()
                    .uri("/latest/" + baseCurrency)
                    .retrieve()
                    .bodyToMono(ExchangeRateResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .doOnError(WebClientResponseException.class, e -> 
                            log.error("Error fetching exchange rates: {} - {}", e.getStatusCode(), e.getResponseBodyAsString()))
                    .onErrorResume(WebClientResponseException.class, e -> 
                            Mono.error(new ExternalApiException("Error fetching exchange rates: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e)))
                    .onErrorResume(e -> {
                        log.error("Unexpected error fetching exchange rates", e);
                        return Mono.error(new ExternalApiException("Unexpected error fetching exchange rates: " + e.getMessage(), e));
                    })
                    .block();  // Block to return the actual response
        } catch (Exception e) {
            log.error("Failed to fetch exchange rates", e);
            throw new ExternalApiException("Failed to fetch exchange rates: " + e.getMessage(), e);
        }
    }
}
