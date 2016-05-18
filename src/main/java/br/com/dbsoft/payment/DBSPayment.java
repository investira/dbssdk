package br.com.dbsoft.payment;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.IDBSMessages;
import br.com.dbsoft.util.DBSDate.PERIODICIDADE;
import br.com.dbsoft.util.DBSObject;

@SuppressWarnings({"rawtypes","unchecked"})
public abstract class DBSPayment implements IDBSPayment {
	
	protected Logger wLogger = Logger.getLogger(this.getClass());
	
	//GETERS AND SETTERS ====================================================================================
	private boolean 		wOk = true;
	private String			wItemName;
	private Double			wValue;
	private Integer			wQuantity;
	private String			wSucessURL;
	private String			wCancelURL;
	private PERIODICIDADE 	wPeriod; 
	private Integer 		wFrequency;
	private boolean 		wInitialPayment = true;
	private String			wProfileId;
	private IDBSMessages 	wMessages;
	
	@Override
	public IDBSMessages getMessages() {
		return wMessages;
	}
	
	@Override
	public boolean isOk() {
		return wOk;
	}
	@Override
	public void setOk(boolean pOk) {
		wOk = pOk;
	}
	
	@Override
	public String getItemName() {
		return wItemName;
	}
	@Override
	public void setItemName(String pItemName) {
		wItemName = pItemName;
	}

	@Override
	public Double getValue() {
		return wValue;
	}
	@Override
	public void setValue(Double pValue) {
		wValue = pValue;
	}

	@Override
	public Integer getQuantity() {
		return wQuantity;
	}
	@Override
	public void setQuantity(Integer pQuantity) {
		wQuantity = pQuantity;
	}

	@Override
	public String getSucessURL() {
		return wSucessURL;
	}
	@Override
	public void setSucessURL(String pSucessURL) {
		wSucessURL = pSucessURL;
	}

	@Override
	public String getCancelURL() {
		return wCancelURL;
	}
	@Override
	public void setCancelURL(String pCancelURL) {
		wCancelURL = pCancelURL;
	}

	@Override
	public Integer getFrequency() {
		return wFrequency;
	}
	@Override
	public void setFrequency(Integer pFrequency) {
		wFrequency = pFrequency;
	}
	
	@Override
	public PERIODICIDADE getPeriod() {
		return wPeriod;
	}
	@Override
	public void setPeriod(PERIODICIDADE pPeriod) {
		wPeriod = pPeriod;
	}
	
	@Override
	public boolean getInitialPayment() {
		return wInitialPayment;
	}
	@Override
	public void setInitialPayment(boolean pInitialPayment) {
		wInitialPayment = pInitialPayment;
	}

	@Override
	public String getProfileId() {
		return wProfileId;
	}
	@Override
	public void setProfileId(String pProfileId) {
		wProfileId = pProfileId;
	}

	//EVENTOS ===============================================================================================
	@Override
	public String preparePayment() throws DBSIOException {
		if (!minimumValidate()) {
			setOk(false);
			return null;
		}
		onPrepare();
		return onRedirect();
	}

	@Override
	public boolean pay() throws DBSIOException {
		if (!pvIsRecurring() || wInitialPayment) {
			wOk = onPay();
		}
		if (wOk && pvIsRecurring()){
			wProfileId = onSchedule();
			if (DBSObject.isEmpty(wProfileId)) {
				setOk(false);
			}
		}
		return wOk;
	}

	@Override
	public PROFILE_STATUS getProfileStatus() throws DBSIOException {
		return onReadProfileStatus();
	}
	
	//Abstract methods=============================================================
	/**
	 * Neste método deve-se implementar a preparação do pagamento junto à operadora. 
	 * @throws DBSIOException
	 */
	public abstract void onPrepare() throws DBSIOException;
	
	/**
	 * Neste método deve-se implementar o retorno da URL para o site de autorização de pagamento.
	 * @return URL para a página de autorização de pagamento do Site da Operadora
	 * @throws DBSIOException
	 */
	public abstract String onRedirect() throws DBSIOException;
	
	/**
	 * Neste método deve-se implementar a operação de lançamento de pagamento em si.
	 * @return Retorna o boolean se a operação ocorreu corretamente ou não.
	 * @throws DBSIOException
	 */
	public abstract boolean onPay() throws DBSIOException;
	
	/**
	 * Neste método deve-se implementar a operação de agendamento/assinatura de pagamento recorrente.
	 * @return Retorna uma String com o ProfileId
	 * @throws DBSIOException
	 */
	public abstract String onSchedule() throws DBSIOException;
	
	/**
	 * Neste método deve-se implementar a operação de leitura do status atual do pagamento recorrente junto a operadora de pagamentos.
	 * @return PROFILE_STATUS Status atual do Profile.
	 * @throws DBSIOException
	 */
	public abstract PROFILE_STATUS onReadProfileStatus() throws DBSIOException;
	
	//=========================================METODOS PRIVADOS=======================================≠≠
	/**
	 * Verifica se os campos obrigatórios foram preenchidos
	 * @return
	 */
	private boolean minimumValidate(){
		boolean xOk = true;
		if (DBSObject.isEmpty(wItemName)) {
			xOk = false;
			wMessages.add(MsgErroValidateItemEmpty);
		}
		if (!DBSObject.isIdValid(wQuantity)) {
			xOk = false;
			wMessages.add(MsgErroValidateQuantityInvalid);
		}
		if (DBSObject.isNull(wValue) || wValue <= 0D) {
			xOk = false;
			wMessages.add(MsgErroValidateValueInvalid);
		}
		if (DBSObject.isEmpty(wSucessURL)) {
			xOk = false;
			wMessages.add(MsgErroValidateSucessURLEmpty);
		}
		if (DBSObject.isEmpty(wCancelURL)) {
			xOk = false;
			wMessages.add(MsgErroValidateCancelURLEmpty);
		}
		return xOk;
	}

	/**
	 * Verifica se o pagamento é do tipo recorrente.
	 * @return
	 */
	protected boolean pvIsRecurring() {
		return !DBSObject.isNull(wPeriod) && DBSObject.isIdValid(wFrequency);
	}
}
