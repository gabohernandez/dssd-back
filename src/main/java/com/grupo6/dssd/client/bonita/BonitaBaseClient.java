package com.grupo6.dssd.client.bonita;

import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;

/**
 * @author nahuel.barrena on 22/11/20
 */
public abstract class BonitaBaseClient {

	static ObjectMapper mapper = new ObjectMapper();

	static void setMapper() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.findAndRegisterModules();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
	}

	static ExchangeStrategies getStrategies(){
		setMapper();
		return ExchangeStrategies
				.builder()
				.codecs(codecConfigurer -> {
					codecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON));
					codecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON));
				}).build();
	}

	protected WebClient webClient = WebClient.builder()
			.baseUrl("http://localhost:49915/bonita")
			.exchangeStrategies(getStrategies())
			.build();



}
