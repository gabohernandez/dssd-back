package com.grupo6.dssd.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.grupo6.dssd.exception.CustomBadRequestException;
import com.grupo6.dssd.exception.CustomNotFoundException;

/**
 * @author nahuel.barrena on 21/10/20
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleEveryException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = CustomNotFoundException.class)
	public ResponseEntity<String> handleNotFoundException(CustomNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = CustomBadRequestException.class)
	public ResponseEntity<String> handleBadRequestException(CustomBadRequestException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
