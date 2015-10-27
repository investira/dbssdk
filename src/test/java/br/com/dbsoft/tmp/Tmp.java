package br.com.dbsoft.tmp;



import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;


import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;



public class Tmp {
	
	private static Integer Banco = 1;
	private static Integer Dtvm = 2;
	private static Integer Ctvm= 4;
	private static Integer Fundo = 8;
	
	
//	@Test
//	public void aaabv(){
//		DateTime xD3 = new DateTime(2001,01,01,12,00,00);
//		DateTime xD4 = new DateTime(2002,01,02,12,00,00);
//		Date xD1 = DBSDate.toDate(xD3.toDate());
//		Date xD2 = DBSDate.toDate(xD4.toDate());
////		Date xD1 = DBSDate.toDateDMYHMS("01/01/2001 12:00:00");
////		Date xD2 = DBSDate.toDateDMYHMS("01/01/2001 13:00:00");
//		System.out.println((xD2.getTime() - xD1.getTime()) / 1000);
//		System.out.println(DBSDate.getTimeDif(xD3, xD4));
//		
//	}
	
	
//	@Test
	public void galtonBoard(){
		Random xR = new Random();
		Integer xTotalBolas = 1000000;
		Integer xTotalVezes = 20;
		Integer xSoma = 0;
		Map<Integer, Integer> xSaldo = new HashMap<Integer, Integer>(); 
		Integer xZ = 0;
		Integer xU = 0;
//		Integer[] xInteger = new Integer[xTotalVezes];
		for (int xA=1;xA <= xTotalBolas;xA++){
			xSoma = 0;
			for (int xI=1;xI <= xTotalVezes;xI++){
				int xB = xR.nextInt(2);
				if (xB == 0){
					xSoma--;
					xZ++;
				}else if (xB == 1){
					xSoma++;
					xU++;
				}else{
					System.out.println("STOP");
				}
			}
			xSaldo.put(xSoma, DBSObject.getNotNull(xSaldo.get(xSoma),0) + 1);
		}
		System.out.println("0=" + xZ);
		System.out.println("1=" + xU);
		for (Integer xKey: xSaldo.keySet()){
			System.out.println(xKey + "\t" + xSaldo.get(xKey) + "\t" + DBSFormat.getFormattedNumber(DBSNumber.multiply(DBSNumber.divide(xSaldo.get(xKey), xTotalBolas), 100), 2));
		}
	}	
	
	//COnta quantas vezes o valor foi seguidamente iqual.
//	@Test
	public void galtonBoard2(){
		Random xR = new Random();
		Long xTotalBolas = 1000000000000L;
//		Long xTotalBolas = 100000L;
		Long xSoma = 1L;
		Map<Integer, Long> xSaldo = new HashMap<Integer, Long>(); 
		Long xZ = 0L;
		Long xU = 0L;
		int xAnterior = -1;
		int xAtual = -1;
//		Integer[] xInteger = new Integer[xTotalVezes];
		for (Long xA=1L;xA <= xTotalBolas;xA++){
			xAtual = xR.nextInt(2);
			if (xAtual == 0){
				xZ++;
			}else if (xAtual == 1){
				xU++;
			}
			if (xAnterior == -1){
				xAnterior = xAtual;
			}else{
				if (xAtual == xAnterior){
					xSoma++;
				}else{
					Long xContador = DBSObject.getNotNull(xSaldo.get(xSoma.intValue()),0L) + 1;
					xSaldo.put(xSoma.intValue(), xContador);
					xAnterior = xAtual;
					xSoma = 1L;
				}
			}
		}
		if (xAtual == xAnterior){
			Long xContador = DBSObject.getNotNull(xSaldo.get(xSoma.intValue()),0L) + 1;
			xSaldo.put(xSoma.intValue(), xContador);
		}
		System.out.println("0=" + xZ);
		System.out.println("1=" + xU);
		for (Integer xKey: xSaldo.keySet()){
			System.out.println(xKey + "\t" + xSaldo.get(xKey) + "\t" + DBSFormat.getFormattedNumber(DBSNumber.multiply(DBSNumber.divide(xSaldo.get(xKey) * xKey, xTotalBolas), 100), 8) + "%");
		}
	}	

