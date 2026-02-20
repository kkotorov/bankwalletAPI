package com.example.bankwalletAPI.controller;

import com.example.bankwalletAPI.dto.WalletDTO;
import com.example.bankwalletAPI.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
@Validated
@Tag(name = "Wallet Operations", description = "Endpoints for financial transactions including deposits, withdrawals, and transfers")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Deposit funds", description = "Adds a positive amount to the specified user's wallet balance.")
    @PostMapping("/{userId}/deposit")
    public ResponseEntity<WalletDTO> deposit(
            @Parameter(description = "ID of the user making the deposit") @PathVariable Long userId,
            @Parameter(description = "Amount to add (must be > 0)") @RequestParam @Positive(message = "Deposit amount must be positive") BigDecimal amount) {
        WalletDTO wallet = walletService.deposit(userId, amount);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @Operation(summary = "Withdraw funds", description = "Deducts a positive amount from the specified user's wallet balance. Fails if funds are insufficient.")
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<WalletDTO> withdraw(
            @Parameter(description = "ID of the user making the withdrawal") @PathVariable Long userId,
            @Parameter(description = "Amount to withdraw (must be > 0)") @RequestParam @Positive(message = "Withdrawal amount must be positive") BigDecimal amount) {
        WalletDTO wallet = walletService.withdraw(userId, amount);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @Operation(summary = "Transfer money", description = "Moves money from one user's wallet to another. Both users must exist and the sender must have enough balance.")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(
            @Parameter(description = "ID of the sender") @RequestParam Long fromUserId,
            @Parameter(description = "ID of the recipient") @RequestParam Long toUserId,
            @Parameter(description = "Amount to transfer (must be > 0)") @RequestParam @Positive(message = "Transfer amount must be positive") BigDecimal amount) {

        if (fromUserId.equals(toUserId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        walletService.transfer(fromUserId, toUserId, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}