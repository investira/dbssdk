package br.com.dbsoft.core;

import java.util.ArrayList;

import br.com.dbsoft.core.DBSSDK.UI.ID_PREFIX;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

public class DBSSeg {
	
	/**
	 * Retorna a chave pai.</br>
	 * Se não houver, retorna vázio("").
	 * @param pKey
	 * @return
	 */
	public static String getKeyParent(String pKey){
		int xI = pKey.lastIndexOf(":");
		if (xI != -1){
			return pKey.substring(0, xI);
		}
		return "";
	}
	
	/**
	 * Retorna chave a partir da id do componente em JSF.<br/>
	 * A chave é composta por <b>SISTEMA:OBJETO</b>. Onde o <b>OBJETO</b> conterá o ID completo, podendo ficar como <b>SISTEMA:OBJETO_AVO:OBJETO_PAI:OBJETO_FILHO</b><br/>
	 * @param pId ClientId completo
	 * @param pSistemaPosition Qual item diz respeito ao nome do sistema.<br/>
	 * 		  No exemplo onde <b>pId</b> seja <b>ATV:OBJETO_AVO:OBJETO_PAI:OBJETO_FILHO</b> e <b>pSistemaPosition</b> seja 1, será retornado <b>ATV</b>.  
	 * @param pObjetoPosition A partir de qual item diz respeito a chave do Objeto que será controlado.<br/>
	 * 		  No exemplo onde <b>pId</b> seja <b>ATV:OBJETO_AVO:OBJETO_PAI:OBJETO_FILHO</b> e <b>pObjetoPosition</b> seja 3, será retornado <b>OBJETO_PAI:OBJETO_FILHO</b>.  
	 * @param pSistemaPrefixoLixo Prefixo que será excluído da chave parte que contém o sistema, caso seja informado
	 * @return
	 */
	public static String getKeyFromComponentId(String pId, int pSistemaPosition, int pObjetoPosition){
		String xSistema = "";
		String xObjeto = "";
		String xId;
		if (DBSObject.isEmpty(pId)){
			return "";
		}
		if (pSistemaPosition < 1
		 || pSistemaPosition >= pObjetoPosition){
			return "";
		}
		
		xId = pId.toUpperCase().trim();

		ArrayList<String> xItens;
		xItens = DBSString.toArrayList(xId, ":");

		//Lê a chave do sistema
		if (xItens.size() >= pSistemaPosition){
			xSistema = xItens.get(pSistemaPosition-1);
			if (xSistema.toLowerCase().startsWith(ID_PREFIX.APPLICATION.getName())){
				xSistema = xSistema.substring(ID_PREFIX.APPLICATION.getName().length());
			}
		}
		
		//Lê a chave do objeto
		if (xItens.size() >= pObjetoPosition){
			for (int xI = pObjetoPosition; xI <= xItens.size(); xI++){
				if (!xObjeto.equals("")){
					xObjeto += ":";
				}
				xObjeto += xItens.get(xI-1);
			}
		}
		
		if (xSistema.equals("")){
			return "";
		}else if (xObjeto.equals("")){
			return xSistema;
		}else{	
			return xSistema + ":" + xObjeto;
		}
	}
	
	/**
	 * Retorna o <b>SISTEMA</b> a partir da chave informada.<br/>
	 * A chave é composta por <b>SISTEMA:OBJETO<b/><br/>
	 * ex:ATV:MNATIVO
	 * 
	 * @param pAcesso
	 * @return
	 * @return
	 */
	public static String getSistemaFromKey(String pKey){
		String xSistema = "";
		if (DBSObject.isEmpty(pKey)){
			return "";
		}
		int xI = 0;
		pKey = pKey.trim().toUpperCase();
		//Recupera somente a parte inicial da chave
		xI = pKey.indexOf(":");
		if (xI != -1){
			xSistema =  DBSString.getSubString(pKey, 1, xI);
		}else{
			xSistema = pKey;
		}
		//Retida o prefixo padrão de aplicação, caso exista
		xI = xSistema.toLowerCase().indexOf(ID_PREFIX.APPLICATION.getName());
		if (xI != -1){
			xSistema =  xSistema.substring(ID_PREFIX.APPLICATION.getName().length());
		}
		
		return xSistema;
	}

	/**
	 * Retorna o <b>OBJETO</b> a partir da chave informada.<br/>
	 * A chave é composta por <b>SISTEMA:OBJETO<b/><br/>
	 * ex:ATV:MNATIVO
	 * 
	 * @param pAcesso
	 * @return
	 */
	public static String getObjetoFromKey(String pKey){
		if (DBSObject.isEmpty(pKey)){
			return "";
		}
		int xI = 0;
		pKey = pKey.trim().toUpperCase();
		//Recupera somente a parte final da chave
		xI = pKey.indexOf(":");
		if (xI != -1){
			return DBSString.getSubString(pKey, xI+2, pKey.length());
		}else{
			return "";
		}
	}
	
	
	/**
	 * Retorna a chave a partir das informações dos dados do dadamodel GrupoAcessoModel/AC_ACESSO.<br/>
	 * A chave é composta por <b>SISTEMA:OBJETO<b/><br/>
	 * ex:ATV:MNATIVO
	 * 
	 * @param pAcesso
	 * @return
	 */
//	public static String getKeyFromAcessoModel(GrupoAcessoModel pAcesso){
//		if (pAcesso ==null){
//			return "";
//		}
//		
//		String xSistemaId = getSistemaFromKey(pAcesso.getSistema_Id());
//
//		if (pAcesso.getObjeto() != null){
//			String xKeySuf = pAcesso.getObjeto().toUpperCase();
//			return xSistemaId + ":" + xKeySuf;
//		}else{
//			return xSistemaId;
//		}
//	}
}
