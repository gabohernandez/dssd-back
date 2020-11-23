package com.grupo6.dssd.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nahuel.barrena on 22/11/20
 * Clase para tomar los datos de inicializacion de los json en /main/resources/initialData
 * TODO proveer un controller para restartear estos datos
 * Como hay dependencias funcionales y de referencia, hay que tener en cuenta en el orden en que se agregan/modifican/eliminan
 * los datos.
 */
@Component
public class DataInitializr implements ApplicationRunner {

	private final ObjectMapper jsonMapper;

	public DataInitializr(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	/*	List<User> users = jsonMapper.readValue(new URL("file:src/main/resources/initialData/users_h2.json"), new TypeReference<List<User>>() {
		});
		List<Role> roles = jsonMapper.readValue(new URL("file:src/main/resources/initialData/roles_h2.json"), new TypeReference<List<Role>>() {
		});
	*/
	}

}
