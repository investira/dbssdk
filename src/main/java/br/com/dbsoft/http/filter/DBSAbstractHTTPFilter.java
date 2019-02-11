package br.com.dbsoft.http.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessageBase.MESSAGE_TYPE;
import br.com.dbsoft.util.DBSObject;

/**
 * Classe abastrada para uso de Filtro HTTP.
 * @author jose.avila@investira.com.br
 *
 */
public abstract class DBSAbstractHTTPFilter implements Filter {
	
	protected final Logger wLogger = Logger.getLogger(this.getClass());

	//METODOS OVERRIDE =============================================================
	/**
	 * NÃO IMPLEMENTADO
	 */
	@Override
	public void destroy() {}
	
	@Override
	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException, ServletException {
		HttpServletRequest xRequest = (HttpServletRequest) pRequest;
		HttpServletResponse xResponse = (HttpServletResponse) pResponse;
		
		//Configura os Headers de resposta
		pvConfigureResponse(xRequest, xResponse);
		
		//Verifica se a Origem da requisição tem permissão 
		if (!pvAllowOrigin(xRequest, xResponse)) {
			wLogger.debug("ORIGEM SEM PERMISSÃO");
			return;
		}
		
		//Se for uma chamada OPTIONS, retorna OK - 200.
		if (pvIsOptionsRequest(xRequest, xResponse)) {
			wLogger.debug("OPTIONS REQUEST");
			return;
		}
		
		prBeforeFilter(xRequest);
		
		//Verifica se a requisição precisa de Autenticação
		if (prIsAuthenticationRequired(prGetPath(xRequest))
		&& !prHasPermission(xRequest, xResponse)) {
			wLogger.debug("SEM PERMISSÃO");
			return;
		}
		
		//Pós-Autorização
		prAfterAuthorize(xRequest, xResponse);
		
		// Proceeding
        pChain.doFilter(xRequest, xResponse);
	}
	
	//METODOS ABSTRATOS =============================================================
	/**
	 * Path para redirecionamento padrão.
	 * @return
	 */
	protected abstract String prPathDefault();

	/**
	 * Define quais Paths são públicos
	 * @return
	 */
	protected abstract List<String> prGetPublicPaths();
	
	/**
	 * Define quais Recursos usarão cache 
	 * @return
	 */
	protected abstract List<String> prGetCachedResources();
	