	//	@Test
	public void teste_bit(){
		Integer xImport = 0;
		
		xImport = xImport & ~Banco;
		System.out.println(xImport);
		
		xImport = xImport & ~Banco;
		System.out.println(xImport);
		
		xImport = xImport | Dtvm;
		System.out.println(xImport);

		xImport = xImport & ~Ctvm;
		System.out.println(xImport);
		
		int xAtual = Banco & Fundo;
	}	

//	@Test
	public void teste_bit2(){
		Integer xImport = 0;
		
		xImport |= 1;
		System.out.println(xImport);
		
		xImport |= 2;
		System.out.println(xImport);
		
		xImport |= 4;
		System.out.println(xImport);

		xImport |= 8;
		System.out.println(xImport);

		xImport |= 8;
		System.out.println(xImport);

		xImport |= 4;
		System.out.println(xImport);
	}	

//	@Test
	public void teste_date(){
		Long xTime = 0L;
		xTime = System.currentTimeMillis();
		System.out.println(DBSDate.toTimestamp(xTime));
//		System.out.println(DBSDate.toDateYMDHMS("2014-01-02 10:10:01").toLocaleString());
//		System.out.println(DBSDate.toDateYMDHMS("2014/01/02 10:10:01").toLocaleString());
//		System.out.println(DBSDate.toDateYMDHMS("20140102000201").toGMTString());
//		System.out.println(DBSDate.toDateYMDHMS("2014a102000201").toLocaleString());
//		assertEquals(DBSDate.toDateYMDHMS("2014-01-02 10:10:01").toLocaleString(), "02/01/2014 10:10:01");
//		assertEquals(DBSDate.toDateYMDHMS("2014/01/02 10:10:01").toLocaleString(), "02/01/2014 10:10:01");
//		assertEquals(DBSDate.toDateYMDHMS("20140102000201").toLocaleString(), "02/01/2014 00:02:01");
	}
	
//	@Test
	public void teste_getToCalendar() {
//		System.out.println(TimeZone.getDefault().getDisplayName());
//		System.out.println(TimeZone.getDefault().getID());
//		System.out.println(TimeUnit.MILLISECONDS.toSeconds(xTime));
//		System.out.println(TimeUnit.MILLISECONDS.toMinutes(xTime));
//		System.out.println(TimeUnit.MILLISECONDS.toHours(xTime));

		System.out.println("---");
		Long 		xTime = 1000L;
		DateTime	xDateTime;
		
		System.out.println("a1:" + DBSDate.toDate(xTime));
		System.out.println("a2:" + DBSDate.toTime(xTime));
		System.out.println("a3:" + DBSDate.toDateTime(xTime));
		System.out.println("a4:" + DBSDate.toDateTime(xTime));
		System.out.println("a5:" + DBSDate.toTimestamp(xTime));
		System.out.println("a6:" + DBSDate.toCalendar(xTime).getTimeInMillis());

		xDateTime = DBSDate.toDateTime(xTime);	
		System.out.println("t1:" + xDateTime.toLocalDate());
		xDateTime = DBSDate.toDateTime(xDateTime.getMillis());
		System.out.println("t2:" + xDateTime.toLocalDate());
		xDateTime = DBSDate.toDateTime(xDateTime.getMillis());
		System.out.println("t3:" + xDateTime.toLocalDate());
		xDateTime = DBSDate.toDateTime(xDateTime.getMillis());
		System.out.println("t4:" + xDateTime.toLocalDate());
		
//		xTime = xDTZ.convertLocalToUTC(xTime, true);
//		System.out.println("t21:" + DBSDate.toDate(xTime));
//		System.out.println("t22:" + DBSDate.toDateTime(xTime));
//		xDateTime = new DateTime(xTime, DateTimeZone.getDefault());
//		System.out.println(xTime + ":" + xDateTime.getZone());
//		show(xDateTime);
//
//		xDateTime = new DateTime(xTime, DateTimeZone.UTC);
//		System.out.println(xTime + ":" + xDateTime.getZone());
//		show(xDateTime);
		System.out.println("---");
	}
	

	/**
	 * Retornar data no tipo Calender a partir de data no tipo Timestamp
	 * @param pTime do tipo Date que se seja converte
	 * @return Data convertida para o tipo Calendar
	 */
	public static LocalDateTime show(DateTime pDateTime){
//		DateTime xDate = new DateTime(pTime, DateTimeZone.forOffsetMillis(0));
		System.out.println("a:" + pDateTime.toString());
		System.out.println("b:" + pDateTime.toDateTime());
		System.out.println("c:" + pDateTime.toDate());

		return null;
	}
}
