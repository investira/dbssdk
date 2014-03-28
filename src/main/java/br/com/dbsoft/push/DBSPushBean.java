package br.com.dbsoft.push;


import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PreDestroy;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSSession;

/**
 * Bean que armazenará os requests recebidos pela DBSPushServlet e os utilizará para enviar o response SSE.
 * Deverá ser definido com applicationScope
 *
 */
public abstract class DBSPushBean  {

	protected static Logger wLogger = Logger.getLogger(DBSPushBean.class);

	protected Queue<AsyncContext> wOnGoingRequests = new ConcurrentLinkedQueue<>();
	
	public static String UPDATE_EVENT = "update";
	

	@PreDestroy
	@Override
	protected void finalize() throws Throwable {
//		Iterator<AsyncContext> xIterator = wOnGoingRequests.iterator(); 
//
//		while (xIterator.hasNext()) {
//			AsyncContext xAc = xIterator.next();
//			try{
//				xAc.complete(); 
//			}catch(Exception ignore){}
//		}
		System.out.println("DBSPUSHBEAN FINALIZE");
		wOnGoingRequests.clear();
		super.finalize();
	}
	
	public Queue<AsyncContext> getOnGoingRequests() {
		return wOnGoingRequests;
	}
	
	/**
	 * Evia mensagens para todas os clientes conectados.
	 * @param pEventName Evento que se deseja disparar no cliente ou nulo.
	 * @param pMessageText Texto a ser enviado
	 * @param pListUserIds Lista dos ids dos usuário que receberão ou não (dependendo do parametro <b>pBlackList</b>) a mensagem ou nulo se a mensagem for para todos 
	 * @param pBlackList Se a lista é dos usuarios que receberão a mensagem(false) ou os que serão ignorados(true)
	 */
	public void broadcastMessage(String pMessageText, String pEventName, List<String> pListUserIds, Boolean pBlackList, String pCurrentSessionId){
		Iterator<AsyncContext> xIterator = wOnGoingRequests.iterator(); 
		AsyncContext xAc = null;
		try{
			boolean xOk;
			while (xIterator.hasNext()) {
				xAc = xIterator.next();
				HttpServletResponse xRes = (HttpServletResponse) xAc.getResponse();
				HttpServletRequest xReq = (HttpServletRequest) xAc.getRequest();
				xOk = true;
				//Verifica se o usuário vinculado ao response está na lista dos usuários que receberão a mensagem
				if (pListUserIds!=null && 
					pListUserIds.size() > 0){
					String xResponseUserId = DBSObject.getNotNull(xAc.getRequest().getAttribute(DBSPushServlet.USER_ID),"").toString();
					//Se usuário da resposta estiver na lista negra, não envia.
					if (pListUserIds.contains(xResponseUserId)){
						if (pBlackList) {
							xOk = false;
						}
					}else{//Se usuário da resposta estiver na lista branca, não envia.
						if (!pBlackList) {
							xOk = false;
						}
					}
				}
				
				//Ignora mensagem para a mesma sessão caso tenha sido informada 
				if (xReq.getRequestedSessionId() != null &&
					pCurrentSessionId != null &&
					!xReq.getRequestedSessionId().equals(pCurrentSessionId)){
					xOk = false;
				}
				
				if (xOk){
					//Efetua o response e exclui da lista em caso de erro
					if (!DBSSession.writeServletEventSourceResponse(xRes, pMessageText, pEventName)){
						xAc.complete();
						xIterator.remove();
					}
				}
			}
		}catch(Exception e){
			//Exclui da lista em caso de erro
			if (xAc !=null){
				xAc.complete();
			}
			xIterator.remove();
			wLogger.error(e);
		}
	}
	
	/**
	 * Envia mensagem, vinculada ao um nome de evento para todos os usuários 
	 * @param pMessageText
	 * @param pEventName
	 */
	public void broadcastMessage(String pMessageText, String pEventName){
		broadcastMessage(pMessageText, pEventName, null, false, null);
	}
	
	/**
	 * Envia mensagem para todos os usuários
	 * @param pMessageText
	 * @param pEventName
	 */
	public void broadcastMessage(String pMessageText){
		broadcastMessage(pMessageText, null, null, false, null);
	}
	
	/**
	 * Envia evento update dos componentes para os usuários
	 * @param pComponentClientIds ids dos componentes que sofrerão update. os ids devem estar separados por espaço
	 */
	public void broadcastUpdate(String pComponentClientIds){
		broadcastMessage(pComponentClientIds, UPDATE_EVENT, null, false, null);
	}
	
	/**
	 * Envia evento update dos componentes para os usuários informados
	 * @param pComponentClientIds ids dos componentes que sofrerão update. os ids devem estar separados por espaço
	 * @param pListUserIds Lista dos ids dos usuários que receberão o update
	 */
	public void broadcastUpdate(String pComponentClientIds, List<String> pListUserIds){
		broadcastMessage(pComponentClientIds, UPDATE_EVENT, pListUserIds, false, null);
	}	

	/**
	 * Envia evento update dos componentes para os usuários informados
	 * @param pComponentClientIds ids dos componentes que sofrerão update. os ids devem estar separados por espaço
	 * @param pListUserIds Lista dos ids dos usuários que receberão ou não(dependendo do parametro <b>pBlackList</b>) o update
	 * @param pBlackList Se a lista é dos usuarios que receberão a mensagem(false) ou os que serão ignorados(true)
	 */
	public void broadcastUpdate(String pComponentClientIds, List<String> pListUserIds, Boolean pBlackList){
		broadcastMessage(pComponentClientIds, UPDATE_EVENT, pListUserIds, pBlackList, null);
	}
	
	/**
	 * Envia evento update dos componentes da sessão informada
	 * @param pComponentClientIds
	 * @param pCurrentSessionId
	 */
	public void broadcastUpdate(String pComponentClientIds, String pCurrentSessionId){
		broadcastMessage(pComponentClientIds, UPDATE_EVENT, null, false, pCurrentSessionId);
	}

	
//	public Boolean isNewSession(HttpServletRequest pReq){
//		Iterator<AsyncContext> xIterator = wOnGoingRequests.iterator(); 
//		while (xIterator.hasNext()) {
//			AsyncContext xAc = xIterator.next();
//			HttpServletRequest xReq = (HttpServletRequest) xAc.getRequest();
////			HttpServletResponse xRes = (HttpServletResponse) xAc.getResponse();
//			if (xReq.getRequestedSessionId().equals(pReq.getRequestedSessionId())){
//				if (xReq.getRequestURI().equals(pReq.getRequestURI())){
////					try {
////						DBSSession.writeServletEventSourceResponse(xAc.getResponse(), "x",null);
////						xAc.complete();
//						getOnGoingRequests().remove(xAc);
//						return false;
////					} catch (IOException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////						return false;
////					}
//				}
//			}
//		}
//		return true;
//	}

}
