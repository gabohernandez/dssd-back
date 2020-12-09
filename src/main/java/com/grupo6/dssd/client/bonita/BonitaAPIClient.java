package com.grupo6.dssd.client.bonita;

import java.util.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.grupo6.dssd.client.bonita.cases.CaseResponse;
import com.grupo6.dssd.client.bonita.process.ProcessResponse;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocol;
import com.grupo6.dssd.client.bonita.protocol.BonitaProtocolResult;
import com.grupo6.dssd.client.bonita.tasks.GetTaskResponse;
import com.grupo6.dssd.client.bonita.users.BonitaUserResponse;
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
	public ClientResponse login()  {
		return webClient.post()
				.uri(uriBuilder ->
						uriBuilder.path("/loginservice")
								.queryParam("username", "juan perez")
								.queryParam("password", "juan.perez")
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
				.exchange();
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
		this.login();
		return Arrays.asList(
				Objects.requireNonNull(
						Objects.requireNonNull(
								Objects.requireNonNull(
										webClient.get().uri("/API/bpm/process?c=10&p=0")
												.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
												.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
												.header("Cookie", bonitaSession.getCookieHeaders())
												.exchange()
												.block())
										.toEntity(ProcessResponse[].class)
										.block())
								.getBody())
		);
	}

	public CaseResponse createNewCase(String processId){
		this.login();
		Map<String, String> body = new HashMap<>();
		body.put("processDefinitionId", processId);
		return Objects.requireNonNull(
				Objects.requireNonNull(
						webClient.post().uri("/API/bpm/case")
								.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
								.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
								.header("Cookie", bonitaSession.getCookieHeaders()).body(BodyInserters.fromValue(body))
								.exchange()
								.block())
						.toEntity(CaseResponse.class)
						.block()
		).getBody()
		;
	}

	/*public CaseResponse executeTaskWithContract(String taskId, List<Map<String, Object>> contract){
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
	}*/

	public CaseResponse instantiateProcessWithNoContract(String processId) {
		return this.createNewCase(processId);
	}

	public <T> Map<String, Object> setCaseVariable(String caseId, String variableName, T variableValue) {
		this.login();
		Map<String, Object> body = new HashMap<>();
		body.put("type", variableValue.getClass().getName());
		body.put("value", variableValue);
		return webClient.put()
				.uri("/API/bpm/caseVariable/{caseId}/{variableName}", caseId, variableName)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.body(BodyInserters.fromValue(body))
				.exchange()
				.block()
				.toEntity(Map.class)
				.block()
				.getBody();
	}

	public List<Map<String, Object>> getCases(){
		this.login();
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/case?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.exchange()
				.block()
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
				.exchange()
				.block()
				.toEntity(Map[].class)
				.block()
				.getBody());

	}

	public Map<String, Object> getUserTaskContract(String taskId){
		return webClient.get()
				.uri("/API/bpm/userTask/{userTaskId}/contract", taskId)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.exchange()
				.block()
				.toEntity(Map.class)
				.block()
				.getBody();
	}

	public Object createProtocols(String userTaskId, List<BonitaProtocol> protocols, int idProject)	{
		this.login();
		Map<String, Object> body = new HashMap<>();
		body.put("protocolosInput", new ArrayList<>());
		protocols.forEach(p -> {
			Map<String, Object> protocolos = new HashMap<>();
			protocolos.put("id", p.getId());
			protocolos.put("nombre", p.getNombre());
			protocolos.put("responsable", p.getResponsable());
			protocolos.put("activo", p.isActivo());
			protocolos.put("persistenceId", p.getPersistenceId());
			protocolos.put("ejecucion_local", p.isEjecucionLocal());
			protocolos.put("proyecto_id", p.getProyectoId());
			protocolos.put("protocolo_uuid", p.getProtocoloUUID());
			((List<Object>) body.get("protocolosInput")).add(protocolos);
		});
		body.put("id_proyecto", idProject);
		return executeTask(userTaskId, body);
	}

	public Object decideOnFailProtocol(String userTaskId, String decision) {
		this.login();
		Map<String, Object> body = new HashMap<>();
		body.put("decision", decision);
		return executeTask(userTaskId, body);
	}

	public Object scoreProtocol(String userTaskId, BonitaProtocolResult protocolResult)	{
		this.login();
		Map<String, Object> body = new HashMap<>();
		Map<String, Object> protocolo = new HashMap<>();
		protocolo.put("resultado", protocolResult.result);
		protocolo.put("exitoso", protocolResult.exitoso);
		body.put("protocoloActivo", protocolo);
		return executeTask(userTaskId, body);
	}

	private HttpStatus executeTask(String userTaskId, Map<String, Object> body) {
		return webClient.post().uri("/API/bpm/userTask/{tId}/execution", userTaskId)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders()).body(BodyInserters.fromValue(body)).exchange()
				.block().toBodilessEntity().block().getStatusCode();
	}

	public void assignUserTask(String taskId, String userId) {
		this.login();
		Map<String, Object> body = new HashMap<>();
		body.put("assigned_id", userId);
		Objects.requireNonNull(
				webClient.put()
						.uri("/API/bpm/userTask/{tId}", taskId)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
						.header("Cookie", bonitaSession.getCookieHeaders()).body(BodyInserters.fromValue(body))
						.exchange()
						.block())
				.toBodilessEntity()
				.block()
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

	public List<GetTaskResponse> getTasks(){
		this.login();
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/task?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.exchange()
				.block()
				.toEntity(GetTaskResponse[].class)
				.block()
				.getBody()
		);
	}

	public List<Map<String, Object>> getArchivedTasks(){
		return Arrays.asList(webClient.get()
				.uri("/API/bpm/archivedTask?p=0&c=1000")
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.exchange()
				.block()
				.toEntity(Map[].class)
				.block()
				.getBody()
		);
	}

	public Integer getCountTasks(){
		return this.getTasks().size();
	}

	public List<BonitaUserResponse> getUsers() {
		this.login();
		return Arrays.asList(
				Objects.requireNonNull(
						Objects.requireNonNull(
							Objects.requireNonNull(webClient.get().uri("/API/identity/user?p=0&c=1000")
									.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
									.header("Cookie", bonitaSession.getCookieHeaders()).exchange().block())
									.toEntity(BonitaUserResponse[].class)
									.block())
								.getBody()
				)
		);
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
}
