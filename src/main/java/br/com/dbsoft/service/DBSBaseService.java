package br.com.dbsoft.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.rest.interfaces.ISearchControl;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

/**
 * @author jose.avila@dbsoft.com.br
 */
public abstract class DBSBaseService {

	protected Logger wLogger =  Logger.getLogger(this.getClass());

	protected 	Connection 			wConnection;
	private 	IDBSMessages 		wMessages = new DBSMessages(true);
	private		int					wStatus = HttpServletResponse.SC_OK;
	private 	Map<String, String> wPageLinks;
	private 	Map<String, Object> wMetaData = new HashMap<String, Object>();
	private 	Map<String, Object> wIncluded = new HashMap<String, Object>();
	
	public IDBSMessages getMessages(){
		return wMessages;
	}

	public int getStatus() {
		return wStatus;
	}
	public void setStatus(int pStatus) {
		wStatus = pStatus;
	}

	public Map<String, String> getPageLinks() {
		return wPageLinks;
	}
	
	public Map<String, Object> getMetaData() {
		return wMetaData;
	}
	
	public Map<String, Object> getIncluded() {
		return wIncluded;
	}	

	//METODOS CONSTRUTORES ================================================
	
	//METODOS PROTECTED ===================================================
	protected void prSetPageLinks(String pPath, ISearchControl pSearchControl, Integer pPagesCount) {
		Map<String, String> xSearchLinks = new HashMap<String, String>();
		
		if (!DBSObject.isNull(pSearchControl) && pPagesCount > 1) {
			Integer xNextPage;
			Integer xPrevPage;
			Integer xLastPage;
			String 	xSize = "";
			String	xSort = "";
			
			if (!DBSObject.isEmpty(pSearchControl.getSort())) {
				xSort = "&sort="+ pSearchControl.getSort();
			}
				
			if(!DBSObject.isNull(pSearchControl.getSize())) {
				xSize = "&size="+pSearchControl.getSize();
			}
			
			if (!DBSObject.isNull(pSearchControl.getPage()) && pSearchControl.getPage() > 1) {
				//Paginação - first
				xSearchLinks.put("firstPage", pPath+"?page=1"+xSize+xSort);
				//Paginação - Prev
				xPrevPage = pSearchControl.getPage()-1;
				xSearchLinks.put("prevPage", pPath+"?page="+xPrevPage+xSize+xSort);
			}
			if (!DBSObject.isNull(pSearchControl.getPage()) && pSearchControl.getPage() < pPagesCount && pSearchControl.getPage() >= 1) {
				//Paginação - Next
				xNextPage = pSearchControl.getPage()+1;
				xSearchLinks.put("nextPage", pPath+"?page="+xNextPage+xSize+xSort);
				//Paginação - Last
				xLastPage = pPagesCount;
				xSearchLinks.put("lastPage", pPath+"?page="+xLastPage+xSize+xSort);
			}
			
			//Total de Páginas
			xSearchLinks.put("totalPages", DBSString.toString(pPagesCount));
		}
		
		wPageLinks = xSearchLinks;
	}
	
	protected void prPutSearchControlInParams(Map<String, String> pParams, ISearchControl pSearchControl) {
		if (DBSObject.isNull(pSearchControl) || DBSObject.isNull(pParams)) {
			return;
		}
		//Sort
		if (!DBSObject.isEmpty(pSearchControl.getSort())) {
			pParams.put("sort", pSearchControl.getSort());
		}
		//Page
		if (DBSObject.isIdValid(pSearchControl.getPage())) {
			pParams.put("page", pSearchControl.getPage().toString());
		}
		//Offset
		if (DBSObject.isIdValid(pSearchControl.getOffset())) {
			pParams.put("offset", pSearchControl.getOffset().toString());
		}
		//Size
		if (!DBSObject.isNull(pSearchControl.getSize())) {
			pParams.put("size", pSearchControl.getSize().toString());
		}
	}
	
}
