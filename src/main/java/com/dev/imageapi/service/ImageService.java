package com.dev.imageapi.service;

import com.dev.imageapi.entity.ImageSNSMessage;
import com.dev.imageapi.exception.ImageNotExist;
import com.dev.imageapi.exception.NotUniqueName;
import com.dev.imageapi.entity.ImageMetaData;
import com.dev.imageapi.repository.ImageMetaDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ImageService {
    @Autowired
    private ImageMetaDataRepository imageMetaDataRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SQSService sqsService;

    private final String BUCKET_NAME = "bucket4webserver";

    private final String PATH_ON_BUCKET = "imageAPI/";

    public void saveMetaData(MultipartFile image) {
        ImageMetaData imageMetaData = new ImageMetaData();
        imageMetaData.setName(image.getOriginalFilename());
        imageMetaData.setCreateDate(Date.valueOf(LocalDate.now()));
        if (imageMetaDataRepository.existsByName(image.getOriginalFilename())) {
            throw new NotUniqueName("Image with such name already exist");
        }
        imageMetaDataRepository.save(imageMetaData);
    }

    public void saveImageMessageToSQS(MultipartFile image) {
        ImageMetaData imageMetaData = imageMetaDataRepository.findByName(image.getOriginalFilename());

        ImageSNSMessage snsMessage = ImageSNSMessage.builder()
                .status(ImageSNSMessage.ImageUploadStatus.SUCCESS)
                .metaData(imageMetaData)
                .build();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String json = ow.writeValueAsString(snsMessage);
            sqsService.postMessageToSQS(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveImageToS3(MultipartFile image) {
        s3Service.uploadToBucket(image, BUCKET_NAME, PATH_ON_BUCKET);
    }

    public byte[] getImageFromS3(String imageName) {
        if (!imageMetaDataRepository.existsByName(imageName)) {
            throw new ImageNotExist("Image doesn't exist");
        }

        File file = s3Service.downloadFromBucket(imageName, BUCKET_NAME, PATH_ON_BUCKET);
        Path path = Paths.get(file.getPath());

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong, try again later");
        } finally {
            file.delete();
        }
    }

    public void deleteImageFromS3(String imageName) {
        if (!imageMetaDataRepository.existsByName(imageName)) {
            throw new ImageNotExist("Image doesn't exist");
        }

        s3Service.deleteImageFromBucket(imageName, BUCKET_NAME, PATH_ON_BUCKET);
        imageMetaDataRepository.deleteByName(imageName);
    }

    public List<ImageMetaData> getAllImagesMetaData() {
        return imageMetaDataRepository.findAll();
    }
}
