// ConversionResponse.java
package com.currencyconverter.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionResponse {
    private String from;
    private String to;
    private Double amount;
    private Double convertedAmount;
}