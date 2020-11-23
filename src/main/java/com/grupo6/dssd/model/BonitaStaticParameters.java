package com.grupo6.dssd.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author nahuel.barrena on 22/11/20
 */

/**
 * Entidad que representa algunos parametros de configuracion hacia bonita
 */
@Entity
@Table(name = "BONITA_PARAMETERS")
public class BonitaStaticParameters {

	@JsonIgnore
	private Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
