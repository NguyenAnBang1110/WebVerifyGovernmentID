package com.signature.backend.service;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class FileUtils {

    public static String normalizeFileName(String fileName) {
        // Chuyển đổi tiếng Việt có dấu thành không dấu
        String normalized = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");

        // Thay thế khoảng trắng bằng dấu gạch dưới
        normalized = normalized.replaceAll("\\s+", "_");

        // Loại bỏ các ký tự đặc biệt không phù hợp với tên file
        normalized = normalized.replaceAll("[^a-zA-Z0-9._-]", "");

        return normalized;
    }
}

