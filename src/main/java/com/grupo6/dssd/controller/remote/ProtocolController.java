package com.grupo6.dssd.controller.remote;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.grupo6.dssd.api.request.CreateProtocolDTO;
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
@RequestMapping("/project")
public class ProtocolController {

	private final ProtocolService protocolService;

	public ProtocolController(ProtocolService protocolService) {
		this.protocolService = protocolService;
	}
	
	@GetMapping("/{project_id}/protocol")
	public ResponseEntity<List<Protocol>> findProtocolFroProject(
			@PathVariable(name = "project_id") Long projectId) throws ProjectNotFoundException {
		return ResponseEntity.ok(protocolService.findByProject(projectId));
	}

	@PostMapping("/{project_id}/protocol/create")
	public ResponseEntity<Protocol> createProtocol(
			@PathVariable(name = "project_id") Long projectId,
			@RequestBody CreateProtocolDTO protocol) throws ProjectNotFoundException {
		return ResponseEntity.ok(protocolService.createProtocol(projectId, protocol));
	}

	@PostMapping("/{project_id}/protocol/{protocol_id}/start")
	public ResponseEntity<Protocol> startProtocol(
			@PathVariable(name = "project_id") Long projectId, @PathVariable(name = "protocol_id") Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		return ResponseEntity.ok(protocolService.startProtocol(projectId, protocolId));
	}

	@GetMapping("/{project_id}/protocol/{protocol_id}/status")
	public ResponseEntity<Protocol> getProtocol(
			@PathVariable(name = "project_id") Long projectId, @PathVariable(name = "protocol_id") Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		return ResponseEntity.ok(protocolService.getProtocol(projectId, protocolId));
	}

	@GetMapping("/protocols/status")
	public ResponseEntity<List<Protocol>> allProtocolStatus() {
		return ResponseEntity.ok(protocolService.findAll());
	}

}
