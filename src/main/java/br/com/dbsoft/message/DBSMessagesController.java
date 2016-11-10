package br.com.dbsoft.message;

import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 *Armazenar e controlar uma lista de mensagem(class DBSMessage)
 * @param <MessageClass> Classe de mensagem
 */
public class DBSMessagesController implements IDBSMessagesController {

	private static final long serialVersionUID = -1653488998078981370L;

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	
	private IDBSMessages				wMessages = new DBSMessages(); 
	private String 						wCurrentMessageKey;

	public DBSMessagesController() {
		//Listener das mensagens
		wMessages.addMessagesListener(this);
	}


	@Override
	public IDBSMessages getMessages() {
		return wMessages;
	}

	@Override
	public void afterMessageValidated(IDBSMessage pMessage) {
		pvRefreshMessages(pMessage);
	}


	@Override
	public void afterAddMessage(IDBSMessage pMessage) {
		//Listener da mensagem(individual)
		pMessage.addMessageListener(this);
		
		pvFindNextMessage();
	}


	@Override
	public void afterRemoveMessage(String pMessageKey) {
		pvFindNextMessage();
	}


	@Override
	public void afterClearMessages() {
		pvFindNextMessage();
	}


	@Override
	public IDBSMessage getCurrentMessage() {
		return wMessages.getMessage(wCurrentMessageKey);
	}

	//PRIVATE =======================================================================================
	private void pvRefreshMessages(IDBSMessage pMessage){
		//Se não for mensagem que não requer confirmação, automaticamente retira mensagem da fila.
		//Mensagens que requerem confirmação, precisam ser mantidar na fila para serem consultados porteriormente para saber qual foi a validação(false, true).
		if (!pMessage.getMessageType().getRequireConfirmation()){
			getMessages().remove(pMessage);
		}
	}

	/**
	 * Busca a próxima mensagem que não foi setada a validação.
	 */
	private void pvFindNextMessage(){
		wCurrentMessageKey = null;
		Iterator<IDBSMessage> xIterator = wMessages.iterator();
		while (xIterator.hasNext()){
			IDBSMessage xMessage = xIterator.next();
			if (xMessage.isMessageValidated() == null){
				wCurrentMessageKey = xMessage.getMessageKey();
				break;
			}
		}
	}


}
