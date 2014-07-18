package br.com.dbsoft.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public  class DBSObject {
	/**
	 * Verifica se objeto está vazio ou nulo
	 * @param pObj Objeto a ser testado
	 * @return true = vazio | false = preenchido. 
	 */
	public static Boolean isEmpty(Object pObj){
		if (pObj == null){
			return true;
		}
		if (pObj instanceof String){ 
			String xObj = pObj.toString();
			if (xObj.trim().equals("")){
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se o objeto informado, é nulo.
	 * @param pObj Objeto a ser testado
	 * @return true = nulo | false = não nulo. 
	 */
	public static Boolean isNull(Object pObj){
		if (pObj == null){
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se o objeto ID está nulo ou zerado, sendo assim inválido.
	 * @param pObj
	 * @return true = nao vazio e maior que zero | false = nulo ou menor ou igual a zero
	 */
	public static Boolean isIdValid(Integer pObj) {
		if (DBSObject.isEmpty(pObj)){
			return false;
		}
		if (pObj <= 0) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Retorna o mesmo dado informado quando este não for nulo. 
	 * Se for nulo, retorna o conteúdo default definido pelo usuário ou vazio, quando este for nulo.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de nulo
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNotNull(T pDado, T pDadoDefault){
		if (DBSObject.isEmpty(pDado)){
			if (DBSObject.isEmpty(pDadoDefault)){
				return (T) ""; //Retorna vazio
			} 
			else {
				return pDadoDefault; //Retorna valor default informado
			}
		}
		else{
			return pDado;
		}
	}
	/**
	 * Retorna o mesmo dado informado quando este não é vazio. 
	 * Se for vazio, retorna o conteúdo default definido pelo usuário ou vazio, quando este for vazio.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de vazio
	 */
	public static <T> T getNotEmpty(T pDado, T pDadoDefault){
		if (DBSObject.isEmpty(pDado)){
			return pDadoDefault; //Retorna valor default informado
		}
		else{
			return pDado;
		}
	}
	
	
	/**
	 * Converte o valor recebido para o tipo de class informado
	 * @param pValue
	 * @param pClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toClass(T pValue, Class<?> pClass){
		if (pClass==null){
			return pValue;
		}
		if (pClass.isAssignableFrom(Boolean.class)){
			return (T) DBSBoolean.toBoolean(pValue);
		}
		if (pValue==null){
			return null;
		}
		if (pClass.isAssignableFrom(Integer.class)){
			pValue = (T) DBSNumber.toInteger(pValue);
		}else if (pClass.isAssignableFrom(BigDecimal.class)){
			pValue = (T) DBSNumber.toBigDecimal(pValue);
		}else if (pClass.isAssignableFrom(Double.class)){
			pValue = (T) DBSNumber.toDouble(pValue);
		}else if (pClass.isAssignableFrom(String.class)){
			pValue = (T) pValue.toString();
		}else if (pClass.isAssignableFrom(Date.class)){
			pValue = (T) DBSDate.toDate(pValue);
		}else if (pClass.isAssignableFrom(Timestamp.class)){
			pValue = (T) DBSDate.toTimestamp(pValue);
		}else if (pClass.isAssignableFrom(Time.class)){
			pValue = (T) DBSDate.toTime(pValue);
		}
		return pValue;
	}

	/**
	 * Retorna a class esperada pelo método que chamou o metodo sendo executado.
	 * @param pParentIndex
	 * @return
	 */
	protected static Class<?> getCallerReturnType(int pParentIndex){
		try {
			StackTraceElement xElement = Thread.currentThread().getStackTrace()[pParentIndex];
			Class<?> xClass = Class.forName(xElement.getClassName());
			Method xMethod = xClass.getDeclaredMethod(xElement.getMethodName()); 
			return xMethod.getReturnType();
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			return null;
		}
	}


}
