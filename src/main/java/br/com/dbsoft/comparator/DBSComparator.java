package br.com.dbsoft.comparator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;

import org.apache.log4j.Logger;

import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSIO.SORT_DIRECTION;

/**
 * Class para ordenar uma lista a partir dos atributos e respectivos valores.<br/>
 * Deve-se utilizar o <b>Collections.sort</b>.<br/> 
 * ex:Collections.sort(wLista, new DBSComparator<IListModel>("Volume", ORDER.DESCENDING));<br/>
 * Onde: <b>wList<b/> é um <b>List<b/> de objetos do tipo <b>IListModel<b/> e <b>Volume<b/> e o nome da atributo a partir de qual será efetuado a ordenação.
 * 
 * @author ricardo.villar
 *
 * @param <DataModelClass>
 */
public class DBSComparator<DataModelClass> implements Comparator<DataModelClass>{
	
	protected Logger	wLogger = Logger.getLogger(this.getClass());
	
	private String  		wFieldName;
	private SORT_DIRECTION	wDirection;
	
	/**
	 * @param pFieldName Nome do campos que será ordenado
	 * @param pDirection Direção da ordenação
	 */
	public DBSComparator(String pFieldName, SORT_DIRECTION pDirection) {
		wFieldName = pFieldName;
		wDirection = pDirection;
	}
	
	@Override
	public int compare(DataModelClass pDataModel1, DataModelClass pDataModel2) {
		try {
			//Retorna o método para posterioemente executar-lo para retornar o valor
			Method xMethod1 = DBSIO.findDataModelMethod(pDataModel1.getClass(), "get" + wFieldName);
			Method xMethod2 = DBSIO.findDataModelMethod(pDataModel2.getClass(), "get" + wFieldName);
			//Executa o método para retornar o valor
			if (xMethod1 == null){
				wLogger.error("Atributo [" + wFieldName + "] não existente em " + pDataModel1.getClass().getName());
				return 0;
			}
			
			Object xValue1 = xMethod1.invoke(pDataModel1);
			Object xValue2 = xMethod2.invoke(pDataModel2);
			if (xValue1 == null &&
				xValue2 == null){
				return 0;
			}
			if (xValue1 == null){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return -1;
				}else{
					return 1;
				}
			}else if (xValue2 == null){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return 1;
				}else{
					return -1;
				}
			}
			if (xValue1 instanceof Date){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((Date) xValue1).compareTo((Date) xValue2);
				}else{
					return ((Date) xValue2).compareTo((Date) xValue1);
				}
			}else if (xValue1 instanceof String){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((String) xValue1).compareTo((String) xValue2);
				}else{
					return ((String) xValue2).compareTo((String) xValue1);
				}
			}else if (xValue1 instanceof Double){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((Double) xValue1).compareTo((Double) xValue2);
				}else{
					return ((Double) xValue2).compareTo((Double) xValue1);
				}
			}else if (xValue1 instanceof Integer){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((Integer) xValue1).compareTo((Integer) xValue2);
				}else{
					return ((Integer) xValue2).compareTo((Integer) xValue1);
				}
			}else if (xValue1 instanceof Long){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((Long) xValue1).compareTo((Long) xValue2);
				}else{
					return ((Long) xValue2).compareTo((Long) xValue1);
				}
			}else if (xValue1 instanceof BigDecimal){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((BigDecimal) xValue1).compareTo((BigDecimal) xValue2);
				}else{
					return ((BigDecimal) xValue2).compareTo((BigDecimal) xValue1);
				}
			}else if (xValue1 instanceof Timestamp){
				if (wDirection == SORT_DIRECTION.ASCENDING){
					return ((Timestamp) xValue1).compareTo((Timestamp) xValue2);
				}else{
					return ((Timestamp) xValue2).compareTo((Timestamp) xValue1);
				}
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
