package com.grupo6.dssd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.repository.ProjectRepository;

@RestController
@RequestMapping("/project")
public class ProjectController {

	private final ProjectRepository repository;

	public ProjectController(ProjectRepository repository) {
		this.repository = repository;
	}

	@GetMapping
	public ResponseEntity<List<Project>> getProjects(){
		return ResponseEntity.ok(repository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Project> getProject(
			@PathVariable Long id
	) {
		return ResponseEntity.ok(repository.findById(id).orElse(new Project()));
	}

	@PostMapping("/create")
	public ResponseEntity<String> createProject() {
		Project project = repository.save(new Project());
		return ResponseEntity.ok("Proyecto con id: " + project.getId() + " creado");
	}

	// DELETE ??

}
