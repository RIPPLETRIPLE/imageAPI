package com.dev.imageapi.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LambdaService {
    @Autowired
    private AWSCredentialsService credentialsService;

    public void runLambda(String region, String lambdaName) {
        InvokeRequest request = new InvokeRequest();
        request.withFunctionName(lambdaName);

        AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentialsService.getCredentials()))
                .withRegion(region).build();

        InvokeResult invoke = awsLambda.invoke(request);

        System.out.println("Result invoking " + lambdaName + ": " + invoke);
    }
}
