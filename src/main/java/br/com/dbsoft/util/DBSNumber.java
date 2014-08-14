package br.com.dbsoft.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.log4j.Logger;


public class DBSNumber {
	protected static Logger			wLogger = Logger.getLogger(DBSNumber.class);
	
	/**
	 * Subtração (pMinuendo - ppSubtraendo)
	 * @param pMinuendo Minuendo 
	 * @param pSubtraendo Subtraendo 
	 * @return Resultado
	 */
	public static Double subtract(Double pMinuendo, Double pSubtraendo){
		if (DBSObject.isEmpty(pMinuendo) 
		 || DBSObject.isEmpty(pSubtraendo)){
			return null;
		}

		BigDecimal x = new BigDecimal(pMinuendo.toString());
		BigDecimal xY = new BigDecimal(pSubtraendo.toString());
		return x.subtract(xY).doubleValue();
	}
	
	/**
	 * Subtração (pMinuendo - ppSubtraendo)
	 * @param pMinuendo Minuendo 
	 * @param pSubtraendo Subtraendo 
	 * @return Resultado
	 */
	public static Double subtract(Object pX, Object pY){
		return subtract(DBSNumber.toDouble(pX), DBSNumber.toDouble(pY));
	}
	
	/**
	 * Adição (pX + pY)
	 * @param pX 
	 * @param pY 
	 * @return Resultado
	 */
	public static Double add(Double pX, Double pY){
		if (DBSObject.isEmpty(pX)
		||	DBSObject.isEmpty(pY)){
			return null;
		}
		BigDecimal x = new BigDecimal(pX.toString());
		BigDecimal xY = new BigDecimal(pY.toString());
		return x.add(xY).doubleValue();
	}
	
	/**
	 * Adição (pX + pY)
	 * @param pX 
	 * @param pY 
	 * @return Resultado
	 */
	public static Double add(Object pX, Object pY){
		return add(DBSNumber.toDouble(pX), DBSNumber.toDouble(pY));
	}
	
	/**
	 * Multiplicação (pX * pY)
	 * @param pX 
	 * @param pY 
	 * @return Resultado
	 */
	public static Double multiply(Double pX, Double pY){
		if (DBSObject.isEmpty(pX) ||
			DBSObject.isEmpty(pY)){
			return null;
		}
		BigDecimal x = new BigDecimal(pX.toString());
		BigDecimal xY = new BigDecimal(pY.toString());
		return x.multiply(xY).doubleValue();
	}	

	/**
	 * Multiplicação (pX * pY)
	 * @param pX 
	 * @param pY 
	 * @return Resultado
	 */
	public static BigDecimal multiply(BigDecimal pX, BigDecimal pY){
		if (DBSObject.isEmpty(pX) ||
				DBSObject.isEmpty(pY)){
			return null;
		}
		BigDecimal x = new BigDecimal(pX.toString());
		BigDecimal xY = new BigDecimal(pY.toString());
//		return x.multiply(xY).doubleValue();
		return x.multiply(xY)              ;
	}	

	/**
	 * Multiplicação (pX * pY).b<br/>
	 * @param pX 
	 * @param pY 
	 * @return Resultado. Retorna nulo caso um dos valores seja nulo.
	 */
	public static BigDecimal multiply(Object pX, Object pY){
		if (DBSObject.isEmpty(pX) ||
			DBSObject.isEmpty(pY)){
			return null;
		}
		BigDecimal x = new BigDecimal(pX.toString());
		BigDecimal xY = new BigDecimal(pY.toString());
		return x.multiply(xY);
	}
	
	/**
	 * Divisão (pDividendo / pDivisor)
	 * @param pDividendo Dividendo
	 * @param pDivisor Divisor
	 * @return Resultado
	 */
	public static Double divide(Double pDividendo, Double pDivisor){
		BigDecimal x = divide(pDividendo, pDivisor, 30);
		if (x != null){
			return x.doubleValue();
		}else{
			return null;
		}
	}	

