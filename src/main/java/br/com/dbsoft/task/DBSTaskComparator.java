/**
 * 
 */
package br.com.dbsoft.task;

import java.sql.Date;
import java.util.Comparator;

import br.com.dbsoft.task.DBSTask;
import br.com.dbsoft.util.DBSObject;

/**
 * Compara duas tarefas e ordena por Habilitado e por Nome.
 * 
 * @author Avila
 * @param <T>
 * @param <T>
 *
 */
public class DBSTaskComparator<T>  implements Comparator<T>{

	private Boolean wSortByScheduleDate = false;
	
	public DBSTaskComparator(){}
	
	public DBSTaskComparator(Boolean pSortByScheduleDate) {
		wSortByScheduleDate = pSortByScheduleDate;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int compare(T pO1, T pO2) {
		DBSTask<T> xTask1 = (DBSTask<T>) pO1;
		DBSTask<T> xTask2 = (DBSTask<T>) pO2;
		
		if (wSortByScheduleDate) { //Por Data agendada
			Date xData1 = xTask1.getScheduleDate();
			Date xData2 = xTask2.getScheduleDate();
			
			if (!DBSObject.isEmpty(xData1) && !DBSObject.isEmpty(xData2)) {
				return xData1.compareTo(xData2);
			} else {
				return +1;
			}
		} else { //Por Nome
			String xName1 = xTask1.getName();
			String xName2 = xTask2.getName();
			
			if (!DBSObject.isEmpty(xName1) && !DBSObject.isEmpty(xName2)) {
				return xName1.compareTo(xName2);
			} else {
				return +1;
			}
		}
	}

}
