package com.dev.imageapi.controller;

import com.dev.imageapi.service.SNSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SubscribeController {
    @Autowired
    private SNSService snsService;

    @PostMapping("/subscription")
    public ResponseEntity<String> addSubscriber(String email) {
        snsService.addSubscription(email);
        return ResponseEntity.ok().body("Confirm yur subscription on email");
    }

    @DeleteMapping("/subscription")
    public ResponseEntity<String> deleteSubscriber(String email) {
        snsService.deleteSubscription(email);
        return ResponseEntity.ok().build();
    }
}
