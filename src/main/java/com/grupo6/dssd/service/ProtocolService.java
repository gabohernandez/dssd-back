package com.grupo6.dssd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.grupo6.dssd.api.request.CreateProtocolDTO;
import com.grupo6.dssd.exception.InvalidOperationException;
import com.grupo6.dssd.exception.InvalidProjectException;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.exception.ProtocolNotFoundException;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.model.User;
import com.grupo6.dssd.repository.ProjectRepository;
import com.grupo6.dssd.repository.ProtocolRepository;
import com.grupo6.dssd.repository.UserRepository;

/**
 * @author nahuel.barrena on 21/10/20
 */
@Service
public class ProtocolService {

	private final ProtocolRepository protocolRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public ProtocolService(ProtocolRepository protocolRepository, ProjectRepository projectRepository, UserRepository userRepository) {
		this.protocolRepository = protocolRepository;
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
	}

	public Protocol createProtocol(Long projectId, CreateProtocolDTO protocolDTO) throws ProjectNotFoundException {
		Protocol protocol = new Protocol(this.getProjectById(projectId));
		protocol.setName(protocolDTO.getName());
		Optional<User> user = userRepository.findById(protocolDTO.getUserId());
		if (user.isPresent()) {
			protocol.setUser(user.get());
		}
		return protocolRepository.save(protocol);
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
