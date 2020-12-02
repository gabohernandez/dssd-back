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
	private String status;

	public Project() {
	}

	public Project(String name) {
		this.name = name;
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

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
