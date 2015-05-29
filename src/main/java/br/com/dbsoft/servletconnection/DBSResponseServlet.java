package br.com.dbsoft.servletconnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dbsoft.core.DBSServlet;
import br.com.dbsoft.core.DBSSDK.CONTENT_TYPE;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSHttp;
import br.com.dbsoft.util.DBSIO;

public abstract class DBSResponseServlet extends DBSServlet {

	private static final long serialVersionUID = -8443498619129984309L;
	
	private ObjectOutputStream 	wObjectOutputStream;
	private ObjectInputStream 	wObjectInputStream;

	public boolean afterRequest() throws DBSIOException {return true;}
	
	public void beforeResponse() throws DBSIOException {}

	@Override
	public void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		System.out.println("--- doPOST");
		try {
			pvOnRequest(pRequest, pResponse);
		} catch (DBSIOException e) {
			wLogger.error(e);
		}
	}

	@SuppressWarnings("unused")
	public void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException, ClassNotFoundException {};

	private void pvOnRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException {
		System.out.println(("--- ON REQUEST"));
		try {
//			pResponse.setContentType(CONTENT_TYPE.APPLICATION_JAVA_SERIALIZED_OBJECT);
			pResponse.setContentType(CONTENT_TYPE.APPLICATION_JSON); 
			pResponse.setHeader("Cache-Control", "no-cache");	 

			onRequest(pRequest, pResponse);
			
	        try{
		        InputStream xIS = pRequest.getInputStream();
		        wObjectInputStream = new ObjectInputStream(xIS);
	        }catch(EOFException e){
	        	wObjectInputStream = null;
	        }

	        if (wObjectInputStream!=null){
		        afterRequest();
	        }
			
	        try{
		        OutputStream xOS = pResponse.getOutputStream();
		        wObjectOutputStream = new ObjectOutputStream(xOS);
	        }catch(EOFException e){
	        	wObjectOutputStream = null;
	        }
	        
	        if (wObjectOutputStream != null){
		        beforeResponse();
	        }


	        wObjectOutputStream.flush();
	        wObjectOutputStream.close();

		} catch (IOException | ClassNotFoundException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	public void writeObject(Object pObject) throws DBSIOException{
		DBSHttp.ObjectOutputStreamWriteObject(wObjectOutputStream, pObject);
	}
	
	public <T> T readObject(Class<T> pClass) throws DBSIOException{
		return DBSHttp.ObjectInputStreamReadObject(wObjectInputStream, pClass);
	}

}