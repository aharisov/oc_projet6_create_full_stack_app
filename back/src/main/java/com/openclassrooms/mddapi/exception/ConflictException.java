package com.openclassrooms.mddapi.exception;

/**
 * Signals a resource state conflict that should return HTTP 409.
 */
public class ConflictException extends RuntimeException {
	/**
	 * Creates a conflict exception with a client-safe error message.
	 *
	 * @param message conflict reason
	 */
	public ConflictException(String message) {
		super(message);
	}
}
