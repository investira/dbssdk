package br.com.investira.access.services;

import java.io.IOException;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.http.DBSHttpMethodDelete;
import br.com.dbsoft.http.DBSHttpMethodGet;
import br.com.dbsoft.http.DBSHttpMethodPatch;
import br.com.dbsoft.http.DBSHttpMethodPost;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessageBase.MESSAGE_TYPE;
import br.com.dbsoft.rest.DBSRestReturn;
import br.com.dbsoft.rest.dados.DadosRecordCount;
import br.com.dbsoft.rest.interfaces.IRecordCount;
import br.com.dbsoft.service.DBSBaseService;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSObject;
import br.com.investira.access.AccessMessages;
import br.com.investira.access.dados.DadosAuthCode;
import br.com.investira.access.interfaces.IAuthCode;

@SuppressWarnings("unchecked")
public class AccessCodeService extends DBSBaseService {
	
	private String wClientToken;
	private String wURLPath;
	
	//CONSTRUTORES ============================================================================================================
	public AccessCodeService(String pClientToken, String pURL) {
		wClientToken = pClientToken;
		wURLPath = DBSFile.getPathNormalized(pURL, "/api/code");
	}
	
	//MÉTODOS PÚBLICOS ========================================================================================================
	/**
	 * Create de Usuário
	 * @param pPayload
	 * @return
	 * @throws DBSIOException
	 */
	public IAuthCode create(Object pPayload) {
		DBSHttpMethodPost 			xMethod;
		DBSRestReturn<IAuthCode> 	xRetorno = new DBSRestReturn<IAuthCode>();
		IAuthCode 					xCode = new DadosAuthCode();
		
		try {
			xMethod = new DBSHttpMethodPost(wClientToken);
			xRetorno = xMethod.doPost(wURLPath, pPayload, DBSRestReturn.class, DadosAuthCode.class);
			if (!DBSObject.isNull(xRetorno) || !DBSObject.isNull(xRetorno.getData())) {
				xCode = xRetorno.getData();
				getMessages().add(AccessMessages.CodigoCriarSucesso);
			} else {
				getMessages().add(AccessMessages.CodigoCriarErro);
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xRetorno.getError().getText()));
			}
		} catch (AuthException | IOException e) {
			getMessages().add(AccessMessages.CodigoCriarErro);
		}
		return xCode;
	}
	
	/**
	 * Recupera as informações contidas no Token de Usuário
	 * @param pClientToken
	 * @return
	 */
	public IAuthCode read(String pCode) {
		DBSHttpMethodGet 			xMethod;
		DBSRestReturn<IAuthCode> 	xRetorno = new DBSRestReturn<IAuthCode>();
		IAuthCode					xCode = null;
		
		try {
			xMethod = new DBSHttpMethodGet(wClientToken);
			xRetorno = xMethod.doGet(DBSFile.getPathNormalized(wURLPath, pCode), DBSRestReturn.class, DadosAuthCode.class);
			xCode = xRetorno.getData();
			if (DBSObject.isNull(xCode) || DBSObject.isEmpty(pCode)) {
				getMessages().add(AccessMessages.TokenInvalido);
			}
		} catch (AuthException | IOException e) {
			getMessages().add(AccessMessages.TokenInvalido);
		}
		return xCode;
	}
	
	/**
	 * Recupera as informações contidas no Token de Usuário
	 * @param pClientToken
	 * @return
	 */
	public IRecordCount delete(String pCode) {
		DBSHttpMethodDelete 		xMethod;
		DBSRestReturn<IRecordCount> xRetorno = new DBSRestReturn<IRecordCount>();
		IRecordCount				xRecordCount = null;
		
		try {
			xMethod = new DBSHttpMethodDelete(wClientToken);
			xRetorno = xMethod.doDelete(DBSFile.getPathNormalized(wURLPath, pCode), DBSRestReturn.class, DadosRecordCount.class);
			if (DBSObject.isNull(xRetorno) || DBSObject.isNull(xRetorno.getData())) {
				getMessages().add(AccessMessages.CodeInvalido);
			} else {
				xRecordCount = xRetorno.getData();
			}
		} catch (AuthException | IOException e) {
			getMessages().add(AccessMessages.CodeInvalido);
		}
		return xRecordCount;
	}
	
	/**
	 * Recupera as informações contidas no Token de Usuário
	 * @param pClientToken
	 * @return
	 */
	public IAuthCode verify(String pCode) {
		DBSHttpMethodPatch 			xMethod;
		DBSRestReturn<IAuthCode> 	xRetorno = new DBSRestReturn<IAuthCode>();
		IAuthCode					xCode = null;
		
		try {
			xMethod = new DBSHttpMethodPatch(wClientToken);
			xRetorno = xMethod.doPatch(DBSFile.getPathNormalized(wURLPath, pCode), DBSRestReturn.class, DadosAuthCode.class);
			xCode = xRetorno.getData();
			if (DBSObject.isNull(xCode) || DBSObject.isEmpty(pCode)) {
				getMessages().add(AccessMessages.TokenInvalido);
			}
		} catch (AuthException | IOException e) {
			getMessages().add(AccessMessages.TokenInvalido);
		}
		return xCode;
	}

//	/**
//	 * TODO IMPLEMENTAR 
//	 * @param pUsername
//	 * @return
//	 */
//	public IAuthCode codeReadFromUsername(String pUsername) {
//		IAuthCode xCode = new DadosAuthCode();
//		
//		xCode.setCode("$2b$04$nyeM6Zwi9hpWmIylLTZnAe0fgihIYJIH2vRaScP5nyW6rX4sWDI4O");
//		xCode.setExpiration(DBSDate.getNowDate(true));
//		return xCode;
//	}
}
