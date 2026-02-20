package com.example.bankwalletAPI.service;

import com.example.bankwalletAPI.dto.CreateUserRequestDTO;
import com.example.bankwalletAPI.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequestDTO userDto);
    UserResponseDTO findByUsername(String username);
    UserResponseDTO getUserById(Long id);
}
