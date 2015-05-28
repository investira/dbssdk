package br.com.dbsoft.servletconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
//			wServletConnection.setRequestMethod("POST");
			wServletConnection.setRequestProperty("Content-Type",CONTENT_TYPE.APPLICATION_JSON);
//			wServletConnection.setRequestProperty("Content-Type","application/x-java-serialized-object");
	        OutputStream xOutputStream = wServletConnection.getOutputStream();
	        wObjectOutputStream = new ObjectOutputStream(xOutputStream);
		} catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	public void writeObject(Object pObject) throws DBSIOException{
		DBSHttp.ObjectOutputStreamWriteObject(wObjectOutputStream, pObject);
	}
	
	public <T> T readObject(Class<T> pClass) throws DBSIOException{
		return DBSHttp.ObjectInputStreamReadObject(wObjectInputStream, pClass);
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
	 * Disconecta com a servlet
	 * @throws DBSIOException
	 */
	public void disconnect() throws DBSIOException{
        try {
	        wObjectInputStream.close();
	        wInputStream.close();
        } catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	
	
//	public void teste() throws ClassNotFoundException{
//		URL xURL = null;
//	    try {
//	        xURL = new URL("http://localhost:8080/dbsusrapp/login");
//	        HttpURLConnection xServletConnection = (HttpURLConnection) xURL.openConnection();
//	        xServletConnection.setDoInput(true);
//	        xServletConnection.setDoOutput(true);
//	        xServletConnection.setUseCaches(false);
//	        xServletConnection.setDefaultUseCaches(false);
////	        xServletConnection.setRequestMethod("POST");
//	        xServletConnection.setRequestProperty("Content-Type","application/json");
////	        xServletConnection.setRequestProperty("Content-Type","application/x-java-serialized-object");
//	        DBSUser xUser = new DBSUser();
//	        xUser.setUser("Ricardo Avião Áções");
//	        xUser.setPassword("teste");
////	        xUser.setData(DBSDate.getNowDate());
////	        Gson xJSON = new Gson();
//
//	        OutputStream xOutputStream = xServletConnection.getOutputStream();
//	        ObjectOutputStream xOOS = new ObjectOutputStream(xOutputStream);
//	        
//	        xOOS.writeObject(xUser);
////	        xOOS.writeObject(xJSON.toJson(xUser));
//	        xOOS.flush();
//	        xOOS.close();
//	        
//	        InputStream xInputStream = xServletConnection.getInputStream();
//	        ObjectInputStream xOIS = new ObjectInputStream(xInputStream);
//	        DBSUser xUser2 = (DBSUser) xOIS.readObject();
////	        String xJSon2 = (String) xOIS.readObject();
////	        DBSUser xUser2 = xJSON.fromJson(xJSon2, DBSUser.class);
//	        		
//	        if (xUser2 != null){
//	            System.out.println(xUser2.getUser());
//	            System.out.println(xUser2.getPassword());
//	            System.out.println(xUser2.getToken());
//	        }else{
//	        	System.out.println("sem parametros");
//	        }
//	        //JOptionPane.showMessageDialog(null, result);
//	        xOIS.close();
//	        xInputStream.close();
//
//	    } catch (MalformedURLException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    } catch (IOException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//	    }
//	}	
}
