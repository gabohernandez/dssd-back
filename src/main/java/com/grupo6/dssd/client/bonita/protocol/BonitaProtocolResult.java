package com.grupo6.dssd.client.bonita.protocol;

/**
 * @author nahuel.barrena on 3/12/20
 */
public class BonitaProtocolResult {

	public String result;
	public boolean exitoso;

	public BonitaProtocolResult(String result, boolean exitoso) {
		this.result = result;
		this.exitoso = exitoso;
	}
}
