package br.com.dbsoft.comparator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;


public class DBSComparator<T> implements Comparator<T>{

	public enum ORDER{
		ASCENDING,
		DESCENDING;
	}
	private String  wFieldName;
	private ORDER	wOrder;
	
	public DBSComparator(String pFieldName, ORDER pOrder) {
		wFieldName = pFieldName;
		wOrder = pOrder;
	}
	
	@Override
	public int compare(T pObject1, T pObject2) {
		try {
			Method xMethod1 = pObject1.getClass().getDeclaredMethod("get" + wFieldName);
			Method xMethod2 = pObject2.getClass().getDeclaredMethod("get" + wFieldName);
			Object xValue1 = xMethod1.invoke(pObject1);
			Object xValue2 = xMethod2.invoke(pObject2);
			if (xValue1 == null &&
				xValue2 == null){
				return 0;
			}
			if (xValue1 == null){
				if (wOrder == ORDER.ASCENDING){
					return -1;
				}else{
					return 1;
				}
			}else if (xValue2 == null){
				if (wOrder == ORDER.ASCENDING){
					return 1;
				}else{
					return -1;
				}
			}
			if (xValue1 instanceof Date){
				if (wOrder == ORDER.ASCENDING){
					return ((Date) xValue1).compareTo((Date) xValue2);
				}else{
					return ((Date) xValue2).compareTo((Date) xValue1);
				}
			}else if (xValue1 instanceof Double){
				if (wOrder == ORDER.ASCENDING){
					return ((Double) xValue1).compareTo((Double) xValue2);
				}else{
					return ((Double) xValue2).compareTo((Double) xValue1);
				}
			}else if (xValue1 instanceof Integer){
				if (wOrder == ORDER.ASCENDING){
					return ((Integer) xValue1).compareTo((Integer) xValue2);
				}else{
					return ((Integer) xValue2).compareTo((Integer) xValue1);
				}
			}else if (xValue1 instanceof Long){
				if (wOrder == ORDER.ASCENDING){
					return ((Long) xValue1).compareTo((Long) xValue2);
				}else{
					return ((Long) xValue2).compareTo((Long) xValue1);
				}
			}else if (xValue1 instanceof Double){
				if (wOrder == ORDER.ASCENDING){
					return ((String) xValue1).compareTo((String) xValue2);
				}else{
					return ((String) xValue2).compareTo((String) xValue1);
				}
			}else if (xValue1 instanceof Timestamp){
				if (wOrder == ORDER.ASCENDING){
					return ((Timestamp) xValue1).compareTo((Timestamp) xValue2);
				}else{
					return ((Timestamp) xValue2).compareTo((Timestamp) xValue1);
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
