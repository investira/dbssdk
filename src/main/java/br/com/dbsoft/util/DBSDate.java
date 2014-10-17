package br.com.dbsoft.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Seconds;

import br.com.dbsoft.core.DBSSDK;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.io.DBSDAO;

public class DBSDate{

	public enum PERIODICIDADE{
		DIARIA		("Dia(s)","Diária", 1),
		MENSAL 		("Mês(es)", "Mensal", 2),
		ANUAL 		("Ano(s)", "Anual", 3),
		IRREGULAR 	("Irregular", "Irregular", 4),
		EMISSAO 	("Emissão", "Emissão", 5),
		VENCIMENTO 	("Vencimento", "Vencimento", 6);
		
		public static PERIODICIDADE get(Object pCode) {
			Integer xI = DBSNumber.toInteger(pCode, null);
			return get(xI);
		}
		
		public static PERIODICIDADE get(Integer pCode) {
			if (pCode == null){
				return null;
			}
			switch (pCode) {
			case 1:
				return PERIODICIDADE.DIARIA;
			case 2:
				return PERIODICIDADE.MENSAL;
			case 3:
				return PERIODICIDADE.ANUAL;
			case 4:
				return PERIODICIDADE.IRREGULAR;
			case 5:
				return PERIODICIDADE.EMISSAO;
			case 6:
				return PERIODICIDADE.VENCIMENTO;
			}
			return null;
		}	
		
		private String 	wName;
		private String	wName2;
		private int 	wCode;
		
		private PERIODICIDADE(String pName, String pName2, int pCode) {
			this.wName = pName;
			this.wName2 = pName2;
			this.wCode = pCode;
		}
	
		public String getName() {
			return wName;
		}

		public String getName2() {
			return wName2;
		}
	
		public int getCode() {
			return wCode;
		}

	}
	
	public enum BASE{
	    PADRAO,
	    D_MENOS_UM,
	    PRIMEIRO_DIA_DO_MES,
	    PRIMEIRO_DIA_DO_ANO,
	    PRIMEIRO_DIA_UTIL_DO_MES,
	    PRIMEIRO_DIA_UTIL_DO_ANO,
	    DIA_UTIL,
	    PRIMEIRO_DIA_DO_MES_ANTERIOR;
	}
	
	protected static Logger			wLogger = Logger.getLogger(DBSDate.class);
	
	//######################################################################################################### 
	//## Public Methods                                                                                     #
	//#########################################################################################################

	//Métodos de Retorno de data atual=========================================================================
	/**
	 * Retorna a hora de hoje 
	 * @return Hora de Hoje
	 */
	public static DateTime getNowDateTime() {
		return new org.joda.time.DateTime();
    }	

	/**
	 * Retorna a data de hoje 
	 * @return Data de Hoje
	 */
	public static Date getNowDate() {
		try{
			Calendar xCurrentTime = Calendar.getInstance();
			return new Date((xCurrentTime.getTime()).getTime());
		}catch(Exception e){
			//DBSError.showException(e);
			return null;
		}
    }

	/**
	 * Retorna a hora de hoje 
	 * @return Hora de Hoje
	 */
	public static Time getNowTime() {
		Calendar xCurrentTime = Calendar.getInstance();
		return new Time(xCurrentTime.getTimeInMillis());
    }

	/**
	 * Retorna a data e hora atual. 
	 * @return Data e hora atual
	 */
	public static Timestamp getNowTimestamp() {
		Calendar xCurrentTime = Calendar.getInstance();
		return new Timestamp(xCurrentTime.getTimeInMillis());
    }
	
	/**
	 * Calcula a quantidade de segundos entre duas datas
	 * @param pTimeInicio Hora Inicio
	 * @param pTimeFim Hora Fim
	 * @return Quantidade de segundos
	 */
	public static int getTimeDif(DateTime pTimeInicio, DateTime pTimeFim){
		if (pTimeInicio.equals(pTimeFim)){
			return 0;
		}
        int xSeconds = Seconds.secondsBetween(pTimeInicio.toLocalDateTime(), pTimeFim.toLocalDateTime()).getSeconds();
        
        return xSeconds;		
	}
	
	//Métodos de Validação de Data=========================================================================
	/**
	 * Retorna se é uma data válida
	 * @param pData Data no formado dd/mm/aa onde dd=dia /mm=mes /aa=ano(2 ou 4 digitos)  
     *			Anos com dois digitos: Ano atual + 20 = século 1900. ex: Atual:12(2012) aa=33(1933) aa=21(2021) 
	 * @return true = Data válida / false = Data inválida
	 */
	public static boolean isDate(String pData) {
		if (toDate(pData) != null){
			return true;
		}
		return false;
	}	

	/**
	 * Retorna se é uma hora válida
	 * @param pHora Hora no formado hh:mm:ss(24hrs)
	 * @return true = Data válida / false = Data inválida
	 */
	public static boolean isTime(String pHora) {
		DateFormat xFormat = new SimpleDateFormat("HH:mm:ss");
		// com setLenient a data/hora não se adapta a numeros altos 
		// se não setado, por exemplo ao inserir 25h a hora 
		// ficaria como 1h da manhã do dia seguinte. 		
		xFormat.setLenient(false);
		try {
			xFormat.parse(pHora);
			return true;
		} catch (ParseException e) {
			//DBSError.showException(e);
			return false;
		}
	}	
	
	//Métodos de conversão de datas=========================================================================
    /**
     * Retorna os parametros pDia, pMes e pAno em variável do tipo Date
     * @param pDia 
     * @param pMes
     * @param pAno com 2 ou 4 digitos. Anos com dois digitos: Ano atual + 20 = século 1900. ex: Atual:12(2012) aa=33(1933) aa=21(2021) 
     * @return Retorna a data
     */
    public static Date toDate(String pDia, String pMes, String pAno) {   
    	return toDate(pDia + "/" + pMes + "/"+ pAno);  
    }
    /**
     * Retorna os parametros pDia,pMes e pAno em variável do tipo Date
     * @param pDia 
     * @param pMes
     * @param pAno com 2 ou 4 digitos. Anos com dois digitos: Ano atual + 20 = século 1900. ex: Atual:12(2012) aa=33(1933) aa=21(2021) 
     * @return Retorna a data
     */
    public static Date toDate(int pDia, int pMes, int pAno) {   
    	return toDate(pDia + "/" + pMes + "/"+ pAno);  
    }   
    /**
     * Retorna a data no tipo Date
     * @param pData no formado dd/mm/aa onde dd=dia /mm=mes /aa=ano(2 ou 4 digitos) 
     *		  Anos com dois digitos: Ano atual + 20 = século 1900. ex:Se atual for 2013 -> 12=2012 33=2033 34=1934 21=2021 
     * @return Retorna a data
     */
    public static Date toDate(String pData) {
    	if (pData == null){
    		return null;
    	}

    	DateFormat xFormat;
    	// Testa se existe '-' na data passada
    	if (pData.contains("-")){
    		// Data no formato ISO
    		xFormat = new SimpleDateFormat("yy-MM-dd"); //Ano com duas casas para forçar que a regra seja definida pelo próprio java 	
    	}else{
    		// Data no formato ABNT
    		xFormat = new SimpleDateFormat("dd/MM/yy"); //Ano com duas casas para forçar que a regra seja definida pelo próprio java
    	}
    	Date xDate = Date.valueOf("0001-01-01");
    	Long xDateTime = 0L;
    	xFormat.setLenient(false);
    	try {
    		xDateTime = xFormat.parse(pData).getTime();
			xDate.setTime(xDateTime);
		} catch (ParseException e) {
			//DBSError.showException(e);
			return null;
		}
    	return xDate;
    }		
    
