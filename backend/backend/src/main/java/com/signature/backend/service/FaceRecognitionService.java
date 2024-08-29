package com.signature.backend.service;

import com.signature.backend.config.CorsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class FaceRecognitionService {

    private final RestTemplate restTemplate;

    public FaceRecognitionService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public boolean compareFaces(String frontImageBase64, String backImageBase64) {
        String url = CorsConstants.PYTHON_PROJECT_ENDPOINT + "/face/compare";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("frontImage", frontImageBase64);
        payload.put("backImage", backImageBase64);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Boolean.class
        );

        return response.getBody();
    }

}

