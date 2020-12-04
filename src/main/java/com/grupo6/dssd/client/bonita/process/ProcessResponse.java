package com.grupo6.dssd.client.bonita.process;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author nahuel.barrena on 23/11/20
 */
public class ProcessResponse implements Serializable {

	private String displayDescription;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime deploymentDate;
	private String displayName;
	private String name;
	private String description;
	private Long deployedBy;
	private Long id;
	private String activationState;
	private String version;
	private String configurationState;
	private Long actorInitiatorId;

	public String getDisplayDescription() {
		return displayDescription;
	}

	public void setDisplayDescription(String displayDescription) {
		this.displayDescription = displayDescription;
	}

	public LocalDateTime getDeploymentDate() {
		return deploymentDate;
	}

	public void setDeploymentDate(LocalDateTime deploymentDate) {
		this.deploymentDate = deploymentDate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDeployedBy() {
		return deployedBy;
	}

	public void setDeployedBy(Long deployedBy) {
		this.deployedBy = deployedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivationState() {
		return activationState;
	}

	public void setActivationState(String activationState) {
		this.activationState = activationState;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getConfigurationState() {
		return configurationState;
	}

	public void setConfigurationState(String configurationState) {
		this.configurationState = configurationState;
	}

	public Long getActorInitiatorId() {
		return actorInitiatorId;
	}

	public void setActorInitiatorId(Long actorInitiatorId) {
		this.actorInitiatorId = actorInitiatorId;
	}
}

