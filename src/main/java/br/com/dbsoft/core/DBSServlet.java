package br.com.dbsoft.core;


import java.io.IOException;
import java.io.StreamCorruptedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;

public abstract class DBSServlet extends HttpServlet {

	private static final long serialVersionUID = 2811720441486182158L;

	protected Logger wLogger = Logger.getLogger(this.getClass());
	
	private Boolean 			wAllowPost = true;
	private Boolean 			wAllowGet = true;

	/**
	 * Se permite chamada POST. O padrão é <b>true</b>.
	 * @return
	 */
	public Boolean getAllowPost() {return wAllowPost;}
	/**
	 * Se permite chamada POST. O padrão é <b>true</b>.
	 * @return
	 */
	public void setAllowPost(Boolean pAllowPost) {wAllowPost = pAllowPost;}

	/**
	 * Se permite chamada GET. O padrão é <b>true</b>.
	 * @return
	 */
	public Boolean getAllowGet() {return wAllowGet;}
	/**
	 * Se permite chamada GET. O padrão é <b>true</b>.
	 * @param pAllowGet
	 */
	public void setAllowGet(Boolean pAllowGet) {wAllowGet = pAllowGet;}
	
	/**
	 * Chamado em qualquer request, seja <b>post</b> ou <b>get</b>.
	 * @param pRequest
	 * @param pResponse
	 * @throws DBSIOException
	 */
	protected abstract void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException;
	
	@Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) {
//		System.out.println("--- doPOST");
		try {
			if (wAllowPost){
				pvOnRequest(pRequest, pResponse);
//				xContext.complete();
			}else{
				super.doPost(pRequest, pResponse);
			}
		} catch (ServletException | StreamCorruptedException | DBSIOException e) {
			wLogger.error(e);
		} catch (IOException e) {
			wLogger.error(e);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest pRequest, HttpServletResponse pResponse) {
//		System.out.println("--- doGET");
		try {
			if (wAllowGet){
				pvOnRequest(pRequest, pResponse);
			}else{
				super.doGet(pRequest, pResponse);
			}
		} catch (ServletException | IOException | DBSIOException e) {
			wLogger.error(e);
		}
	}
	
	private void pvOnRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException {
//		System.out.println(("--- ON REQUEST"));
		onRequest(pRequest, pResponse);
	}

}
