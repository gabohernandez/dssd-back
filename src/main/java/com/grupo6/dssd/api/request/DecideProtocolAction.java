package com.grupo6.dssd.api.request;

/**
 * @author nahuel.barrena on 7/12/20
 */
public class DecideProtocolAction {

	private int action;

	public DecideProtocolAction() {
	}

	public DecideProtocolAction(int action) {
		this.action = action;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
