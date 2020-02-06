package br.com.investira.access.services;

import java.io.IOException;

import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.http.DBSHttpMethodGet;
import br.com.dbsoft.rest.DBSRestReturn;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSObject;
import br.com.investira.access.AccessMessages;
import br.com.investira.access.dados.DadosAuthClient;
import br.com.investira.access.interfaces.IAuthClient;

@SuppressWarnings("unchecked")
public class AccessClientService extends AbstractService {
	
	private String wURLPath;
	
	//CONSTRUTORES ============================================================================================================
	public AccessClientService(String pClientToken, String pURL) {
		wClientToken = pClientToken;
		wURLPath = DBSFile.getPathNormalized(DBSFile.getPathNormalized(pURL, PATH_V1), "/client");
	}
	
	//MÉTODOS PÚBLICOS ========================================================================================================
//	/**
//	 * Create de Usuário
//	 * @param pPayload
//	 * @return
//	 * @throws DBSIOException
//	 */
//	public IAuthClient create(IClientName pClientName) {
//		DBSHttpMethodPost 			xMethod;
//		DBSRestReturn<IAuthClient> 	xRetorno = new DBSRestReturn<IAuthClient>();
//		IAuthClient					xClient = new DadosAuthClient();
//		
//		try {
//			xMethod = new DBSHttpMethodPost(wClientToken);
//			xRetorno = xMethod.doPost(wURLPath, pClientName, DBSRestReturn.class, DadosAuthClient.class);
//			if (!DBSObject.isNull(xRetorno) || !DBSObject.isNull(xRetorno.getData())) {
//				xClient = xRetorno.getData();
//				prAddMessage(AccessMessages.ClientCriarSucesso);
//			} else {
//				prAddMessage(AccessMessages.ClientCriarErro);
//				prAddMessage(new DBSMessage(MESSAGE_TYPE.ERROR, xRetorno.getError().getDescription()));
//			}
//		} catch (AuthException e) {
//			prAddMessage(AccessMessages.NaoAutorizado);
//		} catch (IOException e) {
//			prAddMessage(AccessMessages.ClientCriarErro);
//		}
//		return xClient;
//	}
	
	/**
	 * Recupera as informações contidas no Token de Usuário
	 * @param pClientToken
	 * @return
	 */
	public IAuthClient read(String pClientID) {
		DBSHttpMethodGet 			xMethod;
		DBSRestReturn<IAuthClient> 	xRetorno = new DBSRestReturn<IAuthClient>();
		IAuthClient					xClient = null;
		wLogger.info("=========================Verificando ClientToken=========================");
		try {
			xMethod = new DBSHttpMethodGet(wClientToken);
			xRetorno = xMethod.doGet(DBSFile.getPathNormalized(wURLPath, pClientID), DBSRestReturn.class, DadosAuthClient.class);
			xClient = xRetorno.getData();
			if (DBSObject.isNull(xClient) || !DBSObject.isIdValid(xClient.getClientId())) {
				prAddMessage(AccessMessages.ClientNaoEncontrado);
			}
		} catch (AuthException e) {
			if (e.getStatusCode() == 403) {
				prAddMessage(AccessMessages.UsuarioUsernameOuSenhaInvalida);
			} else {
				prAddMessage(AccessMessages.NaoAutorizado);
			}
		} catch (IOException e) {
			prAddMessage(AccessMessages.ClientReadErro);
		}
		return xClient;
	}
	
//	/**
//	 * Recupera as informações contidas no Token de Usuário
//	 * @param pClientToken
//	 * @return
//	 */
//	public IRecordCount delete(String pClient) {
//		DBSHttpMethodDelete 		xMethod;
//		DBSRestReturn<IRecordCount> xRetorno = new DBSRestReturn<IRecordCount>();
//		IRecordCount				xRecordCount = null;
//		
//		try {
//			xMethod = new DBSHttpMethodDelete(wClientToken);
//			xRetorno = xMethod.doDelete(DBSFile.getPathNormalized(wURLPath, pClient), DBSRestReturn.class, DadosRecordCount.class);
//			if (DBSObject.isNull(xRetorno) || DBSObject.isNull(xRetorno.getData())) {
//				prAddMessage(AccessMessages.ClientNaoEncontrado);
//			} else {
//				xRecordCount = xRetorno.getData();
//			}
//		} catch (AuthException e) {
//			prAddMessage(AccessMessages.NaoAutorizado);
//		} catch (IOException e) {
//			prAddMessage(AccessMessages.ClientDeleteErro);
//		}
//		return xRecordCount;
//	}
	
//	/**
//	 * TODO IMPLEMENTAR 
//	 * @param pUsername
//	 * @return
//	 */
//	public IAuthClient codeReadFromUsername(String pUsername) {
//		IAuthClient xClient = new DadosAuthClient();
//		
//		xClient.setClient("$2b$04$nyeM6Zwi9hpWmIylLTZnAe0fgihIYJIH2vRaScP5nyW6rX4sWDI4O");
//		xClient.setExpiration(DBSDate.getNowDate(true));
//		return xClient;
//	}
}
