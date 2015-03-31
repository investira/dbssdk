/**
 * 
 */
package br.com.dbsoft.log;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.io.DBSDAOTxt;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSString;


/**
 * @author Avila
 * 
 */
public class DBSLog implements Serializable {

	private static final long 	serialVersionUID = 4429215163920856587L;

	/**
	 * Efetua a leitura do arquivo de Log e retorna uma lista de LogModel com as
	 * informações de log.
	 * 
	 * @param pArquivoLog
	 * @return List<LogModel> lista dos registros de log
	 */
	public static List<DBSLogModel> loadLogFile(String pArquivoLog) {
		DBSDAOTxt<DBSLogModel> xDAOTxt = new DBSDAOTxt<DBSLogModel>(pArquivoLog);
		xDAOTxt.setEncode(DBSSDK.FILE.ENCODE.UTF_8);
		xDAOTxt.setColumnsDelimiter(";");
		xDAOTxt.setHeaderDefinesColumnsNames(false);
		xDAOTxt.setKeepData(false);
		xDAOTxt.open();
		
		List<DBSLogModel> xLista = new ArrayList<DBSLogModel>();
		DBSLogModel xLog = new DBSLogModel();
		while (xDAOTxt.readLine()) {
			String xLinha = xDAOTxt.getRowValues().toString();
			// Testa se a Linha possui delimitador. Se não for, considera continuação da mensagem anterior.
			if (xLinha.indexOf(";") == -1) {
				xLog.setMensagem(xLog.getMensagem() + xLinha);
				continue;
			}
			// Testa se o primeiro campo é uma data. Se não for, considera continuação da mensagem anterior. 
			try {
				SimpleDateFormat xDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				xDateFormat.parse(xDAOTxt.getValue(0).toString());
			} catch (ParseException e) {
				xLog.setMensagem(xLog.getMensagem() + xLinha);
				continue;
			}
			xLog = new DBSLogModel();
			xLog.setData(DBSFormat.getFormattedDateCustom(DBSDate.toDateDMYHMS(xDAOTxt.getValue(0).toString()), "dd/MM HH:mm:ss"));
			xLog.setNivel(xDAOTxt.getValue(1).toString());
			xLog.setClasse(xDAOTxt.getValue(2).toString());
			xLog.setMetodo(xDAOTxt.getValue(3).toString());
			xLog.setMensagem(xDAOTxt.getValue(4).toString());
			xLista.add(xLog);
//			System.out.println(xLog.toString());
		}
		
		xDAOTxt.close();
		if (xLista.isEmpty()) {
			return null;
		} else {
			return xLista;
		}
	}
	
	public static List<DBSLogModel> loadServerLogFile(String pArquivoLog) {
		DBSDAOTxt<DBSLogModel> xDAOTxt = new DBSDAOTxt<DBSLogModel>(pArquivoLog);
		String xArquivoNome;
		String xData;
		String xHora;
		String xDataHora;
		String xNivel;
		String xClasse;
		String xMetodo;
		String xMensagem;
		int xFimClasse;
		int xFimMetodo;
		
		xArquivoNome = DBSString.getSubString(pArquivoLog, pArquivoLog.lastIndexOf(File.separator)+2, pArquivoLog.length());
		if (DBSString.getStringCount(xArquivoNome, ".") > 1) {
			xData = DBSString.getSubString(xArquivoNome, 14, 8);
		} else {
			xData = DBSFormat.getFormattedDate(DBSDate.getNowDate());
		}
		xData = xData.substring(0, xData.length()-5);
		
		xDAOTxt.setEncode(DBSSDK.FILE.ENCODE.UTF_8);
//		xDAOTxt.setColumnsDelimiter(";");
		xDAOTxt.setHeaderDefinesColumnsNames(false);
		xDAOTxt.setKeepData(false);
		xDAOTxt.open();
		
		List<DBSLogModel> xLista = new ArrayList<DBSLogModel>();
		DBSLogModel xLog = new DBSLogModel();
		while (xDAOTxt.readLine()) {
			String xLinha = xDAOTxt.getRowValues().toString();
			// Testa se a Linha começa com hora
			xHora = DBSString.getSubString(xLinha, 1, 12);
			try {
				if (!DBSDate.isTime(xHora)) {
					xLog.setMensagem(xLog.getMensagem() + xLinha);
					continue;
				}
			} catch (Exception e) {
				xLog.setMensagem(xLog.getMensagem() + xLinha);
				continue;
			}
			xDataHora = xData +" "+ xHora;
			xNivel = DBSString.getSubString(xLinha, 14, 6).trim();
			xClasse = xLinha.substring(xLinha.indexOf("[")+1, DBSString.getInStr(xLinha, "]", 21)-1);
			xFimClasse = DBSString.getInStr(xLinha, "]", 21)-1;
			xFimMetodo = DBSString.getInStr(xLinha, ")", xFimClasse);
			xMetodo = xLinha.substring(xFimClasse+3, DBSString.getInStr(xLinha, ")", xFimClasse)-1);
			xMensagem = xLinha.substring(xFimMetodo, xLinha.length());
//			// Testa se o primeiro campo é uma data. Se não for, considera continuação da mensagem anterior. 
//			try {
//				SimpleDateFormat xDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
//				xDateFormat.parse(xDataHora);
//			} catch (ParseException e) {
//				xLog.setMensagem(xLog.getMensagem() + xLinha);
//				continue;
//			}
			xLog = new DBSLogModel();
			xLog.setData(xDataHora);
			xLog.setNivel(xNivel);
			xLog.setClasse(xClasse);
			xLog.setMetodo(xMetodo);
			xLog.setMensagem(xMensagem);
			xLista.add(xLog);
		}
		
		xDAOTxt.close();
		if (xLista.isEmpty()) {
			return null;
		} else {
			return xLista;
		}
	}

}
