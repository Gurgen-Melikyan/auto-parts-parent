package com.example.autopartsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = {"com.example.autopartsweb","com.example.autopartscommon"})
@EntityScan("com.example.autopartscommon.entity")
@EnableJpaRepositories(basePackages = "com.example.autopartscommon")
public class AutoPartsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoPartsWebApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
