package com.example.bankwalletAPI.exception;

public class CurrencyNotSupportedException extends RuntimeException {
    public CurrencyNotSupportedException(String currency) {
        super("The currency '" + currency + "' is not supported by our exchange provider.");
    }
}