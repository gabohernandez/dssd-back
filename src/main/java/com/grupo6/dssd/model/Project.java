package com.grupo6.dssd.model;

import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author nahuel.barrena on 19/10/20
 */

@Entity
@Table(name = "PROJECT")
public class Project {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@Column(name = "STATUS")
	private String status = "PENDING";
	@Column(name = "BONITA_PROCESS_ID")
	private String processId;
	@Column(name = "BONITA_CASE_ID")
	private String caseId;
	@Column(name = "BONITA_ASSIGNED_ID")
	private String assignedId;

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public Project(String name, String processId, String caseId, String assignedId) {
		this.name = name;
		this.processId = processId;
		this.caseId = caseId;
		this.assignedId = assignedId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(String assignedId) {
		this.assignedId = assignedId;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
