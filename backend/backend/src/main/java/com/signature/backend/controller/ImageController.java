package com.signature.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.signature.backend.entity.Image;
import com.signature.backend.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public Image uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return imageService.saveImage(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}

