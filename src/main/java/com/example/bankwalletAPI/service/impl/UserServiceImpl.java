package com.example.bankwalletAPI.service.impl;

import com.example.bankwalletAPI.dto.CreateUserRequestDTO;
import com.example.bankwalletAPI.dto.UserResponseDTO;
import com.example.bankwalletAPI.entity.User;
import com.example.bankwalletAPI.entity.Wallet;
import com.example.bankwalletAPI.exception.UserNotFoundException;
import com.example.bankwalletAPI.mapper.UserMapper;
import com.example.bankwalletAPI.repository.UserRepository;
import com.example.bankwalletAPI.service.UserService;
import com.example.bankwalletAPI.types.Currency;
import com.example.bankwalletAPI.types.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserRequestDTO userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .uniqueIdentificationNumber(userDto.getUniqueIdentificationNumber())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .role(UserRole.USER)
                .build();

        Wallet wallet = Wallet.builder()
                .iban(UUID.randomUUID().toString())
                .balance(BigDecimal.ZERO)
                .currency(Currency.EUR)
                .build();

        user.setWallet(wallet);
        wallet.setUser(user);
        
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.userToUserResponseDto(user);
    }
}
