package br.com.dbsoft.core;

import javax.servlet.ServletContextEvent;

public interface IDBSSDKInitializer {

	/**
	 * Método chamado antes do deploy efetivo. Não havendo serviços ativos ainda.<br/>
	 * Caso queira efetuar chamadas http para a própria aplicação, por exemplo, utilize o <b>afterStart</b>.
	 * @return
	 */
	public abstract boolean beforeStart(ServletContextEvent pSCE);
	
	/**
	 * Método chamado após o deploy.<br/>
	 * A chamada a este método é efetuada com um delay mínimo, conforme definido no atributo.<br/> 
	 * Este delay é necesário para aguardar o deploy total da aplicação.
	 *
	 */
	public abstract void afterStart();
	
	
	/**
	 * Disparado quando no undeploy da aplicação.<br/>
	 * Não é disparado caso o servidor de aplicação se ja interrompido. 
	 */
	public abstract void afterStop();

	
	public void onError();
	
	public void fireStarted();
	
}
