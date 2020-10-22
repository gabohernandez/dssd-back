package com.grupo6.dssd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.grupo6.dssd.filter.JWTAuthorizationFilter;

/**
 * @author nahuel.barrena on 20/10/20
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
					.and()
					.authorizeRequests().antMatchers("/swagger-ui.html#/**").permitAll()
					.and()
					.authorizeRequests().antMatchers("/v2/api-docs/**").permitAll()
					////
					.anyRequest().authenticated();
		}
}


