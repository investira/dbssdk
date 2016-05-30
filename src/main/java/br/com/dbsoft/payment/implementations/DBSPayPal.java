package br.com.dbsoft.payment.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.payment.DBSPayment;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSDate.PERIODICIDADE;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSObject;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileReq;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileRequestType;
import urn.ebay.api.PayPalAPI.CreateRecurringPaymentsProfileResponseType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentReq;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentRequestType;
import urn.ebay.api.PayPalAPI.DoExpressCheckoutPaymentResponseType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsReq;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetExpressCheckoutDetailsResponseType;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsReq;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsRequestType;
import urn.ebay.api.PayPalAPI.GetRecurringPaymentsProfileDetailsResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutReq;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutRequestType;
import urn.ebay.api.PayPalAPI.SetExpressCheckoutResponseType;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.AckCodeType;
import urn.ebay.apis.eBLBaseComponents.BillingAgreementDetailsType;
import urn.ebay.apis.eBLBaseComponents.BillingCodeType;
import urn.ebay.apis.eBLBaseComponents.BillingPeriodDetailsType;
import urn.ebay.apis.eBLBaseComponents.BillingPeriodType;
import urn.ebay.apis.eBLBaseComponents.CreateRecurringPaymentsProfileRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.CreateRecurringPaymentsProfileResponseDetailsType;
import urn.ebay.apis.eBLBaseComponents.CurrencyCodeType;
import urn.ebay.apis.eBLBaseComponents.DoExpressCheckoutPaymentRequestDetailsType;
import urn.ebay.apis.eBLBaseComponents.ItemCategoryType;
import urn.ebay.apis.eBLBaseComponents.PaymentActionCodeType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsItemType;
import urn.ebay.apis.eBLBaseComponents.PaymentDetailsType;
import urn.ebay.apis.eBLBaseComponents.ProductCategoryType;
import urn.ebay.apis.eBLBaseComponents.RecurringPaymentsProfileDetailsType;
import urn.ebay.apis.eBLBaseComponents.ScheduleDetailsType;
import urn.ebay.apis.eBLBaseComponents.SetExpressCheckoutRequestDetailsType;

@SuppressWarnings("unchecked")
public class DBSPayPal extends DBSPayment{
	
	private static final String CHECKOUT_EXPRESS_TESTE = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token="; //URL PARA TESTES. TROCAR QUANDO FOR PRA PRODUÇÃO
	private static final String CHECKOUT_EXPRESS = "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&useraction=commit&token="; //URL PARA PRODUÇÃO
	
	private static final String VERSION = "124"; //"104.0";
	private String 				wToken = null;
	private String				wUrlIPN;
	private String				wPayerID;
	private String				wTransactionID;
	private PAYMENT_STATUS		wPaymentStatus;
	private Map<String, String> wConfigMap;
	
	/**
	 * URL do servidor de IPN (Instant Payment Notification).
	 * Utilizando o padrão null, o PayPal utilizará a URL de IPN geral configurada na conta do PayPal.
	 * Utilize esta propriedade apenas se quiser utilizar uma URL específica para uma transação.
	 * @return
	 */
	public String getUrlIPN() {
		return wUrlIPN;
	}
	public void setUrlIPN(String pUrlIPN) {
		wUrlIPN = pUrlIPN;
	}
	
	public Map<String, String> getConfigMap() {
		return wConfigMap;
	}
	public void setConfigMap(Map<String, String> pConfigMap) {
		wConfigMap = pConfigMap;
	}
	
	public String getPayerID() {
		return wPayerID;
	}
	public String getTransactionID() {
		return wTransactionID;
	}
	public PAYMENT_STATUS getPaymentStatus() {
		return wPaymentStatus;
	}
	//CONTRUTORES ====================================================================================================
	/**
	 * Construtor Padrão.
	 * As definições do item a ser paga deverão ser feitas através dos Setters.
	 */
	public DBSPayPal() {}

	/**
	 * Construtor Padrão com mapa de configurações.
	 * As definições do item a ser paga deverão ser feitas através dos Setters.
	 */
	public DBSPayPal(Map<String, String> pConfigMap) {
		setConfigMap(pConfigMap);
	}
	//Overrides =====================================================================================================================================
	
