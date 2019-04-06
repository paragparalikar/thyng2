package com.thyng.gateway.service.message.handler.resolution;

public class InvalidMessageException extends Exception {
	private static final long serialVersionUID = -6177144527282507106L;

	public InvalidMessageException() {
	}

	public InvalidMessageException(String message) {
		super(message);
	}

	public InvalidMessageException(Throwable cause) {
		super(cause);
	}

	public InvalidMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMessageException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
