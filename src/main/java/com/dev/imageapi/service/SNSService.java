package com.dev.imageapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.dev.imageapi.controller.SubscribeController;
import com.dev.imageapi.exception.SubscriptionNotPresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SNSService {
    private AmazonSNS sns;

    @Autowired
    private AWSCredentialsService credentialsService;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.sns.arn}")
    private String arn;

    @PostConstruct
    private void initializeSNS() {
        this.sns = AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentialsService.getCredentials()))
                .withRegion(region)
                .build();
    }

    public void addSubscription(String email) {
        SubscribeRequest request = new SubscribeRequest(arn, "email", email);
        sns.subscribe(request);
    }

    public void deleteSubscription(String email) {
        String subscriptionArn = sns.listSubscriptions().getSubscriptions().stream().filter(e -> e.getEndpoint().equals(email))
                .findFirst().orElseThrow(() -> new SubscriptionNotPresent("Subscription with such email not exist")).getSubscriptionArn();

        sns.unsubscribe(subscriptionArn);
    }
}
