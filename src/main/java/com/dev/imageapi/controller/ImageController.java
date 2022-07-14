package com.dev.imageapi.controller;

import com.dev.imageapi.service.ImageService;
import com.dev.imageapi.entity.ImageMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class ImageController {
    @Autowired
    ImageService imageService;

    @PostMapping("image")
    private ResponseEntity<String> uploadImage(MultipartFile image) {
        imageService.saveMetaData(image);
        imageService.saveImageToS3(image);
        imageService.saveImageMessageToSQS(image);
        return ResponseEntity.ok().build();
    }

    @GetMapping("image")
    private ResponseEntity<byte[]> downloadImage(String imageName) {
        return ResponseEntity.ok().header("Content-Type", "image/jpeg; charset=UTF-8")
                .body(imageService.getImageFromS3(imageName));
    }

    @GetMapping("imageMetaData")
    private ResponseEntity<List<ImageMetaData>> getImagesMetaData() {
        List<ImageMetaData> imageMetaDataList = imageService.getAllImagesMetaData();
        return ResponseEntity.ok().body(imageMetaDataList);
    }

    @DeleteMapping("image")
    private ResponseEntity<String> deleteImage(String imageName) {
        imageService.deleteImageFromS3(imageName);

        return ResponseEntity.ok().build();
    }
}
