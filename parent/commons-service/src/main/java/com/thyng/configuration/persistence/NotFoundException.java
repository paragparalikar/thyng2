package com.thyng.configuration.persistence;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6747502197642231032L;

	public NotFoundException() {
	}

	public NotFoundException(String arg0) {
		super(arg0);
	}

	public NotFoundException(Throwable arg0) {
		super(arg0);
	}

	public NotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}