package br.com.investira.access.services;

import javax.servlet.http.HttpServletResponse;

import br.com.dbsoft.crud.DBSCrud;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.service.DBSBaseService;
import br.com.dbsoft.util.DBSObject;
import br.com.investira.access.AccessMessages;

public class AbstractService extends DBSBaseService {
	
	protected String wClientToken;
	protected final String PATH_V1 = "/api/v1";

	//METODOS PUBLICOS ===================================================
	//METODOS PROTEGIDOS ==================================================
	//Erros-----------------
	protected void prGenericError(DBSIOException pException) {
		wLogger.error(pException);
		getMessages().add(AccessMessages.ErroGenerico);
		setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Adiciona uma mensagem e configura o HTTP StatusCode para a resposta.
	 * @param pMessage
	 */
	protected void prAddMessage(IDBSMessage pMessage) {
		if (DBSObject.isNull(pMessage)) {
			getMessages().add(AccessMessages.ErroGenerico);
		} else {
			getMessages().add(pMessage);
		}
		setStatus(getMessages().getListMessageBase().get(0).getStatusCode());
	}
	
	protected void prAddMessages(IDBSMessages pMessages) {
		if (DBSObject.isNull(pMessages) || DBSObject.isEmpty(pMessages.getListMessage())) {
			getMessages().add(AccessMessages.ErroGenerico);
		} else {
			getMessages().addAll(pMessages);
		}
		setStatus(getMessages().getListMessageBase().get(0).getStatusCode());
	}
	
	protected void prGenericError(DBSIOException pException, IDBSMessage pMessage) {
		wLogger.error(pException);
		prAddMessage(pMessage);
	}
	
	protected void prGenericError(DBSIOException pException, DBSCrud<?> pCrud) {
		prGenericError(pException);
		pvGenericErrorCrud(pCrud);
	}
	
	protected void prGenericError(DBSIOException pException, IDBSMessage pMessage, DBSCrud<?> pCrud) {
		prGenericError(pException, pMessage);
		pvGenericErrorCrud(pCrud);
	}
	
	protected void prGenericError(DBSIOException pException, IDBSMessage pMessage, IDBSMessages pMessages) {
		getMessages().clear();
		prGenericError(pException, pMessage);
		getMessages().addAll(pMessages);
	}
	
	protected void prGenericError(IDBSMessage pMessage, IDBSMessages pCrudMessages) {
		prAddMessage(pMessage);
		getMessages().addAll(pCrudMessages);
	}
	
	//METODOS PRIVADOS ===================================================================
	private void pvGenericErrorCrud(DBSCrud<?> pCrud) {
		if (!DBSObject.isNull(pCrud) && pCrud.getMessages().hasErrorsMessages()) {
			getMessages().addAll(pCrud.getMessages());
		}
	}
	
}
