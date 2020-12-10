package com.grupo6.dssd.client.remote;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author nahuel.barrena on 8/12/20
 */
@Component
public class RemoteClient {

	private WebClient webClient = WebClient.builder()
			//.baseUrl("http://localhost:8082/")
			.baseUrl("http://dssd-grupo06.herokuapp.com/")
			.build();

	private String remoteToken;

	public void login(){
		Map<String, String> body = new HashMap<>();
		body.put("id", "0");
		body.put("name", "grupo06");
		body.put("password", "grupo06");
		this.remoteToken = webClient.post().uri("login").body(BodyInserters.fromValue(body)).exchange().block().toEntity(String.class).block().getBody();
	};

	public Integer getProtocolScore(String protocolUUID) {
		this.login();
		return webClient.get().uri("protocol/{protocol_uuid}/score", protocolUUID)
				.header("Authorization", remoteToken)
				.exchange()
				.block()
				.toEntity(Integer.class)
				.block()
				.getBody();
	}

	public void restartProtocol(String protocolUUID) {
		this.login();
		webClient.put().uri("protocol/{protocol_uuid}/restart", protocolUUID)
				.header("Authorization", remoteToken)
				.body(BodyInserters.empty())
				.exchange()
				.block();
	}
}