    /**
     * Retorna a data a partir da quantidade de milisegundos.<br/>
     * @param pMilliSeconds número em milisegundos a partir de January 1, 1970, 00:00:00 <b>GMT</b>.
     * @return Data no tipo Date convertida para a localização atual.
     */
    public static Date toDate(Long pMilliSeconds) {
    	if (DBSObject.isNull(pMilliSeconds)) {
    		return null;
    	}
    	Date xData = new Date(pMilliSeconds);
    	return xData;  
    }		

    
    /**
	 * Retorna uma Data do tipo Date, a partir de uma data do tipo Calendar.
	 * @param pData
	 * @return Data no tipo Date
	 */
	public static Date toDate(Calendar pData) {
		if (DBSObject.isNull(pData)) {
    		return null;
    	}
		return toDate(pData.getTimeInMillis());
	}
	
    /**
	 * Retorna uma Data do tipo Date, a partir de uma data do tipo Timestamp.
	 * @param pData
	 * @return Data no tipo Date
	 */
	public static Date toDate(Timestamp pData) {
		if (DBSObject.isNull(pData)) {
    		return null;
    	}
		return toDate(pData.getTime());
	}
	
    /**
	 * Retorna uma Data do tipo Date, a partir de uma data do tipo Timestamp.
	 * @param pData
	 * @return Data no tipo Date
	 */
	public static Date toDate(Object pData) {
		if (DBSObject.isEmpty(pData)) {
			return null;
		}
		if (pData instanceof Timestamp) {
			return toDate((Timestamp) pData);
		} else if (pData instanceof String) {
			return toDate((String) pData);
		} else if (pData instanceof Time) {
			return new Date(((Time) pData).getTime());
		} else {
			return (Date) pData;
		}
	}
	
	/**
	 * Retorna uma Data do tipo Date, a partir de uma data do tipo LocalDate.
	 * @param pData
	 * @return Data no tipo Date
	 */
	public static Date toDate(LocalDate pData) {
		java.util.Date xData0 = pData.toDate();
		java.sql.Date xData = new java.sql.Date(xData0.getTime());
		return xData;
	}

	/**
	 * Retorna uma Data do tipo Date, a partir de uma data do tipo DateTime
	 * @param pData
	 * @return Data no tipo Date
	 */
	public static Date toDate(DateTime pData) {
		java.util.Date xData0 = pData.toDate();
		java.sql.Date xData = new java.sql.Date(xData0.getTime());
		return xData;
	}

	public static Date toDate(XMLGregorianCalendar pData) {
		if (DBSObject.isEmpty(pData)) {
			return null;
		}
		DateTime xDataTime = new DateTime(pData.getYear(), pData.getMonth(), pData.getDay(), pData.getHour(), pData.getMinute());
		return DBSDate.toDate(xDataTime);
	}
	
