package com.moises.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.moises.cursomc.services.exceptions.DataIntegrityException;
import com.moises.cursomc.services.exceptions.ObjectNotFoundException;


@ControllerAdvice
public class ResourceExceptionHandler { //Classe que vai capturar um erro e exibir de foma correta
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		
		StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException die, HttpServletRequest request) {
		
		StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), die.getMessage(), System.currentTimeMillis());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
