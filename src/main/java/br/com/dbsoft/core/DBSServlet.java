package br.com.dbsoft.core;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;

public abstract class DBSServlet extends HttpServlet {

	private static final long serialVersionUID = 2811720441486182158L;

	protected Logger wLogger = Logger.getLogger(this.getClass());
	
	private HttpServletRequest 	wHttpServletRequest;
	private HttpServletResponse wHttpServletResponse;
	private Boolean 			wAllowPost = true;
	private Boolean 			wAllowGet = true;

	public Boolean getAllowPost() {return wAllowPost;}
	public void setAllowPost(Boolean pAllowPost) {wAllowPost = pAllowPost;}

	public Boolean getAllowGet() {return wAllowGet;}
	public void setAllowGet(Boolean pAllowGet) {wAllowGet = pAllowGet;}
	
	public HttpServletRequest getHttpServletRequest() {return wHttpServletRequest;}
	
	public HttpServletResponse getHttpServletResponse() {return wHttpServletResponse;}

	protected abstract void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException;
	
	@Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) {
//		System.out.println("--- doPOST");
		try {
			if (wAllowPost){
				pvOnRequest(pRequest, pResponse);
			}else{
				super.doPost(pRequest, pResponse);
			}
		} catch (ServletException | IOException | DBSIOException e) {
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
		wHttpServletRequest = pRequest;
		wHttpServletResponse = pResponse;
		onRequest(pRequest, pResponse);
	}

}