	public static XMLGregorianCalendar toXMLGregorianCalendar(Object pDate){
		return toXMLGregorianCalendar(toDate(pDate));
	}
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date pDate){
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(pDate);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
        	wLogger.error(ex);
        }
        return xmlCalendar;
    }


	/**
	 * Retorna a data e hora no tipo Date, a partir de uma string com data e hora.
	 * @param pData no formado dd/mm/aaaa hh:mm:ss  
	 * @return Retorna a data formatada
	 */
	public static Date toDateDMYHMS(String pData) {
		if (pData.contains("-")) {
			// Data no formato ISO
	    	return pvToDateLong(pData, "dd-MM-yyyy HH:mm:ss");
		}else{
			// Data no formato ABNT
	    	return pvToDateLong(pData, "dd/MM/yyyy HH:mm:ss");
		}
	}

	public static Date toDateYMDHMS(String pData) {
		if (pData.contains("-")) {
			// Data no formato ISO
	    	return pvToDateLong(pData, "yyyy-MM-dd HH:mm:ss");
		}else if(pData.contains("/")){
			// Data no formato ABNT
	    	return pvToDateLong(pData, "yyyy/MM/dd HH:mm:ss");
		}else {
			// Data no formato ABNT
	    	return pvToDateLong(pData, "yyyyMMddHHmmss");
		}
	}

	/**
	 * Retorna uma Data do tipo Timestamp, a partir de uma data do tipo Date.
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestamp(Date pData){
		if (DBSObject.isEmpty(pData)) {
			return null;
		}
		Timestamp xT = new Timestamp(pData.getTime());
		return xT;
	}

	/**
	 * Retorna uma Data do tipo <b>Timestamp</b> a partir da quantidade de milisegundos.
	 * @param pMilliSeconds
	 * @return
	 */
	public static Timestamp toTimestamp(Long pMilliSeconds){
		Timestamp xT = new Timestamp(toDateTime(pMilliSeconds).getMillis());
		return xT;
	}

	/**
	 * Retorna uma Data do tipo <b>Timestamp</b> a partir da quantidade de milisegundos.
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestamp(Integer pMilliSeconds){
		Timestamp xT = new Timestamp(toDateTime(pMilliSeconds.longValue()).getMillis());
		return xT;
	}

	/**
	 * Retorna uma Data do tipo <b>Timestamp</b> a partir da hora.
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestamp(Time pTime){
		Timestamp xT = new Timestamp(pTime.getTime());
		return xT;
	}

	/**
	 * Retorna uma Data do tipo Timestamp, a partir de uma data do tipo Object.
	 * Se object for ""(vazio), retorna nulo.
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestamp(Object pData){
		if (DBSObject.isEmpty(pData)) {
			return null;
		}
		if (pData.equals("")){
			return null;
		}else if (pData instanceof Date){
			return new Timestamp(((Date) pData).getTime());
		} else if (pData instanceof Time) {
			return new Timestamp(((Time) pData).getTime());
		} else if (pData instanceof Timestamp) {
			return (Timestamp) pData;
		} else if (pData instanceof Integer) {
			return new Timestamp((Integer) pData);
		} else {
			return (Timestamp) pData;
		}
	}
	
	/**
	 * Retorna uma Data do tipo Timestamp, a partir de uma data do tipo String
	 * com o formato Ano(4)/Mes/Dia Hora:Minuto:Segundo
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestampYMDHMS(String pData){
		Date xData = DBSDate.toDateYMDHMS(pData);
		return DBSDate.toTimestamp(xData);
	}

	/**
	 * Retorna uma Data do tipo Timestamp, a partir de uma data do tipo String 
	 * com o formato Dia/Mes/Ano(4) Hora:Minuto:Segundo
	 * @param pData
	 * @return
	 */
	public static Timestamp toTimestampDMYHMS(String pData){
		Date xData = DBSDate.toDateDMYHMS(pData);
		return DBSDate.toTimestamp(xData);
	}
	
    /**
	 * Retorna a hora a partir da string no formato HH:MM:SS
	 * @param pHora no formato HH:MM:SS (24hrs)
	 * @return hora
	 */
	public static Time toTime(String pHora){
		DateFormat xFormat = new SimpleDateFormat("HH:mm:ss");
		xFormat.setLenient(true);			
		Time xTime = Time.valueOf("0:0:0");
    	try {
			xTime.setTime(xFormat.parse(pHora).getTime());
		} catch (ParseException e) {
//			throw e;
			//DBSError.showException(e);
			return null;
		}
		return xTime;
	}
	
	/**
	 * Retorna a hora a partir das strings de hora, minuto e segundo.
	 * @param pHora
	 * @param pMinuto
	 * @param pSegundo
	 * @return Hora
	 * @throws ParseException 
	 */
	public static Time toTime(String pHora, String pMinuto, String pSegundo) {
		DateFormat xFormat = new SimpleDateFormat("HH:mm:ss");
		xFormat.setLenient(true);			
		Time xTime = Time.valueOf("0:0:0");
	   	try {
			xTime.setTime(xFormat.parse(pHora + ":" + pMinuto + ":" + pSegundo).getTime());
		} catch (ParseException e) {
			return null;
		}
		return xTime;
	}	
	/**
	 * Retorna a hora a partir das strings de hora, minuto e segundo
	 * @param pHora
	 * @param pMinuto
	 * @param pSegundo
	 * @return Hora
	 * @throws ParseException 
	 */
	public static Time toTime(Long pHora, Long pMinuto, Long pSegundo) {
		return toTime(pHora.toString(),pMinuto.toString(), pSegundo.toString());
	}

	/**
	 * Retorna a hora a partir das strings de hora, minuto e segundo
	 * @param pHora
	 * @param pMinuto
	 * @param pSegundo
	 * @return Hora
	 * @throws ParseException 
	 */
	public static Time toTime(Integer pHora, Integer pMinuto, Integer pSegundo) {
		return toTime(pHora.toString(),pMinuto.toString(), pSegundo.toString());
	}
	
	/**
	 * Retorna a hora a partir da quantidade de milisegundos.
	 * @param pMilliseconds
	 * @return hora
	 */
	public static Time toTime(Long pMilliseconds){
		return toTime(TimeUnit.MILLISECONDS.toHours(pMilliseconds), 
					  TimeUnit.MILLISECONDS.toMinutes(pMilliseconds), 
					  TimeUnit.MILLISECONDS.toSeconds(pMilliseconds)); 
	}
	
	/**
	 * Retorna a hora a partir do object.
	 * @param pMilliseconds
	 * @return hora
	 */
	public static Time toTime(Object pObject){
		if (DBSObject.isEmpty(pObject)) {
			return null;
		}
		if (pObject instanceof Time){
			return (Time) pObject;
		}else if (pObject instanceof Date){
			return toTime((Date) pObject);
		} else if (pObject instanceof Timestamp) {
			return toTime((Timestamp) pObject);
		} else  if (pObject instanceof Number){
			return toTime((Long) pObject); 
		} else {
			return (Time) pObject;
		}		
	}
	
	/**
	 * Retorna a hora a partir da quantidade de timestamp.
	 * @param pMilliseconds
	 * @return hora
	 */
	public static Time toTime(Timestamp pTimestamp){
		Calendar xC = toCalendar(pTimestamp);
		return toTime(xC.get(Calendar.HOUR_OF_DAY), xC.get(Calendar.MINUTE), xC.get(Calendar.SECOND));
	}

	/**
	 * Retorna a hora a partir da data.
	 * @param pMilliseconds
	 * @return hora
	 */
	public static Time toTime(Date pDate){
		Calendar xC = toCalendar(pDate);
		return toTime(xC.get(Calendar.HOUR_OF_DAY), xC.get(Calendar.MINUTE), xC.get(Calendar.SECOND));
	}

	/**
	 * Retornar data no tipo Calender a partir de data no tipo Date
	 * @param pData do tipo Date que se seja converte
	 * @return Data convertida para o tipo Calendar
	 */
	public static Calendar toCalendar(Date pData){
		if (pData == null){
			return null;
		}
		Calendar xData = Calendar.getInstance();
		xData.setTime(pData);
		return xData;
	}
	
	/**
	 * Retornar data no tipo Calender a partir de data no tipo Timestamp
	 * @param pTime do tipo Date que se seja converte
	 * @return Data convertida para o tipo Calendar
	 */
	public static Calendar toCalendar(Timestamp pTime){
		if (pTime == null){
			return null;
		}
		Calendar xData = Calendar.getInstance();
		xData.setTime(pTime);
		return xData;
	}
	
	

	/**
	 * Retornar data no tipo Calendar, a partir do dia, mes e ano informado
	 * @param pDia 
	 * @param pMes
	 * @param pAno
	 * @return Data no formato Calendar
	 */
	public static Calendar toCalendar(int pDia, int pMes, int pAno) {
		Calendar xC = Calendar.getInstance();
		xC.set(pAno, pMes, pDia);
		return xC;
	}
	
	/**
	 * Retornar data no tipo Calendar, a partir da quantidade de milisegundos.
	 * @param pDia 
	 * @param pMes
	 * @param pAno
	 * @return Data no formato Calendar
	 */
	public static Calendar toCalendar(Long pMilliseconds) {
		Calendar xC = Calendar.getInstance();
		xC.setTimeInMillis(pMilliseconds);
		return xC;
	}
	
    /**
     * Retorna a data e hora a partir da quantidade de milisegundos.<br/>
     * @param pMilliSeconds número em milisegundos a partir de January 1, 1970, 00:00:00.
     * @return Data no tipo Date
     */
    public static DateTime toDateTime(Long pMilliSeconds) {
    	return new DateTime(pMilliSeconds, DateTimeZone.UTC);  
    }	
	
	
	/**
	 * Retorna o dia a partir de uma data.
	 * @param pData
	 * @return dia
	 */
	public static Integer getDia(Date pData){
		Calendar xData = toCalendar(pData);
		return xData.get(Calendar.DAY_OF_MONTH);
	}
		
	//Métodos de Cálculo de datas=========================================================================
	/**
	 * Chama getDias(), considerando pApplicationColumnName = null
	 * @param pConexao Conexão do banco de dados
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @param pUtil Indica se é dia útil ou não (True = Dia útil / False = Dia corrido)
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado.
	 *                Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @return Quantidade de dias
	 */
	public static int getDias(Connection pConexao, Date pDataInicio, Date pDataFim, boolean pUtil, int pCidade) {
		return getDias(pConexao, pDataInicio, pDataFim, pUtil, pCidade, null);
	}
	
	/**
	 * Retornar a quantidade de dias(úteis ou corridos) entre duas datas
	 * @param pConexao Conexão do banco de dados
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @param pUtil Indica se é dia útil ou não (True = Dia útil / False = Dia corrido)
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado.
	 *                Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @param pApplicationColumnName Renda Fixa - RF
	 * 								 Renda Variavel - RV
	 * @return Quantidade de dias
	 */
	public static int getDias(Connection pConexao, Date pDataInicio, Date pDataFim, boolean pUtil, int pCidade, String pApplicationColumnName) {
		int xDias;
		int xSinal=1;
		
		if (DBSObject.isEmpty(pDataInicio) 
		 || DBSObject.isEmpty(pDataFim) 
		 || pDataInicio.equals(pDataFim)) {
			return 0;
		} else {
			if (pUtil){
				xDias = getDateDif(pDataInicio, pDataFim);
				if (xDias < 0){	
					xSinal = -1;
				}
				//Subtrai da quantidade de dias os feriádos e finais de semana
				return xSinal * (Math.abs(xDias) - getFinaisDeSemana(pDataInicio, pDataFim) - getFeriados(pConexao, pDataInicio, pDataFim, pCidade, pApplicationColumnName));
			} else {
				return  getDateDif(pDataInicio, pDataFim);  
			}
		}
	}
	
	/**
	 * Calcula a quantidade de meses entre duas datas
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @return Quantidade de meses
	 */
	public static int getMeses(Date pDataInicio, Date pDataFim){
		Calendar xDataInicio = toCalendar(pDataInicio);
		Calendar xDataFim = toCalendar(pDataFim);
		return getMeses(xDataInicio, xDataFim);  
	}
	
	/**
	 * Retorna o mes a partir de uma date(date)
	 * @param pData
	 * @return mes
	 */
	public static Integer getMes(Date pData){
		Calendar xData = toCalendar(pData);
		return xData.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Calcula a quantidade de meses entre duas datas
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @return Quantidade de meses
	 */
	public static int getMeses(Calendar pDataInicio, Calendar pDataFim){
		int xAnos =  pDataFim.get(Calendar.YEAR) -  pDataInicio.get(Calendar.YEAR); //Anos entre duas Data;
		int xMeses  = pDataFim.get(Calendar.MONTH) -  pDataInicio.get(Calendar.MONTH); //Anos entre duas Data;
		return (xAnos * 12) + xMeses;  
	}

	/**
	 * Retorna o ano a partir de uma date(date)
	 * @param pData
	 * @return Ano
	 */
	public static Integer getAno(Date pData){
		Calendar xData = toCalendar(pData);
		return xData.get(Calendar.YEAR);
	}
	
	/**
	 * Retorna a hora(sem minutos ou segundos) a partir de um timestamp
	 * @param pData
	 * @return Ano
	 */
	public static Integer getHora(Timestamp pTime){
		Calendar xData = toCalendar(pTime);
		return xData.get(Calendar.HOUR_OF_DAY); 
	}	

	/**
	 * Retorna o minuto a partir de um timestamp
	 * @param pData
	 * @return Ano
	 */
	public static Integer getMinuto(Timestamp pTime){
		Calendar xData = toCalendar(pTime);
		return xData.get(Calendar.MINUTE);
	}	
	
	/**
	 * Retorna o segundo a partir de um timestamp
	 * @param pData
	 * @return Ano
	 */
	public static Integer getSegundo(Timestamp pTime){
		Calendar xData = toCalendar(pTime);
		return xData.get(Calendar.SECOND);
	}	
	/**
	 * Calcula a quantidade de anos entre duas datas
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @return Quantidade de anos
	 */
	public static int getAnos(Date pDataInicio, Date pDataFim){
		Calendar xDataInicio = toCalendar(pDataInicio);
		Calendar xDataFim = toCalendar(pDataFim);
		return getAnos(xDataInicio, xDataFim);  
	}
	
	/**
	 * Calcula a quantidade de anos entre duas data
	 * @param pDataInicio Data inicio
	 * @param pDataFim Data fim
	 * @return Quantidade de anos
	 */
	public static int getAnos(Calendar pDataInicio, Calendar pDataFim){
		//Anos entre duas datas  
		return pDataFim.get(Calendar.YEAR) -  pDataInicio.get(Calendar.YEAR); 
	}

	/**
	 * Calcula a data a partir da database adicionada de dias
	 * @param pDataBase Data base
	 * @param pPrazo em dias
	 * @return Data
	 */
	public static Date getDateAdd(Date pDataBase, int pPrazo){
		if (pDataBase!=null){
			if (pPrazo==0){
				return pDataBase;
			}else{
				LocalDate xDT = new DateTime(pDataBase).toLocalDate();
				xDT = xDT.plusDays(pPrazo);
				return DBSDate.toDate(xDT); 
		        //int xDias = Days.daysBetween(new DateTime(pDataBase).toLocalDate(), new DateTime(pDataFim).toLocalDate()).getDays();
		        
			}
		}else{
			return null;
		}
	}
	
	/**
	 * Calcula a data a partir da database adicionada de meses
	 * @param pDataBase Data base
	 * @param pPrazo em dias
	 * @return Data
	 */
	public static Date getDateAddMes(Date pDataBase, int pPrazo){
		if (pDataBase!=null){
			if (pPrazo==0){
				return pDataBase;
			}else{
				LocalDate xDT = new DateTime(pDataBase).toLocalDate();
				xDT = xDT.plusMonths(pPrazo);
				return DBSDate.toDate(xDT); 
		        //int xDias = Days.daysBetween(new DateTime(pDataBase).toLocalDate(), new DateTime(pDataFim).toLocalDate()).getDays();
		        
			}
		}else{
			return null;
		}
	}
	
	/**
	 * Calcula a data a partir da database adicionada de anos
	 * @param pDataBase Data base
	 * @param pPrazo em dias
	 * @return Data
	 */
	public static Date getDateAddAno(Date pDataBase, int pPrazo){
		if (pDataBase!=null){
			if (pPrazo==0){
				return pDataBase;
			}else{
				LocalDate xDT = new DateTime(pDataBase).toLocalDate();
				xDT = xDT.plusYears(pPrazo);
				return DBSDate.toDate(xDT); 
		        //int xDias = Days.daysBetween(new DateTime(pDataBase).toLocalDate(), new DateTime(pDataFim).toLocalDate()).getDays();
		        
			}
		}else{
			return null;
		}
	}

	/**
	 * Calcula a quantidade de dias entre duas datas
	 * @param pDataInicio Data Inicio
	 * @param pDataFim Data Fim
	 * @return Quantidade de dias
	 */
	public static int getDateDif(Date pDataInicio, Date pDataFim){
		if (pDataInicio.equals(pDataFim)){
			return 0;
		}
		
		//Calcula intervalo entre as datas desprezando fusohorário
        return Days.daysBetween(new DateTime(pDataInicio).toLocalDate(), new DateTime(pDataFim).toLocalDate()).getDays();
	}
	
	/**
	 * Retorna se uma data é ou não dia util
	 * @param pConexao Conexão do banco de dados
	 * @param pData Data será verificada se é útil ou não
	 * @param pCidade Código da Cidade que será utilizada para considerar os dias úteis
	 *                Código da cidade como -1, indica que o serão considerados somente feriados nacionais
	 * @param pApplicationColumnName ex:Renda Fixa - RF
	 * 								 ex:Renda Variavel - RV
	 * @return true = dia útil ou false = não útil
	 */
	public static boolean isDiaUtil(Connection pConexao, Date pData, int pCidade, String pApplicationColumnName){
		Calendar xData = Calendar.getInstance();
		
		xData.setTime(pData);
		xData.add(Calendar.DAY_OF_MONTH, -1);
		
		//Verifica se é um feriado, sábado ou domingo
		if (getFeriados(pConexao, toDate(xData), pData, pCidade, pApplicationColumnName) > 0 || 
			toCalendar(pData).get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || 
			toCalendar(pData).get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
			return false;
		}
		
		return true;	
	}
	
	/**
	 * Chama o metodo isDiaUtil(), considerando <b>pApplicationColumnName</b> = null
	 * @param pConexao Conexão com banco de dados
	 * @param pData Data será verificada se é útil ou não
	 * @return true = dia útil ou false = não útil
	 */	
	public static boolean isDiaUtil(Connection pConexao, Date pData, int pCidade){
		return isDiaUtil(pConexao, pData, pCidade, null);
	}
	
	/**
	 * Chama o metodo isDiaUtil(), considerando <b>pCidade = -1</b>
	 * @param pConexao Conexão com banco de dados
	 * @param pData Data será verificada se é útil ou não
	 * @return true = dia útil ou false = não útil
	 */	
	public static boolean isDiaUtil(Connection pConexao, Date pData){
		return isDiaUtil(pConexao, pData, -1);
	}
	
	/**
	 * Chama o metodo getFeriados(), considerando <b>pCidade = -1</b>
	 * @param pConexao Conexão com banco de dados
	 * @param pDataInicio Data Inicial
	 * @param pDataFim Data Final
	 * @return Quantidade de dias cadastrados como feriados
	 */
	public static int getFeriados(Connection pConexao, Date pDataInicio, Date pDataFim) {
		return getFeriados(pConexao, pDataInicio, pDataFim, -1);
	}
	
	/**
	 * Chama o metodo getFeriados(), considerando <b>pApplicationColumnName = null</b>
	 * @param pConexao Conexão com banco de dados
	 * @param pDataInicio Data Inicial
	 * @param pDataFim Data Final
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado
	 *                Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @return Quantidade de dias cadastrados como feriados
	 */
	public static int getFeriados(Connection pConexao, Date pDataInicio, Date pDataFim, int pCidade) {
		return getFeriados(pConexao, pDataInicio, pDataFim, pCidade, null);
	}
	
	/**
	 * Retorna a quantidade de feriados entre duas datas.
	 * @param pConexao Conexão com banco de dados
	 * @param pDataInicio Data Inicial
	 * @param pDataFim Data Final
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado
	 *                Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @param pApplicationColumnName ex:Renda Fixa - RF
	 * 								 ex:Renda Variavel - RV
	 * @return Quantidade de dias cadastrados como feriados
	 */
	public static int getFeriados(Connection pConexao, Date pDataInicio, Date pDataFim, int pCidade, String pApplicationColumnName) {
		if (DBSSDK.TABLE.FERIADO.equals("")){
			wLogger.error("DBSSDK.TABLE.FERIADO em branco, Favor informar o tarefa que contém o cadastro de feriados.");
			return 0;
		}
		String 			xSql;
		String 			xFiltroCidade= "";
		Integer 		xDias = 0;
		DBSDAO<Object> 	xDao = new DBSDAO<Object>(pConexao);
		xSql = "SELECT * From " + DBSSDK.TABLE.FERIADO + " ";
		
		if (pDataInicio.after(pDataFim)) {
			xSql = xSql + "WHERE DATA >=" + DBSIO.toSQLDate(pConexao, pDataFim) + 
					       " AND DATA <" + DBSIO.toSQLDate(pConexao, pDataInicio) ;
		} else {
			xSql = xSql + "WHERE DATA >" + DBSIO.toSQLDate(pConexao, pDataInicio) + 
					       " AND DATA <=" + DBSIO.toSQLDate(pConexao, pDataFim) ;
		}
		if (DBSObject.isIdValid(pCidade)) {
			xFiltroCidade = " OR CIDADE_ID = " + pCidade;
		}
		xSql = xSql + " AND (CIDADE_ID = -1 or " + DBSIO.toSQLNull(pConexao, "CIDADE_ID") + xFiltroCidade + ")";// Objetivo: Retorna quantidade de feriados em dias
		//ALBERTO
		//Trecho para retirar os feriados que caem no sábado e domingo
		if (!DBSObject.isEmpty(pApplicationColumnName)) {
			xSql = xSql + " AND "+ pApplicationColumnName + " = -1";
		}
		try {
			if (xDao.open(xSql)) {
				xDao.moveBeforeFirstRow();
				while (xDao.moveNextRow()) {
					//Se feriado for final de semana, ignora
					if (DBSDate.getNumeroDaSemana(DBSDate.toDate(xDao.getValue("DATA"))) != Calendar.SATURDAY
					 && DBSDate.getNumeroDaSemana(DBSDate.toDate(xDao.getValue("DATA"))) != Calendar.SUNDAY) {
			            xDias += 1;
					}
				}
				xDao.close();
			}
		} catch (DBSIOException e) {
			wLogger.error(e);
		}
		return xDias;
	}

	/**
	 * Retorna quantidade de domingos e sábados entre duas datas.
	 * @param pDataInicio Data Inicial
	 * @param pDataFim Data Final
	 * @return Quantidade de domingos e sábados
	 */
	public static int getFinaisDeSemana(Date pDataInicio, Date pDataFim){
		Double xDiasI;
		Double xDiasF;
		int xDias;
		Date xDataBase;
		xDataBase = DBSDate.toDate(01,01,1900);
		//Quantidade sábados e domingos entre a datas
		xDiasI = (((DBSDate.getDateDif(xDataBase,pDataInicio)+1)/7)-0.001); //Quantidade de semanas existentes na data inicio
		xDiasF = (((DBSDate.getDateDif(xDataBase,pDataFim)+1)/7)-0.001); //Quantidade de semanas existenstes na data fim
		xDias = (xDiasF.intValue() - xDiasI.intValue()) * 2; //Quantidade de dias(sábados e domingos);
		xDias = Math.abs(xDias);
		// Ajusta os dias caso as data seja um final de semana
		if (pDataFim.compareTo(pDataInicio) > 0) {
			// Ajusta os dias se as datas forem iguais a sábado
			if (DBSDate.toCalendar(pDataInicio).get(Calendar.DAY_OF_WEEK) == 7){
				xDias--;
			}
			if (DBSDate.toCalendar(pDataFim).get(Calendar.DAY_OF_WEEK) == 7){
				xDias++;
			}
		} else {
			// Ajusta os dias se as datas forem iguais a domingo
			if (DBSDate.toCalendar(pDataInicio).get(Calendar.DAY_OF_WEEK) == 1){
				xDias--;
			}
			if (DBSDate.toCalendar(pDataFim).get(Calendar.DAY_OF_WEEK) == 1){
				xDias++;
			}
		}
		
		return xDias;
	}
	/**
	 * Calcula a quantidade de dias corridos/úteis no ano informado.
	 * @param pConexao Conexão com banco de dados
	 * @param pAno Ano 
	 * @param pUtil indicativo se é dia útil(True) ou não(False)
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado
	 * 			      Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @return Quantidade de dias do ano informado
	 */
	public static int getDiasDoAno(Connection pConexao, int pAno, boolean pUtil, int pCidade, String pApplicationColumnName){
		Date xInicio = DBSDate.toDate("31","12", String.valueOf(pAno - 1));
		Date xFim = DBSDate.toDate("31","12", String.valueOf(pAno));
		return DBSDate.getDias(pConexao, xInicio, xFim, pUtil, pCidade, pApplicationColumnName);
	}

	/**
	 * Chama o metodo getDiasDoAno().
	 * @param pConexao Conexão com banco de dados
	 * @param pAno Ano 
	 * @param pUtil indicativo se é dia útil(True) ou não(False)
	 * @param pCidade Código da Cidade que se deseja pesquisar o feriado
	 * 			      Código da cidade como -1 indica que será pesquisado feriado Nacional
	 * @return Quantidade de dias do ano informado
	 */
	public static int getDiasDoAno(Connection pConexao, int pAno, boolean pUtil, int pCidade){
		return getDiasDoAno(pConexao, pAno, pUtil, pCidade, null);
	}
	/**
	 * Chama o metodo getDiasDoAno(), considerando cidade_id = -1
	 * @param pConexao Conexão com banco de dados
	 * @param pAno Ano 
	 * @param pUtil indicativo se é dia útil(True) ou não(False)
	 * @return Quantidade de dias do ano informado
	 */
	public static int getDiasDoAno(Connection pConexao, int pAno, boolean pUtil){
		return getDiasDoAno(pConexao, pAno, pUtil, -1);
	}

	/**
	 * Retorna uma Data a partir de uma data base e um prazo.
	 * Chama o metodo getProximaData(), considerando cidade_id = -1 e pApplicationColumnName = null.
	 * @param pConexao Conexao do banco de dados
	 * @param pDataBase Data a partir do qual será efetuado o cálculo da próxima data
	 * @param pPrazo Prazo em dias que serão somados a data atual (pDataAtual). 
	 * 			     Quando for para retornar dia útil e o prazo for 0, retornará a própria data se esta for dia útil, ou a próxima data útil.
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return Próxima data
	 */
	public static Date getProximaData(Connection pConexao, Date pDataBase, int pPrazo, boolean pUtil){
		return getProximaData(pConexao, pDataBase, pPrazo, pUtil, -1, null);
	}
	
	/**
	 * Retorna uma Data a partir de uma data base e um prazo.
	 * Chama o getProximaData() considerando ApplicationColumnName = null.
	 * @param pConexao Conexao do banco de dados
	 * @param pDataBase Data a partir do qual será efetuado o cálculo da próxima data
	 * @param pPrazo Prazo em dias que serão somados a data atual (pDataAtual).
	 * 			     Quando for para retornar dia útil e o prazo for 0, retornará a própria data se esta for dia útil, ou a próxima data útil.
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 *                Código da cidade como -1, indica que o serão considerados somente feriados nacionais
	 * @return Próxima data
	 */
	public static Date getProximaData(Connection pConexao, Date pDataBase, int pPrazo, boolean pUtil, int pCidade){
		return getProximaData(pConexao, pDataBase, pPrazo, pUtil, pCidade, null);
	}
	
	/**
	 * Retorna uma Data a partir de uma data base e um prazo.
	 * @param pConexao Conexao do banco de dados
	 * @param pDataBase Data a partir do qual será efetuado o cálculo da próxima data
	 * @param pPrazo Prazo em dias que serão somados a data atual (pDataAtual)
	 * 			     Quando for para retornar dia útil e o prazo for 0, retornará a própria data se esta for dia útil, ou a próxima data útil.
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 *                Código da cidade como -1, indica que o serão considerados somente feriados nacionais
	 * @param pApplicationColumnName Nome da Coluna de aplicação específica
	 * @return Próxima data
	 */
	public static Date getProximaData(Connection pConexao, Date pDataBase, int pPrazo, boolean pUtil, int pCidade, String pApplicationColumnName){
		Date xDataFim = pDataBase;

		if (pDataBase == null){
			return null;
		}
		if (pPrazo == 0){
			if (pUtil){
				while (!isDiaUtil(pConexao, xDataFim, pCidade, pApplicationColumnName)) {
					xDataFim = DBSDate.getDateAdd(xDataFim, 1);
				}
			}
			return xDataFim;
		}
		if (pUtil){
			xDataFim = pvGetProximaDataUtil(pConexao,pDataBase,pPrazo,pCidade, pApplicationColumnName);
			return xDataFim;
		}else{
			xDataFim = DBSDate.getDateAdd(xDataFim, pPrazo);
			return xDataFim;
		}
	}
	
	/**
	 * Objetivo: Retorna o nome do Mês(conforme o idioma configurado) a partir de uma data
	 * @param pData
	 * @return Retorna o nome do Mês a partir de uma data.
	 */
	public static String getNomeDoMesAbreviado(Date pData){
		String xRetorno = "";
		if (pData == null) {
			return xRetorno;
		}
		DateFormatSymbols xDF= new DateFormatSymbols(new Locale("pt", "BR"));
    	xRetorno = xDF.getShortMonths()[DBSDate.getMes(pData)];
		return xRetorno; 
	}
	
	/**
	 * Objetivo: Retorna o nome do Mês(conforme o idioma configurado) a partir de uma data
	 * @param pData
	 * @return Retorna o nome do Mês a partir de uma data.
	 */
	public static String getNomeDoMes(Date pData){
		String xRetorno = "";
		if (pData == null) {
			return xRetorno;
		}
		return getNomeDosMeses()[DBSDate.getMes(pData)-1];// xRetorno; 
	}	

	/**
	 * Retorna array com os nomes de todos os meses
	 * @return
	 */
	public static String[] getNomeDosMeses(){
//		DateFormatSymbols xDF= new DateFormatSymbols(new Locale("pt", "BR"));
		DateFormatSymbols xDF = new DateFormatSymbols();
    	return xDF.getMonths();

	}
	/**
	 * Objetivo: Retorna nome da Semana a partir de uma data
	 * @param pData
	 * @return Nome da semana a partir de uma data.
	 */
	public static String getNomeDaSemana(Date pData){
	    int xDiaSemana = getNumeroDaSemana(pData);
	    String xRetorno = "";
    
	    if (xDiaSemana==-1) {
	    	return xRetorno;
		}
	    DateFormatSymbols xDF= new DateFormatSymbols(new Locale("pt", "BR"));
	    xRetorno = xDF.getWeekdays()[xDiaSemana];
		return xRetorno;     	
	}

	/**
	 * Objetivo: Retorna nome da Semana abreviado a partir de uma data
	 * @param pData
	 * @return Nome da semana a partir de uma data.
	 */
	public static String getNomeDaSemanaAbreviado(Date pData){
	    int xDiaSemana = getNumeroDaSemana(pData);
	    String xRetorno = "";
    
	    if (xDiaSemana==-1) {
	    	return xRetorno;
		}
	    DateFormatSymbols xDF= new DateFormatSymbols(new Locale("pt", "BR"));
	    xRetorno = xDF.getShortWeekdays()[xDiaSemana];
		return xRetorno;     	
	}
	
	/**
	 * Retorna Primeiro dia do mês
	 * @param pConexao Conexão com banco de dados
	 * @param pData Data a partir do qual será efetuado o cálculo do primeiro dia do mês
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return
	 */
	public static Date getPrimeiroDiaDoMes(Connection pConexao, Date pData, boolean pUtil){
		if (pData == null){
			return null;
		}
		Calendar xData = toCalendar(pData);
		pData = toDate(01,xData.get(Calendar.MONTH)+1,xData.get(Calendar.YEAR));
		return getProximaData(pConexao, pData, 0, pUtil);
	}
	/**
	 * Retorna último dia do Mês.
	 * @param pConexao Conexão com banco de dados
	 * @param pData Data a partir do qual será efetuado o cálculo do último dia do mês
	 * @param pUtil  Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return
	 */
	public static Date getUltimoDiaDoMes(Connection pConexao, Date pData, boolean pUtil){
		if (pData == null){
			return null;
		}
		Calendar xData = toCalendar(pData);
		//Encontra o próximo mês
		xData.add(Calendar.MONTH, 1);
		//Encontra o primeiro dia do próximo mês
		pData = getPrimeiroDiaDoMes(pConexao, toDate(xData), false);
		//Encontra o dia anterior
		return getProximaData(pConexao, pData, -1, pUtil);
	}
	/**
	 * Calcula a quantidade de dias corridos/úteis do mês
	 * @param pData Data a partir do qual será efetuado o cálculo dos dias do mês
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 * @param pApplicationColumnName Renda Fixa - RF
	 * 								 Renda Variavel - RV
	 * @return Quantidade de dias do mês
	 */
	public static int getDiasDoMes(Connection pConexao, Date pData, boolean pUtil, int pCidade, String pApplicationColumnName) {
		if (pData == null){
			return 0;
		}
		Date xInicio = getPrimeiroDiaDoMes(pConexao, pData, false);
		Date xFim = getUltimoDiaDoMes(pConexao, pData, false);
		//Ultimo dia do mês anterior
		xInicio = getProximaData(pConexao, xInicio,-1,false, pCidade); 
		return getDias(pConexao, xInicio, xFim, pUtil, pCidade, pApplicationColumnName);
	}
	
	/**
	 * Chama o metodo getDiasDoMes(), considerando pApplicationColumnName = null
	 * @param pConexao
	 * @param pData Data a partir do qual será efetuado o cálculo dos dias do mês
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 * @return
	 */
	public static int getDiasDoMes(Connection pConexao, Date pData, boolean pUtil, int pCidade) {
		return getDiasDoMes(pConexao, pData, pUtil, pCidade, null);
	}
	
	/**
	 * Chama o metodo getDiasDoMes(), considerando pCidade = -1
	 * @param pData Data a partir do qual será efetuado o cálculo dos dias do mês
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return Quantidade de dias do mês
	 */
	public static int getDiasDoMes(Connection pConexao, Date pData, boolean pUtil) {
		return getDiasDoMes(pConexao, pData, pUtil, -1);
	}

	/**
	 * Retorna Último dia do Ano
	 * @param pData Data a partir do qual será efetuado o cálculo do último dia do ano
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return Último dia do Ano
	 */
	public static Date getUltimoDiaDoAno(Connection pConexao, Date pData, boolean pUtil){
		if (pData == null){
			return null;
		}
		Calendar xData = toCalendar(pData);
		//Primeiro Dia do ano Seguinte
		pData = toDate(1,1,xData.get(Calendar.YEAR)+1);
		//Menos um dia
		return getProximaData(pConexao, pData,-1,pUtil);  
	}
	
	/**
	 * Retorna o primeiro dia do ano
	 * @param pData Data a partir do qual será efetuado o cálculo do primeiro dia do ano
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia Corrido(False)
	 * @return Primeiro dia do ano
	 */
	public static Date getPrimeiroDiaDoAno(Connection pConexao, Date pData, boolean pUtil) {
		if (pData == null){
			return null;
		}
		Calendar xData = toCalendar(pData);
		//Primeiro dia do ano
		pData = toDate(1,1,xData.get(Calendar.YEAR));
		//Retorna o próximo dia util caso pUtil=true
		if (pUtil){
			return getProximaData(pConexao, pData, 0, pUtil);
		}
		return pData;
	}
	
	/**
	 * Retorna o número que representa o dia da semana onde 1-domingo/2-Segunda/etc 
	 * @param pData
	 * @return número do dia da semana
	 */
	public static int getNumeroDaSemana(Date pData){
	    Calendar xCalendar = Calendar.getInstance();   
	    if (pData == null) {
	    	return -1;
		}	
	    xCalendar.setTime(pData); 
		return xCalendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Retorna número do mês a partir do nome.<br/>
	 * @param pMes Número do mês e número <b>0</b>(zero) caso não encontre.
	 * @return Número do mês
	 */
	public static int getNumeroDoMes(String pMes){
		String[] xMeses = DBSDate.getNomeDosMeses();
		int xI = 0;
		//Uniformiza texto
		pMes = pMes.trim().toUpperCase();
		for (String xMes : xMeses){
			//Uniformiza texto
			xMes = xMes.trim().toUpperCase();
			xI++;
			//Se encontrou o mes, retorna o número
			if (!xMes.equals("") 
			  && xMes.equals(pMes)){
				return xI; 
			}
		}
		return 0;
//
//		if (pMes.toUpperCase().equals("JANEIRO")) {
//			xRetorno = 1;
//		} 
//		if (pMes.toUpperCase().equals("FEVEREIRO")) {
//			xRetorno = 2;
//		} 
//		if (pMes.toUpperCase().equals("MARÇO")) {
//			xRetorno = 3;
//		} 
//		if (pMes.toUpperCase().equals("ABRIL")) {
//			xRetorno = 4;
//		} 
//		if (pMes.toUpperCase().equals("MAIO")) {
//			xRetorno = 5;
//		} 
//		if (pMes.toUpperCase().equals("JUNHO")) {
//			xRetorno = 6;
//		} 
//		if (pMes.toUpperCase().equals("JULHO")) {
//			xRetorno = 7;
//		} 
//		if (pMes.toUpperCase().equals("AGOSTO")) {
//			xRetorno = 8;
//		} 
//		if (pMes.toUpperCase().equals("SETEMBRO")) {
//			xRetorno = 9;
//		} 
//		if (pMes.toUpperCase().equals("OUTUBRO")) {
//			xRetorno = 10;
//		} 
//		if (pMes.toUpperCase().equals("NOVEMBRO")) {
//			xRetorno = 11;
//		} 
//		if (pMes.toUpperCase().equals("DEZEMBRO")) {
//			xRetorno = 12;
//		} 
//		return xRetorno;     
	}
	
	/**
	 * Retorna uma Data a partir de uma data base, um prazo e o dia da semana desejado
	 * @param pConexao Conexão com banco de dados
	 * @param pDataAtual Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo Dias que serão somados a data atual (pDataAtual)
	 * @param pDiaDaSemana Dia da Semana que se deseja retornar a data
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False);
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 * @param pApplicationColumnName Renda Fixa - RF
	 * 								 Renda Variavel - RV
	 * @return
	 */
	public static Date getProximaSemana(Connection pConexao, Date pDataAtual, int pPrazo, int pDiaDaSemana, boolean pUtil, int pCidade, String pApplicationColumnName){
		if (pDataAtual == null){
			return null;
		}
		if (pDiaDaSemana <= 0 || pDiaDaSemana > 7){
			return null;			
		}
		//Calcula um data aproximada a data desejada
		pDataAtual = getProximaData(pConexao, pDataAtual, pPrazo, false, pCidade);
		Calendar xData = toCalendar(pDataAtual);
		//Loop até encontrar a data que seja do dia da semana selecionado
		while (xData.get(Calendar.DAY_OF_WEEK) != pDiaDaSemana) {
			pDataAtual = getProximaData(pConexao, toDate(xData), 1, false, pCidade);
			xData.setTime(pDataAtual);
		} 
		//Se for busca uma data útil e a data encontrada não for dia útil... 
		if (pUtil && !isDiaUtil(pConexao, pDataAtual, pCidade, pApplicationColumnName)){
			int xSinal=1;
			if (pPrazo<0){
				xSinal=-1;
			}
			//Procura próxima data util
			pDataAtual = getProximaData(pConexao, pDataAtual, xSinal, true, pCidade);		
		}
		return pDataAtual;
	}
	
	/**
	 * Chama o metodo getProximaSemana(), considerando pApplicationColumnName = null
	 * @param pConexao Conexão com banco de dados
	 * @param pDataAtual Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo Dias que serão somados a data atual (pDataAtual)
	 * @param pDiaDaSemana Dia da Semana que se deseja retornar a data
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False);
	 * @param pCidade Código da cidade que será utilizada para considerar os dias úteis
	 * @return
	 */
	public static Date getProximaSemana(Connection pConexao, Date pDataAtual, int pPrazo, int pDiaDaSemana, boolean pUtil, int pCidade){
		return getProximaSemana(pConexao, pDataAtual, pPrazo, pDiaDaSemana, pUtil, pCidade, null);
	}

	/**
	 * Chama o metodo getProximaSemana(), considerando pCidade = -1
	 * @param pConexao Conexão com banco de dados
	 * @param pDataAtual Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo Dias que serão somados a data atual (pDataAtual)
	 * @param pDiaDaSemana Dia da Semana que se deseja retornar a data
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False);
	 * @return
	 */
	public static Date getProximaSemana(Connection pConexao, Date pDataAtual, int pPrazo, int pDiaDaSemana, boolean pUtil){
		return getProximaSemana(pConexao, pDataAtual, pPrazo, pDiaDaSemana, pUtil, -1);
	}

	/**
	 * Retorna data do próximo aniversário considerando diferentes tipos de vigências/periodicidade.
	 * @param pConexao Conexão do banco de dados
	 * @param pData  Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo  Prazo em periodos que serão somados a data base
	 * @param pPeriodicidade =  Indica em que base é informada o Prazo (diária, mensal, anual)
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False)
	 * @param pCidade Código da Cidade que será utilizada para considerar os dias úteis.
	 * @return Data do próximo aniversário
	 */
	public static Date getProximoAniversario(Connection pConexao, Date pData, int pPrazo, PERIODICIDADE pPeriodicidade, boolean pUtil, int pCidade, String pApplicationColumnName){
		if (pData == null 
		 || pPeriodicidade == null){
			return null;
		}
		Calendar xData = toCalendar(pData);
		if (pPeriodicidade == PERIODICIDADE.DIARIA){
			xData.add(Calendar.DAY_OF_MONTH, pPrazo);
		} else if (pPeriodicidade == PERIODICIDADE.MENSAL){
			xData.add(Calendar.MONTH, pPrazo);
		} else if (pPeriodicidade == PERIODICIDADE.ANUAL) {
			xData.add(Calendar.YEAR, pPrazo);
		}
		return getProximaData(pConexao, toDate(xData), 0, pUtil, pCidade, pApplicationColumnName);		
	}
	
	/**
	 * Retorna data do próximo aniversário considerando diferentes tipos de vigências/periodicidade.
	 * @param pConexao Conexão do banco de dados
	 * @param pData  Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo  Prazo em periodos que serão somados a data base
	 * @param pPeriodicidade Indica em que base é informada o Prazo (diária, mensal, anual)
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False)
	 * @return Data do próximo aniversário
	 */
	public static Date getProximoAniversario(Connection pConexao, Date pData, int pPrazo, PERIODICIDADE pPeriodicidade, boolean pUtil, int pCidade){
		return getProximoAniversario(pConexao, pData, pPrazo, pPeriodicidade, pUtil, pCidade, null);
	}
	
	/**
	 * Retorna data do próximo aniversário considerando diferentes tipos de vigências/periodicidade e somente feriádos nacionais(BR).
	 * @param pConexao Conexão do banco de dados
	 * @param pData  Data a partir do qual será efetuado o cálculo do próximo aniversário
	 * @param pPrazo  Prazo em periodos que serão somados a data base
	 * @param pPeriodicidade Indica em que base é informada o Prazo (diária, mensal, anual)
	 * @param pUtil Indica se o cálculo será em dia útil(True) ou dia corrido(False)
	 * @return Data do próximo aniversário
	 */
	public static Date getProximoAniversario(Connection pConexao, Date pData, int pPrazo, PERIODICIDADE pPeriodicidade, boolean pUtil){
		return getProximoAniversario(pConexao, pData, pPrazo, pPeriodicidade, pUtil, -1);		
	}	
	
	/**
	 * Retorna data do próximo vencimento considerando diferentes tipos de vigências/periodicidade
	 * @param pParcela Quantidade de parcelas que serão somados a data base
	 * @param pPrimeiraParcela Data da primeira parcela para calcular o vencimento
	 * @param pPeriodicidade Indica em que base é informada o Prazo (diária, mensal, anual)
	 * @param pPrazo Prazo em periodos que serão somados a data base
	 * @param pUtil
	 * @param pConexao Conexão do banco de dados
	 * @return
	 */
//	public static Date getVencimento(Connection pConexao, int pParcela, Date pPrimeiraParcela, PERIODICIDADE pPeriodicidade, int pPrazo, boolean pUtil, String pApplicationColumnName){
//		if (pPrimeiraParcela == null || pPeriodicidade == null){
//			return null;
//		}
//		return getProximoAniversario(pConexao, pPrimeiraParcela, pPrazo * DBSNumber.toInteger(pParcela-1), pPeriodicidade, pUtil, -1, pApplicationColumnName);
//	}
//	
//	//MOVIDO DE DBSFND - era usado em CALCULAPUBEAN
	public static Date getVencimento(Connection pConnection, Integer pParcelas, Date pPrimeiraParcela, PERIODICIDADE pPeriodicidade, Integer pPrazo, boolean pUtil, String pApplicationColumnName) {
		Date xVencimento = pPrimeiraParcela;
		
	    if (DBSObject.isEmpty(xVencimento)) return xVencimento;
	    if (PERIODICIDADE.DIARIA.equals(pPeriodicidade)) {
	        xVencimento = DBSDate.getProximaData(pConnection, xVencimento, pPrazo * (pParcelas - 1), false, -1, pApplicationColumnName);
	        if (pUtil) {
	            if (!DBSDate.isDiaUtil(pConnection, xVencimento, -1)) {
	                xVencimento = DBSDate.getProximaData(pConnection, xVencimento, 1, true, -1, pApplicationColumnName);
	            }
	        }
		} else {
	        xVencimento = DBSDate.getProximoAniversario(pConnection, xVencimento, pPrazo * DBSNumber.toInteger(pParcelas - 1), pPeriodicidade, pUtil, -1, pApplicationColumnName);
		}
		return xVencimento;
	}
	
	/**
	 * Adiciona minutos a uma data informada.
	 * @param Data e Hora
	 * @param pMinutes
	 * @return
	 */
	public static Date getDateAddMinutes(Date pDate, int pMinutes){
		LocalDateTime xDT = new LocalDateTime(pDate);
		xDT = xDT.plusMinutes(pMinutes);
		return DBSDate.toDate(xDT.toDateTime());
	}
	
	/**
	 * Adiciona minutos a uma data informada.
	 * @param Data e Hora
	 * @param pMinutes
	 * @return
	 */
	public static Date getTimestampAddMinutos(Timestamp pDate, int pMinutes){
		Date xDate = DBSDate.toDate(pDate);
		return getDateAddMinutes(xDate, pMinutes);
	}

	/**
	 * Adiciona minutos a uma data informada.
	 * @param pDate Data e Hora
	 * @param pSeconds
	 * @return
	 */
	public static Date getDateAddSeconds(Date pDate, int pSeconds){
		LocalDateTime xDT = new LocalDateTime(pDate);
		xDT = xDT.plusSeconds(pSeconds);
		return DBSDate.toDate(xDT.toDateTime());
	}
	

	/**
     * Parse de string para data.
     * @param pValue String no formato YYYYMMDD.
     * @return Date
     */
	public static Date toDateYYYYMMDD(String pValue) {
		Date xData = null;
		String xAno = DBSString.getSubString(pValue, 1, 4);
		String xMes = DBSString.getSubString(pValue, 5, 2);
		String xDia = DBSString.getSubString(pValue, 7, 2);
		xData = toDate(xDia, xMes, xAno);
		return xData;
	}
	
	/**
    * Parse de string para data.
    * @param pValue String no formato YYMMDD.
    * @return Date
    */
	public static Date toDateYYMMDD(String pValue) {
		Date xData = null;
		String xAno = DBSString.getSubString(pValue, 1, 2);
		String xMes = DBSString.getSubString(pValue, 3, 2);
		String xDia = DBSString.getSubString(pValue, 5, 2);
		xData = toDate(xDia, xMes, xAno);
		return xData;
	}

	//========================================================================================
	// privates
	//========================================================================================
	private static Date pvGetProximaDataUtil(Connection pConexao, Date pDataBase, int pPrazo, int pCidade, String pApplicationColumnName){
		Date xDataFim = DBSDate.getDateAdd(pDataBase, pPrazo);
		int xDiasNaoUteis = getFinaisDeSemana(pDataBase, xDataFim) + 
				            getFeriados(pConexao, pDataBase, xDataFim, pCidade, pApplicationColumnName);
		if (pPrazo < 0){
			xDiasNaoUteis = -xDiasNaoUteis; 
		}
		if (xDiasNaoUteis!=0){
			xDataFim = pvGetProximaDataUtil(pConexao,xDataFim,xDiasNaoUteis,pCidade, pApplicationColumnName);
		}
		return xDataFim;
	}

	private static Date pvToDateLong(String pData, String pDateFormat) {
		if (pData == null){
			return null;
		}
	
		DateFormat xFormat = DateFormat.getDateInstance(DateFormat.LONG,  new Locale("pt", "BR"));
		xFormat = new SimpleDateFormat(pDateFormat);
	    Date xDate = new Date(0);
		xFormat.setLenient(false);
		try {
			xDate.setTime(xFormat.parse(pData).getTime());
		} catch (ParseException e) {
			wLogger.error(e);
			return null;
		}   
		return xDate;
	}


}
