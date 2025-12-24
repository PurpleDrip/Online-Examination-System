package com.examportal.marks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Marks Service Application
 * Calculates and manages student results
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MarksServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarksServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
