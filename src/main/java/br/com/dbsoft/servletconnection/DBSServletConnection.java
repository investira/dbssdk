package br.com.dbsoft.servletconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.CONTENT_TYPE;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSHttp;
import br.com.dbsoft.util.DBSIO;

/**
 * @author ricardo.villar
 *
 */
public class DBSServletConnection {

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	private String 				wURL;
	private HttpURLConnection 	wServletConnection;
	private ObjectOutputStream 	wObjectOutputStream;
	private ObjectInputStream 	wObjectInputStream;
	private OutputStream 		wOutputStream;
	private InputStream 		wInputStream;
	
	public String getURL() {return wURL;}
	public void setURL(String pURL) {wURL = pURL;}

	
	public DBSServletConnection(String pURL) throws DBSIOException{
		try {
			wURL = pURL;
			URL xURL = new URL(pURL);
			wServletConnection = (HttpURLConnection) xURL.openConnection();
			wServletConnection.setConnectTimeout(60000); //1 Minuto;
			wServletConnection.setReadTimeout(60000); //1 Minuto;
			wServletConnection.setDoInput(true);
			wServletConnection.setDoOutput(true);
			wServletConnection.setUseCaches(false);
			wServletConnection.setDefaultUseCaches(false);
			wServletConnection.setRequestProperty("Content-Type",CONTENT_TYPE.APPLICATION_JSON);
//			wServletConnection.setRequestProperty("Content-Type","application/x-java-serialized-object");
	        wOutputStream = wServletConnection.getOutputStream();
	        wObjectOutputStream = new ObjectOutputStream(wOutputStream);
		} catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	/**
	 * Escreve objeto no OutputStream.<br/>
	 * @param pObject
	 * @throws DBSIOException
	 */
	public void writeObject(Object pObject) throws DBSIOException{
		DBSHttp.ObjectOutputStreamWriteObject(wObjectOutputStream, pObject);
	}
	
	/**
	 * Retorna objeto a partir do objeto JSON lido do InputStream.<br/>
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public <T> T readObject(Class<T> pClass) throws DBSIOException{
		return DBSHttp.ObjectInputStreamReadObject(wObjectInputStream, pClass);
	}
	
	/**
	 * Retorna objeto a partir do objeto JSON lido do InputStream.<br/>
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public <T> List<T> readObjectList(Class<T> pClass) throws DBSIOException{
		return DBSHttp.ObjectInputStreamReadObjectList(wObjectInputStream, pClass);
	}
	
	/**
	 * Conecta com a servlet informada, envia e recebe os parametros.
	 * @throws DBSIOException
	 */
	public void connect() throws DBSIOException{
        try {
			wObjectOutputStream.flush();
			wObjectOutputStream.close();
	        wInputStream = wServletConnection.getInputStream();
			wObjectInputStream = new ObjectInputStream(wInputStream);
		} catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	/**
	 * Desconecta com a servlet
	 * @throws DBSIOException
	 */
	public void disconnect() throws DBSIOException{
        try {
        	wObjectOutputStream.close();
        	wOutputStream.close();
	        wObjectInputStream.close();
	        wInputStream.close();
        } catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	public Integer getResponseCode() throws IOException {
		return wServletConnection.getResponseCode();
	}
	

}
