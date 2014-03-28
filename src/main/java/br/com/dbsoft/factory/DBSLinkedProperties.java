package br.com.dbsoft.factory;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

/**
 * @author ricardovillar
 * Controle de propriedades com seus respectivos valores que 
 * são lidos dos arquivos com extensão .properties localizados na pasta META-INF
 * 
 */
public class DBSLinkedProperties extends Properties {


	private static final long serialVersionUID = 6327872294644301890L;

	private final LinkedHashSet<Object> wKeys = new LinkedHashSet<Object>();

	@Override
	public Enumeration<Object> keys() {
		return Collections.<Object>enumeration(wKeys);
	}

	@Override
	public Object put(Object pKey, Object pValue) {
		wKeys.add(pKey);
		return super.put(pKey, pValue);
	}
}
