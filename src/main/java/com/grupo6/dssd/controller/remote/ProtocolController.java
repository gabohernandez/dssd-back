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
import com.grupo6.dssd.service.bonita.BonitaService;

/**
 * @author nahuel.barrena on 20/10/20
 */
@RestController
public class ProtocolController {

	private final ProtocolService protocolService;
	private final BonitaService bonitaService;

	public ProtocolController(ProtocolService protocolService, BonitaService bonitaService) {
		this.protocolService = protocolService;
		this.bonitaService = bonitaService;
	}
	
	@GetMapping("/project/{project_id}/protocol")
	public ResponseEntity<List<Protocol>> findProtocolForProject(
			@PathVariable(name = "project_id") Long projectId) throws ProjectNotFoundException {
		return ResponseEntity.ok(protocolService.findByProject(projectId));
	}

	@PostMapping("/project/{project_id}/protocol/create")
	public ResponseEntity<Protocol> createProtocol(
			@PathVariable(name = "project_id") Long projectId,
			@RequestBody CreateProtocolDTO protocol) throws ProjectNotFoundException {

		return ResponseEntity.ok(protocolService.createProtocol(projectId, protocol));
	}

	@PostMapping("/project/{project_id}/start")
	public ResponseEntity<Void> startProject(
			@PathVariable(name = "project_id") Long projectId) throws Exception {
		this.protocolService.startProject(projectId);
		this.bonitaService.postProtocols(protocolService.findByProject(projectId));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/project/{project_id}/protocol/{protocol_id}/start")
	public ResponseEntity<Protocol> startProtocol(
			@PathVariable(name = "project_id") Long projectId, @PathVariable(name = "protocol_id") Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		return ResponseEntity.ok(protocolService.startProtocol(projectId, protocolId));
	}

	@GetMapping("/project/{project_id}/protocol/{protocol_id}/status")
	public ResponseEntity<Protocol> getProtocol(
			@PathVariable(name = "project_id") Long projectId, @PathVariable(name = "protocol_id") Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		return ResponseEntity.ok(protocolService.getProtocol(projectId, protocolId));
	}

	@GetMapping("/project/protocols/status")
	public ResponseEntity<List<Protocol>> allProtocolStatus() {
		return ResponseEntity.ok(protocolService.findAllProtocols());
	}
	
	@GetMapping("/protocols/{user_id}/my-protocols")
	public ResponseEntity<List<Protocol>> myProtocols(@PathVariable(name = "user_id") Long userId) {
		//TODO: No deberíamos recibir el id por parámetro sino sacarlo del jwt
		return ResponseEntity.ok(protocolService.findByUser(userId));
	}
	
	@PostMapping("/protocols/{protocol_id}/score/{score}")
	public ResponseEntity<Object> score(@PathVariable(name = "protocol_id") Long protocolId,@PathVariable(name = "score") Integer score) {
		try {
			return ResponseEntity.ok(protocolService.score(protocolId, score));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
