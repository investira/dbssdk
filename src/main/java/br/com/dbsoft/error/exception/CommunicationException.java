package br.com.dbsoft.error.exception;

/**
 * Exceção de erro de comunicaçao com o servidor de autorização.
 */
public class CommunicationException extends AuthException {
	private static final long serialVersionUID = 2992537803720873736L;

	public CommunicationException() {
		super("Erro com comunicar com servidor de autorização");
	}

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(String message, int statusCode) {
		super(message, statusCode);
	}
	
	public CommunicationException(Throwable cause) {
		super(cause);
	}
	
}
