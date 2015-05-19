package br.com.dbsoft.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.ENCODE;
import br.com.dbsoft.util.DBSObject;

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
	
	public static String getResourcePath(){
		return getRealPath("WEB-INF" + File.separator + "classes" + File.separator);
	}
	
	public static String getRealPath(String pRelativePath){
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath(pRelativePath);
	}

	/**
	 * Retorna String contendo os parametros sepadados por '&' e codificados em UTF-8 para utilização em requests http
	 * @param pParams
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeParams(Map<String, String> pParams) throws UnsupportedEncodingException{
		StringBuilder xString = new StringBuilder();
		for (String xParametro:pParams.keySet()){
			xString.append("&");
			xString.append(xParametro.trim());
			xString.append("=");
			xString.append(URLEncoder.encode(pParams.get(xParametro), ENCODE.ISO_8859_1));
		}
		return xString.toString();
	}
	
	/**
	 * Retorna lista com o nome do parametro e respectivo valor ja convertidos a partir de UTF-8;
	 * @param pParams
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> decodeParams(String pParams) throws UnsupportedEncodingException{
		Map<String, String> xParams = new HashMap<String, String>();
		ArrayList<String> xList = DBSString.toArrayList(pParams, "&");
		for (String xParam: xList){
			ArrayList<String> xValues = DBSString.toArrayList(xParam, "=");
			if (!DBSObject.isEmpty(xValues.get(0))
			 && !DBSObject.isEmpty(xValues.get(1))){
				xParams.put(URLDecoder.decode(xValues.get(0), ENCODE.ISO_8859_1), URLDecoder.decode(xValues.get(1), ENCODE.ISO_8859_1));
			}
		}
		return xParams;
	}
}


///**
// * Evia um arquivo local para o browser
// * @param pLocalFilePath Nome do arquivo contendo o caminho completo
// * @return
// */
//public static Boolean sendFile(String pLocalFilePath){
//	return sendFile(pLocalFilePath, "");
//}
//
///**
// * Evia um arquivo local para o browser
// * @param pLocalFilePath Nome do arquivo contendo o caminho completo
// * @param pRemoteFileName Nome simples do arquivo que será utilizado remotamente.
// * @return
// */
//public static Boolean sendFile(String pLocalFilePath, String pRemoteFileName){
//	String			xRemoteFileName = pRemoteFileName; 
//	FacesContext 	xFC = FacesContext.getCurrentInstance();
//	ExternalContext xEC = xFC.getExternalContext();
//	
//	InputStream 	xReportInputStream = null;
//	File			xFile = new File(pLocalFilePath);
//	
//	//Verifica se arquivo existe. Se não existir...
//	try {
//		if (DBSFile.exists(pLocalFilePath)){
//			xReportInputStream = new FileInputStream(xFile);
//		}
//		//Se o arquivo já existir apenas entrega-o para o browser
//		if (!DBSObject.isNull(xReportInputStream)) {
//			if (xRemoteFileName.equals("")){
//				xRemoteFileName = xFile.getName();
//			}
//			xEC.responseReset();
//			xEC.setResponseContentType(xEC.getMimeType(pLocalFilePath));
//			xEC.setResponseContentLength((int) xFile.length());
//			xEC.setResponseHeader("Content-Disposition", "attachment;filename=\"" + xRemoteFileName + "\"");
//			OutputStream xOutputStream = xEC.getResponseOutputStream();
//			if (xOutputStream != null){
//				IOUtils.copy(xReportInputStream, xOutputStream);
//			}
//			xFC.responseComplete();
//			return true;
//		}
//		return false;
//	} catch (IOException e) {
//		wLogger.error(e);
//		return false;
//	}		
//}
//// create new session
//((HttpServletRequest) ec.getRequest()).getSession(true);
// 
//// restore last used user settings because login / logout pages reference "userSettings"
//FacesAccessor.setValue2ValueExpression(userSettings, "#{userSettings}");
// 
//// redirect to the specified logout page
//ec.redirect(ec.getRequestContextPath() + "/views/logout.jsf");

