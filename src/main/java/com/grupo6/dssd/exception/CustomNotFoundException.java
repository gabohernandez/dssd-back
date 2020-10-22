package com.grupo6.dssd.exception;

/**
 * @author nahuel.barrena on 21/10/20
 */
public abstract class CustomNotFoundException extends Exception {

	public CustomNotFoundException() {
	}

	public CustomNotFoundException(String message) {
		super(message);
	}
}
