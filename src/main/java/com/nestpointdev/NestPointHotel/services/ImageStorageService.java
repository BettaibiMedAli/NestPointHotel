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

    private final String uploadDirectory;

    public ImageStorageService(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + getExtension(image);
        Path filePath = Paths.get(uploadDirectory, fileName);

        File uploadDir = filePath.getParent().toFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // if the directory does not exist, it creates a new one :)
        }

        Files.write(filePath, image.getBytes());

        return filePath.toString();
    }

    private String getExtension(MultipartFile image) {
        String originalFileName = image.getOriginalFilename();
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    } // this is a helper method to extract the file extension
}