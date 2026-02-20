package com.example.bankwalletAPI.controller;

import com.example.bankwalletAPI.dto.WalletDTO;
import com.example.bankwalletAPI.service.WalletService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
@Validated // Necessary for @Positive to work on @RequestParams
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<WalletDTO> deposit(
            @PathVariable Long userId,
            @RequestParam @Positive(message = "Deposit amount must be positive") BigDecimal amount) {
        WalletDTO wallet = walletService.deposit(userId, amount);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<WalletDTO> withdraw(
            @PathVariable Long userId,
            @RequestParam @Positive(message = "Withdrawal amount must be positive") BigDecimal amount) {
        WalletDTO wallet = walletService.withdraw(userId, amount);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam @Positive(message = "Transfer amount must be positive") BigDecimal amount) {

        if (fromUserId.equals(toUserId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        walletService.transfer(fromUserId, toUserId, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}