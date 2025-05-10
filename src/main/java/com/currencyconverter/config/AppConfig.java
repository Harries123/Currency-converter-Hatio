package com.currencyconverter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
public class AppConfig {
    @Value("${openexchangerates.api.url}")
    private String apiUrl;
    
    @Value("${openexchangerates.app.id}")
    private String appId;

    @Bean
    public WebClient webClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
        
        return WebClient.builder()
                .baseUrl(apiUrl)
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public String appId() {
        return appId;
    }
}