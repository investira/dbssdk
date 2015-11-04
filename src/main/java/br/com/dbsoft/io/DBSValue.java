package br.com.dbsoft.io;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class DBSValue<T extends Object>{

	public 	T 		 	value = null;
	public 	Type		wType = null;
	public  Class<T>	wClass = null;
	
	public DBSValue() {
		pvSetType();
	}

	public DBSValue(T pValue) {
		pvSetType();
		value = pValue;
	}
	
	@SuppressWarnings("unchecked")
	private void pvSetType(){
	    Type xSuperclass = getClass().getGenericSuperclass();
	    if (xSuperclass instanceof Class) {
	      throw new RuntimeException("Missing type parameter.");
	    }
	    wType = ((ParameterizedType) xSuperclass).getActualTypeArguments()[0];
	    wClass = (Class<T>) wType; 
	}
	
	public Class<T> getValueClass(){
		return wClass;
	}
	
	public Type getValueType(){
		return wType;
	}
}
