package com.interview.canva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CanvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanvaApplication.class, args);
	}

}
