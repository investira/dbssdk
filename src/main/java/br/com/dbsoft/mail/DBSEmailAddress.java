package br.com.dbsoft.mail;

/**
 * @author ricardo.villar
 *
 */
public class DBSEmailAddress {
	private String wAddress;
	private String wName;

	public DBSEmailAddress(){}
	
	/**
	 * @param pAddress ex:joão@dominio.com.br
	 */
	public DBSEmailAddress(String pAddress){
		wAddress = pAddress;
		wName = pAddress;
	}
		
	/**
	 * @param pAddress ex:joão@dominio.com.br
	 * @param pName Nome a que se refere o e-mail. ex:João da Silva
	 */
	public DBSEmailAddress(String pAddress, String pName){
		wAddress = pAddress;
		wName = pName;
	}

	/**
	 * E-mail. ex:joão@dominio.com.br
	 * @return
	 */
	public String getAddress() {return wAddress;}
	/**
	 * @return E-mail. ex:joão@dominio.com.br
	 */
	public void setAddress(String pAddress) {wAddress = pAddress;}

	/**
	 * Nome a que se refere o e-mail. ex:João da Silva
	 * @return
	 */
	public String getName() {return wName;}
	/**
	 * @param Nome a que se refere o e-mail. ex:João da Silva
	 */
	public void setName(String pName) {wName = pName;}

}
