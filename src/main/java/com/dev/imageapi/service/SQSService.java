package com.dev.imageapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SQSService {
    @Value("${cloud.aws.end-point.uri}")
    private String endPoint;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    public void postMessageToSQS(String message) {
        queueMessagingTemplate.send(endPoint, MessageBuilder.withPayload(message).build());
    }
}
