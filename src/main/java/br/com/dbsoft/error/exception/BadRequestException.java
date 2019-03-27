package br.com.dbsoft.error.exception;

import org.apache.commons.httpclient.HttpStatus;

public class BadRequestException extends AuthException {
	private static final long serialVersionUID = 2450602483451765796L;

	public BadRequestException() {
		super("Requisição inválida", HttpStatus.SC_BAD_REQUEST);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SC_BAD_REQUEST);
	}

	public BadRequestException(String message) {
		super(message, HttpStatus.SC_BAD_REQUEST);
	}

	public BadRequestException(Throwable cause) {
		super(cause, HttpStatus.SC_BAD_REQUEST);
	}
	
}
