/**
 * 
 */
package br.com.dbsoft.io;

import java.io.File;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * @author Avila
 *
 */
@SuppressWarnings("rawtypes")
public class DBSXml {

	private static Logger wLogger = Logger.getLogger(DBSXml.class);
	
	/**
	 * Efetua a leitura de um arquivo XML e transforma-o em um objeto.
	 * 
	 * @param pClass Classe Modelo do Arquivo XML
	 * @param pArquivoXml Arquivo XML
	 * @return Object
	 */
	public static Object toObject(Class pClass, File pArquivoXml) {
		try {
			JAXBContext xContext = JAXBContext.newInstance(pClass);
			Unmarshaller xReader = xContext.createUnmarshaller();
			return xReader.unmarshal(new StreamSource(pArquivoXml));
		} catch (JAXBException e) {
			wLogger.error(e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Object toObject(Class pClass, String pStringXml) {
		return JAXB.unmarshal(pStringXml, pClass);
	}

	/**
	 * Efetua a conversão do objeto para um arquivo XML.
	 * 
	 * @param pClass Classe Modelo
	 * @param pArquivoXml Arquivo XML que será criado.
	 * @param pObjeto Objeto a ser convertido.
	 * @return boolean
	 */
	public static boolean toXML(Class pClass, File pArquivoXml, Object pObjeto) {
		try {
			JAXBContext xContext = JAXBContext.newInstance(pClass);
			Marshaller xWriter = xContext.createMarshaller();
			xWriter.marshal(pObjeto, pArquivoXml);
			return true;
		} catch (JAXBException e) {
			wLogger.error(e);
			return false;
		}
	}
}
