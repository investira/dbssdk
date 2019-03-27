package br.com.investira.access.services;

import java.io.IOException;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.http.DBSHttpMethodDelete;
import br.com.dbsoft.http.DBSHttpMethodGet;
import br.com.dbsoft.http.DBSHttpMethodPost;
import br.com.dbsoft.http.DBSHttpMethodPut;
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
import br.com.investira.access.dados.DadosAuthToken;
import br.com.investira.access.dados.DadosAuthUser;
import br.com.investira.access.interfaces.IAuthCode;
import br.com.investira.access.interfaces.IAuthToken;
import br.com.investira.access.interfaces.IAuthUser;

@SuppressWarnings("unchecked")
public class AccessUserService extends DBSBaseService {
	
	private String wClientToken;
	private String wURLPath;

	//CONSTRUTORES ============================================================================================================
	public AccessUserService(String pClientToken, String pURL) {
		wClientToken = pClientToken;
		wURLPath = DBSFile.getPathNormalized(pURL, "/api/user");
	}
	
	//MÉTODOS PÚBLICOS ========================================================================================================
	/**
	 * Create de Usuário
	 * @param pUserInfo
	 * @return
	 * @throws DBSIOException
	 */
	public IAuthCode create(IAuthUser pUserInfo) {
		DBSRestReturn<IAuthCode> 	xRetorno = new DBSRestReturn<IAuthCode>();
		DBSHttpMethodPost 			xMethod;
		IAuthCode 					xUserCode = null;
		
		try {
			xUserCode = new DadosAuthCode();
			xMethod = new DBSHttpMethodPost(wClientToken);
			xRetorno = xMethod.doPost(wURLPath, pUserInfo, DBSRestReturn.class, DadosAuthCode.class);
			if (!DBSObject.isNull(xRetorno) || !DBSObject.isNull(xRetorno.getData())) {
				xUserCode = xRetorno.getData();
				getMessages().add(AccessMessages.CadastroCriarSucesso);
			} else {
				getMessages().add(AccessMessages.CadastroCriarErro);
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xRetorno.getError().getText()));
			}
		} catch (AuthException | IOException e) {
			wLogger.error(e);
			getMessages().add(AccessMessages.CadastroCriarErro);
		}
		return xUserCode;
	}
	
	/**
	 * Recupera as informações contidas de Usuário
	 * @param pClientToken
	 * @return
	 */
	public IAuthUser read(String pUsername) {
		DBSRestReturn<IAuthUser> 	xRetorno = null;
		DBSHttpMethodGet 		xMethod = new DBSHttpMethodGet(wClientToken);
		IAuthUser				xUserInfo = null;
		
		try {
			xRetorno = xMethod.doGet(DBSFile.getPathNormalized(wURLPath, pUsername), DBSRestReturn.class, DadosAuthUser.class);
			xUserInfo = xRetorno.getData();
			if (DBSObject.isNull(xUserInfo) || DBSObject.isEmpty(xUserInfo.getUsername())) {
				getMessages().add(AccessMessages.TokenInvalido);
			}
		} catch (AuthException | IOException e) {
			wLogger.error(e);
			getMessages().add(AccessMessages.TokenInvalido);
		}
		return xUserInfo;
	}
	
	/**
	 * Atualiza os dados de Usuário
	 * @param pUserInfo
	 * @return
	 * @throws DBSIOException
	 */
	public IAuthUser update(IAuthUser pUserInfo) {
		DBSRestReturn<IAuthUser> 	xRetorno = new DBSRestReturn<IAuthUser>();
		DBSHttpMethodPut 			xMethod;
		IAuthUser 					xUser = null;
		
		try {
			xUser = new DadosAuthUser();
			xMethod = new DBSHttpMethodPut(wClientToken);
			xRetorno = xMethod.doPut(wURLPath, pUserInfo, DBSRestReturn.class, DadosAuthUser.class);
			if (!DBSObject.isNull(xRetorno) || !DBSObject.isNull(xRetorno.getData())) {
				xUser = xRetorno.getData();
				getMessages().add(AccessMessages.CadastroCriarSucesso);
			} else {
				getMessages().add(AccessMessages.CadastroCriarErro);
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xRetorno.getError().getText()));
			}
		} catch (AuthException | IOException e) {
			wLogger.error(e);
			getMessages().add(AccessMessages.CadastroCriarErro);
		}
		return xUser;
	}
	
	/**
	 * Exclúi um Usuário
	 * @param pClientToken
	 * @return
	 */
	public IRecordCount delete(String pUsername) {
		DBSHttpMethodDelete 		xMethod;
		DBSRestReturn<IRecordCount> xRetorno = new DBSRestReturn<IRecordCount>();
		IRecordCount				xRecordCount = null;
		
		try {
			xMethod = new DBSHttpMethodDelete(wClientToken);
			xRetorno = xMethod.doDelete(DBSFile.getPathNormalized(wURLPath, pUsername), DBSRestReturn.class, DadosRecordCount.class);
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
	public IAuthToken checkToken(String pToken) {
		DBSRestReturn<IAuthToken> 	xRetorno = null;
		DBSHttpMethodGet 			xMethod = new DBSHttpMethodGet(wClientToken);
		IAuthToken					xToken = null;
		String 						xURL = DBSFile.getPathNormalized(DBSFile.getPathNormalized(wURLPath, "token"), pToken);
		
		try {
			xRetorno = xMethod.doGet(xURL, DBSRestReturn.class, DadosAuthToken.class);
			xToken = xRetorno.getData();
			if (DBSObject.isNull(xToken) || DBSObject.isEmpty(xToken.getAccessToken())) {
				getMessages().add(AccessMessages.TokenInvalido);
			}
		} catch (AuthException | IOException e) {
			wLogger.error(e);
			getMessages().add(AccessMessages.TokenInvalido);
		}
		return xToken;
	}
	
	/**
	 * Recupera o code gerado para um usuário
	 * @param pClientToken
	 * @return
	 */
	public IAuthCode userCode(String pUsername) {
		DBSRestReturn<IAuthCode> 	xRetorno = null;
		DBSHttpMethodGet 			xMethod = new DBSHttpMethodGet(wClientToken);
		IAuthCode					xCode = null;
		String 						xURL = DBSFile.getPathNormalized(DBSFile.getPathNormalized(wURLPath, pUsername), "code");
		
		try {
			xRetorno = xMethod.doGet(xURL, DBSRestReturn.class, DadosAuthUser.class);
			xCode = xRetorno.getData();
			if (DBSObject.isNull(xCode) || DBSObject.isEmpty(xCode.getCode())) {
				getMessages().add(AccessMessages.TokenInvalido);
			}
		} catch (AuthException | IOException e) {
			wLogger.error(e);
			getMessages().add(AccessMessages.TokenInvalido);
		}
		return xCode;
	}
}
