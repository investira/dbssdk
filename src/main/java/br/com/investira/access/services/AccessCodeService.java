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
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSObject;
import br.com.investira.access.AccessMessages;
import br.com.investira.access.dados.DadosAuthCode;
import br.com.investira.access.interfaces.IAuthCode;

@SuppressWarnings("unchecked")
public class AccessCodeService extends AbstractService {
	
	private String wURLPath;
	
	//CONSTRUTORES ============================================================================================================
	public AccessCodeService(String pClientToken, String pURL) {
		wClientToken = pClientToken;
		wURLPath = DBSFile.getURLNormalized(DBSFile.getURLNormalized(pURL, PATH_V1), "/code");
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
				prAddMessage(AccessMessages.CodigoCriarSucesso);
			} else {
				prAddMessage(AccessMessages.CodigoCriarErro);
				prAddMessage(new DBSMessage(MESSAGE_TYPE.ERROR, xRetorno.getError().getDescription()));
			}
		} catch (AuthException e) {
			prAddMessage(AccessMessages.NaoAutorizado);
		} catch (IOException e) {
			prAddMessage(AccessMessages.CodigoCriarErro);
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
			xRetorno = xMethod.doGet(DBSFile.getURLNormalized(wURLPath, pCode), DBSRestReturn.class, DadosAuthCode.class);
			xCode = xRetorno.getData();
			if (DBSObject.isNull(xCode) || DBSObject.isEmpty(pCode)) {
				prAddMessage(AccessMessages.TokenInvalido);
			}
		} catch (AuthException e) {
			prAddMessage(AccessMessages.NaoAutorizado);
		} catch (IOException e) {
			prAddMessage(AccessMessages.TokenInvalido);
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
			xRetorno = xMethod.doDelete(DBSFile.getURLNormalized(wURLPath, pCode), DBSRestReturn.class, DadosRecordCount.class);
			if (DBSObject.isNull(xRetorno) || DBSObject.isNull(xRetorno.getData())) {
				prAddMessage(AccessMessages.CodeInvalido);
			} else {
				xRecordCount = xRetorno.getData();
			}
		} catch (AuthException e) {
			prAddMessage(AccessMessages.NaoAutorizado);
		} catch (IOException e) {
			prAddMessage(AccessMessages.CodeInvalido);
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
			xRetorno = xMethod.doPatch(DBSFile.getURLNormalized(wURLPath, pCode), DBSRestReturn.class, DadosAuthCode.class);
			xCode = xRetorno.getData();
			if (DBSObject.isNull(xCode) || DBSObject.isEmpty(pCode)) {
				prAddMessage(AccessMessages.TokenInvalido);
			}
		} catch (AuthException e) {
			prAddMessage(AccessMessages.NaoAutorizado);
		} catch (IOException e) {
			prAddMessage(AccessMessages.TokenInvalido);
		}
		return xCode;
	}

}
