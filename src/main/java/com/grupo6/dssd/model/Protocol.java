package com.grupo6.dssd.model;

import java.time.LocalDateTime;
import java.util.Random;
import javax.persistence.*;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

import antlr.StringUtils;

@Entity
@Table(name = "PROTOCOL")
public class Protocol {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "START_TIME")
	private LocalDateTime startTime;

	@Column(name = "END_TIME")
	private LocalDateTime endTime;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private ProtocolStatus status;

	private Integer score;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id")
	private Project project;

	public Protocol(){}

	public Protocol(Project projectDto) {
		status = ProtocolStatus.PENDING;
		project = projectDto;
		startTime = LocalDateTime.MAX;
		endTime = LocalDateTime.MAX;
		score = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime starttime) {
		this.startTime = starttime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endtime) {
		this.endTime = endtime;
	}

	public ProtocolStatus getStatus() {
		if(!status.equals(ProtocolStatus.FINISHED) && LocalDateTime.now().isAfter(endTime)){
			this.finish();
		}
		return status;
	}



	public void setStatus(ProtocolStatus status) {
		this.status = status;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void finish(){
		this.setStatus(ProtocolStatus.FINISHED);
		this.setScore(Math.abs(new Random().nextInt(100)));
	}

	public void start(){
		status = ProtocolStatus.STARTED;
		startTime = LocalDateTime.now();
		// Queda el endtime con un random entre 10 segundos y 120
		endTime = startTime.plusSeconds(Math.abs(new Random().nextInt(120 - 10) + 10));

	}

	@JsonIgnore
	public boolean isStarted(){
		return this.getStatus().equals(ProtocolStatus.STARTED);
	}

	@JsonIgnore
	public boolean isPending(){
		return this.getStatus().equals(ProtocolStatus.PENDING);
	}

	@JsonIgnore
	public boolean isFinished() {
		return this.getStatus().equals(ProtocolStatus.FINISHED);
	}

	@JsonIgnore
	public boolean isFailed() {
		return this.getStatus().equals(ProtocolStatus.FAILED);
	}

	@Override
	public String toString(){
			return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
