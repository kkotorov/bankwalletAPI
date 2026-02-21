package com.example.bankwalletAPI.service.external;

import com.example.bankwalletAPI.exception.CurrencyNotSupportedException;
import com.example.bankwalletAPI.exception.ExternalExchangeApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    @Value("${layer.api.key}")
    private String apiKey;

    @Value("${layer.api.latest-url}")
    private String convertUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public BigDecimal convertToEur(String fromCurrency, BigDecimal amount) {
        if ("EUR".equalsIgnoreCase(fromCurrency)) {
            return amount;
        }

        Map<String, Object> responseBody = executeFetchRatesRequest();

        if (responseBody != null && (boolean) responseBody.get("success")) {
            Map<String, Object> rates = (Map<String, Object>) responseBody.get("rates");
            Object rateValue = rates.get(fromCurrency.toUpperCase());

            if (rateValue == null) {
                throw new CurrencyNotSupportedException(fromCurrency);
            }

            BigDecimal rate = new BigDecimal(rateValue.toString());
            return amount.divide(rate, 2, RoundingMode.HALF_UP);
        }

        throw new RuntimeException("Could not fetch exchange rates.");
    }

    /**
     * Helper for fetching the rates list
     */
    private Map<String, Object> executeFetchRatesRequest() {
        String url = UriComponentsBuilder.fromUriString(convertUrl)
                .queryParam("access_key", apiKey)
                .toUriString();

        ParameterizedTypeReference<Map<String, Object>> responseType =
                new ParameterizedTypeReference<Map<String, Object>>() {};

        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, responseType).getBody();
        } catch (Exception e) {
            throw new ExternalExchangeApiException("Failed to connect to exchange service: " + e.getMessage());
        }
    }
}