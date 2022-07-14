package com.dev.imageapi.service;

import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AWSCredentialsService {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    private BasicAWSCredentials credentials;

    @PostConstruct
    public void initializeCredentials() {
        credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    }

    public BasicAWSCredentials getCredentials() {
        return credentials;
    }
}
