package com.grupo6.dssd.exception;

/**
 * @author nahuel.barrena on 21/10/20
 */
public abstract class CustomBadRequestException extends Exception {

	public CustomBadRequestException() {
	}

	public CustomBadRequestException(String message) {
		super(message);
	}
}
