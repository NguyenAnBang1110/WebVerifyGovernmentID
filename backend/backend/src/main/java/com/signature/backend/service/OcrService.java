package com.signature.backend.service;

import com.signature.backend.config.CorsConstants;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.bytedeco.javacpp.Loader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

@Service
public class OcrService {

    static {
        Loader.load(org.bytedeco.opencv.opencv_java.class);
    }

    private static final String TESSERACT_DATA_PATH = CorsConstants.TESSERACT_lANGUAGE_FOLDER; // Đường dẫn tới thư mục dữ liệu Tesseract
    private static final String OUTPUT_TEXT_PATH = CorsConstants.TESSERACT_OCR_OUTPUT; // Đường dẫn tới thư mục lưu file OCR output

    public String extractTextFromImage(MultipartFile file, boolean saveToFile) throws IOException, TesseractException {
        // Lưu file tạm thời để xử lý OCR
        Path tempDir = Files.createTempDirectory("ocr-uploads");
        String originalFileName = file.getOriginalFilename();
        String normalizedFileName = FileUtils.normalizeFileName(originalFileName);
        File tempFile = new File(tempDir.toFile(), normalizedFileName);
        file.transferTo(tempFile);

        // Tiền xử lý ảnh
        File preprocessedFile = preprocessImage(tempFile);

        // Cấu hình Tesseract
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH); // Đường dẫn tới thư mục dữ liệu Tesseract
        tesseract.setLanguage("vie"); // Sử dụng ngôn ngữ tiếng Việt
//        tesseract.setLanguage("vie+eng");

        // Thực hiện OCR
        String result = tesseract.doOCR(preprocessedFile);

        // Lưu kết quả OCR vào file nếu cần
        if (saveToFile) {
            Path outputFilePath = Paths.get(OUTPUT_TEXT_PATH, System.currentTimeMillis() + "_ocr_result.txt");
            Files.write(outputFilePath, result.getBytes());
        }

        // Xóa file tạm thời
        tempFile.delete();
        tempDir.toFile().delete();

        return result;
    }

    private File preprocessImage_v1(File imageFile) {
        // Đọc ảnh vào Mat
        Mat src = opencv_imgcodecs.imread(imageFile.getAbsolutePath());

        // Kiểm tra nếu ảnh được tải thành công
        if (src.empty()) {
            throw new RuntimeException("Failed to load image: " + imageFile.getAbsolutePath());
        }

        // Chuyển đổi sang grayscale
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(src, gray, opencv_imgproc.COLOR_BGR2GRAY);

        // Áp dụng GaussianBlur để giảm nhiễu
        Mat blurred = new Mat();
        opencv_imgproc.GaussianBlur(gray, blurred, new org.bytedeco.opencv.opencv_core.Size(5, 5), 0);

        // Áp dụng thresholding để làm rõ các ký tự
        Mat thresh = new Mat();
        opencv_imgproc.threshold(blurred, thresh, 0, 255, opencv_imgproc.THRESH_BINARY_INV + opencv_imgproc.THRESH_OTSU);

        // Lưu ảnh đã tiền xử lý vào một file tạm thời
        File preprocessedFile = new File(imageFile.getParent(), "preprocessed_" + imageFile.getName());
        opencv_imgcodecs.imwrite(preprocessedFile.getAbsolutePath(), thresh);

        return preprocessedFile;
    }

    private File preprocessImage_v2(File imageFile) {
        // Đọc ảnh vào Mat
        Mat src = opencv_imgcodecs.imread(imageFile.getAbsolutePath());

        // Kiểm tra nếu ảnh được tải thành công
        if (src.empty()) {
            throw new RuntimeException("Failed to load image: " + imageFile.getAbsolutePath());
        }

        // Chuyển đổi sang grayscale
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(src, gray, opencv_imgproc.COLOR_BGR2GRAY);

        // Tăng cường độ tương phản bằng cách sử dụng CLAHE
        Mat clahe = new Mat();
        opencv_imgproc.createCLAHE().apply(gray, clahe);

        // Áp dụng một chút GaussianBlur để làm mịn hình ảnh, nhưng không làm mất chi tiết quan trọng
        Mat blurred = new Mat();
        opencv_imgproc.GaussianBlur(clahe, blurred, new org.bytedeco.opencv.opencv_core.Size(3, 3), 0);

        // Áp dụng thresholding để làm rõ các ký tự
        Mat thresh = new Mat();
        opencv_imgproc.threshold(blurred, thresh, 0, 255, opencv_imgproc.THRESH_BINARY + opencv_imgproc.THRESH_OTSU);

        // Lưu ảnh đã tiền xử lý vào một file tạm thời
        File preprocessedFile = new File(imageFile.getParent(), "preprocessed_" + imageFile.getName());
        opencv_imgcodecs.imwrite(preprocessedFile.getAbsolutePath(), thresh);

        return preprocessedFile;
    }

    private File preprocessImage(File imageFile) {
        // Đọc ảnh vào Mat
        Mat src = opencv_imgcodecs.imread(imageFile.getAbsolutePath());

        // Kiểm tra nếu ảnh được tải thành công
        if (src.empty()) {
            throw new RuntimeException("Failed to load image: " + imageFile.getAbsolutePath());
        }

        // Chuyển đổi sang grayscale
        Mat gray = new Mat();
        opencv_imgproc.cvtColor(src, gray, opencv_imgproc.COLOR_BGR2GRAY);

        // Tăng cường độ tương phản bằng cách sử dụng CLAHE (Contrast Limited Adaptive Histogram Equalization)
        Mat clahe = new Mat();
        opencv_imgproc.createCLAHE(2.0, new org.bytedeco.opencv.opencv_core.Size(8, 8)).apply(gray, clahe);

        // Áp dụng một chút GaussianBlur nhẹ để làm mịn hình ảnh, nhưng không làm mất chi tiết quan trọng
        Mat blurred = new Mat();
        opencv_imgproc.GaussianBlur(clahe, blurred, new org.bytedeco.opencv.opencv_core.Size(3, 3), 0);

        // Áp dụng global thresholding với Otsu's method để làm rõ các ký tự
        Mat thresh = new Mat();
        opencv_imgproc.threshold(blurred, thresh, 0, 255, opencv_imgproc.THRESH_BINARY + opencv_imgproc.THRESH_OTSU);

        // Lưu ảnh đã tiền xử lý vào một file tạm thời
        File preprocessedFile = new File(imageFile.getParent(), "preprocessed_" + imageFile.getName());
        opencv_imgcodecs.imwrite(preprocessedFile.getAbsolutePath(), thresh);

        return preprocessedFile;
    }



}
