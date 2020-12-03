package com.grupo6.dssd.client.bonita.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nahuel.barrena on 1/12/20
 */
public class BonitaProtocol {

	private long id;
	@JsonProperty(value = "persistence_id")
	private String persistenceId;
	private String nombre;
	private int responsable;
	private int resultado;
	@JsonProperty(value = "ejecucion_local")
	private boolean ejecucionLocal;
	private boolean activo;
	private boolean exitoso;
	@JsonProperty(value = "ejecucion_id")
	private long ejecucionId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPersistenceId() {
		return persistenceId;
	}

	public void setPersistenceId(String persistenceId) {
		this.persistenceId = persistenceId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getResponsable() {
		return responsable;
	}

	public void setResponsable(int responsable) {
		this.responsable = responsable;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public boolean isEjecucionLocal() {
		return ejecucionLocal;
	}

	public void setEjecucionLocal(boolean ejecucionLocal) {
		this.ejecucionLocal = ejecucionLocal;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isExitoso() {
		return exitoso;
	}

	public void setExitoso(boolean exitoso) {
		this.exitoso = exitoso;
	}

	public long getEjecucionId() {
		return ejecucionId;
	}

	public void setEjecucionId(long ejecucionId) {
		this.ejecucionId = ejecucionId;
	}
}


