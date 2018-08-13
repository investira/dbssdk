package br.com.dbsoft.error.exception;

/**
 * Classe base de exceção de autorização. 
 */
public class AuthException extends RuntimeException 
{
	private static final long serialVersionUID = -1075299054230044969L;
	
	/**
	 * Código de status do erro, geralmente usado como código 
	 * da resposta HTTP. 
	 */
	protected final int statusCode;

	public AuthException() {
		super("Ocorreu um erro desconhecido");
		this.statusCode = 0;
	}

	public AuthException(String message, Throwable cause, int statusCode) {
		this.statusCode = 0;
	}
	
	public AuthException(String message, Throwable cause) {
		super(message, cause);
		this.statusCode = 0;
	}

	public AuthException(String message) {
		super(message);
		this.statusCode = 0;
	}

	public AuthException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public AuthException(Throwable cause) {
		super(cause);
		this.statusCode = 0;
	}

	public AuthException(Throwable cause, int statusCode) {
		super(cause);
		this.statusCode = statusCode;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	
	@Override
	public String getMessage() {
		return "[" + statusCode+ "] " + super.getMessage();
	}

}
