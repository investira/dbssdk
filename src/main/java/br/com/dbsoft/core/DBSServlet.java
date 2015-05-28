package br.com.dbsoft.core;


import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public abstract class DBSServlet extends HttpServlet {

	private static final long serialVersionUID = 2811720441486182158L;

	protected Logger wLogger = Logger.getLogger(this.getClass());
	

}
