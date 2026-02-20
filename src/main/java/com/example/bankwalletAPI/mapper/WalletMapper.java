package com.example.bankwalletAPI.mapper;

import com.example.bankwalletAPI.dto.WalletDTO;
import com.example.bankwalletAPI.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletDTO walletToWalletDto(Wallet wallet);
}
