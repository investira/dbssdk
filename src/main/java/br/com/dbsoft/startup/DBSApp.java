package br.com.dbsoft.startup;

import java.net.URL;


/**
 * @author ricardo.villar
 * Classe que contém informações da aplicação.
 * Esta classe é preenchida automaticamente pela classe DBDAppStartup.<br/>
 * Seus valores não deve ser alterados manualmente;
 */
public class DBSApp {

	public enum PROJECT_STAGE{
		DEVELOPMENT,
		PRODUCTION;
	}

	/**
	 * Nome da aplicação
	 */
	public static String AppName = null;
	
	/**
	 * Versão da aplicação
	 */
	public static String AppVersion = null;
	
	/**
	 * Caminho raiz da aplicação
	 */
	public static String ContextPath = null;
	
	/**
	 * Retorna estágio do projeto conforme definição do web.xml.
	 * @return
	 */
	public static PROJECT_STAGE ProjectStage;
	
	/**
	 * URL do servidor HTTP
	 * @return
	 */
	public static URL URLHttp = null;
	
	/**
	 * URL do servidor HTTPs
	 * @return
	 */
	public static URL URLHttps = null;
	
	public static String getAppDescription(){
		StringBuilder xSB = new StringBuilder();

		if (AppName!=null){
			xSB.append(AppName);
		}
		if (AppVersion!=null){
			xSB.append("-");
			xSB.append(AppVersion);
		}
		if (ProjectStage!=null){
			xSB.append("[");
			xSB.append(ProjectStage.toString());
			xSB.append("]");
		}
		if (URLHttp!=null){
			xSB.append("[");
			xSB.append(URLHttp.toString());
			xSB.append("]");
		}
		if (URLHttps!=null){
			xSB.append("[");
			xSB.append(URLHttps.toString());
			xSB.append("]");
		}
		return xSB.toString();
	}
}
