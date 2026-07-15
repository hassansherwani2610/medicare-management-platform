package com.hassansherwani.medicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MedicareManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareManagementPlatformApplication.class, args);
	}

}
