package com.openclassrooms.mddapi.exception;

/**
 * Signals an authentication or authorization failure that should return HTTP 401.
 */
public class UnauthorizedException extends RuntimeException {
	/**
	 * Creates an unauthorized exception with a client-safe error message.
	 *
	 * @param message authentication failure description
	 */
	public UnauthorizedException(String message) {
		super(message);
	}
}
