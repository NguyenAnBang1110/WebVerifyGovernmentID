package com.signature.backend.service;
import com.signature.backend.entity.Account;
import com.signature.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validateAccount(String username, String rawPassword) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.isPresent() && passwordEncoder.matches(rawPassword, account.get().getPassword());
    }
}

