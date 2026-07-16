package com.hassansherwani.medicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.hassansherwani.medicare")
@EnableRedisRepositories(basePackages = "com.hassansherwani.medicare.redis") // package doesn't exist yet — created when we build Redis features
public class MedicareManagementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicareManagementPlatformApplication.class, args);
	}

}