	/**
	 * Divisão (pDividendo / pDivisor)
	 * @param pDividendo Dividendo
	 * @param pDivisor Divisor
	 * @return Resultado
	 */
	public static BigDecimal divide(BigDecimal pDividendo, BigDecimal pDivisor){
		return divide(pDividendo, pDivisor, 30);
	}	

	/**
	 * Divisão (pDividendo / pDivisor)
	 * @param pDividendo Dividendo
	 * @param pDivisor Divisor
	 * @return Resultado
	 */
	public static BigDecimal divide(Object pDividendo, Object pDivisor){
		return divide(pDividendo, pDivisor, 30);
	}
	
	/**
	 * Divisão (pDividendo / pDivisor)
	 * @param pDividendo Dividendo
	 * @param pDivisor Divisor
	 * @return Resultado
	 */
	public static BigDecimal divide(Object pDividendo, Object pDivisor, int pCasas){
		if (DBSObject.isEmpty(pDividendo) ||
			DBSObject.isEmpty(pDivisor)){
			return null;
		}
		BigDecimal x = new BigDecimal(pDividendo.toString());
		BigDecimal xY = new BigDecimal(pDivisor.toString());
		return x.divide(xY, pCasas, RoundingMode.UP);
	}	

	
	/**
	 * Exponenciação (pX ^ pY)
	 * @param pBase Base
	 * @param pExpoente Expoente
	 * @return Resultado
	 */
	public static Double exp(Double pBase, Double pExpoente){
		if (DBSObject.isEmpty(pBase) ||
			DBSObject.isEmpty(pExpoente)){
			return null;
		}
		Double x = Math.pow(pBase, pExpoente);
		return x;
	}	
	/**
	 * Exponenciação (pX ^ pY)
	 * @param pBase Base
	 * @param pExpoente Expoente
	 * @return Resultado
	 */
	public static BigDecimal exp(BigDecimal pBase, BigDecimal pExpoente){
		if (DBSObject.isEmpty(pBase) ||
			DBSObject.isEmpty(pExpoente)){
			return null;
		}
		BigDecimal x = DBSNumber.toBigDecimal(Math.pow(DBSNumber.toDouble(pBase), DBSNumber.toDouble(pExpoente)));
		return x;
	}
	
	/**
	 * Logaritmo
	 * @param pX Valor que se deseja retornar o Log
	 * @return Restorna log do pX
	 */
	public static Double log(Double pX){
		if (DBSObject.isEmpty(pX)){
			return null;
		}
		Double x = Math.log(pX);
		return x;
	}
	
	/**
     * Emulates Excel/Calc's PMT(interest_rate, number_payments, PV, FV, Type)
     * function, which calculates the mortgage or annuity payment / yield per
     * period.
     * 
     * @param r - periodic interest rate represented as a decimal.
     * @param nper - number of total payments / periods.
     * @param pv - present value -- borrowed or invested principal.
     * @param fv - future value of loan or annuity.
     * @param type - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing periodic payment amount.
     */
    public static double pmt(double r, int nper, double pv, double fv, int type) {
        // pmt = r / ((1 + r)^N - 1) * -(pv * (1 + r)^N + fv)
        double pmt = r / (Math.pow(1 + r, nper) - 1) * -(pv * Math.pow(1 + r, nper) + fv);
    
        // account for payments at beginning of period versus end.
        if (type == 1) {
            pmt /= (1 + r);
    	}
    
        // return results to caller.
        return pmt;
    }
    
    /**
     * Overloaded pmt() call omitting type, which defaults to 0.
     * 
     * @see #pmt(double, int, double, double, int)
     */
    public static double pmt(double r, int nper, double pv, double fv) {
        return pmt(r, nper, pv, fv, 0);
    }
    
    /**
     * Overloaded pmt() call omitting fv and type, which both default to 0.
     * 
     * @see #pmt(double, int, double, double, int)
     */
    public static double pmt(double r, int nper, double pv) {
        return pmt(r, nper, pv, 0);
    }
    
