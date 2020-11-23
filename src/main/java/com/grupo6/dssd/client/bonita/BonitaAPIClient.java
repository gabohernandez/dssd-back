package com.grupo6.dssd.client.bonita;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import com.grupo6.dssd.client.bonita.cases.CaseResponse;
import com.grupo6.dssd.client.bonita.process.ProcessResponse;
import com.grupo6.dssd.exception.BonitaAccessException;
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
					throw new BonitaAccessException(e.getMessage());
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

	public ProcessResponse getProcess(){
		return Objects.requireNonNull(Objects.requireNonNull(webClient.get()
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
				.getBody())[0]
				;

	}

	public CaseResponse postCase(){
		return webClient.post()
				.uri("/API/bpm/case")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(bonitaSession.getToken().getKey(), bonitaSession.getToken().getValue())
				.header("Cookie", bonitaSession.getCookieHeaders())
				.body(BodyInserters.fromValue(new PDID(getProcess().getId().toString())))
				.retrieve()
				.toEntity(CaseResponse.class)
				.block()
				.getBody()
		;

	}

	private void throwCommError(ClientResponse clientResponse) {
		if (clientResponse.statusCode().is5xxServerError()
				|| clientResponse.statusCode().is4xxClientError()) {
			throw new BonitaAccessException(
					String.format("Status: %s for object: %s",
							clientResponse.statusCode(), clientResponse.bodyToMono(Object.class))
			);
		}
	}

	class PDID {
		private String processDefinitionId;

		public PDID(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}

		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
	}

}
