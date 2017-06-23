package com.example.textSearchEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TextSearchEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextSearchEngineApplication.class, args);
    }
}
