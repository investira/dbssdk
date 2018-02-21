package br.com.dbsoft.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.dbsoft.comparator.DBSComparator;
import br.com.dbsoft.util.DBSIO.SORT_DIRECTION;

public class DBSSort {

	/**
	 * Retorna Map ordenado pelo value.
	 * @param pUnSortedMap
	 * @param pSortDirection
	 * @return
	 */
	public static <K, V> Map<K, V> getMapByValue(Map<K, V> pUnSortedMap, SORT_DIRECTION pSortDirection) {
		DBSComparator<Map.Entry<K, V>> xComparator = new DBSComparator<Map.Entry<K, V>>("Value", pSortDirection);
		// 1. Convert Map to List of Map
		List<Map.Entry<K, V>> xList = new LinkedList<Map.Entry<K, V>>(pUnSortedMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		Collections.sort(xList, xComparator);
	   
		// 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
		Map<K, V> xSortedMap = new LinkedHashMap<K ,V>();
		for (Map.Entry<K, V> xEntry : xList){
			xSortedMap.put(xEntry.getKey(), xEntry.getValue());
		}
		return xSortedMap;
	}
}
