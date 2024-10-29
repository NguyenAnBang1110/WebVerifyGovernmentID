package com.signature.backend.service;

import com.signature.backend.config.CorsConstants;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

@Service
public class OcrService {

    static {
        Loader.load(org.bytedeco.opencv.opencv_java.class);
    }

    @Autowired
    private GoogleVisionOcrService googleVisionOcrService;

    private static final String TESSERACT_DATA_PATH = CorsConstants.TESSERACT_LANGUAGE_FOLDER; // Đường dẫn tới thư mục dữ liệu Tesseract
    private static final String OUTPUT_TEXT_PATH = CorsConstants.TESSERACT_OCR_OUTPUT; // Đường dẫn tới thư mục lưu file OCR output

    public String extractTextFromImage(MultipartFile file, boolean isGoogleCloudOCR) throws IOException, TesseractException {
        // Lưu file tạm thời để xử lý OCR
        Path tempDir = Files.createTempDirectory("ocr-uploads");
        String originalFileName = file.getOriginalFilename();
        String normalizedFileName = FileUtils.normalizeFileName(originalFileName);
        File tempFile = new File(tempDir.toFile(), normalizedFileName);
        file.transferTo(tempFile);

        String result = "";
        if (isGoogleCloudOCR) {
            result = googleVisionOcrService.performOcr(tempFile.getAbsolutePath());
        } else {
            // Tiền xử lý ảnh
            File preprocessedFile = preprocessImage(tempFile);

            // Cấu hình Tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(TESSERACT_DATA_PATH); // Đường dẫn tới thư mục dữ liệu Tesseract
            tesseract.setLanguage("vie"); // Sử dụng ngôn ngữ tiếng Việt
//        tesseract.setLanguage("vie+eng");

            // Thực hiện OCR
            result = tesseract.doOCR(preprocessedFile);

            // Xử lý hậu kỳ để làm sạch kết quả
            result = cleanOcrResult(result);

            // Lưu kết quả OCR vào file nếu cần
//            Path outputFilePath = Paths.get(OUTPUT_TEXT_PATH, System.currentTimeMillis() + "_ocr_result.txt");
//            Files.write(outputFilePath, result.getBytes());
        }

        // Xóa file tạm thời
        tempFile.delete();
        tempDir.toFile().delete();

        return result;
    }

    private File preprocessImage(File imageFile) {
        // Đọc ảnh vào Mat
        Mat src = opencv_imgcodecs.imread(imageFile.getAbsolutePath());

        // Kiểm tra nếu ảnh được tải thành công
        if (src.empty()) {
            throw new RuntimeException("Failed to load image: " + imageFile.getAbsolutePath());
        }

        // Chuyển đổi sang grayscale
        Mat des = new Mat();
        opencv_imgproc.cvtColor(src, des, opencv_imgproc.COLOR_BGR2GRAY);
        
        // Áp dụng GaussianBlur để làm mịn hình ảnh, loại bỏ nhiễu
        opencv_imgproc.GaussianBlur(des, des, new org.bytedeco.opencv.opencv_core.Size(3, 3), 0); // MedianBlur giúp loại bỏ nhiễu mà vẫn giữ được các cạnh rõ ràng
        
        // Lưu ảnh đã tiền xử lý vào một file tạm thời
        File preprocessedFile = new File(imageFile.getParent(), "preprocessed_" + imageFile.getName());
        opencv_imgcodecs.imwrite(preprocessedFile.getAbsolutePath(), des);

        return preprocessedFile;
    }

    private String cleanOcrResult(String ocrText) {
        // Giữ lại các ký tự hợp lệ bao gồm các chữ cái, số, dấu câu và khoảng trắng, đồng thời giữ lại các dòng xuống dòng
        return ocrText.replaceAll("[^\\p{L}\\p{Nd}\\p{P}\\s\\n]", "").replaceAll("[\\t\\r]", "").replaceAll("(?m)^[ \\t]+", "").trim();
    }


}
