package com.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Service is up and running", HttpStatus.OK);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }
}