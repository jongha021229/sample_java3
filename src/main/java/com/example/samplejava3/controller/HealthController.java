package com.example.samplejava3.controller;

import org.springframework.boot.SpringBootVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("status", "ok");
        info.put("framework", "Spring Boot " + SpringBootVersion.getVersion());
        info.put("java", System.getProperty("java.version"));
        return info;
    }
}
