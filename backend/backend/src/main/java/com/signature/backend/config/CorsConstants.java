package com.signature.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@Configuration
public class CorsConstants {

    // Define static variables
    public static List<String> ALLOWED_ORIGINS;
    public static String DIR_UPLOAD;
    public static String TESSERACT_LANGUAGE_FOLDER;
    public static String TESSERACT_OCR_OUTPUT;
    public static String GOOGLE_CLOUD_VISION_AUTHEN_JSON;
    public static String PYTHON_PROJECT_ENDPOINT;
    public static String SECRET_KEY;
    public static Long TOKEN_EXPIRATION_TIME;
    public static Long REFRESHTOKEN_EXPIRATION_TIME;

    public static List<String> PUBLIC_APIS;

    // Inject properties using @Value annotations
    @Value("${allowed.origins}")
    private String allowedOrigins;

    @Value("${dir.upload}")
    private String dirUpload;

    @Value("${tesseract.language.folder}")
    private String tesseractLanguageFolder;

    @Value("${tesseract.ocr.output}")
    private String tesseractOcrOutput;

    @Value("${google.cloud.vision.authen.json}")
    private String googleCloudVisionAuthenJson;

    @Value("${python.project.endpoint}")
    private String pythonProjectEndpoint;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${public.api}")
    private String publicApi;

    @Value("${jwt.expirationMs}")
    private Long jwtExpirationMs;

    @Value("${jwt.refreshExpirationMs}")
    private Long jwtRefreshExpirationMs;

    // Load values into static fields after construction
    @PostConstruct
    private void init() {
        ALLOWED_ORIGINS = Arrays.asList(allowedOrigins.split(","));
        DIR_UPLOAD = dirUpload;
        TESSERACT_LANGUAGE_FOLDER = tesseractLanguageFolder;
        TESSERACT_OCR_OUTPUT = tesseractOcrOutput;
        GOOGLE_CLOUD_VISION_AUTHEN_JSON = googleCloudVisionAuthenJson;
        PYTHON_PROJECT_ENDPOINT = pythonProjectEndpoint;
        SECRET_KEY = secretKey;
        PUBLIC_APIS = Arrays.asList(publicApi.split(","));
        TOKEN_EXPIRATION_TIME = jwtExpirationMs;
        REFRESHTOKEN_EXPIRATION_TIME = jwtRefreshExpirationMs;
    }
}
