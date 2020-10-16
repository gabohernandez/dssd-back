package com.grupo6.dssd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.repository.ProtocolRepository;

@RestController
@RequestMapping("/protocol")
public class ProcessController {

	@Autowired
	ProtocolRepository repository;

	@PostMapping("/start")
	public ResponseEntity start() {
		Protocol protocol = repository.save(new Protocol());
		return ResponseEntity.ok("Protocolo con id: " + protocol.getId() + " arrancado");
	}
	
	@GetMapping("/status/{id}")
	public ResponseEntity status(@PathVariable Integer id) {
		return ResponseEntity.ok(repository.findById(id));
	}

	@GetMapping("/status")
	public ResponseEntity status() {
		return ResponseEntity.ok(repository.findAll());
	}

}
