package com.grupo6.dssd.controller.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.grupo6.dssd.api.request.CreateProjectDTO;
import com.grupo6.dssd.client.bonita.BonitaAPIClient;
import com.grupo6.dssd.exception.ProjectNotFoundException;
import com.grupo6.dssd.model.Project;
import com.grupo6.dssd.repository.ProjectRepository;
import com.grupo6.dssd.service.bonita.BonitaService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	private final ProjectRepository projectRepository;
	private final BonitaAPIClient bonitaAPIClient;

	public ProjectController(ProjectRepository projectRepository, BonitaAPIClient bonitaAPIClient) {
		this.projectRepository = projectRepository;
		this.bonitaAPIClient = bonitaAPIClient;
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
		Long processId = bonitaAPIClient.getProcesses().get(0).getId();
		Long caseId = bonitaAPIClient.createNewCase(processId.toString()).getId();
		Integer userIdForAssign = bonitaAPIClient.getUsers().stream().filter(u -> u.job_title.contains("Responsable del proy")).findFirst().get().id;
		Integer taskId = bonitaAPIClient.getTasks().stream().filter(task -> task.name.contains("protocolos necesarios") && task.caseId == caseId).findFirst().get().id;
		bonitaAPIClient.assignUserTask(taskId.toString(), userIdForAssign.toString());
		return ResponseEntity.ok(projectRepository.save(new Project(createProjectDTO.getName(), processId.toString(), caseId.toString())));
	}

}
