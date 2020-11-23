package com.grupo6.dssd.client.bonita;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;

/**
 * @author nahuel.barrena on 23/11/20
 */
public class BonitaSession implements Serializable {

	private static final String TENANT = "bonita.tenant";
	private static final String JSESSION = "JSESSIONID";
	private static final String TOKEN = "X-Bonita-API-Token";

	private final HttpHeaders httpHeaders;
	private final String cookieHeaders;
	private final Pair<String, String> tenant;
	private final Pair<String, String> session;
	private final Pair<String, String> token;

	public BonitaSession(HttpHeaders headers) {
		this.httpHeaders = headers;
		this.cookieHeaders = String.join("; ", Optional.ofNullable(headers.get(HttpHeaders.SET_COOKIE)).orElse(new ArrayList<>()));
		List<String> cookies = Optional.ofNullable(headers.get(HttpHeaders.SET_COOKIE)).orElse(new ArrayList<>());
		this.tenant = cookies.stream().filter(h -> h.contains(TENANT)).map(c ->
			Pair.of(TENANT, bonitaCookieSplit(c)[1])
		).findFirst().orElse(null);
		this.token = cookies.stream().filter(h -> h.contains(TOKEN)).map(c ->
			Pair.of(TOKEN, bonitaCookieSplit(c)[1])
		).findFirst().orElse(null);
		this.session = cookies.stream().filter(h -> h.contains(JSESSION)).map(c ->
			Pair.of(JSESSION, bonitaCookieSplit(c)[1])
		).findFirst().orElse(null);
	}

	private String[] bonitaCookieSplit(String c) {
		return c.split(";")[0].split("=");
	}

	public Pair<String, String> getTenant() {
		return this.tenant;
	}

	public Pair<String, String> getSession() {
		return this.session;
	}

	public Pair<String, String> getToken() {
		return this.token;
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public String getCookieHeaders() {
		return cookieHeaders;
	}


}
