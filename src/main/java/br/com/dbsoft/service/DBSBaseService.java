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

	protected 	Connection 		wConnection;
	private 	IDBSMessages 	wMessages = new DBSMessages(true);
	private		int				wStatusCode = HttpServletResponse.SC_OK;
	private 	Map<String, String> wPageLinks;
	
	public IDBSMessages getMessages(){
		return wMessages;
	}

	public int getStatusCode() {
		return wStatusCode;
	}
	public void setStatusCode(int pStatusCode) {
		wStatusCode = pStatusCode;
	}

	public Map<String, String> getPageLinks() {
		return wPageLinks;
	}

	//METODOS CONSTRUTORES ================================================
	
	//METODOS PROTECTED ===================================================
	protected void prSetPageLinks(String pPath, ISearchControl pSearchControl, Integer pPagesCount) {
		Map<String, String> xSearchLinks = new HashMap<String, String>();
		
		
		if (!DBSObject.isNull(pSearchControl) && !DBSObject.isNull(pSearchControl.getPage())) {
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
			
			if (pSearchControl.getPage() > 1) {
				//Paginação - first
				xSearchLinks.put("firstPage", pPath+"?page=1"+xSize+xSort);
				//Paginação - Prev
				xPrevPage = pSearchControl.getPage()-1;
				xSearchLinks.put("prevPage", pPath+"?page="+xPrevPage+xSize+xSort);
			}
			if (pSearchControl.getPage() < pPagesCount && pSearchControl.getPage() >= 1) {
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
	
}
