/**
 * 
 */
package br.com.dbsoft.log;


/**
 * @author Avila
 *
 */
public class DBSLogModel {

	private String wData;
	private String wNivel;
	private String wClasse;
	private String wMetodo;
	private String wMensagem;
	
	public String getData() {
		return wData;
	}
	public void setData(String pData) {
		wData = pData;
	}
	public String getNivel() {
		return wNivel;
	}
	public void setNivel(String pNivel) {
		wNivel = pNivel;
	}
	public String getClasse() {
		return wClasse;
	}
	public void setClasse(String pClasse) {
		wClasse = pClasse;
	}
	public String getMetodo() {
		return wMetodo;
	}
	public void setMetodo(String pMetodo) {
		wMetodo = pMetodo;
	}
	public String getMensagem() {
		return wMensagem;
	}
	public void setMensagem(String pMensagem) {
		wMensagem = pMensagem;
	}
	
	@Override
	public String toString() {
		return wData + " " + wNivel + " (" + wClasse + ") [Método: " + wMetodo + "] - " + wMensagem;
	}
}
