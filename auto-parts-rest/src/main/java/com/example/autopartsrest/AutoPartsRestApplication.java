package com.example.autopartsrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.example.autopartsrest", "com.example.autopartscommon"})
@EntityScan("com.example.autopartscommon.entity")
@EnableJpaRepositories(basePackages = "com.example.autopartscommon")
public class AutoPartsRestApplication {


    public static void main(String[] args) {
        SpringApplication.run(AutoPartsRestApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
