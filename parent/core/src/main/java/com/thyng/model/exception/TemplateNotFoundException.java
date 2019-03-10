package com.thyng.model.exception;

public class TemplateNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -6747502197642231032L;

	public TemplateNotFoundException() {
	}

	public TemplateNotFoundException(String arg0) {
		super(arg0);
	}

	public TemplateNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public TemplateNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TemplateNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
