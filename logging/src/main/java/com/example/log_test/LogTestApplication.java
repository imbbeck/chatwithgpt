package com.example.log_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class LogTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogTestApplication.class, args);
	}

}
