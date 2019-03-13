package br.com.dbsoft.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import br.com.dbsoft.core.DBSSDK.ENCODE;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.json.DBSJson;

public class DBSHttp {

	private static Logger wLogger = Logger.getLogger(DBSHttp.class);

	/**
	 * Retorna o response da conexão web atual
	 * @return
	 */
	public static HttpServletResponse getHttpServletResponse(){ 		
		FacesContext xContext = FacesContext.getCurrentInstance();
		return (HttpServletResponse) xContext.getExternalContext().getResponse();
	}

	public static HttpServletRequest getHttpServletRequest(){ 		
		FacesContext xContext = FacesContext.getCurrentInstance();
		return (HttpServletRequest) xContext.getExternalContext().getRequest();
	}
	
	/**
	 * Evia um arquivo local para o browser
	 */
	public static Boolean sendFile(ByteArrayOutputStream pByteArrayOutputStream, String pRemoteFileName, String pContentType){
		FacesContext 	xFC = FacesContext.getCurrentInstance();
		ExternalContext xEC = xFC.getExternalContext();
		
		//Verifica se arquivo existe. Se não existir...
		try {
			//Se o arquivo já existir apenas entrega-o para o browser
			if (!DBSObject.isEmpty(pByteArrayOutputStream)) {
				xEC.responseReset();
				xEC.setResponseContentType(pContentType);
				xEC.setResponseContentLength(pByteArrayOutputStream.size());
				xEC.setResponseHeader("Content-Disposition", "attachment;filename=\"" + pRemoteFileName + "\"");
				OutputStream xOutputStream = xEC.getResponseOutputStream();
				if (xOutputStream != null){
					pByteArrayOutputStream.writeTo(xOutputStream);
				}
				xFC.responseComplete();
				return true;
			}
			return false;
		} catch (IOException e) {
			wLogger.error(e);
			return false;
		}			
	}
	
	/**
	 * Retorna o caminho local do web-info/classes da aplicação
	 * @param pExternalContext
	 * @return
	 */
	public static String getLocalPathWebInfClasses(ExternalContext pExternalContext){
		return getLocalPath(pExternalContext, "WEB-INF" + File.separator + "classes" + File.separator);
	}
	
	/**
	 * Retorna o caminho local a partir de caminho virtual
	 * @param pRelativePath
	 * @return
	 */
	public static String getLocalPath(ExternalContext pExternalContext, String pRelativePath){
		return pExternalContext.getRealPath(pRelativePath);
	}
	
	/**
	 * Retorna caminho da URL do servidor local a partir do ExternalContext corrente
	 * @return
	 */
	public static String getServerURLString(){
		if (FacesContext.getCurrentInstance() == null){
			return "";
		}
		return getServerURLString(FacesContext.getCurrentInstance().getExternalContext());
	}

	/**
	 * Retorna caminho da URL do servidor local a partir do ExternalContext informado
	 * @return
	 */
	public static String getServerURLString(ExternalContext pExternalContext){
		if (pExternalContext == null){return "";}
		StringBuilder xLink = new StringBuilder();
		xLink.append(pExternalContext.getRequestScheme()).append("://");
		xLink.append(pExternalContext.getRequestServerName());
		if (!DBSObject.isEmpty(pExternalContext.getRequestServerPort())){
			xLink.append(":").append(pExternalContext.getRequestServerPort());
		}
//		xLink += xEC.getRequestContextPath();
		return xLink.toString();
	}
	
	/**
	 * Retorna URL do servidor local a partir de um request
	 * @param pContext
	 * @return
	 */
	public static String getServerURLString(HttpServletRequest pContext){
		if (pContext == null){return "";}
		StringBuilder xLink = new StringBuilder();
		
		xLink.append(pContext.getScheme()).append("://");
		xLink.append(pContext.getServerName());
		
		if (!DBSObject.isEmpty(pContext.getServerPort())){
			xLink.append(":").append(pContext.getServerPort());
		}
		return xLink.toString();
	}
	
