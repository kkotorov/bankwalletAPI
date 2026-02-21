package com.example.bankwalletAPI.service;

import com.example.bankwalletAPI.dto.WalletDTO;
import java.math.BigDecimal;

public interface WalletService {
    WalletDTO deposit(Long userId, BigDecimal amount, String currency);
    WalletDTO withdraw(Long userId, BigDecimal amount);
    void transfer(Long fromUserId, Long toUserId, BigDecimal amount);
}
