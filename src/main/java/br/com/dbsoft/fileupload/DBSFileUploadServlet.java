package br.com.dbsoft.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import br.com.dbsoft.core.DBSServlet;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;


/**
 * @author ricardo.villar
 * Deve-se configurar a servlet que herdará esta, com as anotações abaixo.<br/>
 * <b>MultipartConfig</b><br/>
 * <b>WebServlet(value='caminho e nome da servlet')</b><br/>
 * 
 * É necessário também configurar o standalone.xhml inserindo o parametro max-post-size=1000000000 no subitem de:
 * <subsystem xmlns="urn:jboss:domain:undertow:2.0">
 * 	..
 * 	<http-listener name="default" socket-binding="http" redirect-socket="https" max-post-size="1000000000"/>
 */
public abstract class DBSFileUploadServlet extends DBSServlet{

	private static final long serialVersionUID = 1063600676650691271L;

	private List<IDBSFileUploadServletEventsListener>	wEventListeners = new ArrayList<IDBSFileUploadServletEventsListener>();

	private String wLocalPath = "";
	private String wFileName = "";
	
	public DBSFileUploadServlet() {
		super();
		setAllowGet(false);
	}
	
	@PreDestroy
	private void finalizeClass(){
		wEventListeners.clear();
	}

	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.<br/>
	 * Para isso, classe deverá implementar a interface IDBSFileUploadServletEventsListener.<br/>
	 * Lembre-se de remove-la utilizando removeEventListener quando a classe for destruida, para evitar que ela seja chamada quando já não deveria. 
	 * @param pEventListener Classe
	 */
	public void addEventListener(IDBSFileUploadServletEventsListener pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
		}
	}

	
	public void removeEventListener(IDBSFileUploadServletEventsListener pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
		}
	}	
	
    /**
	 * Caminho da pasta local onde o arquivo recebido será salvo.
     * @return
     */
    public String getLocalPath() {return wLocalPath;}

	/**
	 * Caminho da pasta local onde o arquivo recebido será salvo.
	 * @param pLocalPath
	 */
	public void setLocalPath(String pLocalPath) {
		wLocalPath = DBSFile.getPathFromFolderName(pLocalPath);
	}
	
	// ========================================================================================================
    @Override
	protected void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) {
 		boolean xOk = true;
    	try {
 		   //Dispara evento 
 	       if (!pvFireEventBeforeUpload()
	         || DBSObject.isEmpty(getLocalPath())){
	        	return;
	        }
			for (Part xPart : pRequest.getParts()) {
			    wFileName = "";
			    for (String xS : xPart.getHeader("content-disposition").split(";")) {
			        if (xS.trim().startsWith("filename")) {
			        	wFileName = xS.split("=")[1].replaceAll("\"", "");
			        }
			    }
			    //Dispara evento 
			    if (pvFireEventBeforeSave()){
			        if (!DBSObject.isEmpty(wFileName)){
			        	//Verifica se a pasta existe
			        	if (!DBSFile.existsPath(wLocalPath+wFileName)) {
			        		//Cria a pasta caso ela não exista
			        		String xAbsolutePath = wLocalPath+wFileName;
			        		xOk = DBSFile.mkDir(DBSString.getSubString(xAbsolutePath, 1, xAbsolutePath.lastIndexOf(File.separator)));
			        	}
			        	if (xOk){
			        		xPart.write(wLocalPath + wFileName);
			        		//Dispara evento 
			        		pvFireEventAfterSave();
			        	}else{
			        		pvFireEventOnError();
			        	}
			        }
			    }
			}
			//Dispara evento 
			pvFireEventAfterUpload();
// 		} catch (java.io.FileNotFoundException e){
//			try {
//				DBSIO.throwIOException(e);
//				pvFireEventOnError();
//			} catch (DBSIOException e1) {
//				wLogger.error(e1);
//			}
		} catch (Exception e) { //java.io.FileNotFoundException
			try {
//				System.out.println("EXCEPTION:" + e.getMessage());
				wLogger.error(e);
				pvFireEventOnError();
				pResponse.getWriter().print(e.getMessage());
				pResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				pResponse.flushBuffer();
			} catch (DBSIOException | IOException e1) {
				wLogger.error(e1);
			}
		}
	}
	
	//Eventos locais=================================================================
	/**
	 * Evento ocorre antes de iniciar o upload.<br/>
	 * Este evento ocorre somente uma vez, mesmo que esteja sendo efetuado o upload de diversos arquivos.<br/>
	 * Para tratar arquivos individualmente, utilize o evento <b>beforeSave</b>.<br/>  
	 * Para impedir o upload, deve-se setar <b>setOk(False)</b>.
	 * Neste evento deve-se configurar o caminho local onde o arquivo será salvo utilizando o método <b>setLocalPath</b>, 
	 * bem como os listners ser houver.<br/>
	 */
	protected abstract void beforeUpload(DBSFileUploadServletEvent pEvent) throws DBSIOException;
	
	/**
	 * Evento ocorre após finializado o upload.<br/>
	 * @param pEvent 
	 */
	protected void afterUpload(DBSFileUploadServletEvent pEvent) throws DBSIOException {}


	/**
	 * Evento ocorre após finalizado o upload do arquivo e antes que ele seja salvo localmente.<br/>
	 * Pode-se neste evento, alterar o nome do arquivo utilizando <b>setFileName</b> para que seja salvo com outro nome.
	 * Pode-se impedir que ele seja salvo,  setando <b>setOk(False)</b>.<br/>
	 * @param pEvent 
	 */
	protected void beforeSave(DBSFileUploadServletEvent pEvent) throws DBSIOException{}
	
	/**
	 * Evento ocorre após o arquivo ter sido salvo localmente.<br/>
	 * @param pEvent 
	 */
	protected void afterSave(DBSFileUploadServletEvent pEvent) throws DBSIOException{}
	
	/**
	 * @param pEvent  
	 */
	protected void onError(DBSFileUploadServletEvent pEvent) throws DBSIOException{}

	//Fire Events =========================================================================
	private boolean pvFireEventBeforeUpload() throws DBSIOException{
		DBSFileUploadServletEvent xE = new DBSFileUploadServletEvent(this);
		try{
//			openConnection();
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeUpload(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).beforeUpload(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
			return xE.isOk();
		}catch(Exception e){
			wLogger.error("BeforeUpload:", e);
			throw e;
		}finally{
//			closeConnection();
		}
	}

	private void pvFireEventAfterUpload() throws DBSIOException{
		DBSFileUploadServletEvent xE = new DBSFileUploadServletEvent(this);
		try{
			afterUpload(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).beforeUpload(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
		}catch(Exception e){
			wLogger.error("AfterUpload:", e);
			throw e;
		}
	}
	
	private boolean pvFireEventBeforeSave() throws DBSIOException{
		DBSFileUploadServletEvent xE = new DBSFileUploadServletEvent(this);
		xE.setFileName(wFileName);
		try{
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeSave(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).beforeSave(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
			wFileName = xE.getFileName();
			return xE.isOk();
		}catch(Exception e){
			wLogger.error("beforeSave:", e);
			throw e;
		}
	}

	private void pvFireEventAfterSave() throws DBSIOException{
		DBSFileUploadServletEvent xE = new DBSFileUploadServletEvent(this);
		xE.setFileName(wFileName);
		try{
			afterSave(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).afterSave(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
		}catch(Exception e){
			wLogger.error("afterSave:", e);
			throw e;
		}
	}
	
	private void pvFireEventOnError() throws DBSIOException{
		DBSFileUploadServletEvent xE = new DBSFileUploadServletEvent(this);
		xE.setFileName(wFileName);
		try{
			onError(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).onError(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
		}catch(Exception e){
			wLogger.error("onError:", e);
			throw e;
		}
	}


}
	

