package com.swiftly.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.swiftly")
@EnableJpaRepositories(basePackages = "com.swiftly.persistence")
@EntityScan(basePackages = "com.swiftly.domain")
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
