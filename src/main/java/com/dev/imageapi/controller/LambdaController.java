package com.dev.imageapi.controller;

import com.dev.imageapi.service.LambdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LambdaController {
    @Autowired
    private LambdaService lambdaService;

    @GetMapping("/invoke")
    public ResponseEntity<String> invokeLambda() {
        lambdaService.runLambda("us-east-1", "ImageAPI-uploads-batch-notifie");
        return ResponseEntity.ok().build();
    }
 }
