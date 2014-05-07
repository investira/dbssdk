package br.com.dbsoft.io;

import java.util.SortedMap;

import javax.faces.model.ArrayDataModel;


/**
 * @author ricardo.villar
 * Dados recuperados do banco e organizados como array.<br/>
 * Desta forma permite a manipulação dos dados, independentemente dos dados no banco.<br/>
 *  
 */
public class DBSResultDataModel extends ArrayDataModel<SortedMap<String,Object>> {

	public DBSResultDataModel() {
		super();
	}
	
	public DBSResultDataModel(SortedMap<String,Object>[] pRows) {
		super(pRows);
	}
}

//public class DBSResultDataModel extends ResultDataModel {
//
//	public DBSResultDataModel() {
//		super();
//	}
//	
//	public DBSResultDataModel(Result pRows) {
//		super(pRows);
//	}
//}
