package com.example.bankwalletAPI.service;

import com.example.bankwalletAPI.dto.CreateUserRequestDTO;
import com.example.bankwalletAPI.dto.UserResponseDTO;
import com.example.bankwalletAPI.entity.User;
import com.example.bankwalletAPI.exception.UserNotFoundException;
import com.example.bankwalletAPI.mapper.UserMapper;
import com.example.bankwalletAPI.repository.UserRepository;
import com.example.bankwalletAPI.service.impl.UserServiceImpl;
import com.example.bankwalletAPI.types.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequestDTO requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new CreateUserRequestDTO();
        requestDto.setUsername("johndoe");
        requestDto.setEmail("john@example.com");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setUniqueIdentificationNumber("123456789");
        requestDto.setAddress("123 Street");
    }

    @Test
    void createUser_ShouldInitializeWalletWithZeroBalance() {
        // Arrange
        // We use thenAnswer to capture the User object being saved to inspect the Wallet
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.userToUserResponseDto(any(User.class))).thenReturn(new UserResponseDTO());

        // Act
        userService.createUser(requestDto);

        // Assert
        verify(userRepository).save(argThat(user -> {
            assertNotNull(user.getWallet(), "Wallet should be automatically created");
            assertEquals(BigDecimal.ZERO, user.getWallet().getBalance());
            assertEquals(Currency.EUR, user.getWallet().getCurrency());
            assertNotNull(user.getWallet().getIban(), "IBAN should be generated");
            return true;
        }));
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }
}