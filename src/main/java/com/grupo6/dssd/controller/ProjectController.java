package com.grupo6.dssd.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.api.request.CreateProjectDTO;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.repository.ProjectRepository;

@RestController
@RequestMapping("/project")
public class ProjectController {

	private final ProjectRepository projectRepository;

	public ProjectController(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@GetMapping
	public ResponseEntity<List<Project>> getProjects(){
		return ResponseEntity.ok(projectRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Project> getProject(
			@PathVariable Long id
	) throws ProjectNotFoundException {
		return ResponseEntity.ok(projectRepository.findById(id).orElseThrow(() ->
				new ProjectNotFoundException("El proyecto con id: " + id + " no existe"))
		);
	}

	@PostMapping("/create")
	public ResponseEntity<Project> createProject(@RequestBody CreateProjectDTO createProjectDTO) {
		return ResponseEntity.ok(projectRepository.save(new Project(createProjectDTO.getName())));
	}

	// DELETE ??

}
