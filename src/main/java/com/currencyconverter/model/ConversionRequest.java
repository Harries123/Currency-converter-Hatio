// ConversionRequest.java
package com.currencyconverter.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRequest {
    @NotBlank(message = "Source currency is required")
    private String from;
    
    @NotBlank(message = "Target currency is required")
    private String to;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}
