package com.grupo6.dssd.client.bonita;

import java.util.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import com.grupo6.dssd.client.bonita.cases.CaseResponse;
import com.grupo6.dssd.client.bonita.process.ProcessResponse;
import com.grupo6.dssd.exception.BonitaAccessException;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.model.User;

/**
 * @author nahuel.barrena on 19/11/20
 */
@Component
public class BonitaAPIClient extends BonitaBaseClient {

	/**
	 * Este atributo es contexto. Cada llamada de login lo cambia. Por lo tanto no es concurrente.
	 * Si se quiere tener una pegada sin este contexto, los datos tienen que venir del front,
	 * en cada pegada a procesos y casos. Por ahora se deja asi.
	 * De todas formas implementar el envio de header desde el front.
	 * HEADERS NECESARIOS DESDE EL FRONT (con el formato exacto y valores dinamicos)
	 * 'Cookie: bonita.tenant=1; X-Bonita-API-Token=c5af50ba-e6e3-4216-ad1f-ba1726b66440; JSESSIONID=F884ECE8D74D265DFFA795E915C25559'
	 * 'X-Bonita-API-Token=c5af50ba-e6e3-4216-ad1f-ba1726b66440'
	 */
	private BonitaSession bonitaSession;
	/**
	 * Si se opta por llamar al login cada vez que quiero hacer algo
	 * Para ahorrarme el envio de cookies desded el front, hacer este metodo estatico
	 * y devolver lo siguiente
	 * new BonitaSession(Objects.requireNonNull(bonitaApiToken).getHeaders());
	 */
	public ClientResponse login(User user)  {
		return webClient.post()
				.uri(uriBuilder ->
						uriBuilder.path("/loginservice")
								.queryParam("username", user.getName())
								.queryParam("password", user.getPassword())
								.queryParam("redirect", false)
								.build()
				)
				.body(BodyInserters.empty())
				.exchange()
				.doOnSuccess(clientResponse -> {
					this.throwCommError(clientResponse);
					bonitaSession = new BonitaSession(clientResponse.headers().asHttpHeaders());
				})
				.doOnError(e -> {
					throw new BonitaAccessException("Cannot login " + e.getMessage());
				})
				.block()
				;

	}

	public void logout() {
		webClient.get()
				.uri("/logoutservice")
				.retrieve()
				.toBodilessEntity()
				.block();
	}

	public ProcessResponse getProcessByName(String processName){
		return this.getProcesses()
				.stream()
				.filter(process -> process.getName().equalsIgnoreCase(processName))
				.findFirst()
				.orElseThrow(() -> new BonitaAccessException("Cannot find any process with name" + processName))
				;

	}

	public List<ProcessResponse> getProcesses(){
		return Arrays.asList(Objects.requireNonNull(Objects.requireNonNull(webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/API/bpm/process")
						.queryParam("c", "10")
						.queryParam("p", "0")
						.build())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(ProcessResponse[].class)
				.block())
				.getBody()))
				;

	}

	public CaseResponse createNewCase(String processId){
		Map<String, String> body = new HashMap<>();
		body.put("processDefinitionId", processId);
		return Objects.requireNonNull(
				webClient.post().uri("/API/bpm/case").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
						.header("Cookie", bonitaSession.getCookieHeaders())
						.body(BodyInserters.fromValue(body))
						.retrieve()
						.toEntity(CaseResponse.class).block())
				.getBody()
		;
	}

	public CaseResponse executeTaskWithContract(String taskId, List<Map<String, Object>> contract){
		contract.get(0).put("nuevo_protocolo", "1");
		return Objects.requireNonNull(
				webClient.post()
						.uri("/API/bpm/userTask/{tId}/execution", taskId)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
						.header("Cookie", bonitaSession.getCookieHeaders())
						.body(BodyInserters.fromValue(contract))
						.retrieve()
						.toEntity(CaseResponse.class).block())
				.getBody()
				;
	}

	public CaseResponse instantiateProcessWithNoContract(String processId) {
		return this.createNewCase(processId);
	}

	public <T> Map<String, Object> setCaseVariable(String caseId, String variableName, T variableValue) {
		Map<String, Object> body = new HashMap<>();
		body.put("type", variableValue.getClass().getName());
		body.put("value", variableValue);
		return webClient.put()
				.uri("/API/bpm/caseVariable/{caseId}/{variableName}", caseId, variableName)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toEntity(Map.class)
				.block()
				.getBody();
	}

	public List<Map<String, Object>> getCases(){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/case?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map[].class)
				.block()
				.getBody()
		)
				;
	}

	public List<Map<String, Object>> getVariableCases(String caseId){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/caseVariable?p=0&c=10&f=case_id=" + caseId)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map[].class)
				.block()
				.getBody());

	}

	public Map<String, Object> getUserTaskContract(String taskId){
		return webClient.get()
				.uri("/API/bpm/userTask/{userTaskId}/contract", taskId)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map.class)
				.block()
				.getBody();
	}

	public Map<String, Object> postProtocols(String userTaskId, List<Protocol> protocols) {
		Map<String, Object> body = new HashMap<>();
		Map<String, Object> protocol = new HashMap<>();
		protocol.put("id", 1);
		protocol.put("nombre", "a");
		protocol.put("responsable", "asd");
		protocol.put("resultado", "asd");
		protocol.put("ejecucion_local", false);
		protocol.put("activo", true);
		body.put("protocolos_list", Arrays.asList(protocol));
		return webClient.post()
				.uri("/API/bpm/userTask/{tId}/execution", getTasks().get(0).get("id"))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toEntity(Map.class)
				.block()
				.getBody();
	}

	public void assignUserTask(String taskId, String userId) {
		Map<String, Object> body = new HashMap<>();
		body.put("assigned_id", userId);
		webClient.put()
				.uri("/API/bpm/userTask/{tId}", taskId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toBodilessEntity().block().getStatusCode();
		;
	}

	public List<Map<String, Object>> getArchivedCases(){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/archivedCase?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map[].class)
				.block()
				.getBody())
				;
	}

	public Integer getCountCases(){
		return this.getCases().size();
	}

	public List<Map<String, Object>> getTasks(){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/task?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map[].class)
				.block()
				.getBody()
		);
	}

	public List<Map<String, Object>> getArchivedTasks(){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/archivedTask?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.retrieve()
				.toEntity(Map[].class)
				.block()
				.getBody()
		);
	}

	public Integer getCountTasks(){
		return this.getTasks().size();
	}


	private void throwCommError(ClientResponse clientResponse) {
		if (clientResponse.statusCode().is5xxServerError()
				|| clientResponse.statusCode().is4xxClientError()) {
			throw new BonitaAccessException(
					String.format("Cannot login to bonita. Status: %s for object: %s",
							clientResponse.statusCode(), clientResponse.bodyToMono(Object.class))
			);
		}
	}

	static class PDID {

		private String processDefinitionId;

		public PDID(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}

		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
	}

	static class HumanTaskUpdate {
		private String assignedId;
		private String state;

		public HumanTaskUpdate(String assignedId, String state) {
			this.assignedId = assignedId;
			this.state = state;
		}

		public String getAssignedId() {
			return assignedId;
		}

		public void setAssignedId(String assignedId) {
			this.assignedId = assignedId;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}

}
