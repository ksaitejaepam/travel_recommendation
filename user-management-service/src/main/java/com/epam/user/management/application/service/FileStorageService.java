package com.epam.user.management.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        if (!Files.exists(Path.of(uploadDir))) {
            Files.createDirectories(Path.of(uploadDir));
        }
        File destinationFile = new File(uploadDir, uniqueFilename);

        Files.copy(file.getInputStream(), Paths.get(destinationFile.getAbsolutePath()));

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath() .path(uploadDir) .path(uniqueFilename) .toUriString();
        return fileUri;
    }
}
