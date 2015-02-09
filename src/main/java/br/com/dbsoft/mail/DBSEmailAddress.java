package br.com.dbsoft.mail;

public class DBSEmailAddress {
	private String wAddress;
	private String wName;

	public DBSEmailAddress(){}
	
	public DBSEmailAddress(String pAddress){
		wAddress = pAddress;
		wName = pAddress;
	}
		
	public DBSEmailAddress(String pAddress, String pName){
		wAddress = pAddress;
		wName = pName;
	}

	public String getAddress() {return wAddress;}
	public void setAddress(String pAddress) {wAddress = pAddress;}

	public String getName() {return wName;}
	public void setName(String pName) {wName = pName;}

}
