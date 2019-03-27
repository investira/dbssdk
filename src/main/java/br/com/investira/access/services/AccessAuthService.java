package br.com.investira.access.services;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.error.exception.AuthException;
import br.com.dbsoft.http.DBSHttpMethodPost;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.rest.DBSRestReturn;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSEmail;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;
import br.com.investira.access.AccessMessages;
import br.com.investira.access.dados.DadosAuthToken;
import br.com.investira.access.interfaces.IAuthToken;

@SuppressWarnings("unchecked")
public class AccessAuthService extends AbstractService {
	
	private String wURLPath;
//	private String wURLPathGoogle;
	
	//CONSTRUTORES ============================================================================================================
	public AccessAuthService(String pClientToken, String pURL) {
		wClientToken = pClientToken;
		wURLPath = DBSFile.getPathNormalized(DBSFile.getPathNormalized(pURL, PATH_V1), "/auth");
//		wURLPathGoogle = DBSFile.getPathNormalized(pIfeedURL, "/api/auth/google");
	}

	//MÉTODOS PÚBLICOS ========================================================================================================
	/**
	 * Solicita um AcessToken de Cliente
	 * @return
	 */
	public IAuthToken getAccessTokenClient(String pClientId, String pClientSecret) {
		IAuthToken 				xToken = null;
		DBSHttpMethodPost 			xMethod = new DBSHttpMethodPost();
		Map<String, String> 		xParams = new HashMap<String, String>();
		DBSRestReturn<IAuthToken> 	xRetorno = null;
		
		xParams.put("grant_type", "client_credentials");
		xParams.put("client_id", pClientId);
		xParams.put("client_secret", pClientSecret);
		try {
			xRetorno = xMethod.doPost(wURLPath, xParams, DBSRestReturn.class, DadosAuthToken.class);
			xToken = xRetorno.getData();
			if (DBSObject.isNull(xToken) || DBSObject.isEmpty(xToken.getAccessToken())) {
				prAddMessage(AccessMessages.ErroGerandoTokenClient);
			}
		} catch (AuthException e) {
			prAddMessage(e);
		} catch (IOException e) {
			prAddMessage(AccessMessages.ErroGerandoTokenClient);
		}
		return xToken;
	}
	
	public IAuthToken getAccessTokenUser(String pUsername, String pPassword) throws DBSIOException {
		Map<String, String> xParams = new HashMap<String, String>();
		IAuthToken 		xToken = null;
		
		if (!pvValidateLogin(pUsername, pPassword)) {
			return null;
		}
		
		xParams.put("grant_type", 		"password");
		xParams.put("username", 		pUsername);
		xParams.put("password", 		pPassword);
		try {
			xToken = pvGetUserToken(xParams);
		} catch (DBSIOException xException) {
			if (DBSObject.isEmpty(getMessages().getListMessageBase())) {
				pvGenericError(xException, AccessMessages.ErroGenerico);
			}
			throw new DBSIOException(getMessages().getCurrentMessage());
		}
		return xToken;
	}

	/**
	 * Atualiza o AccessToken através de um RefreshToken
	 * @param pRefreshToken
	 * @return
	 * @throws DBSIOException 
	 */
	public IAuthToken refreshToken(String pRefreshToken) throws DBSIOException {
		String xRefreshToken = pvNormalizeToken(pRefreshToken);
		if (!prValidateStringParam(xRefreshToken)) {
			return null;
		}
		IAuthToken 				xToken = null;
		Map<String, String> 		xParams = new HashMap<String, String>();
		
		xParams.put("grant_type", 		"refresh_token");
		xParams.put("refresh_token", 	xRefreshToken);
		try {
			xToken = pvGetUserToken(xParams);
		} catch (DBSIOException xException) {
			pvGenericError(xException, AccessMessages.ErroGenerico);
			throw new DBSIOException(getMessages().getCurrentMessage());
		}
		return xToken;
	}
	
	//User Token Google
	
	//Authorization Code
	
//	/**
//	 * Verifica se o Token é válido
//	 * @param pToken
//	 * @return
//	 */
//	public boolean validateUserToken(String pToken) {
//		boolean 	xOK = false;
//		IUserInfo xUserInfo = getUserInfo(pToken);
//		if (DBSObject.isNull(xUserInfo) || !DBSObject.isIdValid(xUserInfo.getUserId())) {
//			prAddMessage(DBSAuthMessages.TokenInvalido);
//		} else {
//			xOK = true;
//		}
//		return xOK;
//	}
	
