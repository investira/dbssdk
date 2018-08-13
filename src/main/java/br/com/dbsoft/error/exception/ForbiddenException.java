package br.com.dbsoft.error.exception;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Exceção de acesso negado. 
 */
public class ForbiddenException extends AuthException {
	private static final long serialVersionUID = 1389340345834003446L;

	public ForbiddenException() {
		super("Usuário não possui permissão", HttpStatus.SC_FORBIDDEN);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SC_FORBIDDEN);
	}

	public ForbiddenException(String message) {
		super(message, HttpStatus.SC_FORBIDDEN);
	}

	public ForbiddenException(Throwable cause) {
		super(cause, HttpStatus.SC_FORBIDDEN);
	}
	
}
