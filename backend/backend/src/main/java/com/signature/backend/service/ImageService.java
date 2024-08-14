package com.signature.backend.service;

import com.signature.backend.config.CorsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.signature.backend.entity.Image;
import com.signature.backend.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = CorsConstants.DIR_UPLOAD; // Đường dẫn đến thư mục lưu trữ

    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(MultipartFile file) throws IOException {
        // Tạo tên file duy nhất dựa trên thời gian hiện tại
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Tạo đường dẫn file
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file vào hệ thống file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Lưu thông tin file vào cơ sở dữ liệu
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setPath(filePath.toString());
        return imageRepository.save(image);
    }
}

