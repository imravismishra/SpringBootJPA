package com.cogent.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.cogent.controller")
@EnableJpaRepositories(basePackages = { "com.cogent.repository" })
@EntityScan(basePackages = { "com.cogent.entity" })
public class SpringBootMvcjpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMvcjpaApplication.class, args);
	}

}
