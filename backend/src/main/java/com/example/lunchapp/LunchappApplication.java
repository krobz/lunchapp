package com.example.lunchapp;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LunchappApplication {

    public static void main(String[] args) {

        // Load .env file
        Dotenv dotenv = Dotenv.load();
        System.setProperty("USER_NAME", dotenv.get("USER_NAME"));
        System.setProperty("USER_PASSWORD", dotenv.get("USER_PASSWORD"));
        System.setProperty("API_KEY", dotenv.get("API_KEY"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        SpringApplication.run(LunchappApplication.class, args);
    }

}
