package com.example.bankwalletAPI.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletDTO {
    private String iban;
    private BigDecimal balance;
    private String currency;
}
