package com.itemManagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageUtil {

    @Value("${spring.img.path}")
    private String imgPath;

    private Path imagePath(String imgName){
       return Paths.get(imgPath).resolve(imgName).normalize();
    }

    private void validateImageFile(MultipartFile imgFile) {
        if (imgFile.isEmpty()) {
            throw new MultipartException("Failed to upload empty file.");
        }

        String contentType = imgFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new MultipartException("File must be an image.");
        }
    }

    private String generateRandomFileName(String originalImgName) {
        if (originalImgName != null && originalImgName.contains(".")) {
            String ext = originalImgName.substring(originalImgName.lastIndexOf(".") + 1).toLowerCase();
            return UUID.randomUUID().toString() + "." + ext;
        }
        return "";
    }

    public Resource viewImage(String imgName){
        try {
            Path imgPath = imagePath(imgName);
            if (Files.exists(imgPath)) {
                return new UrlResource(imgPath.toUri());
            } else {
                System.out.println("Image not found");
                return null;
            }

        } catch (MalformedURLException e) {
            System.out.println("Wrong image path url");
            return null;
        }
    }

    public String uploadImage(MultipartFile imgFile) {
        validateImageFile(imgFile);
        String newImgName = generateRandomFileName(imgFile.getOriginalFilename());
        Path uploadPath = Paths.get(imgPath);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(imgFile.getInputStream(), uploadPath.resolve(newImgName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            System.out.println("Could not save image file: " + newImgName);
        }
        return "/" + newImgName;
    }

    public String updateImage(String existingImgName, MultipartFile newImgFile) {
        validateImageFile(newImgFile);
        deleteImage(existingImgName);
        return uploadImage(newImgFile);
    }

    public void deleteImage(String imgName){
        if (imgName == null || imgName.isEmpty()) {
            System.out.println("Image name is null or empty. Skipping delete operation.");
            return;
        }

        imgName = imgName.substring(1);
        Path imgPath = imagePath(imgName);

        try {
            if (Files.exists(imgPath)) {
                Files.delete(imgPath);
            } else {
                System.out.println("Delete image fail. File not found");
            }
        } catch (IOException e) {
            System.out.println("Could not delete image");
        }
    }


}
