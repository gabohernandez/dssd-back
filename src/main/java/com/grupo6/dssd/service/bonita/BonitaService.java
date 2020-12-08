package com.grupo6.dssd.service.bonita;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocol;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocolResult;
import com.grupo6.dssd.client.bonita.users.BonitaUserResponse;
import com.grupo6.dssd.model.Protocol;

/**
 * @author nahuel.barrena on 3/12/20
 */
@Service
public class BonitaService {

	private BonitaAPIClient bonitaAPIClient;

	public BonitaService(BonitaAPIClient bonitaAPIClient) {
		this.bonitaAPIClient = bonitaAPIClient;
	}

	public void postProtocols(List<Protocol> protocols) {
		List<BonitaProtocol> bonitaProtocols = new ArrayList<>();
		protocols.forEach(p -> {
			BonitaProtocol bonitaProtocol = new BonitaProtocol();
			bonitaProtocol.setId(p.getId());
			bonitaProtocol.setProyectoId(p.getProject().getId());
			bonitaProtocol.setActivo(true);
			bonitaProtocol.setEjecucionLocal(p.isLocal());
			bonitaProtocol.setPersistenceId(p.getId().toString());
			bonitaProtocol.setNombre(p.getName());
			bonitaProtocol.setResponsable(String.valueOf(p.getUserAssignId()));
			bonitaProtocols.add(bonitaProtocol);
		});
		Integer taskId = this.getTaskIdByTaskName("protocolos necesarios", protocols.get(0).getProject().getCaseId());
		bonitaAPIClient.createProtocols(taskId.toString(), bonitaProtocols, protocols.get(0).getProject().getId().intValue());
	}

	public Integer getTaskIdByTaskName(String taskName, String caseId) {
		return bonitaAPIClient.getTasks().stream().filter(task -> StringUtils.containsIgnoreCase(task.name, taskName) && String.valueOf(task.caseId).equalsIgnoreCase(caseId)).findFirst().get().id;
	}

	public void assignUserTask(String assignedId, String taskId) {
		bonitaAPIClient.assignUserTask(assignedId, taskId);
	}

	public Integer assignUserTaskByTaskNameForCase(String taskName, String caseId, String userId){
		Integer taskId = getTaskIdByTaskName(taskName, caseId);
		assignUserTask(taskId.toString(), userId);
		return taskId;
	}

	public void decideOnFailProtocol(Integer taskId, String description) {
		bonitaAPIClient.decideOnFailProtocol(taskId.toString(), description);
	}

	public void scoreProtocol(String taskId, BonitaProtocolResult bonitaProtocolResult) {
		bonitaAPIClient.scoreProtocol(taskId, bonitaProtocolResult);
	}
}
