package com.signature.backend.service;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleVisionOcrService {

    public static ImageAnnotatorClient createVisionClient() throws IOException {
        // Thay thế đường dẫn dưới đây bằng đường dẫn đến tệp JSON của bạn
        String jsonPath = "D:\\BANGNA1\\Devlog\\Personal\\WebVerifyGovernmentID\\backend\\GoogleCloudVisionKey\\ocr-project-433608-e50557a6a865.json";

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
                .createScoped("https://www.googleapis.com/auth/cloud-platform");

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return ImageAnnotatorClient.create(settings);
    }

    public String performOcr(String imagePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(imagePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        // Sử dụng BatchAnnotateImagesResponse để xử lý kết quả trả về
        try (ImageAnnotatorClient client = createVisionClient()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            StringBuilder result = new StringBuilder();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    throw new IOException("Error during image annotation: " + res.getError().getMessage());
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    result.append(annotation.getDescription());
                }
            }
            return result.toString();
        }
    }
}


