package com.example.demo.component;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GenerateLink {
    public String encode(String originLink) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
