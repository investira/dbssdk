package br.com.dbsoft.io;

import java.math.BigDecimal;
import java.sql.Date;

public class TstDAOModel {

	private Integer CRUD_ID;
	private String CRUD;
	private Date TIPO_DATA;
	private BigDecimal TIPO_DECIMAL;
	private Integer TIPO_INT;

	public Integer getCRUD_ID() {
		return CRUD_ID;
	}
	public void setCRUD_ID(Integer pCRUD_ID) {
		this.CRUD_ID = pCRUD_ID;
	}
	public String getCRUD() {
		return CRUD;
	}
	public void setCRUD(String pCRUD) {
		this.CRUD = pCRUD;
	}

	public BigDecimal getTIPO_DECIMAL() {
		return TIPO_DECIMAL;
	}
	public void setTIPO_DECIMAL(BigDecimal pTIPO_DECIMAL) {
		TIPO_DECIMAL = pTIPO_DECIMAL;
	}
	public Date getTIPO_DATA() {
		return TIPO_DATA;
	}
	public void setTIPO_DATA(Date pTIPO_DATA) {
		TIPO_DATA = pTIPO_DATA;
	}
	public Integer getTIPO_INT() {
		return TIPO_INT;
	}
	public void setTIPO_INT(Integer pTIPO_INT) {
		TIPO_INT = pTIPO_INT;
	}

}
