package com.grupo6.dssd.controller;

import java.util.Arrays;
import java.util.Iterator;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.model.Protocol;
import com.grupo6.dssd.repository.ProjectRepository;
import com.grupo6.dssd.repository.ProtocolRepository;

@RestController
@RequestMapping("/cloud-service")
public class CloudServiceController {
	
	private final ProtocolRepository protocolService;
	private final ProjectRepository projectRepository;
	private final JdbcTemplate jdbcTemplate;
	
	public CloudServiceController(ProjectRepository projectRepository, ProtocolRepository protocolService, JdbcTemplate jdbcTemplate) {
		this.projectRepository = projectRepository;
		this.protocolService = protocolService;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@PostMapping("/restart-all")
	public ResponseEntity<String> restartAll() {
		//Clean Database
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
	    try {
	        Iterator var1 = Arrays.asList("Protocol","Project").iterator();//getTableNames(jdbcTemplate).iterator();

	        while(var1.hasNext()) {
	            String tableName = (String)var1.next();
	            jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY;");
	            //jdbcTemplate.execute("ALTER SEQUENCE " + tableName + "_SEQ_ID RESTART WITH 1");
	        }
	        
	    } finally {
	        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
	    }
		
		//Create Projects
		Project project1 = new Project("Project 1");
		Project project2 = new Project("Project 2");
		Project project3 = new Project("Project 3");
		
		//Create Protocols
		Protocol protocol1 = new Protocol(project1);
		Protocol protocol2 = new Protocol(project2);
		Protocol protocol3 = new Protocol(project3);
		Protocol protocol4 = new Protocol(project3);
		
		//Save Projects
		projectRepository.save(project1);
		projectRepository.save(project2);
		projectRepository.save(project3);
		
		//Save Protocols
		protocolService.save(protocol1);
		protocolService.save(protocol2);
		protocolService.save(protocol3);
		protocolService.save(protocol4);
		
		return ResponseEntity.ok().build();
	}
}
