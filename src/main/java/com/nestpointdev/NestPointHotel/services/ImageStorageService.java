package com.nestpointdev.NestPointHotel.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final String uploadDirectory; // Define your local storage path

    public ImageStorageService(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + getExtension(image); // Generate unique filename
        Path filePath = Paths.get(uploadDirectory, fileName);

        // Create upload directory if it doesn't exist
        File uploadDir = filePath.getParent().toFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Write image data to the local file
        Files.write(filePath, image.getBytes());

        return filePath.toString(); // Return the full path to the saved image
    }

    private String getExtension(MultipartFile image) {
        String originalFileName = image.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }
}