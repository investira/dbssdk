package br.com.dbsoft.conversation;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.context.SessionScoped;

/**
 * Para resolver o problema org.jboss.weld.context.BusyConversationException: WELD-000322: Conversation lock timed out
 */
@SessionScoped
public class DBSConversationBean implements Serializable {

	private static final long serialVersionUID = 2626024860861390072L;

	private Map<String, Lock> conversationLocks = new ConcurrentHashMap<>();

	public Lock get(String cid) {
		if (conversationLocks.containsKey(cid)) {
			return conversationLocks.get(cid);
		} else {
			return initConversationLock(cid);
		}
	}

	private synchronized Lock initConversationLock(String key) {
		if (conversationLocks.containsKey(key)) {
			return conversationLocks.get(key);
		} else {
			ReentrantLock lock = new ReentrantLock();
			conversationLocks.put(key, lock);
			return lock;
		}
	}
}