package com.hassansherwani.medicare.common.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hassansherwani.medicare.common.exception.BusinessRuleViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileStorageService {

    private final Cloudinary cloudinary;

    @Autowired
    public FileStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_TYPES = {"image/jpeg", "image/png", "image/jpg"};

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessRuleViolationException("Profile picture is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessRuleViolationException("Image size must not exceed 5MB");
        }

        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String type : ALLOWED_TYPES) {
            if (type.equalsIgnoreCase(contentType)) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new BusinessRuleViolationException("Only JPG and PNG images are allowed");
        }
    }

    public String uploadImage(MultipartFile file, String folder) {
        validateFile(file);

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    )
            );
            return uploadResult.get("secure_url").toString();
        } catch (IOException exception) {
            throw new BusinessRuleViolationException("Failed to upload image. Please try again.");
        }
    }
}