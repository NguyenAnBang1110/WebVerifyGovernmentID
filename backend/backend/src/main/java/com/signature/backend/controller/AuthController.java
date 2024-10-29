package com.signature.backend.controller;
import com.signature.backend.entity.AuthResponse;
import com.signature.backend.entity.LoginRequest;
import com.signature.backend.entity.RefreshTokenRequest;
import com.signature.backend.service.AccountService;
import com.signature.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean isValidAccount = accountService.validateAccount(loginRequest.getUsername(), loginRequest.getPassword());

        if (isValidAccount) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token, refreshToken));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());

        if (username != null && jwtUtil.validateToken(request.getRefreshToken())) {
            String newToken = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(newToken));
        } else {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }
    }

}