    /**
     * Emulates Excel/Calc's IPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that is the interest on previous balance.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param per
     *            - period (payment number) to check value at.
     * @param nper
     *            - number of total payments / periods.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param fv
     *            - future value of loan or annuity.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing interest portion of payment.
     * 
     * @see #pmt(double, int, double, double, int)
     * @see #fv(double, int, double, double, int)
     */
    public static double ipmt(double r, int per, int nper, double pv, double fv, int type) {
    
        // Prior period (i.e., per-1) balance times periodic interest rate.
        // i.e., ipmt = fv(r, per-1, c, pv, type) * r
        // where c = pmt(r, nper, pv, fv, type)
        double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type), pv, type) * r;
    
        // account for payments at beginning of period versus end.
        if (type == 1)
            ipmt /= (1 + r);
    
        // return results to caller.
        return ipmt;
    }
    
    /**
     * Emulates Excel/Calc's FV(interest_rate, number_payments, payment, PV,
     * Type) function, which calculates future value or principal at period N.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param nper
     *            - number of total payments / periods.
     * @param c
     *            - periodic payment amount.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing future principal value.
     */
    public static double fv(double r, int nper, double c, double pv, int type) {
    
        // account for payments at beginning of period versus end.
        // since we are going in reverse, we multiply by 1 plus interest rate.
        if (type == 1)
            c *= (1 + r);
    
        // fv = -(((1 + r)^N - 1) / r * c + pv * (1 + r)^N);
        double fv = -((Math.pow(1 + r, nper) - 1) / r * c + pv * Math.pow(1 + r, nper));
    
        // return results to caller.
        return fv;
    }
    

    
    /**
     * Overloaded fv() call omitting type, which defaults to 0.
     * 
     * @see #fv(double, int, double, double, int)
     */
    public static double fv(double r, int nper, double c, double pv) {
        return fv(r, nper, c, pv);
    }
    
    
    /**
     * Emulates Excel/Calc's PPMT(interest_rate, period, number_payments, PV,
     * FV, Type) function, which calculates the portion of the payment at a
     * given period that will apply to principal.
     * 
     * @param r
     *            - periodic interest rate represented as a decimal.
     * @param per
     *            - period (payment number) to check value at.
     * @param nper
     *            - number of total payments / periods.
     * @param pv
     *            - present value -- borrowed or invested principal.
     * @param fv
     *            - future value of loan or annuity.
     * @param type
     *            - when payment is made: beginning of period is 1; end, 0.
     * @return <code>double</code> representing principal portion of payment.
     * 
     * @see #pmt(double, int, double, double, int)
     * @see #ipmt(double, int, int, double, double, int)
     */
    public static double ppmt(double r, int per, int nper, double pv, double fv, int type) {
    
        // Calculated payment per period minus interest portion of that period.
        // i.e., ppmt = c - i
        // where c = pmt(r, nper, pv, fv, type)
        // and i = ipmt(r, per, nper, pv, fv, type)
        return pmt(r, nper, pv, fv, type) - ipmt(r, per, nper, pv, fv, type);
    }

	/**
	 * Método de cálculo de TIR onde o Valor Presente Líquido (VPL)
	 * por padrão é zero.
	 * 
	 * @param pFluxo
	 * @param pPeriodo periodo em dias para cada fluxo
	 * @return double
	 */
	public static double TIR(double[] pFluxo, int[] pPeriodo) {
		return TIR(pFluxo, pPeriodo, 0.00001);
	}
	
	/**
	 * Método retirado da biblioteca org.apache.poi.ss.formula.functions.Irr
	 * Este método foi modificado para retornar 0 caso o numero de iterações
	 * exceda o número do fluxos.
	 * 
	 * @param pFluxo
	 * @param pVPL
	 * @return
	 */
	public static double TIR(double[] pFluxo, int[] pPeriodo, double pVPL) {
		int 	xIteracoesMaxima = 20; //Numero de tentativas de cálculo
        double 	xPrecisao = 1E-7;
        double	x0 = pVPL;
        double 	x1;

        int xCount = 0;
        while (xCount < xIteracoesMaxima) { //pFluxo.length

        	//O valor da função (VPL) e o seu derivado são calculados no mesmo loop
            double xValor = 0;
            double fDerivado = 0;
            for (int xIndice = 0; xIndice < pFluxo.length; xIndice++) {
            	if (DBSObject.isEmpty(pPeriodo)) {
            		xValor += pFluxo[xIndice] / Math.pow(1.0 + x0, xIndice);
            		fDerivado += -xIndice * pFluxo[xIndice] / Math.pow(1.0 + x0, xIndice + 1);
            	} else {
            		xValor += pFluxo[xIndice] / Math.pow(1.0 + x0, pPeriodo[xIndice]);
            		fDerivado += -(pPeriodo[xIndice] * pFluxo[xIndice]) / Math.pow(1.0 + x0, pPeriodo[xIndice] + 1);
            	}
            }

            //Método Newton-Raphson
            x1 = x0 - xValor/fDerivado;

            if (Math.abs(x1 - x0) <= xPrecisao) {
                return x1;
            }

            x0 = x1;
            ++xCount;
        }
        //Se o numero de iterações for maio que o tamanho do fluxo. 
        return 0.0;
    }

	/**
	 * Calcula preço médio utilizando média ponderada
	 * @param pPuAtual 				Preço atual
	 * @param pQuantidadeAtual 		Quantidade atual
	 * @param pPuOperado 			Preço da operação
	 * @param pQuantidadeOperada	Quantidade da operação
	 * @param pExclusao				Indica que está sendo uma operação de exclusão
	 * @return						Pu médio calculado
	 */
	public static BigDecimal calculaPuMedio(Object pPuAtual, Object pQuantidadeAtual, Object pPuOperado, Object pQuantidadeOperada, boolean pExclusao){
		if (pPuAtual == null 
		 || pQuantidadeAtual == null 
		 || pPuOperado == null 
		 ||	pQuantidadeOperada == null){
			return null;
		}
		
		Double 		xSaldo;
		Double 		xFinOperado;
		Double 		xFinAtual;
		
		BigDecimal 	xPuAtual = DBSNumber.toBigDecimal(pPuAtual);
		BigDecimal 	xPuOperado = DBSNumber.toBigDecimal(pPuOperado);
		Double 		xQuantidadeAtual = DBSNumber.toDouble(pQuantidadeAtual);
		Double 		xQuantidadeOperada = DBSNumber.toDouble(pQuantidadeOperada);
		
		xSaldo = DBSNumber.add(xQuantidadeAtual, xQuantidadeOperada);
		
		if (xSaldo.equals(0D)){
			return xPuAtual;
		} else {
			if (isRealizacao(pQuantidadeAtual, pQuantidadeOperada) == pExclusao){
				/* 
				 * Preço médio deve ser calcula quando:
	             * Ocorrer uma nova movimentação com o mesmo sinal da posição ou
	             * for excluido uma movimentação que mudou a preço médio.
	             */
				xFinOperado = DBSNumber.multiply(pQuantidadeOperada, pPuOperado).doubleValue();
				xFinAtual = DBSNumber.multiply(pQuantidadeAtual, pPuAtual).doubleValue();
				
				return DBSNumber.toBigDecimal(DBSNumber.divide(DBSNumber.add(xFinOperado, xFinAtual), xSaldo, 30));
			} else {
				if (isPosicaoVirada(pQuantidadeAtual, pQuantidadeOperada)){
					return xPuOperado;
				} else {
					/*
					 * Mantém o preço da posição quando:
					 *   movimentação for realização sem virar a posição ou for exclusão da mesma
					 */
					return xPuAtual;
				}
			}
		}	
	}

	/**
	 * Verifica se é uma virada de posição ou inicio de uma nova posição (partindo do estoque zerado).
     * Verifica se posição passa a ficar negativa quando estava positiva ou vice-versa.
	 * @param pQuantidadeAtual		Quantidade atual
	 * @param pQuantidadeOperada	Quantidade da operação
	 * @return TRUE para virada de posição e FALSE caso contrário 
	 */
	public static boolean isPosicaoVirada(Object pQuantidadeAtual, Object pQuantidadeOperada){
		if (pQuantidadeAtual == null || 
			pQuantidadeOperada == null){
			return false;
		}
		
		Double xSaldo;
		Double xQuantidadeAtual = DBSNumber.toDouble(pQuantidadeAtual);
		Double xQuantidadeOperada = DBSNumber.toDouble(pQuantidadeOperada);
		xSaldo = DBSNumber.add(xQuantidadeAtual, xQuantidadeOperada);
		
		/*
		 * Posição é considerada virada quando:
		 * xSaldo(Novo Saldo) diferente de zero e
		 * Sinal de xSaldo deferente do estoque atual(pQuantidadeAtual) ou estoque atual(pQuantidadeAtual) = 0  
	    */
		
		if (xSaldo != 0D && 
		    (DBSNumber.sign(xSaldo).intValue() != DBSNumber.sign(xQuantidadeAtual).intValue() 
		   || xQuantidadeAtual == 0)){
			return true;
		}
		return false;
	}

	/**
	 * Verifica se a operação é uma realização da posição.
	 * @param pQuantidadeAtual		Quantidade Atual
	 * @param pQuantidadeOperada	Quantidade que está sendo operada
	 * @return TRUE é Operação de realização, FALSE não é operação de realização
	 */
	public static boolean isRealizacao(Object pQuantidadeAtual, Object pQuantidadeOperada){
		if (pQuantidadeAtual == null || pQuantidadeOperada == null){
			return false;
		}
		Double xQuantidadeAtual = DBSNumber.toDouble(pQuantidadeAtual);
		Double xQuantidadeOperada= DBSNumber.toDouble(pQuantidadeOperada);		
		
		if ((xQuantidadeAtual < 0 && xQuantidadeOperada > 0) || 
			(xQuantidadeAtual > 0 && xQuantidadeOperada < 0)){
			return true;
		}
		return false;
	}
	/**
	 * Sinal do valor
	 * @param pValor Valor que se deseja conhecer o sinal
	 * @return Retorna 0 = Zero / 1 = Positivo / 1 = Negativo
	 */
	public static Double sign(Double pValor){
		if (DBSObject.isEmpty(pValor)){
			return null;
		}	
		return Math.signum(pValor);
	}

	/**
	 * Retornar o valor sem sinal
	 * @param pValor Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 */
	public static Double abs(Double pValor){
		if (DBSObject.isEmpty(pValor)){
			return null;
		}		
		return Math.abs(pValor);
	}
	
	/**
	 * Retornar o valor sem sinal
	 * @param pValor Valor que se deseja retinar o sinal
	 * @return Restorna valor sem sinal
	 */
	public static Double abs(Object pValor){
		return abs(toDouble(pValor));
	}
	
	/**
	 * Retorna um número randomico 0 > 1.
	 * @return Retorna número randômico
	 */
	public static Double random(){
		return (double) Math.random();
	}
	
	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar até parte inteira. 
	 * @param pValor Valor que se deseja truncar
	 * @param pCasasDecimais Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Double trunc(Object pValor, Integer pCasasDecimais){ 
		if (DBSObject.isEmpty(pValor) ||
			DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}	
		BigDecimal xValor = toBigDecimal(pValor);
		return toDouble(trunc(xValor, pCasasDecimais));
	}

	/**
	 * Retorna valor truncado.<br/>
	 * Caso a quantidade de casas seja negativa irá truncar até parte inteira. 
	 * @param pValor Valor que se deseja truncar
	 * @param pCasasDecimais Quantidade de casas decimais
	 * @return Resultado
	 */
	public static BigDecimal trunc(BigDecimal pValor, Integer pCasasDecimais){
		if (DBSObject.isEmpty(pValor) ||
			DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}	
		BigDecimal xAjuste = new BigDecimal(Math.pow(10, pCasasDecimais));
		BigDecimal xInteiro;
		//Multiplica pela quantidade de casas a serem considerada
		pValor = DBSNumber.multiply(pValor, xAjuste);
		//Retorna a parte inteira;
		xInteiro = new BigDecimal(pValor.toBigInteger().toString());
		pValor = DBSNumber.toBigDecimal(DBSNumber.divide(xInteiro, xAjuste, pCasasDecimais));
		return pValor;
	}

	
	/**
	 * Retorna valor arredondado (exemplo 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar até parte inteira. 
	 * @param pValor Valor que se deseja arredondar
	 * @param pCasasDecimais Quantidade de casas decimais
	 * @return Resultado
	 */
	public static Double round(Object pValor, Integer pCasasDecimais){
		if (DBSObject.isEmpty(pValor) ||
			DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}	

		BigDecimal xValor = toBigDecimal(pValor);
		return toDouble(round(xValor, pCasasDecimais));
	}

	/**
	 * Retorna valor arredondado (exemplo 1,45 = 1,5  | 1,44 = 1,4).<br/>
	 * Caso a quantidade de casas seja negativa irá arrendodar até parte inteira. 
	 * @param pValor Valor que se deseja arredondar
	 * @param pCasasDecimais Quantidade de casas decimais
	 * @return Resultado
	 */

	public static BigDecimal round(BigDecimal pValor, Integer pCasasDecimais){
		if (DBSObject.isEmpty(pValor) ||
			DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}	
		BigDecimal xAjuste = new BigDecimal( Math.pow(10, pCasasDecimais));
		BigDecimal xInteiro;
		//Multiplica pela quantidade de casas a serem considerada
		pValor = DBSNumber.multiply(pValor, xAjuste);
		//Arredonda
		pValor = new BigDecimal( Math.rint(pValor.doubleValue()));
		//Retorna a parte inteira;
		xInteiro = new BigDecimal(pValor.toBigInteger());
		pValor = DBSNumber.toBigDecimal(DBSNumber.divide(xInteiro, xAjuste, pCasasDecimais));
		return pValor;
	}

	
	/**
	 * Retorna valor inteiro 
	 * @param pValor Valor que se deseja a parte inteira
	 * @return Retorna valor inteiro
	 */
	public static Double inte(Double pValor){
		if (DBSObject.isEmpty(pValor) ){
			return null;
		}		
		return (double) pValor.longValue();
	}

	/**
	 * Retorna se a string é um valor numérico
	 * @param pTextoNumerico Texto se será verificado 
	 * @return true = é um valor numérico / false = NÃO é um valor numérico
	 */
	public static boolean isNumber(String pTextoNumerico){
		if (pTextoNumerico == null){
			return false;
		}
		return pTextoNumerico.matches("([-+]?\\d*\\.*\\,*\\d+)([eE][+-]?[0-9]+)?$");
	}
	
	/**
	 * Retorna se número é inteiro(Não contém casas decimais)
	 * @param pValue
	 * @return
	 */
	public static boolean isInteger(Object pValue){
		Integer xInteiro = DBSNumber.toInteger(pValue);
		if (xInteiro != null){
			if (xInteiro.toString().trim().equals(pValue.toString().trim())){
				return true;
			}
		}
		return false;
	}

	/** 
	 * Retira da string todos os dados não numéricos
	 * Antigo: getOnlyNumber
	 * @param pTexto
	 * @return String somente com valores numéricos. Caso não exista valores numéricos, retorna 0(zero)
	 */
	public static String getOnlyNumber(String pTexto){
		if (DBSObject.isEmpty(pTexto)){
			return null;
		}
		String xA = "";
		String xB = "";
		for (int x=0;x<pTexto.length();x++){
			xB = pTexto.substring(x, x+1);
			if (isNumber(xB)){
				xA = xA + xB;
			}
		}
		return DBSString.getNotEmpty(xA, "0");
	}	
	
	/**
	 * Retorna um número a partir de uma string, forçando uma determinada quantidade de casas decimais
	 * Antigo StringToNumber
	 * @param pString String com conteúdo a ser utilizado
	 * @param pCasasDecimais Quantidade de Casas decimais a serem consideradas.
	 * @return Valor numérico
	 */
	public static Double toNumber(String pString, Integer pCasasDecimais){
		if (DBSObject.isEmpty(pString) ||
			DBSObject.isEmpty(pCasasDecimais)){
			return null;
		}
		Double xD;
		Double xE = DBSNumber.exp(10D, pCasasDecimais.doubleValue());
	
		if (isNumber(pString)){
			xD = Double.valueOf(pString);
			xD = DBSNumber.divide(xD, xE);
			xD = DBSNumber.round(xD, pCasasDecimais);
			return xD;
		}
		else{
			return null;
		}
	}

	/**
	 * Converte para BigDecimal
	 * @return Retorna o valor convertido para BigDecimal ou 0(zero) caso o valor a ser convertido seja nulo
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object pValor) {
		return toBigDecimal(pValor, BigDecimal.ZERO);
	}

	/**
	 * Converte para BigDecimal
	 * @param pDefaultValue
	 * @return Retorna o valor convertido para BigDecimal ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object pValor, Object pDefaultValue) {
		if (DBSObject.isEmpty(pValor)) {
			return (BigDecimal) pDefaultValue;
		}
		if (pValor instanceof BigDecimal) {
			return (BigDecimal) pValor;
		} else if (pValor instanceof Integer ||
				   pValor instanceof Long ||
				   pValor instanceof Double) {
			BigDecimal x = new BigDecimal(pValor.toString());
			return x;
		} else if (pValor instanceof String) {	
			return new BigDecimal(pvStringToDouble((String) pValor));
		} else {
			return null;
		}
	}

	/**
	 * Converte para Integer
	 * @param pValor
	 * @return Retorna o valor convertido ou o 0(zero), caso o valor informado seja nulo.
	 */
	public static Long toLong(Object pValor) {
		return toLong(pValor, 0L);
	}

	/**
	 * Converte para Integer
	 * @param pValor
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Long toLong(Object pValor, Long pDefaultValue) {
		if (DBSObject.isEmpty(pValor)) { 
			return pDefaultValue;
		}

		if (pValor instanceof Integer) {
			return new Long((Integer) pValor);
		} else if (pValor instanceof BigDecimal) {
			return ((BigDecimal) pValor).longValue();
		} else if (pValor instanceof Double) {
			return ((Double) pValor).longValue();
		} else if (pValor instanceof Float) {
			return ((Float) pValor).longValue();
		} else if (pValor instanceof Long) {
			return (Long) pValor;
		} else if (pValor instanceof Boolean) {
			if (!(Boolean) pValor) {
				return 0L;
			} else {
				return -1L;
			}
		} else if (pValor instanceof String) {	
			return pvStringToDouble((String) pValor).longValue();
		} else {
			return null;
		}
	}
	
	/**
	 * Converte para Integer
	 * @param pValor 
	 * @return Retorna o valor convertido ou o 0(zero), caso o valor informado seja nulo.
	 */
	public static Integer toInteger(Object pValor) {
		return toInteger(pValor, 0);
	}
	
	/**
	 * Converte para Integer
	 * @param pValor
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Integer toInteger(Object pValor, Integer pDefaultValue) {
		if (DBSObject.isEmpty(pValor)) { 
			return pDefaultValue;
		}

		if (pValor instanceof Integer) {
			return (Integer) pValor;
		} else if (pValor instanceof BigDecimal) {
			return ((BigDecimal) pValor).intValue();//Exact();
		} else if (pValor instanceof Double) {
			return ((Double) pValor).intValue();
		} else if (pValor instanceof Float) {
			return ((Float) pValor).intValue();
		} else if (pValor instanceof Long) {
			return ((Long) pValor).intValue();
		} else if (pValor instanceof Boolean) {
			if (!(Boolean) pValor) {
				return 0;
			} else {
				return -1;
			}
		} else if (pValor instanceof String) {	
			return pvStringToDouble((String) pValor).intValue();
		} else {
			return null;
		}
	}
	
	/**
	 * Converte para double. Se for nulo, retorna Zero.
	 * @param pValor
	 * @return Retorna o valor convertido ou o valor 0.0, caso o valor informado seja nulo
	 */
	public static Double toDouble(Object pValor) {
		return toDouble(pValor, 0.0);
	}
	
	/**
	 * Converte para double.
	 * @param pValor
	 * @param pDefaultValue
	 * @return Retorna o valor convertido ou o valor informado em pDefaultValue caso o valor a ser convertido seja nulo
	 */
	public static Double toDouble(Object pValor, Double pDefaultValue) {
		if (DBSObject.isEmpty(pValor) || pValor.equals("")) {
			return pDefaultValue;
		}
//		if (DBSObject.isNull(pValor)) {
//			return null;
//		}
		if (pValor instanceof Double) {
			return (Double) pValor;
		} else if (pValor instanceof BigDecimal) {
			return ((BigDecimal) pValor).doubleValue();
		} else if (pValor instanceof Integer) {
			return ((Integer)pValor).doubleValue();
		} else if (pValor instanceof Long) {
			return ((Long)pValor).doubleValue();
		} else if (pValor instanceof String) {	
			return pvStringToDouble((String) pValor);
		} else {
			return null;
		}
	}

	
	/**
	 * Retorna o mesmo dado informado como <b>Double</b> quando este não for zero. 
	 * Se for zero, retorna 1.
	 * @param pDado Dado a ser verificado
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Double pDado){
		return toDoubleNotZero(pDado, 1D);
	}

	/**
	 * Retorna o mesmo dado informado como <b>Double</b> quando este não for zero. 
	 * Se for zero, retorna 1.
	 * @param pDado Dado a ser verificado
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Object pDado){
		return toDoubleNotZero(pDado, 1D);
	}

	/**
	 * Retorna o mesmo dado informado como <b>Double</b> quando este não for zero. 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou zero, quando este for zero.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Double pDado, Double pDadoDefault){
		if (DBSObject.isEmpty(pDadoDefault)){
			pDadoDefault = 1D; //Reseta valor default para 1 se não for informado na chamada deste função
		}
		if(DBSObject.isEmpty(pDado) || pDado==0){
			return pDadoDefault;
		}
		else{
			return pDado;
		}
	}

	/**
	 * Retorna o mesmo dado informado como <b>Double</b> quando este não for zero. 
	 * Se for zero, retorna o conteúdo default definido pelo usuário ou zero, quando este for zero.
	 * @param pDado Dado a ser verificado
	 * @param pDadoDefault Conteúdo a ser considerado caso o dado seja vazio
	 * @return Dado contendo o valor diferente de zero
	 */
	public static Double toDoubleNotZero(Object pDado, Object pDadoDefault){
		Double xDado = DBSNumber.toDouble(pDado);
		Double xDadoDefault = DBSNumber.toDouble(pDadoDefault);
		return toDoubleNotZero(xDado, xDadoDefault);
	}

	/** 
	 * Converte string para double, considerando a especificidades do ponto decimal para casa localidade 
	 * O comando parseDouble só aceita ponto decimal como "."
	 * @param pValue
	 * @return
	 */
	private static Double pvStringToDouble(String pValue){
		NumberFormat xNF = NumberFormat.getInstance(new Locale("pt","BR"));
		try {
			if (pValue!=null){
				return xNF.parse(pValue).doubleValue();
			}else{
				return null;
			}
		} catch (ParseException e) {
			return null;
		}
	}
	
}


