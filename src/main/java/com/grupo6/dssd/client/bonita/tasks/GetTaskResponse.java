package com.grupo6.dssd.client.bonita.tasks;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author nahuel.barrena on 3/12/20
 */
public class GetTaskResponse {

	public Integer id;
	public String description;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@JsonProperty(value = "assigned_date")
	public LocalDateTime assigned_date;
	public long caseId;
	public String name;
	@JsonProperty(value = "assigned_id")
	public long assigned_id;
	public String type;
	public String state;
	public long rootCaseId;

	/*{
		"displayDescription": "Form web para que se creen los protocolos",
			"executedBy": "0",
			"rootContainerId": "11003",
			"assigned_date": "",
			"displayName": "Cargar protocolos necesarios",
			"executedBySubstitute": "0",
			"dueDate": "",
			"description": "Form web para que se creen los protocolos",
			"type": "USER_TASK",
			"priority": "normal",
			"actorId": "1601",
			"processId": "6890431997892402305",
			"caseId": "11003",
			"name": "Cargar protocolos necesarios",
			"reached_state_date": "2020-12-03 20:26:25.226",
			"rootCaseId": "11003",
			"id": "220006",
			"state": "ready",
			"parentCaseId": "11003",
			"last_update_date": "2020-12-03 20:26:25.226",
			"assigned_id": ""
	},*/
}
