package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.openclassrooms.mddapi.dto.MessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<MessageResponse> handleBadRequest(BadRequestException ex) {
		return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<MessageResponse> handleConflict(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new MessageResponse(ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();
		String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed";
		return ResponseEntity.badRequest().body(new MessageResponse(message));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MessageResponse> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new MessageResponse("Unexpected error"));
	}
}
