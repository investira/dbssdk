package br.com.dbsoft.enums;

import br.com.dbsoft.util.DBSObject;
import urn.ebay.apis.eBLBaseComponents.PaymentStatusCodeType;
import urn.ebay.apis.eBLBaseComponents.RecurringPaymentsProfileStatusType;

public class DBSSDKEnums {
	
	public static enum PROFILE_STATUS {
		ACTIVE			("Active", 		0),
		CANCELED		("Canceled", 	1),
		PENDING			("Pending", 	2),
		SUSPENDED		("Suspended", 	3),
		EXPIRED			("Expired", 	4);
		
		private String 	wName;
		private int 	wCode;
		
		private PROFILE_STATUS(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static PROFILE_STATUS get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 0:
				return ACTIVE;
			case 1:
				return CANCELED;
			case 2:
				return PENDING;
			case 3:
				return SUSPENDED;
			case 4:
				return EXPIRED;
			default:
				return null;
			}
		}
		
		public static PROFILE_STATUS getFromPayPal(RecurringPaymentsProfileStatusType pPayPalStatusType) {
			if (RecurringPaymentsProfileStatusType.ACTIVEPROFILE.equals(pPayPalStatusType)) {
				return ACTIVE;
			} else if (RecurringPaymentsProfileStatusType.CANCELLEDPROFILE.equals(pPayPalStatusType)) {
				return CANCELED;
			} else if (RecurringPaymentsProfileStatusType.PENDINGPROFILE.equals(pPayPalStatusType)) {
				return PENDING;
			} else if (RecurringPaymentsProfileStatusType.SUSPENDEDPROFILE.equals(pPayPalStatusType)) {
				return SUSPENDED;
			} else if (RecurringPaymentsProfileStatusType.EXPIREDPROFILE.equals(pPayPalStatusType)) {
				return EXPIRED;
			} else {
				return null;
			}
			
		}
	}
	
	public static enum PAYMENT_TYPE {
		PAYPAL			("PayPal",			1),
		CREDIT_CARD		("Credit Card", 	2),
		BOLETO			("Boleto", 			3);
		
		private String 	wName;
		private int 	wCode;
		
		private PAYMENT_TYPE(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return	wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static PAYMENT_TYPE get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 1:
				return PAYPAL;
			case 2:
				return CREDIT_CARD;
			case 3:
				return BOLETO;
			default:
				return null;
			}
		}
	}
	
	public static enum PAYMENT_STATUS {
		COMPLETE		("Complete",	0),
		PENDING			("Pending", 	1),
		FAILED			("Failed", 		2);
		
		private String 	wName;
		private int 	wCode;
		
		private PAYMENT_STATUS(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static PAYMENT_STATUS get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 0:
				return COMPLETE;
			case 1:
				return PENDING;
			case 2:
				return FAILED;
			default:
				return null;
			}
		}
		
		public static PAYMENT_STATUS getFromPayPal(PaymentStatusCodeType pPayPalStatusType) {
			if (DBSObject.isNull(pPayPalStatusType)) {
				return null;
			}
			return getFromPayPal(pPayPalStatusType.getValue());
		}
		public static PAYMENT_STATUS getFromPayPal(String pPayPalStatusType) {
			if (DBSObject.isNull(pPayPalStatusType)) {
				return null;
			}
			if (pPayPalStatusType.equals(PaymentStatusCodeType.COMPLETED.getValue())) {
				return COMPLETE;
			} else if (pPayPalStatusType.equals(PaymentStatusCodeType.PENDING.getValue())) {
				return PENDING;
			} else {
				return FAILED;
			}
		}
	}
	
	public static enum PAYMENT_FROM {
		PAYPAL		(0, "PayPal", "173.0.81.33");
		
		private int 	wCode;
		private String 	wName;
		private String 	wAddress;
		
		private PAYMENT_FROM(int pCode, String pName, String pAddress) {
			this.wCode = pCode;
			this.wName = pName;
			this.wAddress = pAddress;
		}

		public int getCode() {
			return wCode;
		}
		public String getName() {
			return wName;
		}
		public String getAddress() {
			return wAddress;
		}
		
		public static PAYMENT_FROM get(Integer pCode) {
			if (DBSObject.isNull(pCode)) {
				return null;
			}
			switch (pCode) {
			case 0:
				return PAYPAL;
			default:
				return null;
			}
		}
		
		public static PAYMENT_FROM getFromAddress(String pAddress) {
			if (DBSObject.isNull(pAddress)) {
				return null;
			}
			if (PAYPAL.getAddress().equals(pAddress)) {
				return PAYPAL;
			} else {
				return null;
			}
		}
		
	}
}
