package br.com.dbsoft.conversation;

import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import br.com.dbsoft.util.DBSObject;

/**
 * Para resolver o problema org.jboss.weld.context.BusyConversationException: WELD-000322: Conversation lock timed out
 */
@WebListener
public class DBSConversationWebListener implements ServletRequestListener, Serializable {

	private static final long serialVersionUID = 4446201770218552841L;

	@Inject
	private DBSConversationBean conversationLocks;

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
	    if (hasConversationContext(event)) {
	      conversationLocks.get(getConversationId(event)).unlock();
	    }
	  }
	 
	  @Override
	public void requestInitialized(ServletRequestEvent event) {
	    if (hasConversationContext(event)) {
	      obtainConvesationLock(event);
	    }
	  }
	 
	  private void obtainConvesationLock(ServletRequestEvent event) {
	    conversationLocks.get(getConversationId(event)).lock();
	  }
	 
	  private boolean hasConversationContext(ServletRequestEvent event) {
	    return !DBSObject.isEmpty(getConversationId(event));
	  }
	 
	  private String getConversationId(ServletRequestEvent event) {
	    return event.getServletRequest().getParameter("cid");
	  }
}
