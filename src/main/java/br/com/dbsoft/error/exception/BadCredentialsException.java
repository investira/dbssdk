package br.com.dbsoft.error.exception;

import org.apache.commons.httpclient.HttpStatus;

/**
 * Exceção de credenciais inválidas. 
 */
public class BadCredentialsException extends AuthException {
	private static final long serialVersionUID = 8374915753966755102L;
	
	public BadCredentialsException() {
		super("Nome de usuário e/ou senha inválida", HttpStatus.SC_UNAUTHORIZED);
	}

	public BadCredentialsException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SC_UNAUTHORIZED);
	}

	public BadCredentialsException(String message) {
		super(message, HttpStatus.SC_UNAUTHORIZED);
	}

	public BadCredentialsException(Throwable cause) {
		super(cause, HttpStatus.SC_UNAUTHORIZED);
	}
	
}
