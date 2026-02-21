package com.example.bankwalletAPI.service.external;

import java.math.BigDecimal;

public interface CurrencyConverterService {
     BigDecimal convertToEur(String fromCurrency, BigDecimal amount);
}
