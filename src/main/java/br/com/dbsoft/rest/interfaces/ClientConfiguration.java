package br.com.dbsoft.rest.interfaces;

/**
 * Interface com as configurações da aplicação cliente.
 */
public interface ClientConfiguration {
	
	/**
	 * URL de endereço do servidor de autorização.
	 */
	String getOAuthServer();
	
	/**
	 * Identificador da aplicação cliente.
	 */
	String getClientId();

	/**
	 * Senha da aplicação cliente.
	 */
	String getClientSecret();
	
	/**
	 * URI para redirecionamento após a Autorização
	 */
	String getRedirectURI();
	
}