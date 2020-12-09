package com.grupo6.dssd.service;

import java.util.List;
import java.util.UUID;


import org.springframework.stereotype.Service;
import com.grupo6.dssd.exception.InvalidOperationException;
import com.grupo6.dssd.exception.InvalidProjectException;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.exception.ProtocolNotFoundException;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.repository.ProjectRepository;
import com.grupo6.dssd.repository.ProtocolRepository;

/**
 * @author nahuel.barrena on 21/10/20
 */
@Service
public class ProtocolService {

	private final ProtocolRepository protocolRepository;
	private final ProjectRepository projectRepository;

	public ProtocolService(ProtocolRepository protocolRepository, ProjectRepository projectRepository) {
		this.protocolRepository = protocolRepository;
		this.projectRepository = projectRepository;
	}

	public Protocol createProtocol(Long projectId) throws ProjectNotFoundException {
		return protocolRepository.save(new Protocol(this.getProjectById(projectId)));
	}

	public String startNewProtocol(Long projectId, String protocolUUID){
		Project project = projectRepository.findById(projectId).orElseGet(() -> {
			Project newProject = new Project();
			newProject.setId(projectId);
			newProject.setName("Proyecto_id_" + projectId + "_ran_" + protocolUUID.substring(0, 7));
			return projectRepository.save(newProject);
		});
		Protocol protocol = new Protocol(project, protocolUUID);
		protocol.start();
		Protocol savedProtocol = protocolRepository.save(protocol);
		return savedProtocol.getProtocolUUID();
	}

	/*public Protocol startProtocol(Long projectId, Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		Protocol protocol = this.protocolRepository
				.findById(protocolId)
				.orElseThrow(() -> new ProtocolNotFoundException("El protocolo con id " + protocolId + " no existe."));
		if(!protocol.getProject().getId().equals(projectId))
			throw new InvalidProjectException(String.format("El protocolo: [%s] no esta asociado al proyecto con id: %s.",
					protocol, projectId));
		if(protocol.isStarted()) {
			throw new InvalidOperationException(String.format("El protocolo: [%s] ya ha sido iniciado.", protocol));
		}
		if(protocol.isFinished()){
			this.protocolRepository.save(protocol);
			throw new InvalidOperationException(String.format("El protocolo: [%s] tiene estado finalizado.", protocol));
		}
		protocol.start();
		this.protocolRepository.save(protocol);
		return protocol;
	}*/

/*
	public Protocol getProtocol(Long projectId, Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException {
		Protocol protocol = this.protocolRepository
				.findById(protocolId)
				.orElseThrow(() -> new ProtocolNotFoundException("El protocolo con id " + protocolId + " no existe."));
		if(!protocol.getProject().getId().equals(projectId))
			throw new InvalidProjectException(String.format("El protocolo: [%s] no esta asociado al proyecto con id: %s.",
					protocol, projectId));
		return protocol;
	}
*/


	public Integer getProtocol(String protocolUUID) {
		return this.protocolRepository.findByRandomUUID(protocolUUID).map(protocol -> {
			protocol.finish();
			protocolRepository.save(protocol);
			return protocol.getScore();
		}).orElse(0);
	}



	private Project getProjectById(Long projectId) throws ProjectNotFoundException {
		return this.projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(
				"El proyecto con id " + projectId + " no existe. Imposible crear el protocolo."));
	}

	public List<Protocol> findAll() {
		return this.protocolRepository.findAll();
	}
}
