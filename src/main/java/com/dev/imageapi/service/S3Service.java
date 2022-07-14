package com.dev.imageapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.SneakyThrows;
import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Objects;

@Service
public class S3Service {
    private AmazonS3 s3;

    @Autowired
    private AWSCredentialsService credentialsService;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    private void initializeS3() {
        this.s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentialsService.getCredentials()))
                .withRegion(region)
                .build();
    }

    public void uploadToBucket(MultipartFile multipartFile, String bucketName, String pathOnBucket) {
        File fileToSave = null;
        try {
            fileToSave = createFileFromMultipartFile(multipartFile);
            PutObjectRequest request = new PutObjectRequest(bucketName, pathOnBucket + multipartFile.getOriginalFilename(), fileToSave);

            s3.putObject(request);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (fileToSave != null) {
                fileToSave.delete();
            }
        }
    }

    public File downloadFromBucket(String fileName, String bucketName, String pathOnBucket) {
        File file = new File(fileName);
        ObjectMetadata objectMetadata = s3.getObject(new GetObjectRequest(bucketName, pathOnBucket + fileName), file);

        return file;
    }

    public void deleteImageFromBucket(String fileName, String bucketName, String pathOnBucket) {
        s3.deleteObject(bucketName, pathOnBucket + fileName);
    }

    private File createFileFromMultipartFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(multipartFile.getInputStream(), outputStream);
        } catch (IOException e) {
            throw e;
        }

        return file;
    }
}
