/**
 * 
 */
package br.com.dbsoft.log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Classe criada para formatar a data do arquivo HTML de log Por padrão mostra
 * apenas o inteiro de timestamp. Com esta classe irá mostrar as datas com o
 * formato dd/MM/yyyy HH:mm:ss
 * 
 * @author Avila
 * 
 */
public class DBSLogLayout extends HTMLLayout {

	static String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String wRxTimestamp = "\\s*<\\s*tr\\s*>\\s*<\\s*td\\s*>\\s*(\\d*)\\s*<\\s*/td\\s*>";
	private String wFormatoData = "dd/MM/yyyy HH:mm:ss";
	private SimpleDateFormat wFomarter = new SimpleDateFormat(wFormatoData);

	public DBSLogLayout() {
		super();
	}

	/**
	 * Sobreescreve o método de formatação de data de HTMLLayout
	 */
	@Override
	public String format(LoggingEvent event) {
		// Recupera o registro de log no formato padrão de HTMLLayout
		String record = super.format(event);
		

		Pattern pattern = Pattern.compile(wRxTimestamp);
		Matcher matcher = pattern.matcher(record);

		// Se a data padrão não puder se encontrada,
		if (!matcher.find()) {
			return record;
			// apenas retorna-a sem modificações
		}

		StringBuffer buffer = new StringBuffer(record);

		// Substitui a data padrão pela data formatada.
		buffer.replace(matcher.start(1), matcher.end(1),
				wFomarter.format(new Date(event.timeStamp)));

		// Retorna o registro do log com a data formatada.
		record = buffer.toString();
		record = record.replaceAll("<tr>", "<tr class=\"-row\">");
		
		return record;
	}

	/**
	 * Retorna o Cabeçalho da página HTML. No caso do DBSLogLayout, retorna
	 * apenas o cabeçalho da da Tabela de log.
	 */
	@Override
	public String getHeader() {

//		SimpleDateFormat xFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		Date xData = new Date(System.currentTimeMillis());
//		String xDataFormatada = xFormat.format(xData);
//
//		StringBuffer sbuf = new StringBuffer();
//		sbuf.append("<f:view xmlns=\"http://www.w3.org/1999/xhtml\"" + Layout.LINE_SEP);
//		sbuf.append("xmlns:c=\"http://java.sun.com/jsp/jstl/core\"" + Layout.LINE_SEP);
//		sbuf.append("xmlns:ui=\"http://java.sun.com/jsf/facelets\"" + Layout.LINE_SEP);
//		sbuf.append("xmlns:h=\"http://java.sun.com/jsf/html\"" + Layout.LINE_SEP);
//		sbuf.append("xmlns:f=\"http://java.sun.com/jsf/core\"" + Layout.LINE_SEP);
//		sbuf.append("xmlns:dbs=\"http://www.dbsoft.com.br/ui/dbsfaces\">" + Layout.LINE_SEP);
//		sbuf.append("<f:subview id=\"import\">" + Layout.LINE_SEP);
//		sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
//		sbuf.append("</style>" + Layout.LINE_SEP);
//		sbuf.append("<dbs:div>" + Layout.LINE_SEP);
//		sbuf.append("<dbs:div styleClass=\"dbs_table\">" + Layout.LINE_SEP);
//		sbuf.append("<dbs:div styleClass=\"-content\">" + Layout.LINE_SEP);
//
//		String xCabecalho = "Sessão de log começou em " + xDataFormatada + Layout.LINE_SEP;
//
//		sbuf.append("<dbs:label styleClass=\"-label\" value=\"" + xCabecalho + "\"/>");
//		sbuf.append("<br>" + Layout.LINE_SEP);
//		sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">"
//				+ Layout.LINE_SEP);
//		sbuf.append("<tr class=\"-subtitle\">" + Layout.LINE_SEP);
//		sbuf.append("<th>Hora</th>" + Layout.LINE_SEP);
//		sbuf.append("<th>Thread</th>" + Layout.LINE_SEP);
//		sbuf.append("<th>Nível</th>" + Layout.LINE_SEP);
//		sbuf.append("<th>Categoria</th>" + Layout.LINE_SEP);
//		if (super.getLocationInfo()) {
//			sbuf.append("<th>Arquivo:Linha</th>" + Layout.LINE_SEP);
//		}
//		sbuf.append("<th>Mensagem</th>" + Layout.LINE_SEP);
//		sbuf.append("</tr>" + Layout.LINE_SEP);
//		return sbuf.toString();
		return "";
	}

	/**
	 * Retorna o rodapé HTML No caso do DBSLogLayout, retorna uma String vazia
	 * pois não é necessário um rodapé.
	 */
	@Override
	public String getFooter() {
//		 StringBuffer sbuf = new StringBuffer();
//		 sbuf.append("</table>" + Layout.LINE_SEP);
//		 sbuf.append("</dbs:div>" + Layout.LINE_SEP);
//		 sbuf.append("</dbs:div>" + Layout.LINE_SEP);
//		 sbuf.append("</dbs:div>" + Layout.LINE_SEP);
//		 sbuf.append("</f:subview>" + Layout.LINE_SEP);
//		 return sbuf.toString();
		return "";
	}

	/**
	 * Setter para o formato da data. Chamado se a propriedade
	 * log4j.appender.<category>.layout.TimestampFormat for especificada.
	 */
	public void setTimestampFormat(String format) {
		this.wFormatoData = format;
		this.wFomarter = new SimpleDateFormat(format);
	}

	/**
	 * Getter da data formatada.
	 */
	public String getTimestampFormat() {
		return this.wFormatoData;
	}

}
