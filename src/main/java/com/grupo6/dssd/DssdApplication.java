package com.grupo6.dssd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DssdApplication {

	public static void main(String[] args) {
		SpringApplication.run(DssdApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200")
				        .allowedMethods("GET", "POST", "PUT", "DELETE").maxAge(3600);
			}

		};
	}

}
