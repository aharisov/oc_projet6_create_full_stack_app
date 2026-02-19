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

/**
 * Centralized translation layer from application exceptions to HTTP responses.
 *
 * <p>This handler keeps controllers and services focused on business logic by mapping
 * expected domain errors to consistent response codes and payloads.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maps {@link BadRequestException} to HTTP 400.
	 *
	 * @param ex source exception
	 * @return error payload with the original message
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<MessageResponse> handleBadRequest(BadRequestException ex) {
		return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
	}

	/**
	 * Maps {@link ConflictException} to HTTP 409.
	 *
	 * @param ex source exception
	 * @return error payload with the original message
	 */
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<MessageResponse> handleConflict(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new MessageResponse(ex.getMessage()));
	}

	/**
	 * Maps {@link NotFoundException} to HTTP 404.
	 *
	 * @param ex source exception
	 * @return error payload with the original message
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<MessageResponse> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse(ex.getMessage()));
	}

	/**
	 * Maps {@link UnauthorizedException} to HTTP 401.
	 *
	 * @param ex source exception
	 * @return error payload with the original message
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<MessageResponse> handleUnauthorized(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new MessageResponse(ex.getMessage()));
	}

	/**
	 * Returns the first bean validation error as a single HTTP 400 message.
	 *
	 * @param ex validation exception raised by Spring
	 * @return error payload with the first field error message
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();
		String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed";
		return ResponseEntity.badRequest().body(new MessageResponse(message));
	}

	/**
	 * Fallback mapping for uncaught exceptions.
	 *
	 * @param ex uncaught exception
	 * @return generic HTTP 500 message to avoid leaking internals
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<MessageResponse> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new MessageResponse("Unexpected error"));
	}

	/**
	 * Maps unsupported HTTP methods to HTTP 405.
	 *
	 * @param ex unsupported method exception
	 * @return error payload for method mismatch
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<MessageResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(new MessageResponse("Method not allowed"));
	}

	/**
	 * Maps unknown routes to HTTP 404.
	 *
	 * @param ex no handler exception
	 * @return error payload for unknown route
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<MessageResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse("Route not found"));
	}
}