	/**
	 * Retorna String contendo os parametros sepadados por '&' e codificados em UTF-8 para utilização em requests http
	 * @param pParams
	 * @return
	 * @throws DBSIOException 
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeParams(Map<String, String> pParams) throws DBSIOException {
		return encodeParams(pParams, ENCODE.ISO_8859_1);
	}
	/**
	 * Retorna String contendo os parametros sepadados por '&' e codificados em UTF-8 para utilização em requests http
	 * @param pParams
	 * @param pEncode
	 * @return
	 * @throws DBSIOException 
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeParams(Map<String, String> pParams, String pEncode) throws DBSIOException {
		StringBuilder xString = new StringBuilder();
		try {
			for (String xParametro:pParams.keySet()){
				xString.append("&");
				xString.append(xParametro.trim());
				xString.append("=");
				String xValue = DBSString.toString(pParams.get(xParametro),"").trim();
				xString.append(URLEncoder.encode(xValue, pEncode));
			}
			return xString.toString();
		} catch (UnsupportedEncodingException e) {
			DBSIO.throwIOException(e);
		}
		return null;
	}
	
	/**
	 * Retorna lista com o nome do parametro e respectivo valor ja convertidos a partir de UTF-8;
	 * @param pParams
	 * @return
	 * @throws DBSIOException 
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> decodeParams(String pParams) throws DBSIOException {
		try {
			Map<String, String> xParams = new HashMap<String, String>();
			ArrayList<String> xList = DBSString.toArrayList(pParams, "&");
			for (String xParam: xList){
				ArrayList<String> xValues = DBSString.toArrayList(xParam, "=");
				if (xValues.size() ==  2){
					if (!DBSObject.isEmpty(xValues.get(0))
					 && !DBSObject.isEmpty(xValues.get(1))){
						xParams.put(URLDecoder.decode(xValues.get(0), ENCODE.ISO_8859_1), URLDecoder.decode(xValues.get(1), ENCODE.ISO_8859_1));
					}
				}
			}
			return xParams;
		} catch (UnsupportedEncodingException e) {
			DBSIO.throwIOException(e);
		}
		return null;
	}
	
	
	/**
	 * Escreve objeto JSON do OutputStream.<br/>
	 * @param pObjectOutputStream
	 * @param pObject
	 * @throws DBSIOException
	 */
	public static void ObjectOutputStreamWriteObject(ObjectOutputStream pObjectOutputStream, Object pObject) throws DBSIOException{
		if (pObjectOutputStream == null){return;}
		try {
			pObjectOutputStream.writeObject(DBSJson.toJson(pObject));
		} catch (IOException e) {
			DBSIO.throwIOException(e);
		}
	}
	

	/**
	 * Retorna objeto a partir do objeto JSON lido do InputStream.<br/>
	 * A classe do retornada deverá conter variáveis com os mesmos nomes dos campos contidos no objeto JSON lido. Não são necessários <i>setter e getter</i>.<br/>
	 * Os objetos são lidos sequencialmente(FIFO/PEPS). O primeiro objeto enviado, será o primeiro lido.
	 * @param pObjectInputStream
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> T ObjectInputStreamReadObject(ObjectInputStream pObjectInputStream, Class<T> pClass) throws DBSIOException{
		if (pObjectInputStream == null){return null;}
		try {
			Object xObject = pObjectInputStream.readObject();
			return DBSJson.fromJson(xObject, pClass);
		} catch (EOFException | StreamCorruptedException e){
			return null;
		} catch (JsonSyntaxException | ClassNotFoundException e) {
			DBSIO.throwIOException(e);
			return null;
		} catch (IOException e) {
			DBSIO.throwIOException(e);
			return null;
		}
	}
	
	/**
	 * Retorna objeto a partir do objeto JSON lido do InputStream.<br/>
	 * A classe do retornada deverá conter variáveis com os mesmos nomes dos campos contidos no objeto JSON lido. Não são necessários <i>setter e getter</i>.<br/>
	 * Os objetos são lidos sequencialmente(FIFO/PEPS). O primeiro objeto enviado, será o primeiro lido.
	 * @param pObjectInputStream
	 * @param pClass
	 * @return
	 * @throws DBSIOException
	 */
	public static <T> List<T> ObjectInputStreamReadObjectList(ObjectInputStream pObjectInputStream, Class<T> pClass) throws DBSIOException{
		if (pObjectInputStream == null){return null;}
		try {
			Object xObject = pObjectInputStream.readObject();
			return DBSJson.fromJsonList(xObject, pClass);
		} catch (EOFException | StreamCorruptedException e){
			return null;
		} catch (JsonSyntaxException | ClassNotFoundException e) {
			DBSIO.throwIOException(e);
			return null;
		} catch (IOException e) {
			DBSIO.throwIOException(e);
			return null;
		}
	}
	
	/**
	 * Retorna string a partir de um InputStream
	 * @param pInputStream
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromInputStream(InputStream pInputStream) throws IOException {
		ByteArrayOutputStream xBufferStream = new ByteArrayOutputStream();
		byte[] xBuffer = new byte[1024];
		int xFileLenght;
		while ((xFileLenght = pInputStream.read(xBuffer)) >= 0) {
			xBufferStream.write(xBuffer, 0, xFileLenght);
		}
		xBufferStream.close();
		return xBufferStream.toString();  
	}
}


