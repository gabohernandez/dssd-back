package com.grupo6.dssd.controller.remote;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.grupo6.dssd.Constant;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;
import com.grupo6.dssd.client.bonita.cases.CaseResponse;
import com.grupo6.dssd.client.bonita.process.ProcessResponse;
import com.grupo6.dssd.model.User;
import com.grupo6.dssd.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

	final UserRepository repository;
	final BonitaAPIClient bonitaAPIClient;

	public UserController(UserRepository repository, BonitaAPIClient bonitaAPIClient) {
		this.repository = repository;
		this.bonitaAPIClient = bonitaAPIClient;
	}

	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		Optional<User> foundUser = repository.findByNameAndPassword(user.getName(), user.getPassword());
		if(!foundUser.isPresent()) {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
		bonitaAPIClient.login(foundUser.get());
		List<ProcessResponse> process = bonitaAPIClient.getProcesses();
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(new HashMap<>());
		bonitaAPIClient.postProtocols("", new ArrayList<>());
		String authToken = getJWTToken(user.getName());
		return ResponseEntity.ok(authToken);
	}

	private String getJWTToken(String username) {
//		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId(Constant.SECRET_KEY).setSubject(username)
//		        .claim("authorities",
//		                grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(new Date(System.currentTimeMillis() + 1200000))
		        .signWith(SignatureAlgorithm.HS512, Constant.SECRET_KEY.getBytes()).compact();

		return Constant.AUTHORIZATION_TYPE + token;
	}
}
