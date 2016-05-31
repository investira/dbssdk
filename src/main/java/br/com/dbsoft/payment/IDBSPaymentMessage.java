package br.com.dbsoft.payment;

import java.io.Serializable;

import br.com.dbsoft.enums.DBSSDKEnums.PAYMENT_FROM;
import br.com.dbsoft.enums.DBSSDKEnums.PAYMENT_STATUS;

public interface IDBSPaymentMessage extends Serializable{

	public Integer getPaymentMessageId();
	public void setPaymentMessageId(Integer pPaymentMessageId);
	
	public String getPayerId();
	public void setPayerId(String pPayerId);
	
	public Integer getProductId();
	public void setProductId(Integer pProductId);
	
	public PAYMENT_FROM getPaymentFrom();
	public void setPaymentFrom(PAYMENT_FROM pPaymentFrom);
	
	public String getTransactionId();
	public void setTransactionId(String pTransactionId);
	
	public Integer getTransactionType();
	public void setTransactionType(Integer pTransactionType);
	
	public PAYMENT_STATUS getPaymentStatus();
	public void setPaymentStatus(PAYMENT_STATUS pPaymentStatus);
	
	public String getPendingReason();
	public void setPendingReason(String pPendingReason);
	
	public String getCustom();
	public void setCustom(String pCustom);
	
	public String getMessage();
	public void setMessage(String pMessage);
	
	public String getHash();
	public void setHash(String pHash);
	
	
}
