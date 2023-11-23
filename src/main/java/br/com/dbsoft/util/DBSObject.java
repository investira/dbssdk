package br.com.dbsoft.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

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
		} else if (pObj instanceof List<?>) {
			if (((List<?>) pObj).isEmpty()) {
				return true;
			}
		} else if (pObj instanceof LinkedHashMap<?, ?>){
			if (((LinkedHashMap<?, ?>) pObj).isEmpty()) {
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
	 * Retorna valor inteiro se id por maior que zero e nulo se for menor que 1 ou nulo.
	 * @param pId
	 * @return
	 */
	public static Integer toId(Object pId){
		Integer xId = DBSNumber.toInteger(pId, 0);
		//maior que zero
		if (xId.compareTo(0) > 0){
			return xId;
		}
		return null;
	}
	
	/**
	 * Retorna o mesmo dado informado quando este não for NaN. 
	 * Se for NaN, retorna o valor default definido pelo usuário ou vazio.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja NaN
	 * @return Dado contendo o valor diferente de NaN
	 */
	public static <T> double getNotNaN(T pDado, double pDadoDefault) {
		Double xDado = DBSNumber.toDouble(pDado);
        if (Double.isNaN(xDado)) {
            return pDadoDefault;
        }
        return xDado;
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
	 * Converte o valor recebido para o tipo de class informada<br/>
	 * Caso o valor informado seja nulo, retorna o valor informado em <b>pNullValue</b>.
	 * @param pValue
	 * @param pClass
	 * @param pNullValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toClassValue(Object pValue, Class<T> pClass, T pNullValue){
		if (pValue==null){
			return pNullValue;
		}
		if (pClass==null){
			return (T) pValue;
		}
		return toClassValue(pValue, pClass);
	}

	/**
	 * Converte o valor recebido para o tipo de class informada.
	 * @param pValue
	 * @param pClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toClassValue(Object pValue, Class<T> pClass){
		if (pClass==null){
			return (T) pValue;
		}
		if (pValue==null){
			return null;
		}
		if (pClass.isAssignableFrom(Boolean.class)){ //2016-11-04 - Boolean estava antes do pValue == null acima. Verificar o pq.
			return (T) DBSBoolean.toBoolean(pValue);
		}else if (pClass.isAssignableFrom(Integer.class)){
			return (T) DBSNumber.toInteger(pValue);
		}else if (pClass.isAssignableFrom(BigDecimal.class)){
			return (T) DBSNumber.toBigDecimal(pValue);
		}else if (pClass.isAssignableFrom(Double.class)){
			return (T) DBSNumber.toDouble(pValue);
		}else if (pClass.isAssignableFrom(Long.class)){
			return (T) DBSNumber.toLong(pValue);
		}else if (pClass.isAssignableFrom(String.class)){
			return (T) pValue.toString();
		}else if (pClass.isAssignableFrom(Date.class)){
			return (T) DBSDate.toDate(pValue);
		}else if (pClass.isAssignableFrom(Timestamp.class)){
			return (T) DBSDate.toTimestamp(pValue);
		}else if (pClass.isAssignableFrom(Time.class)){
			return (T) DBSDate.toTime(pValue);
		}
		return (T) pValue;
	}
	
	/**
	 * Retorna se os dois objetos informados são iquais, considerando também como verdadeiro(<i>true</i>) se ambos forem nulos.<br/>
	 * Objetos de classes diferentes serão considerados diferentes, mesmo que contenham os 'mesmos' valores.
	 * @param pA 
	 * @param pB
	 * @return true = iquais <br/>
	 * 		   false = diferentes
	 */
	public static boolean isEqual(Object pA, Object pB){
		//Se ambos os itens forem nulos
		if (pA == null
		 && pB == null){
			return true;
		}
		//Se somente um dos itens for nulo
		if (pA == null
		 || pB == null){
			return false;
		}
		
		//Se forem de classes diferentes
		if (!pA.getClass().equals(pB.getClass())){
			return false;
		}
		return pA.equals(pB);
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

	//Serialize e Deserialize
	public static String serialize(Object pObject) throws IOException { 
        ByteArrayOutputStream xBaos = new ByteArrayOutputStream(); 
        ObjectOutputStream xOOS = new ObjectOutputStream(xBaos); 
        xOOS.writeObject(pObject); 
        xOOS.close(); 
        return DatatypeConverter.printBase64Binary(xBaos.toByteArray()); 
	} 
	
	public static Object deserialize(String pString) throws IOException, ClassNotFoundException { 
        ByteArrayInputStream xBais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(pString)); 
        ObjectInputStream xOIS = new ObjectInputStream(xBais); 
        Object xObject = xOIS.readObject(); 
        xOIS.close(); 
        return xObject; 
	}
}
