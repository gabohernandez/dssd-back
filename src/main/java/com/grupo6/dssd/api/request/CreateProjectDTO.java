package com.grupo6.dssd.api.request;

/**
 * @author nahuel.barrena on 21/10/20
 */
public class CreateProjectDTO {

	private String name;

	public CreateProjectDTO() {
	}

	public CreateProjectDTO(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
