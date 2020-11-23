package com.grupo6.dssd.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 * @author nahuel.barrena on 22/11/20
 */
@Entity
@Table(name ="ROLE")
public class Role implements Serializable {

	private Long id;
	private String name;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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


}

