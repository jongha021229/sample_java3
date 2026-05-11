package com.example.samplejava3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Intentional training-only vulnerabilities for scanner validation.
 * DO NOT use these patterns in production code.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    // Intentional vulnerability (training): hardcoded database credentials.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sample";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "P@ssw0rd123!";

    // Intentional vulnerability (training): SQL Injection via string concatenation.
    @GetMapping("/users")
    public String findUser(@RequestParam String name) throws Exception {
        StringBuilder out = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id, name FROM users WHERE name = '" + name + "'";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    out.append(rs.getString("id"))
                       .append(":")
                       .append(rs.getString("name"))
                       .append("\n");
                }
            }
        }
        return out.toString();
    }

    // Intentional vulnerability (training): OS command injection via Runtime.exec.
    @GetMapping("/ping")
    public String ping(@RequestParam String host) throws Exception {
        Process p = Runtime.getRuntime().exec("ping -c 1 " + host);
        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.append(line).append("\n");
            }
        }
        return out.toString();
    }

    // Intentional vulnerability (training): weak cryptographic hash (MD5).
    @GetMapping("/token")
    public String token(@RequestParam String userId) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest((userId + ":" + DB_PASSWORD).getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
