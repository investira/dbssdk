/**
 * 
 */
package br.com.dbsoft.log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.io.DBSDAOTxt;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;


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

}
