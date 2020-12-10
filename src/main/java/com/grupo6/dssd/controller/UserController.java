package com.grupo6.dssd.controller;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.grupo6.dssd.Constant;
import com.grupo6.dssd.model.User;
import com.grupo6.dssd.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	
	final UserRepository repository;

	public UserController(UserRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		if(!repository.findByNameAndPassword(user.getName(), user.getPassword()).isPresent()) {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
		String token = getJWTToken(user.getName());
		return ResponseEntity.ok(token);

	}

	private String getJWTToken(String username) {
//		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId(Constant.SECRET_KEY).setSubject(username)
//		        .claim("authorities",
//		                grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(new Date(System.currentTimeMillis() + 12000000))
		        .signWith(SignatureAlgorithm.HS512, Constant.SECRET_KEY.getBytes()).compact();

		return Constant.AUTHORIZATION_TYPE + token;
	}
}