	//MÉTODOS PRIVADOS ==========================================================
	private IAuthToken pvGetUserToken(Map<String, String> pParams) throws DBSIOException {
		IAuthToken 					xToken = null;
		DBSHttpMethodPost 			xMethod;
		DBSRestReturn<IAuthToken> 	xRetorno = null;
		
		try {
			xMethod = new DBSHttpMethodPost(wClientToken);
			xRetorno = xMethod.doPost(wURLPath, pParams, DBSRestReturn.class, DadosAuthToken.class);
			xToken = xRetorno.getData();
			if (DBSObject.isNull(xToken) || DBSObject.isEmpty(xToken.getAccessToken())) {
				prAddMessage(AccessMessages.UsuarioUsernameOuSenhaInvalida);
			}
		} catch (AuthException e) {
			prAddMessage(e);
		} catch (IOException e) {
			wLogger.error(e);
			if (e.getLocalizedMessage().contains("Bloqueado")) {
				String xMenssagem = e.getMessage();
				xMenssagem = DBSString.getSubString(xMenssagem, DBSString.getInStr(xMenssagem, "\"error_message\": \""), e.getMessage().length());
				xMenssagem = DBSString.changeStr(xMenssagem, "\"error_message\": \"", "");
				xMenssagem = DBSString.changeStr(xMenssagem, "\"}", "");
				xMenssagem = xMenssagem.trim();
				Date xData = DBSDate.toDateCustom(DBSString.changeStr(xMenssagem, "Usuário Bloqueado até ", ""), "yyyy-MM-dd HH:mm:ss.SSS");
				xMenssagem = "Usuário bloqueado até às "+ DBSFormat.getFormattedTime(xData) +" do dia "+ DBSFormat.getFormattedDate(xData);
				IDBSMessage xMessage = AccessMessages.UsuarioBloqueadoAte;
				xMessage.setMessageText(xMenssagem);
				prAddMessage(xMessage);
			} else if (e.getLocalizedMessage().contains("offline") || e.getLocalizedMessage().contains("Serviço indisponível")) {
				prAddMessage(AccessMessages.ServicoIndisponivel);
			} else {
				prAddMessage(AccessMessages.UsuarioUsernameOuSenhaInvalida);
			}
			throw new DBSIOException(getMessages().getCurrentMessage());
		}
		return xToken;
	}
	
	/**
	 * Validação do Login
	 * @param pLogin
	 * @return
	 */
	private boolean pvValidateLogin(String pUsername, String pPassword) {
		if (prValidateUsername(pUsername) 
		 && prValidateStringParam(pPassword)) {
			return true;
		}
		return false;
	}
	
	private void pvGenericError(DBSIOException pException, IDBSMessage pMessage) {
		wLogger.error(pException);
		pvAddMessage(pMessage);
	}
	
	/**
	 * Adiciona uma mensagem e configura o HTTP StatusCode para a resposta.
	 * @param pMessage
	 */
	private void pvAddMessage(IDBSMessage pMessage) {
		if (DBSObject.isNull(pMessage)) {
			prAddMessage(AccessMessages.ErroGenerico);
		} else {
			prAddMessage(pMessage);
		}
		setStatus(getMessages().getListMessageBase().get(0).getStatusCode());
	}
	
	private static String pvNormalizeToken(String pToken) {
		if (DBSObject.isEmpty(pToken)) {
			return "";
		}
		String xToken = pToken;
		xToken = DBSString.changeStr(xToken, "Bearer ", "");
		xToken = DBSString.changeStr(xToken, "BEARER ", "");
		xToken = DBSString.changeStr(xToken, "bearer ", "");
		return xToken;
	}
	
	//Validações-----------------
	/**
	 * Validação do Username
	 * @param pUsername
	 * @return
	 */
	private boolean prValidateUsername(String pUsername) {
		if (DBSObject.isEmpty(pUsername)) {
			prAddMessage(AccessMessages.ParametrosInvalidos);
			setStatus(AccessMessages.ParametrosInvalidos.getStatusCode());
			return false;
		}
		//E-mail Valido
		if (!DBSEmail.isValidEmailAddress(DBSString.toString(pUsername))) {
			prAddMessage(AccessMessages.EmailInvalido);
			setStatus(AccessMessages.EmailInvalido.getStatusCode());
			return false;
		}
		return true;
	}

	/**
	 * Validação se o parametro String não está vazio ou nulo
	 * @param pStringParam
	 * @return
	 */
	private boolean prValidateStringParam(String pStringParam) {
		if (DBSObject.isEmpty(pStringParam)) {
			prAddMessage(AccessMessages.ParametrosInvalidos);
			return false;
		}
		return true;
	}
}
