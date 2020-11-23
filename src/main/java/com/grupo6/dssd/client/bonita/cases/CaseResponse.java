package com.grupo6.dssd.client.bonita.cases;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author nahuel.barrena on 23/11/20
 */
public class CaseResponse {

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime end_date;
	private Long processDefinitionId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime start;
	private Long rootCaseId;
	private Long id;
	private String state;
	private Long started_by;
	private Long startedBySubstitute;

	public LocalDateTime getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDateTime end_date) {
		this.end_date = end_date;
	}

	public Long getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(Long processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public Long getRootCaseId() {
		return rootCaseId;
	}

	public void setRootCaseId(Long rootCaseId) {
		this.rootCaseId = rootCaseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getStarted_by() {
		return started_by;
	}

	public void setStarted_by(Long started_by) {
		this.started_by = started_by;
	}

	public Long getStartedBySubstitute() {
		return startedBySubstitute;
	}

	public void setStartedBySubstitute(Long startedBySubstitute) {
		this.startedBySubstitute = startedBySubstitute;
	}
}
