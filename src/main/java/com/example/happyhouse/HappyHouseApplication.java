package com.example.happyhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HappyHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyHouseApplication.class, args);
    }

}
