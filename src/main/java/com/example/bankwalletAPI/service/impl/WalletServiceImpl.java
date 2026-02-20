package com.example.bankwalletAPI.service.impl;

import com.example.bankwalletAPI.dto.WalletDTO;
import com.example.bankwalletAPI.entity.User;
import com.example.bankwalletAPI.entity.Wallet;
import com.example.bankwalletAPI.exception.InsufficientFundsException;
import com.example.bankwalletAPI.exception.UserNotFoundException;
import com.example.bankwalletAPI.mapper.WalletMapper;
import com.example.bankwalletAPI.repository.UserRepository;
import com.example.bankwalletAPI.repository.WalletRepository;
import com.example.bankwalletAPI.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;


    @Autowired
    public WalletServiceImpl(UserRepository userRepository, WalletRepository walletRepository, WalletMapper walletMapper) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Override
    @Transactional
    public WalletDTO deposit(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().add(amount));

        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.walletToWalletDto(savedWallet);
    }

    @Override
    @Transactional
    public WalletDTO withdraw(Long userId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Wallet wallet = user.getWallet();
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in wallet");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Wallet savedWallet = walletRepository.save(wallet);
        return walletMapper.walletToWalletDto(savedWallet);
    }

    @Override
    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new UserNotFoundException("Sender user not found with id: " + fromUserId));

        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new UserNotFoundException("Receiver user not found with id: " + toUserId));

        Wallet fromWallet = fromUser.getWallet();
        Wallet toWallet = toUser.getWallet();

        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds in sender's wallet");
        }

        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }
}
