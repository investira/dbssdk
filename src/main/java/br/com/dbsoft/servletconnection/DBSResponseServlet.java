package br.com.dbsoft.servletconnection;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.CONTENT_TYPE;
import br.com.dbsoft.core.DBSServlet;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSHttp;
import br.com.dbsoft.util.DBSIO;

public abstract class DBSResponseServlet extends DBSServlet {

	protected Logger wLogger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = -8443498619129984309L;
//	private InputStream			wInputStream;
//	private OutputStream		wOutputStream;
//	private ObjectOutputStream 	wObjectOutputStream = null;
//	private ObjectInputStream  	wObjectInputStream = null;

	private String 				wIPAddress;

	public DBSResponseServlet() {
		setAllowGet(false);
	}
	
	/**
	 * Retorna IP do chamador.
	 * @return
	 */
	public String getIPAddress() {
		return wIPAddress;
	}

	/**
	 * Implementar leitura dos dados recebido utilizando readObject.
	 * @return
	 * @throws DBSIOException
	 */
	public abstract void readInputStream(ObjectInputStream pOutputStream) throws DBSIOException;
	
	/**
	 * Implementar escrita dos dados para serem enviados utilizando writeObject.
	 * @throws DBSIOException
	 */
	public abstract void writeOutputStream(ObjectOutputStream pInputStream) throws DBSIOException;


	@Override
	protected void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException {
		try {
			
			InputStream			wInputStream = null;
			OutputStream		wOutputStream = null;
			ObjectOutputStream 	wObjectOutputStream = null;
			ObjectInputStream  	wObjectInputStream = null;
//			System.out.println("-------------------onRequest START \t" + pRequest.getHeaderNames().toString());
//			pResponse.setContentType(CONTENT_TYPE.APPLICATION_JAVA_SERIALIZED_OBJECT);
			pResponse.setContentType(CONTENT_TYPE.APPLICATION_JSON); 
			pResponse.setHeader("Cache-Control", "no-cache");	

			wIPAddress = pvGetIpRequest(pRequest);
	        try{
		        wInputStream = pRequest.getInputStream();
		        wObjectInputStream = new ObjectInputStream(wInputStream);
	        }catch(EOFException e){
	        	wObjectInputStream = null;
	        }

	        readInputStream(wObjectInputStream);

	        try{
	        	wOutputStream = pResponse.getOutputStream();
		        wObjectOutputStream = new ObjectOutputStream(wOutputStream);
	        }catch(EOFException e){
	        	wObjectOutputStream = null;
	        }
	        
	        writeOutputStream(wObjectOutputStream);

	        if (wObjectOutputStream !=null){
		        wObjectOutputStream.flush();
	        	wObjectOutputStream.close();
	        	wOutputStream.close();
	        }
	        
	        if (wObjectInputStream !=null){
	        	wObjectInputStream.close();
	        	wInputStream.close();
	        }
//			System.out.println("-----------------------onRequest END \t" + pRequest.getRequestedSessionId());

		} catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	
	/**
	 * Escreve objeto no OutputStream.
	 * @param pObject
	 * @throws DBSIOException
	 */
	public void writeObject(ObjectOutputStream pOutputStream, Object pObject) throws DBSIOException{
		DBSHttp.ObjectOutputStreamWriteObject(pOutputStream, pObject);
	}
	
	/**
	 * Retorna objeto a partir do objeto JSON lido do InputStream.<br/>
	 * A classe do retornada deverá conter variáveis com os mesmos nomes dos campos contidos no objeto JSON lido. Não são necessários <i>setter e getter</i>.<br/>
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public <T> T readObject(ObjectInputStream pInputStream, Class<T> pClass) throws DBSIOException{
//		System.out.println("---readObject START");
       	return DBSHttp.ObjectInputStreamReadObject(pInputStream, pClass);
	}

	private static String pvGetIpRequest(HttpServletRequest pRequest) {
        String xIPAddress = pRequest.getHeader("X-FORWARDED-FOR");
        
        if (xIPAddress == null) {
            xIPAddress = pRequest.getRemoteAddr();
        }

        return xIPAddress;
	}
}
