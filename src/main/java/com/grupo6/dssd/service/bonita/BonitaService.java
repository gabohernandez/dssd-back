package com.grupo6.dssd.service.bonita;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocol;
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
		List<BonitaUserResponse> bonitaUsers = bonitaAPIClient.getUsers();
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
		Integer taskId = bonitaAPIClient.getTasks().stream().filter(task -> task.name.contains("protocolos necesarios")
				&& String.valueOf(task.caseId).equals(protocols.get(0).getProject().getCaseId())).findFirst().get().id;
		bonitaAPIClient.createProtocols(taskId.toString(), bonitaProtocols, protocols.get(0).getProject().getId().intValue());
	}
}
