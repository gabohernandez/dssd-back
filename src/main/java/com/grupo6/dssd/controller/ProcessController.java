package com.grupo6.dssd.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.repository.ProtocolRepository;

@RestController
@RequestMapping("/protocol")
public class ProcessController {

	final ProtocolRepository repository;

	public ProcessController(ProtocolRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/start")
	public ResponseEntity<String> start() {
		Protocol protocol = repository.save(new Protocol());
		return ResponseEntity.ok("Protocolo con id: " + protocol.getId() + " arrancado");
	}
	
	@GetMapping("/status/{id}")
	public ResponseEntity<Optional<Protocol>> status(@PathVariable Integer id) {
		return ResponseEntity.ok(repository.findById(id));
	}

	@GetMapping("/status")
	public ResponseEntity<List<Protocol>> status() {
		return ResponseEntity.ok(repository.findAll());
	}

}