	@Override
	public void onPrepare() throws DBSIOException {
		SetExpressCheckoutRequestDetailsType 	xSetExpressCheckoutRequestDetails = new SetExpressCheckoutRequestDetailsType();
		SetExpressCheckoutRequestType 			xSetExpressCheckoutRequest;
		SetExpressCheckoutReq 					xSetExpressCheckoutReq = new SetExpressCheckoutReq();
		PayPalAPIInterfaceServiceService 		xService;
		SetExpressCheckoutResponseType 			xSetExpressCheckoutResponse = null;
		BillingAgreementDetailsType 			xBillingAgreement = new BillingAgreementDetailsType(BillingCodeType.RECURRINGPAYMENTS);
		List<BillingAgreementDetailsType> 		xBillList = new ArrayList<BillingAgreementDetailsType>();

		if (pvIsRecurring()) {
			xBillingAgreement.setBillingAgreementDescription(getItemName());
			xBillList.add(xBillingAgreement);
			xSetExpressCheckoutRequestDetails.setBillingAgreementDetails(xBillList);
		}
		
		xSetExpressCheckoutRequestDetails.setReturnURL(getSucessURL());
		xSetExpressCheckoutRequestDetails.setCancelURL(getCancelURL());
		xSetExpressCheckoutRequestDetails.setPaymentDetails(pvSetPaymentDetails(getItemName(), getValue(), getQuantity(), null));

		xSetExpressCheckoutRequest = new SetExpressCheckoutRequestType(xSetExpressCheckoutRequestDetails);
		xSetExpressCheckoutRequest.setVersion(VERSION);

		xSetExpressCheckoutReq.setSetExpressCheckoutRequest(xSetExpressCheckoutRequest);

		xService = new PayPalAPIInterfaceServiceService(getConfigMap());
		try {
			setOk(true);
			xSetExpressCheckoutResponse = xService.setExpressCheckout(xSetExpressCheckoutReq);
		} catch (SSLConfigurationException | InvalidCredentialException | HttpErrorException
				| InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
				| OAuthException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
			setOk(false);
			getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, e.getMessage()));
			wLogger.error(e);
			DBSIO.throwIOException(e.getMessage());
		}
		if (DBSObject.isNull(xSetExpressCheckoutResponse)) {
			getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, MsgErroSemResposta.getMessageText()));
			setOk(false);
			return;
		}
		if (!xSetExpressCheckoutResponse.getErrors().isEmpty()) {
			for (int xI = 0; xI < xSetExpressCheckoutResponse.getErrors().size(); xI++) {
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xSetExpressCheckoutResponse.getErrors().get(xI).getLongMessage()));
			}
			setOk(false);
			return;
		}
		wToken = xSetExpressCheckoutResponse.getToken();
	}

	@Override
	public String onRedirect() throws DBSIOException {
		if (getConfigMap().get("mode").equals("sandbox")) {
			return CHECKOUT_EXPRESS_TESTE + wToken;
		}
		return CHECKOUT_EXPRESS + wToken;
	}

	@Override
	public boolean onPay() throws DBSIOException {
		boolean xOk = false;
		GetExpressCheckoutDetailsResponseType 	xPaymentDetails;
		DoExpressCheckoutPaymentResponseType 	xExpressCheckout; 
		
		xPaymentDetails = pvGetPaymentDetails(wToken);
		wPayerID = xPaymentDetails.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();
		xExpressCheckout = pvDoExpressCheckout(wToken, wPayerID, getItemName(), getValue(), getQuantity(), getUrlIPN());
		
		if (!DBSObject.isEmpty(xExpressCheckout.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo())) {
			wTransactionID = xExpressCheckout.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().get(0).getTransactionID();
			wPaymentStatus = PAYMENT_STATUS.getFromPayPal(xExpressCheckout.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().get(0).getPaymentStatus());
		}
		if (xExpressCheckout.getAck().equals(AckCodeType.SUCCESS)){
			xOk = true;
		}
		setOk(xOk);
		return xOk;
	}
	
	@Override
	public String onSchedule() throws DBSIOException {
		CreateRecurringPaymentsProfileResponseDetailsType xRecurringPayment = pvCreateRecurringPayment(wToken, getItemName(), getValue(), pvParserPeriod(getPeriod()), getFrequency());
		setProfileId(xRecurringPayment.getProfileID());
		return getProfileId();
	}

	@Override
	public PROFILE_STATUS onReadProfileStatus() throws DBSIOException {
		GetRecurringPaymentsProfileDetailsRequestType 	xRequestType = new GetRecurringPaymentsProfileDetailsRequestType(getProfileId());
		GetRecurringPaymentsProfileDetailsReq 			xRequest = new GetRecurringPaymentsProfileDetailsReq();
		PayPalAPIInterfaceServiceService 				xService = new PayPalAPIInterfaceServiceService(getConfigMap());
		GetRecurringPaymentsProfileDetailsResponseType 	xRecurringPaymentsProfileDetails = null;
		
		xRequestType.setVersion(VERSION);
		xRequest.setGetRecurringPaymentsProfileDetailsRequest(xRequestType);
		try {
			xRecurringPaymentsProfileDetails = xService.getRecurringPaymentsProfileDetails(xRequest);
		} catch (SSLConfigurationException | InvalidCredentialException | HttpErrorException
				| InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
				| OAuthException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
			setOk(false);
			wLogger.error(e);
			DBSIO.throwIOException(e.getMessage());
		}
		if (DBSObject.isNull(xRecurringPaymentsProfileDetails)) {
			setOk(false);
			DBSIO.throwIOException(new DBSIOException(MsgErroSemResposta.getMessageText()));
		}
		if (!xRecurringPaymentsProfileDetails.getErrors().isEmpty()) {
			setOk(false);
			wLogger.error(xRecurringPaymentsProfileDetails.getErrors().get(0).getLongMessage());
			DBSIO.throwIOException(new DBSIOException(xRecurringPaymentsProfileDetails.getErrors().get(0).getLongMessage()));
		}
		return PROFILE_STATUS.getFromPayPal(xRecurringPaymentsProfileDetails.getGetRecurringPaymentsProfileDetailsResponseDetails().getProfileStatus());
	}
	
	//METODOS PRIVADOS =======================================================================================================================================
	private GetExpressCheckoutDetailsResponseType pvGetPaymentDetails(String pToken) throws DBSIOException{
		GetExpressCheckoutDetailsRequestType 	xGetExpressCheckoutDetailsRequest = new GetExpressCheckoutDetailsRequestType(pToken);
		GetExpressCheckoutDetailsReq 			xGetExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
		PayPalAPIInterfaceServiceService 		xService = new PayPalAPIInterfaceServiceService(getConfigMap());
		GetExpressCheckoutDetailsResponseType 	xGetExpressCheckoutDetailsResponse = null;
		
		xGetExpressCheckoutDetailsRequest.setVersion(VERSION);
		xGetExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(xGetExpressCheckoutDetailsRequest);

		try {
			xGetExpressCheckoutDetailsResponse = xService.getExpressCheckoutDetails(xGetExpressCheckoutDetailsReq);
		} catch (SSLConfigurationException | InvalidCredentialException | HttpErrorException
				| InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
				| OAuthException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
			setOk(false);
			wLogger.error(e);
			DBSIO.throwIOException(e.getMessage());
		}
		if (DBSObject.isNull(xGetExpressCheckoutDetailsResponse)) {
			setOk(false);
			getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, MsgErroSemResposta.getMessageText()));
			return null;
		}
		if (!xGetExpressCheckoutDetailsResponse.getErrors().isEmpty()) {
			for (int xI = 0; xI < xGetExpressCheckoutDetailsResponse.getErrors().size(); xI++) {
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xGetExpressCheckoutDetailsResponse.getErrors().get(xI).getLongMessage()));
			}
			setOk(false);
			return null;
		}
		return xGetExpressCheckoutDetailsResponse;
	}
	
	private DoExpressCheckoutPaymentResponseType pvDoExpressCheckout(String pToken, String pPayerId, String pItemName, Double pValue, Integer pQuantity, String pUrlIPN) throws DBSIOException {
		DoExpressCheckoutPaymentRequestDetailsType xDoExpressCheckoutPaymentRequestDetails = new DoExpressCheckoutPaymentRequestDetailsType();
		DoExpressCheckoutPaymentRequestType xDoExpressCheckoutPaymentRequest;
		DoExpressCheckoutPaymentReq xDoExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
		PayPalAPIInterfaceServiceService xService = new PayPalAPIInterfaceServiceService(getConfigMap());
		DoExpressCheckoutPaymentResponseType xDoExpressCheckoutPaymentResponse = null;
		
		xDoExpressCheckoutPaymentRequestDetails.setToken(pToken);
		xDoExpressCheckoutPaymentRequestDetails.setPayerID(pPayerId);
		xDoExpressCheckoutPaymentRequestDetails.setPaymentDetails(pvSetPaymentDetails(pItemName, pValue, pQuantity, pUrlIPN));

		xDoExpressCheckoutPaymentRequest = new DoExpressCheckoutPaymentRequestType(xDoExpressCheckoutPaymentRequestDetails);
		xDoExpressCheckoutPaymentRequest.setVersion(VERSION);

		xDoExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(xDoExpressCheckoutPaymentRequest);

		try {
			xDoExpressCheckoutPaymentResponse = xService.doExpressCheckoutPayment(xDoExpressCheckoutPaymentReq);
		} catch (SSLConfigurationException | InvalidCredentialException | HttpErrorException
				| InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
				| OAuthException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
			setOk(false);
			wLogger.error(e);
			DBSIO.throwIOException(e.getMessage());
		} 
		if (DBSObject.isNull(xDoExpressCheckoutPaymentResponse)) {
			setOk(false);
			getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, MsgErroSemResposta.getMessageText()));
			return null;
		}
		if (!xDoExpressCheckoutPaymentResponse.getErrors().isEmpty()) {
			for (int xI = 0; xI < xDoExpressCheckoutPaymentResponse.getErrors().size(); xI++) {
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xDoExpressCheckoutPaymentResponse.getErrors().get(xI).getLongMessage()));
			}
			setOk(false);
			return null;
		}
		return xDoExpressCheckoutPaymentResponse;
	}
	
	private CreateRecurringPaymentsProfileResponseDetailsType pvCreateRecurringPayment(String pToken, String pItemName, Double pValue, BillingPeriodType pPeriod, Integer pFrequency) throws DBSIOException {
		RecurringPaymentsProfileDetailsType 				xProfileDetails = new RecurringPaymentsProfileDetailsType();
		CreateRecurringPaymentsProfileRequestDetailsType 	xCreateRPProfileRequestDetails = new CreateRecurringPaymentsProfileRequestDetailsType();
		CreateRecurringPaymentsProfileRequestType 			xCreateRPProfileRequest = new CreateRecurringPaymentsProfileRequestType();
		CreateRecurringPaymentsProfileReq 					xCreateRPPProfileReq = new CreateRecurringPaymentsProfileReq();
		PayPalAPIInterfaceServiceService 					xService = new PayPalAPIInterfaceServiceService(getConfigMap());
		CreateRecurringPaymentsProfileResponseType 			xCreateRPProfileResponse = null;
		
		xProfileDetails.setBillingStartDate(DBSFormat.getFormattedDateCustom(DBSDate.getNowDate(), "yyyy-MM-dd")+"T00:00:00:000Z");
		
		xCreateRPProfileRequestDetails.setRecurringPaymentsProfileDetails(xProfileDetails);
		xCreateRPProfileRequestDetails.setScheduleDetails(pvSetScheduleDetails(pItemName, pValue, pPeriod, pFrequency));
		xCreateRPProfileRequestDetails.setToken(pToken);

		xCreateRPProfileRequest.setCreateRecurringPaymentsProfileRequestDetails(xCreateRPProfileRequestDetails);

		xCreateRPPProfileReq.setCreateRecurringPaymentsProfileRequest(xCreateRPProfileRequest);

		try {
			xCreateRPProfileResponse = xService.createRecurringPaymentsProfile(xCreateRPPProfileReq);
		} catch (SSLConfigurationException | InvalidCredentialException | HttpErrorException
				| InvalidResponseDataException | ClientActionRequiredException | MissingCredentialException
				| OAuthException | IOException | InterruptedException | ParserConfigurationException | SAXException e) {
			setOk(false);
			wLogger.error(e);
			DBSIO.throwIOException(e.getMessage());
		}
		if (DBSObject.isNull(xCreateRPProfileResponse)) {
			setOk(false);
			getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, MsgErroSemResposta.getMessageText()));
			return null;
		}
		if (!xCreateRPProfileResponse.getErrors().isEmpty()) {
			for (int xI = 0; xI < xCreateRPProfileResponse.getErrors().size(); xI++) {
				getMessages().add(new DBSMessage(MESSAGE_TYPE.ERROR, xCreateRPProfileResponse.getErrors().get(xI).getLongMessage()));
			}
			setOk(false);
			return null;
		}
		return xCreateRPProfileResponse.getCreateRecurringPaymentsProfileResponseDetails();
	}
	
	private BasicAmountType pvSetAmount(Double pValue) {
		BasicAmountType xAmt = new BasicAmountType();
		xAmt.setCurrencyID(CurrencyCodeType.BRL);
		xAmt.setValue(DBSFormat.getFormattedNumber(pValue, 2, Locale.US));
		return xAmt;
	}
	
	private PaymentDetailsItemType pvSetItem(String pItemName, Double pValue, Integer pQuantity) {
		PaymentDetailsItemType xItem = new PaymentDetailsItemType();
		xItem.setQuantity(pQuantity);
		xItem.setName(pItemName);
		xItem.setAmount(pvSetAmount(pValue));
		xItem.setItemCategory(ItemCategoryType.DIGITAL);
		xItem.setProductCategory(ProductCategoryType.SOFTWARE);
		return xItem;
	}
	
	private List<PaymentDetailsItemType> pvSetListItem(String pItemName, Double pValue, Integer pQuantity) {
		List<PaymentDetailsItemType> xListItems = new ArrayList<PaymentDetailsItemType>();
		xListItems.add(pvSetItem(pItemName, pValue, pQuantity));
		return xListItems;
	}

	private BasicAmountType pvSetOrder(Double pValue) {
		BasicAmountType xOrderTotal = new BasicAmountType();
		xOrderTotal.setCurrencyID(CurrencyCodeType.BRL);
		xOrderTotal.setValue(DBSFormat.getFormattedNumber(pValue, 2, Locale.US));
		return xOrderTotal;
	}
	
	private List<PaymentDetailsType> pvSetPaymentDetails(String pItemName, Double pValue, Integer pQuantity, String pNotifyURL) {
 		PaymentDetailsType 						xPaymentDetails = new PaymentDetailsType();
		List<PaymentDetailsType> 				xPaymentDetailsList = new ArrayList<PaymentDetailsType>();
		
		xPaymentDetails.setNotifyURL(pNotifyURL);
		xPaymentDetails.setPaymentDetailsItem(pvSetListItem(pItemName, pValue, pQuantity));
		xPaymentDetails.setPaymentAction(PaymentActionCodeType.ORDER);
		xPaymentDetails.setOrderTotal(pvSetOrder(pValue));
		xPaymentDetailsList.add(xPaymentDetails);
		
		return xPaymentDetailsList;
	}
	
	private ScheduleDetailsType pvSetScheduleDetails(String pItemName, Double pValue,BillingPeriodType pPeriod, Integer pFrequency) {
		BillingPeriodDetailsType 	xPaymentPeriod;
		ScheduleDetailsType 		xScheduleDetails = new ScheduleDetailsType();
		xPaymentPeriod = new BillingPeriodDetailsType();
		xPaymentPeriod.setAmount(pvSetAmount(pValue));
		xPaymentPeriod.setBillingPeriod(pPeriod);
		xPaymentPeriod.setBillingFrequency(pFrequency);
		xScheduleDetails.setDescription(pItemName);
		xScheduleDetails.setPaymentPeriod(xPaymentPeriod);
		return xScheduleDetails;
	}

	private BillingPeriodType pvParserPeriod(PERIODICIDADE pPeriod) {
		if (pPeriod == null){
			return BillingPeriodType.NOBILLINGPERIODTYPE;
		} else if (pPeriod == PERIODICIDADE.DIARIA) {
			return BillingPeriodType.DAY;
		} else if (pPeriod == PERIODICIDADE.MENSAL) {
			return BillingPeriodType.MONTH;
		} else if (pPeriod == PERIODICIDADE.ANUAL) {
			return BillingPeriodType.YEAR;
		} else {
			return BillingPeriodType.NOBILLINGPERIODTYPE;
		}
	}

}
