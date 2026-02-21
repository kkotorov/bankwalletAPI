package com.example.bankwalletAPI.service;

import com.example.bankwalletAPI.dto.WalletDTO;
import com.example.bankwalletAPI.entity.User;
import com.example.bankwalletAPI.entity.Wallet;
import com.example.bankwalletAPI.mapper.WalletMapper;
import com.example.bankwalletAPI.repository.UserRepository;
import com.example.bankwalletAPI.repository.WalletRepository;
import com.example.bankwalletAPI.service.external.CurrencyConverterService;
import com.example.bankwalletAPI.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private CurrencyConverterService converterService;
    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void deposit_ShouldConvertCurrencyAndAddBalance() {
        // Arrange
        Long userId = 1L;
        BigDecimal depositAmount = new BigDecimal("100");
        BigDecimal rate = new BigDecimal("1.10"); // 1 EUR = 1.10 USD
        BigDecimal expectedEur = new BigDecimal("90.91"); // 100 / 1.10 rounded

        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("50.00"));
        user.setWallet(wallet);

        when(converterService.convertToEur("USD", depositAmount)).thenReturn(expectedEur);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.save(any())).thenReturn(wallet);
        when(walletMapper.walletToWalletDto(any())).thenReturn(new WalletDTO());

        walletService.deposit(userId, depositAmount, "USD");

        assertEquals(new BigDecimal("140.91"), wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }
}