package br.com.dbsoft.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class DBSSession {
	
	private static Logger wLogger = Logger.getLogger(DBSSession.class);
	
	/**
	 * Retorna a sessão web corrente do usuário
	 * @return
	 */
	public static HttpSession getSession(){
		ExternalContext xEC = FacesContext.getCurrentInstance().getExternalContext();
		return (HttpSession) xEC.getSession(false);
	}

	/**
	 * Invalida a sessão web atual 
	 */
	public static void invalidateCurrentSession(){
		HttpSession xHS = getSession();
		xHS.invalidate();
	}

	/**
	 * Cria uma nova sessão
	 */
	public static void createSession(){
		ExternalContext xEC = FacesContext.getCurrentInstance().getExternalContext();
		((HttpServletRequest) xEC.getRequest()).getSession(true);
	}
	
	/**
	 * Invalida e cria uma nova sessão
	 */
	public static void renewSession(){
		invalidateCurrentSession();
		createSession();
	}
	
	/**
	 * Invalida e cria uma nova sessão e redireciona para uma nova página
	 * @param pPageToGo
	 */
	public static void renewSession(String pPageToGo){
		renewSession();
		ExternalContext xEC = FacesContext.getCurrentInstance().getExternalContext();
		try {
			xEC.redirect(xEC.getRequestContextPath() + pPageToGo);
		} catch (IOException e) {
			wLogger.error(e);
		}
	}
	
	
	/**
	 * Envia a resposta de Servlet no padrão EventSource
	 * @param pRes
	 * @param pMessageText
	 * @param pEventName
	 * @return
	 * @throws IOException
	 */
	public static boolean writeServletEventSourceResponse(ServletResponse pRes, String pMessageText, String pEventName){
		try {
			PrintWriter xOut = pRes.getWriter();
			String xString = "";
			xString += "retry: 100\n"; //60.000 = 60 segundos
	//		xString += "heartbeatTimeout: 0\n";
			xString += "data: " +  pMessageText + "\n";
			
			if (pEventName!=null){
				xString += "event: " +  pEventName + "\n";
			}
			xString += "\n";
			xOut.write(xString);
			xOut.flush();
			if (xOut.checkError()) { //checkError calls flush, and flush() does not throw IOException
				return false;
			}
			xOut.flush();
		}catch(Exception e){ 
			return false;
		}
		return true;
	}
}
