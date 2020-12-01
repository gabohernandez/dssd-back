package com.grupo6.dssd.service;

import java.util.List;

import javax.transaction.Transactional;

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

	public Protocol startProtocol(Long projectId, Long protocolId)
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
	}

	public Protocol getProtocol(Long projectId, Long protocolId)
			throws InvalidProjectException, ProtocolNotFoundException, InvalidOperationException {
		Protocol protocol = this.protocolRepository
				.findById(protocolId)
				.orElseThrow(() -> new ProtocolNotFoundException("El protocolo con id " + protocolId + " no existe."));
		if(!protocol.getProject().getId().equals(projectId))
			throw new InvalidProjectException(String.format("El protocolo: [%s] no esta asociado al proyecto con id: %s.",
					protocol, projectId));
		return protocol;
	}


	private Project getProjectById(Long projectId) throws ProjectNotFoundException {
		return this.projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(
				"El proyecto con id " + projectId + " no existe. Imposible crear el protocolo."));
	}

	public List<Protocol> findAll() {
		return this.protocolRepository.findAll();
	}

	public List<Protocol> findByProject(Long projectId) {
		return this.protocolRepository.findByProjectId(projectId);
	}
}
