package com.signature.backend.config;

public class CorsConstants {
    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:4200", // URL cho frontend khi phát triển
            "http://your-production-url.com" // URL cho frontend khi deploy production
    };

    public static final String DIR_UPLOAD = "D:/BANGNA1/Devlog/Personal/WebVerifyGovernmentID/backend/upload";
}