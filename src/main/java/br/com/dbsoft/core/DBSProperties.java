package br.com.dbsoft.core;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

/**
 * Properties ordenadas alfabeticamente
 * @author ricardo.villar
 *
 */
public class DBSProperties extends Properties {

	private static final long serialVersionUID = -7657143795257546946L;

	@Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
    }
}
