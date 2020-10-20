package com.grupo6.dssd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.repository.ProtocolRepository;

/**
 * @author nahuel.barrena on 20/10/20
 */
@RestController
@RequestMapping("/project")
public class ProcessController {

		private final ProtocolRepository repository;

		public ProcessController(ProtocolRepository repository) {
			this.repository = repository;
		}

		@PostMapping("/{project_id}/protocol/create")
		public ResponseEntity<String> createProtocol(
				@PathVariable(name = "project_id") Long projectId
		) {
			Protocol protocol = repository.save(new Protocol());
			return ResponseEntity.ok("Protocolo con id: " + protocol.getId() + " arrancado");
		}


		@PostMapping("/{project_id}/protocol/{protocol_id/start")
		public ResponseEntity<String> startProtocol(
				@PathVariable(name = "project_id") Long projectId,
				@PathVariable(name = "protocol_id") Long protocolId
		) {
			Protocol protocol = repository.save(new Protocol());
			return ResponseEntity.ok("Protocolo con id: " + protocol.getId() + " arrancado");
		}

		@GetMapping("{project_id}/protocol/{protocol_id}/status")
		public ResponseEntity<Protocol> protocolStatus(
				@PathVariable(name = "project_id") String projectId,
				@PathVariable(name = "protocol_id") Long id
		) {
			return ResponseEntity.ok(repository.findById(id).orElse(new Protocol()));
		}

		@GetMapping("{project_id}/protocols/status")
		public ResponseEntity<List<Protocol>> allProtocolStatus(
				@PathVariable(name = "project_id") String projectId
		) {
			return ResponseEntity.ok(repository.findAll());
		}



}
