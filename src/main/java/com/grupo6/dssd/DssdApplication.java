package com.grupo6.dssd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.grupo6.dssd.filter.JWTAuthorizationFilter;

@SpringBootApplication
public class DssdApplication {

	public static void main(String[] args) {
		SpringApplication.run(DssdApplication.class, args);
	}



}
