package com.grupo6.dssd.model;

import java.time.LocalDateTime;
import java.util.Random;

import javax.persistence.*;

@Entity
@Table(name = "PROTOCOL")
public class Protocol {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String status;
	private Integer score;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	public Protocol() {
		startTime = LocalDateTime.now();
		// Queda el endtime con un random entre 10 segundos y 60
		endTime = startTime.plusSeconds(Math.abs(new Random().nextInt(60 - 10) + 10));
		score = Math.abs(new Random().nextInt(10));
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

	public String getStatus() {
		return this.getEndTime().isBefore(LocalDateTime.now()) ? "Finalizado" : "En proceso";
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getScore() {
		return getStatus().equals("Finalizado") ? score : null;
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
}
