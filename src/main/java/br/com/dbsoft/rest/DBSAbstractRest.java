package br.com.dbsoft.rest;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.rest.dados.DadosRestError;
import br.com.dbsoft.rest.dados.DadosRestErrorCode;
import br.com.dbsoft.rest.interfaces.IIncludeAtivo;
import br.com.dbsoft.rest.interfaces.IRestError;
import br.com.dbsoft.rest.interfaces.IRestErrorCode;
import br.com.dbsoft.rest.interfaces.ISearchControl;
import br.com.dbsoft.service.DBSBaseService;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

public abstract class DBSAbstractRest {

	//METODOS PROTEGIDOS ====================================================================
	/**
	 * Preenche os campos de Paginação e Pesquisa da Interface (ISearchControl) 
	 * através dos parametros recebidos na requisição
	 * 
	 * @param pParams Parametros da Requisição
	 * @param pFilterSearchControl Interface que extende o ISearchControl
	 */
	protected void prToFilterSearchControl(MultivaluedMap<String, String> pParams, ISearchControl pFilterSearchControl) {
		//SearchControl - Sort (Ordenação)
		String xSort = DBSString.toString(pParams.getFirst("sort"), null);
		if (!DBSObject.isEmpty(xSort)) {
			pFilterSearchControl.setSort(xSort);
		}
		//SearchControl - Page (Numero da página)
		Integer xPage = DBSNumber.toInteger(pParams.getFirst("page"), null);
		if (!DBSObject.isNull(xPage)) {
			pFilterSearchControl.setPage(xPage);
		}
		//SearchControl - Offset (Inicio da pesquisa)
		Integer xOffset = DBSNumber.toInteger(pParams.getFirst("offset"), null);
		if (!DBSObject.isNull(xOffset)) {
			pFilterSearchControl.setOffset(xOffset);
		}
		//SearchControl - Size (Número de itens por página)
		Integer xSize = DBSNumber.toInteger(pParams.getFirst("size"), null);
		if (!DBSObject.isNull(xSize)) {
			pFilterSearchControl.setSize(xSize);
		}
	}
	
	protected void prToFilterIncludeAtivo(MultivaluedMap<String, String> pParam, IIncludeAtivo pFilter) {
		String xInclude = DBSString.toString(pParam.getFirst("include"));
		if (DBSObject.isEqual(xInclude, "ativo")) {
			pFilter.setIncludeAtivo(true);
		}		
	}
	
	/**
	 * Cria um Response padrão.
	 * @param pService
	 * @param pData
	 * @return
	 */
	protected Response prGetResponse(DBSBaseService pService, Object pData) {
		return prGetResponse(pService, pData, null);
	}
	
	/**
	 * Cria um Response padrão.
	 * @param pService
	 * @param pData
	 * @param pExtraHeaders
	 * @return
	 */
	protected Response prGetResponse(DBSBaseService pService, Object pData, Map<String, String> pExtraHeaders) {
		return prGetResponse(pService.getStatus(), pService.getMessages(), pData, pService.getPageLinks(), pService.getIncluded(), pService.getMetaData(), pExtraHeaders);
	}
	
	/**
	 * Cria um Response padrão com StatusCode HTTP, os dados, lista de mensagens, links de paginação, MetaDados e Cabeçalhos extras.
	 * @param pStatus
	 * @param pData
	 * @param pMessages
	 * @param pPages
	 * @param pMetaData
	 * @param pExtraHeaders
	 * @return
	 */
	protected Response prGetResponse(int pStatus, IDBSMessages pMessages, Object pData, Map<String, String> pPages, Map<String, Object> pInclude, Map<String, Object> pMetaData, Map<String, String> pExtraHeaders) {
		DBSRestReturn<Object> xRetorno = new DBSRestReturn<Object>();

		//Se o Código HTTP for 204 - No Content, não adiciona o corpo na resposta
		if (DBSObject.isEqual(pStatus, HttpServletResponse.SC_NO_CONTENT)) {
			xRetorno = null;
		//Se houver dados
		} else if (!DBSObject.isNull(pData)) {
			xRetorno.setData(pData);
			xRetorno.setPages(pPages);
			xRetorno.setInclude(pInclude);
			xRetorno.setMetaData(pMetaData);
			xRetorno.setMessages(pMessages.getListMessageBase());
		//Se houver erros
		} else if (!DBSObject.isEmpty(pMessages)) {
			IRestError xRestError = new DadosRestError();
			IRestErrorCode xRestErrorCode = new DadosRestErrorCode();
			xRestError.setDescription(pMessages.getCurrentMessage().getMessageText());
			xRestErrorCode.setStatus(pStatus);
			xRestErrorCode.setSource(DBSString.toString(pMessages.getCurrentMessage().getMessageCode()));
			xRestErrorCode.setRef(DBSString.toString(pMessages.getCurrentMessage().getMessageCode()));
			xRestError.setCode(xRestErrorCode);
			xRetorno.setError(xRestError);
		}
		
		//Cria a resposta
		Response xResponse = Response
				.status(pStatus)
				.entity(xRetorno)
				.type(MediaType.APPLICATION_JSON)
				.build();
		
		//Adiciona os Headers extras
		if (!DBSObject.isNull(pExtraHeaders)) {
			for (Map.Entry<String, String> xHeader : pExtraHeaders.entrySet()) {
				xResponse.getHeaders().add(xHeader.getKey(), xHeader.getValue());
			}
		}
		return xResponse;
	}
	
}
