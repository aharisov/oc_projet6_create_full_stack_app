package com.openclassrooms.mddapi.exception;

/**
 * Signals invalid client input that should return HTTP 400.
 */
public class BadRequestException extends RuntimeException {
	/**
	 * Creates a bad request exception with a client-safe error message.
	 *
	 * @param message validation or input error description
	 */
	public BadRequestException(String message) {
		super(message);
	}
}
