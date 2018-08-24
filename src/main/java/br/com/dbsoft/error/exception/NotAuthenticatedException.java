package br.com.dbsoft.error.exception;

import org.apache.commons.httpclient.HttpStatus;

import br.com.dbsoft.error.exception.AuthException;

/**
 * Exceção de usuário não autenticado.
 */
public class NotAuthenticatedException extends AuthException 
{
	private static final long serialVersionUID = -7062170673357738372L;

	public NotAuthenticatedException() {
		super("Usuário não está autenticado", HttpStatus.SC_UNAUTHORIZED);
	}

	public NotAuthenticatedException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SC_UNAUTHORIZED);
	}

	public NotAuthenticatedException(String message) {
		super(message, HttpStatus.SC_UNAUTHORIZED);
	}

	public NotAuthenticatedException(Throwable cause) {
		super(cause, HttpStatus.SC_UNAUTHORIZED);
	}
	
}
