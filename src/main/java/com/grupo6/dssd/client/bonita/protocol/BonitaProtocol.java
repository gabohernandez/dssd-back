package com.grupo6.dssd.client.bonita.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nahuel.barrena on 1/12/20
 */
public class BonitaProtocol {

	private long id;
	@JsonProperty(value = "persistenceId")
	private String persistenceId;
	private String nombre;
	private String responsable;
	@JsonProperty(value = "ejecucion_local")
	private boolean ejecucionLocal;
	private boolean activo;
	@JsonProperty(value = "proyecto_id")
	private long proyectoId;
	@JsonProperty(value = "protocolo_uuid")
	private String protocoloUUID;

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

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
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

	public long getProyectoId() {
		return proyectoId;
	}

	public void setProyectoId(long proyectoId) {
		this.proyectoId = proyectoId;
	}

	public String getProtocoloUUID() {
		return protocoloUUID;
	}

	public void setProtocoloUUID(String protocoloUUID) {
		this.protocoloUUID = protocoloUUID;
	}
}


