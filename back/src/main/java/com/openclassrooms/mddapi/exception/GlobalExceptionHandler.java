package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.openclassrooms.mddapi.payload.response.MessageResponse;

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

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<MessageResponse> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse(ex.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<MessageResponse> handleUnauthorized(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<MessageResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(new MessageResponse("Method not allowed"));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<MessageResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse("Route not found"));
	}
}
