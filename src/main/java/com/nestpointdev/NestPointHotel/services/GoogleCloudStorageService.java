package com.nestpointdev.NestPointHotel.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class GoogleCloudStorageService {

    @Value("${google.cloud.bucket.name}")
    private String bucketName;

    private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageService.class);

    // Set your credentials JSON file location
    private static final String CREDENTIALS_FILE_PATH = "client-secret.json";

    public String saveImageToGoogleCloud(MultipartFile photo) throws IOException {
        String fileName = photo.getOriginalFilename();
        String contentType = photo.getContentType();  // Get content type from MultipartFile

        // Load the credentials from the JSON file
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH));

        // Build the storage client with the credentials
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();

        // Define BlobId and BlobInfo
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        try (InputStream fileInputStream = photo.getInputStream()) {
            storage.create(blobInfo, fileInputStream);  // Upload the file to Google Cloud Storage
        } catch (IOException e) {
            logger.error("Error uploading image to Google Cloud Storage", e);
            throw e;  // Re-throw the exception for handling by caller
        }

        // Return the URL of the uploaded file
        return buildImageUrl(bucketName, fileName);
    }

    private String buildImageUrl(String bucketName, String fileName) {
        // Construct the URL to access the uploaded image in Google Cloud Storage
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }
}