	/**
	 * A Requisição é privada. Verifica se há permissão para atender a requisição.
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws IOException
	 */
	protected abstract boolean prHasPermission(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException;
	
	/**
	 * Método chamado no início do Filter (antes de verificar se a requisição é pública ou não).
	 * @param pRequest
	 */
	protected abstract void prBeforeFilter(HttpServletRequest pRequest);
	
//	/**
//	 * Método chamado ao final do Filter (depois do doFilter, a execução de fato da requisição).
//	 * Deve ser usado para recuperar informações da Requisição
//	 * @param pRequest
//	 * @param pResponse
//	 */
//	protected abstract HttpServletResponse prAfterFilter(HttpServletRequest pRequest, HttpServletResponse pResponse);
	
	/**
	 * Método chamado após as verificações de autorização
	 * @param pRequest
	 * @param pResponse
	 */
	protected abstract void prAfterAuthorize(HttpServletRequest pRequest, HttpServletResponse pResponse);
	
	//METODOS PROTECTED =============================================================
	/**
	 * Define quais as origens permitidas a acessar os serviços (tanto públicos como privados).
	 * Default vazio = Aceita todas as origens.
	 * @return
	 */
	protected List<String> prGetAllowOrigin() {
		List<String> xOrigins = new ArrayList<String>();
		return xOrigins;
	}
	
	/**
	 * Retorna {@code true} caso a requisição exija autenticação.
	 */
	private boolean prIsAuthenticationRequired(String pPath) {
		if (pvIsPublicRequest(pPath)) {
			return false;
		}
		if (pvIsResourceRequest(pPath)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Retorna caminho da requisição 
	 * @param pRequest
	 * @return
	 */
	protected String prGetPath(HttpServletRequest pRequest) {
		String xPath = pRequest.getServletPath();
		if (!DBSObject.isEmpty(pRequest.getRequestURI())) {
			xPath = pRequest.getRequestURI();
		}
		return xPath;
	}
	
	protected void prResponseErrorUnauthorized(HttpServletRequest pRequest, HttpServletResponse pResponse, String pRedirectPage) throws IOException {
		prResponseError(pRequest, pResponse, pRedirectPage, HttpServletResponse.SC_UNAUTHORIZED, new DBSMessage(MESSAGE_TYPE.ERROR, "Não autorizado."));
	}
	
	protected void prResponseErrorInvalidToken(HttpServletRequest pRequest, HttpServletResponse pResponse, String pRedirectPage) throws IOException {
		prResponseError(pRequest, pResponse, pRedirectPage, HttpServletResponse.SC_UNAUTHORIZED, new DBSMessage(MESSAGE_TYPE.ERROR, "Não autorizado."));
	}
	
	protected void prResponseError(HttpServletRequest pRequest, HttpServletResponse pResponse, String pRedirectPage, Integer pStatusCode, IDBSMessage pMessage) throws IOException {
		String xRedirectURL = pRequest.getContextPath() + pRedirectPage;;
		String xAccept = pRequest.getHeader("accept");
		if (xAccept.contains(MediaType.APPLICATION_XHTML_XML)
		 || xAccept.contains(MediaType.TEXT_HTML)) {
			wLogger.debug("--- ACESSO NEGADO! REDIRECIONANDO PARA A PAGINA PADRAO.");
			pResponse.sendRedirect(xRedirectURL);
		} else {
			wLogger.debug("--- ACESSO NEGADO ");
			pvResponseJSonError(pResponse, pStatusCode, pMessage);
		}
	}
	
	//METODOS PRIVADOS =============================================================
	/**
	 * Configura o Responde com os Cabeçalhos Permitidos e a origem de qualquer lugar
	 * @param pRequest 
	 * @param xResponse
	 */
	private void pvConfigureResponse(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		pResponse.setHeader("Access-Control-Allow-Origin", "*");
        pResponse.setHeader("Access-Control-Max-Age", "1");
        pResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        pResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Methods, Content-Type, Accept, X-Requested-With, Authorization, username");
        
        /**
         * Verifica se é um recurso que está sendo requisitado. Se não for, desativa o CACHE.
         * Serve principalmente para as requisições protegidas, proibindo "Voltar" para uma página 
  		 * segura após logout.
         */
  		if (!pvIsResourceRequest(prGetPath(pRequest))) { 
  			pResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
  			pResponse.setHeader("Pragma", "no-cache");
  			pResponse.setDateHeader("Expires", 0);
  		}
	}
	
	private boolean pvAllowOrigin(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException {
//		System.out.println("A Origem "+ pRequest.getRemoteHost() +" solicitou o recurso "+ pRequest.getRequestURI());
		if (DBSObject.isEmpty(prGetAllowOrigin())) {
			return true;
		}
		if (prGetAllowOrigin().contains(pRequest.getRemoteHost())) {
			return true;
		}
		pvResponseJSonError(pResponse, HttpServletResponse.SC_UNAUTHORIZED, new DBSMessage(MESSAGE_TYPE.ERROR, "Origem não autorizada."));
		return false;
	}
	
	/**
	 * Verifica se é uma requisição do Method OPTIONS. Se for, retorna OK 200.
	 * @param pRequest
	 * @param pResponse
	 * @return
	 */
	private boolean pvIsOptionsRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		if ("OPTIONS".equalsIgnoreCase(pRequest.getMethod())) {
            pResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
		}
		return false;
	}
	
	/**
	 * Define se o Path é para um Recurso (CSS, JS, Image...)
	 * @param pPath
	 * @return
	 */
	private boolean pvIsResourceRequest(String pPath) {
		for (String xPublicResource : prGetCachedResources()) {
			if (pPath.startsWith(xPublicResource)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Define se o Path é público
	 * @param pPath
	 * @return
	 */
	private boolean pvIsPublicRequest(String pPath) {
		for (String xPublicPath : prGetPublicPaths()) {
			if (pPath.startsWith(xPublicPath)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  
	 * @param pResponse
	 * @param pErrorCode
	 * @param pMessage
	 * @throws IOException
	 */
	private void pvResponseJSonError(HttpServletResponse pResponse, int pErrorCode, IDBSMessage pMessage) throws IOException {
		String xJSonError = "{\"messages\": [{\"messageTime\": %d, \"messageCode\": %d, \"messageType\": \"%s\", \"messageText\": \"%s\"}], \"values\": []}";
		String xMessage = String.format(xJSonError, new Date().getTime(), pErrorCode, pMessage.getMessageType(), pMessage.getMessageText());
		pResponse.setStatus(pErrorCode);
		pResponse.setCharacterEncoding("UTF-8");
		pResponse.setContentType(MediaType.APPLICATION_JSON);
		pResponse.getWriter().println(xMessage);
	}
	
}
