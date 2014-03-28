package br.com.dbsoft.core;

import java.util.Hashtable;

//import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class DBSServiceLocator {

	private Hashtable<String, String> env = new Hashtable<String, String>();
	private InitialContext wsIC;
 
	public DBSServiceLocator() throws NamingException{
//		JBoss-----------------------
//		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
//		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		wsIC = new InitialContext(env);
	}
	
	public InitialContext getIC(){
		return wsIC;
	}
}
