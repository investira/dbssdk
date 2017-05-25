package br.com.dbsoft.listener;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.dbsoft.startup.DBSApp;
import br.com.dbsoft.util.DBSObject;

public abstract class DBSPhaseListener implements PhaseListener/*, Filter*/ {
	
	private static final long serialVersionUID = -7673392794150326788L;

	/**
	 * Retorna o path relativo da página de login.
	 * <p>
	 * Default: {@code /login.xhtml}
	 */
	protected String getLoginPath() {
		return "/login.xhtml";
	}
	
	/**
	 * Retorna o path relativo da página quando o usuário nao tem Autorização.
	 * <p>
	 * Default: {@code /role.xhtml}
	 */
	protected String getRolePath() {
		return "/role.xhtml";
	}
	
	/**
	 * Retorna o path relativo da página quando o usuário nao tem Autorização de Escopo.
	 * <p>
	 * Default: {@code /scope.xhtml}
	 */
	protected String getScopePath() {
		return "/scope.xhtml";
	}
	
	/**
	 * Retorna {@code true} caso a requisição seja feita para 
	 * seção pública da aplicação, que não exige autenticação.
	 * <p>
	 * Default: {@code /public*}
	 */
	protected boolean isPublic(String pPath) {
		return pPath.startsWith("/resources/")
			|| pPath.startsWith("/images")
			|| pPath.startsWith("/public");
	}
	
//	/**
//	 * Retorna {@code true} caso a requisição seja para algum recurso
//	 * público da aplicação, que não exige autenticação e também
//	 * permite cache. 
//	 */
//	protected boolean isResourceRequest(String pPath) {
//		return pPath.startsWith(ResourceHandler.RESOURCE_IDENTIFIER + "/");
//	}

	/**
	 * Retorna {@code true} caso a requisição seja feita para a página
	 * de login, que não deve exigir autenticação.
	 */
	protected boolean isLoginRequest(String pPath) {
		return DBSObject.isEqual(pPath, getLoginPath());
	}
	
	/**
	 * Retorna {@code true} caso a requisição exija autenticação.
	 */
	protected boolean isAuthenticationRequired(String pPath) {
		if (isPublic(pPath)) {
			return false;
		}
//		if (isResourceRequest(pPath)) {
//			return false;
//		}
		if (isLoginRequest(pPath)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Retorna {@code true} caso o usuário tenha permissão
	 * para acessar o recurso da requisição, caso esta
	 * se enquadre com alguma das regras de Authority definidas. 
	 * <p>
	 * Default: {@code true} (caso não tenha nada definido).
	 * 
	 * @param pPath
	 * @param pAuthorities
	 * @return
	 */
	protected boolean hasAuthorityPermission(String pPath, List<String> pAuthorities) {
		return true;
	}
	
	/**
	 * Retorna {@code true} caso o usuário tenha permissão
	 * para acessar o recurso da requisição, caso esta
	 * se enquadre com alguma das regras de Scope definidas. 
	 * <p>
	 * Default: {@code true} (caso não tenha nada definido).
	 * 
	 * @param pPath
	 * @param pScopes
	 * @return
	 */
	protected boolean hasScopePermission(String pPath, Set<String> pScopes) {
		return true;
	}
	
	/**
	 * Retorna true caso o usuário esteja autenticado.
	 * @return
	 */
	protected abstract boolean isLoggedIn();
	
	/**
	 * Authorities do Usuário
	 * @return
	 */
	protected abstract List<String> getUserRoles();
	
	/**
	 * Scopes do Usuário
	 * @return
	 */
	protected abstract Set<String> getUserScopes();

	/**
	 * 
	 * @param pAuthorities
	 * @param pAuthority
	 * @return
	 */
	protected boolean hasPermission(String pAuthority, List<String> pAuthorities) {
		boolean xOK = false;
		for (String xDadosAuthority : pAuthorities) {
			if (DBSObject.isEqual(xDadosAuthority, pAuthority)) {
				xOK = true;
				break;
			}
		}
		return xOK;
	}
	
	//METODOS LISTENER ========================================================================
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
	@Override
	public void afterPhase(PhaseEvent pEvent) {}
	
	@Override
	public void beforePhase(PhaseEvent pEvent) {
		//FacesContext = Objeto para acessar a árvore
		FacesContext xContext = pEvent.getFacesContext();
		String xCurrentView = xContext.getViewRoot().getViewId().toLowerCase();	
		
		//Página que não são obrigados que o usuário esteja logado
		if (isPublic(xCurrentView)) {
			return;
		}
		
		FacesContext 	xFC = FacesContext.getCurrentInstance();
		ExternalContext xEC = xFC.getExternalContext();
		
		try {
			//Precisa de Authenticação?
			if (!isAuthenticationRequired(xCurrentView)) {
				return;
			}
			
			//Está Logado?
			if (isLoggedIn()) {
				//Tem Permissão de Authority
				if (!hasAuthorityPermission(xCurrentView, getUserRoles())) {
					xEC.redirect(DBSApp.URLHttp+getRolePath());
					return;
				}
				
				//Tem Permissão de Scopo
				if (!hasScopePermission(xCurrentView, getUserScopes())) {
					xEC.redirect(DBSApp.URLHttp+getScopePath());
					return;
				}
			} else {
				xEC.redirect(DBSApp.URLHttp+getLoginPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	//METODOS FILTER =====================================================================================
//	@Override
//	public void init(FilterConfig pFilterConfig) throws ServletException {}
//
//	@Override
//	public void destroy() {}
//	
//	@Override
//	public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException, ServletException {
//		HttpServletRequest xRequest = (HttpServletRequest) pRequest;
//		HttpServletResponse xResponse = (HttpServletResponse) pResponse;
//		String xRequestPath = xRequest.getRequestURI().substring(xRequest.getContextPath().length());
//		
//		//Não é um recurso?
//		if (!isResourceRequest(xRequestPath)) { 
//			pvDisableCache(xResponse);
//		}
//		System.out.println("Filtro: "+ xRequestPath);
//		pChain.doFilter(xRequest, xResponse);
//	}
//	
//	/**
//	 * Configura a resposta para não utilizar cache, principalmente 
//	 * para as requisições protegidas, proibindo "Voltar" para uma página 
//	 * segura após logout. 
//	 */
//	private void pvDisableCache(HttpServletResponse pResponse) {
//		pResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//		pResponse.setHeader("Pragma", "no-cache");
//		pResponse.setDateHeader("Expires", 0);
//	}

}
