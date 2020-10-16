package com.grupo6.dssd.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	UserRepository repository;

	@PostMapping("login")
	public ResponseEntity login(@RequestBody User user) {
		
		if (this.repository.findByName(user.getName()) == null) {
			return ResponseEntity.notFound().build();
		}

		String token = getJWTToken(user.getName());
		
//		User user = new User();
		return ResponseEntity.ok(token);

	}

	private String getJWTToken(String username) {
//		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId(Constant.SECRET_KEY).setSubject(username)
//		        .claim("authorities",
//		                grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(new Date(System.currentTimeMillis() + 600000))
		        .signWith(SignatureAlgorithm.HS512, Constant.SECRET_KEY.getBytes()).compact();

		return Constant.AUTHORIZATION_TYPE + token;
	}
}
