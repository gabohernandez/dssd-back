package com.grupo6.dssd.controller.bonita;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;

/**
 * @author nahuel.barrena on 1/12/20
 */
@RestController
@RequestMapping("/bonita")
public class BonitaController {

	private final BonitaAPIClient bonitaClient;

	public BonitaController(BonitaAPIClient bonitaClient) {
		this.bonitaClient = bonitaClient;
	}

	/*@GetMapping(value = "/users")
	public ResponseEntity<List<Map<String, Object>>> getUsers(){
		return ResponseEntity.ok(bonitaClient.getUsers());
	}*/


}
