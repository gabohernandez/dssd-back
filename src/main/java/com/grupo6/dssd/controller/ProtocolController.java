package com.grupo6.dssd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.exception.InvalidOperationException;
import com.grupo6.dssd.exception.InvalidProjectException;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.exception.ProtocolNotFoundException;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.service.ProtocolService;

/**
 * @author nahuel.barrena on 20/10/20
 */
@RestController
@RequestMapping()
public class ProtocolController {

	private final ProtocolService protocolService;

	public ProtocolController(ProtocolService protocolService) {
		this.protocolService = protocolService;
	}

	@PostMapping("/project/{project_id}/protocol/create")
	public ResponseEntity<Protocol> createProtocol(
			@PathVariable(name = "project_id") Long projectId) throws ProjectNotFoundException {
		return ResponseEntity.ok(protocolService.createProtocol(projectId));
	}

	@PostMapping("/project/{project_id}/protocol/start")
	public ResponseEntity<String> startProtocol(
			@PathVariable(name = "project_id") Long projectId)
			 {
		return ResponseEntity.ok(protocolService.startNewProtocol(projectId));
	}

	@GetMapping("protocol/{protocol_id}/status")
	public ResponseEntity<Integer> getProtocolScore(
			@PathVariable(name = "protocol_id") String protocolUUID)
			{
		return ResponseEntity.ok(protocolService.getProtocol(protocolUUID));
	}


/*
	@GetMapping("/project/{project_id}/protocol/{protocol_id}/status")
	public ResponseEntity<Protocol> getProtocol(
			@PathVariable(name = "project_id") Long projectId, @PathVariable(name = "protocol_id") Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		return ResponseEntity.ok(protocolService.getProtocol(projectId, protocolId));
	}
*/

	@GetMapping("/project/protocols/status")
	public ResponseEntity<List<Protocol>> allProtocolStatus() {
		return ResponseEntity.ok(protocolService.findAll());
	}

}
