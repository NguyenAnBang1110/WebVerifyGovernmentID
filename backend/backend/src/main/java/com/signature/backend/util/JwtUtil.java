package com.signature.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.signature.backend.config.CorsConstants;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {


    public JwtUtil() {
    }

    public String generateToken(String username) {
        return createToken(username, CorsConstants.TOKEN_EXPIRATION_TIME);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, CorsConstants.REFRESHTOKEN_EXPIRATION_TIME);
    }

    private String createToken(String username, long expirationTime) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(CorsConstants.SECRET_KEY));
    }

    public boolean validateToken(String token) {
        try {
            // Create JWT verifier and verify the token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(CorsConstants.SECRET_KEY)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return claimsResolver.apply(decodedJWT);
    }
}
