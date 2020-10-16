package com.grupo6.dssd.model;

import java.time.LocalDateTime;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Protocol {

	@Id
	@GeneratedValue
	private Integer id;
	private LocalDateTime starttime;
	private LocalDateTime endtime;
	private String status;
	private Integer score;

	public Protocol() {
		starttime = LocalDateTime.now();
		endtime = starttime.plusSeconds(10);
		score = new Random().nextInt(10);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getStarttime() {
		return starttime;
	}

	public void setStarttime(LocalDateTime starttime) {
		this.starttime = starttime;
	}

	public LocalDateTime getEndtime() {
		return endtime;
	}

	public void setEndtime(LocalDateTime endtime) {
		this.endtime = endtime;
	}

	public String getStatus() {
		return this.getEndtime().isBefore(LocalDateTime.now()) ? "Finalizado" : "En proceso";
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

}
