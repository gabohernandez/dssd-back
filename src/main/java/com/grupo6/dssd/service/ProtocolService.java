package com.grupo6.dssd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.grupo6.dssd.api.request.CreateProtocolDTO;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;
import com.grupo6.dssd.client.bonita.cases.CaseResponse;
import com.grupo6.dssd.client.bonita.process.ProcessResponse;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocolResult;
import com.grupo6.dssd.exception.InvalidOperationException;
import com.grupo6.dssd.exception.InvalidProjectException;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.exception.ProtocolNotFoundException;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.model.ProtocolStatus;
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
	private final BonitaAPIClient bonitaAPIClient;

	public ProtocolService(ProtocolRepository protocolRepository, ProjectRepository projectRepository,
			UserRepository userRepository, BonitaAPIClient bonitaAPIClient) {
		this.protocolRepository = protocolRepository;
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.bonitaAPIClient = bonitaAPIClient;
	}

	public Protocol createProtocol(Long projectId, CreateProtocolDTO protocolDTO) throws ProjectNotFoundException {

		Protocol protocol = new Protocol(this.getProjectById(projectId));
		protocol.setName(protocolDTO.getName());
		protocol.setLocal(protocolDTO.isLocal());
		if(protocolDTO.getUserId() != null) {
			Optional<User> user = userRepository.findById(protocolDTO.getUserId());
			user.ifPresent(protocol::setUser);
			int idToAssign = bonitaAPIClient.getUsers().stream()
					.filter(u -> u.userName.equalsIgnoreCase(protocol.getUser().getName())).findFirst().get().id;
			protocol.setUserAssignId(idToAssign);

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

	public List<Protocol> findAllProtocols() {
		return this.protocolRepository.findAll();
	}

	public List<Project> findAllProjects() {
		return projectRepository.findAll();
	}

	public User getMostBusyUser() {
		return protocolRepository.busiestUser().get(0);
	}

	public List<Protocol> findByProject(Long projectId)  {
		return this.protocolRepository.findByProjectId(projectId);
	}
	
	public List<Protocol> findByUser(Long userId) {
		List<Protocol> fromStartedProject = new ArrayList<>();
		List<Protocol> protocols = this.protocolRepository.findByUserId(userId);
		protocols.forEach(p -> {
					if (p.getProject().getStatus().equalsIgnoreCase("STARTED")){
						fromStartedProject.add(p);
					}
				}
		);
		return fromStartedProject;
	}

	public Protocol score(Long protocolId, Integer score) throws Exception {
		Optional<Protocol> protocol = protocolRepository.findById(protocolId);
		if (protocol.isPresent()) {
			Integer taskId = bonitaAPIClient.getTasks().stream().filter(task -> task.name.contains("Ejecutar protocolo") && String.valueOf(task.caseId).equalsIgnoreCase(protocol.get().getProject().getCaseId())).findFirst().get().id;
			bonitaAPIClient.assignUserTask(taskId.toString(), String.valueOf(protocol.get().getUserAssignId()));
			protocol.get().setScore(score);
			if(score >= 7) {
				 protocol.get().finish();
			}
			else {
				protocol.get().toFail();
			}
			bonitaAPIClient.scoreProtocol(taskId.toString(), new BonitaProtocolResult(score.toString(), score >= 7));
			return protocolRepository.save(protocol.get());
		}else {
			throw new Exception("Protocolo no encontrado");
		}
	}

	public void startProject(Long projectId) throws Exception {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new Exception("No hay proyecto."));
		project.setStatus("STARTED");
		projectRepository.save(project);
		protocolRepository.findByProjectId(projectId).forEach(
				protocol -> {
					protocol.start();
					protocolRepository.save(protocol);
				}
		);
	}
}
