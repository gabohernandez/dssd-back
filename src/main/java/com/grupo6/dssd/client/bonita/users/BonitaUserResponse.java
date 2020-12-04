package com.grupo6.dssd.client.bonita.users;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nahuel.barrena on 3/12/20
 */
public class BonitaUserResponse {

	public int id;
	@JsonProperty(value = "userName")
	public String userName;
	@JsonProperty(value = "job_title")
	public String job_title;

}
