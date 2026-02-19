package com.openclassrooms.mddapi.exception;

/**
 * Signals that the requested resource was not found and should return HTTP 404.
 */
public class NotFoundException extends RuntimeException {
	/**
	 * Creates a not found exception with a client-safe error message.
	 *
	 * @param message missing resource description
	 */
	public NotFoundException(String message) {
		super(message);
	}
}
