package com.example.bankwalletAPI.mapper;

import com.example.bankwalletAPI.dto.UserResponseDTO;
import com.example.bankwalletAPI.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "wallet.iban", target = "wallet.iban")
    @Mapping(source = "wallet.balance", target = "wallet.balance")
    @Mapping(source = "wallet.currency", target = "wallet.currency")
    UserResponseDTO userToUserResponseDto(User user);
}
