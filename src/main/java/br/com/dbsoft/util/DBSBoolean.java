package br.com.dbsoft.util;

public  class DBSBoolean {
	/**
	 * Retorna valor informado como Boolean
	 * @param pValue
	 * @return
	 */
	public static Boolean toBoolean(Object pValue) {
		return toBoolean(pValue, false);
	}
	
	public static Boolean toBoolean(Object pValue, Boolean pDefaultValue) {
		if (DBSObject.isEmpty(pValue)) {
			return pDefaultValue;
		}
		if (DBSNumber.isNumber(pValue.toString())) {
			if (DBSNumber.toInteger(pValue) == 0) {
				return false;
			} else {
				return true;
			}
		} else if (pValue instanceof Boolean) {
			return (Boolean) pValue;
		} else if (pValue instanceof String) {
			String xValue = ((String) pValue).trim().toUpperCase();
			if (xValue.equals("FALSE")){
				return false;
			}else if (xValue.equals("N")){
				return false;
			}else if (xValue.equals("S")){
				return true;
			} else {
				return true;
			}
		} else {
			return null;
		}
	}
	
	public static String toSN(Object pValue) {
		Boolean xB = toBoolean(pValue);
		if (xB){
			return "S";
		}else{
			return "N";
		}
	}
}
