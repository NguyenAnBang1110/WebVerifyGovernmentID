package com.signature.backend.config;

public class CorsConstants {
    public static final String[] ALLOWED_ORIGINS = {
            "http://localhost:4200", // URL cho frontend khi phát triển
            "http://your-production-url.com" // URL cho frontend khi deploy production
    };

    public static final String DIR_UPLOAD = "D:/BANGNA1/Devlog/Personal/WebVerifyGovernmentID/backend/upload";

    public static final String TESSERACT_lANGUAGE_FOLDER = "C:/Program Files/Tesseract-OCR/tessdata";

    public static final String TESSERACT_OCR_OUTPUT = "D:/BANGNA1/Devlog/Personal/WebVerifyGovernmentID/backend/OCR";
    
    public static final String GOOGLE_CLOUD_VISION_AUTHEN_JSON = "D:/BANGNA1/Devlog/Personal/WebVerifyGovernmentID/backend/GoogleCloudVisionKey/ocr-project-433608-e50557a6a865.json";

    public static final String PYTHON_PROJECT_ENDPOINT = "http://127.0.0.1:5001/api";
}
