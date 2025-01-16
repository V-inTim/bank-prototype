package com.example.deal.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SesCodeService {
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateNumericCode(int codeLength) {
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            code.append(secureRandom.nextInt(10)); // Случайное число от 0 до 9
        }
        return code.toString();
    }
}
