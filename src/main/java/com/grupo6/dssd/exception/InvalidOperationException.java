package com.grupo6.dssd.exception;

/**
 * @author nahuel.barrena on 21/10/20
 */
public class InvalidOperationException extends CustomBadRequestException {

	public InvalidOperationException() {
	}

	public InvalidOperationException(String message) {
		super(message);
	}
}
