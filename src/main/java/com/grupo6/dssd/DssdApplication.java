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

	@EnableWebSecurity
	@Configuration
	static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
			        .addFilterAfter(new JWTAuthorizationFilter(authenticationManager()),
			                UsernamePasswordAuthenticationFilter.class)

			        .authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll()
					// .antMatchers(HttpMethod.GET, "/process")
					// Conf para habilitar la consola de h2
					// si deja de funcionar algo, comentar
					.and()
					.headers().frameOptions().disable()
					.and()
					.authorizeRequests().antMatchers("/h2-console/**").permitAll()
					////
					.anyRequest().authenticated();
		}
	}

}
