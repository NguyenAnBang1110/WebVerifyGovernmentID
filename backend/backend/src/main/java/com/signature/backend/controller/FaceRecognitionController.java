package com.signature.backend.controller;

import com.signature.backend.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.signature.backend.entity.Image;
import com.signature.backend.service.ImageService;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/face")
public class FaceRecognitionController {

    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @PostMapping("/compare")
    public ResponseEntity<Boolean> compareFaces(
            @RequestParam("frontImage") MultipartFile frontImage,
            @RequestParam("backImage") MultipartFile backImage) throws IOException {
        try {
            String frontImageBase64 = Base64.getEncoder().encodeToString(frontImage.getBytes());
            String backImageBase64 = Base64.getEncoder().encodeToString(backImage.getBytes());
            boolean isMatch = faceRecognitionService.compareFaces(frontImageBase64, backImageBase64);
            return ResponseEntity.ok(isMatch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
