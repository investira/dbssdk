package br.com.dbsoft.core;

import br.com.dbsoft.util.DBSNumber;

public class DBSApproval {

	public static enum APPROVAL_STAGE{
		APPROVED	(1),
		VERIFIED	(2),
		CONFERRED	(4),
		REGISTERED	(8);

	    Integer wCode;
		
	    public static APPROVAL_STAGE get(Object pCode) {
			Integer xI = DBSNumber.toInteger(pCode);
			if (xI != null){
				return get(xI);
			}else{
				return null;
			}
		}
	    
		public static APPROVAL_STAGE get(Integer pCode) {
			switch (pCode) {
			case 1:
				return APPROVAL_STAGE.APPROVED;
			case 2:
				return APPROVAL_STAGE.VERIFIED;
			case 4:
				return APPROVAL_STAGE.CONFERRED;
			case 8:
				return APPROVAL_STAGE.REGISTERED;
			}
			return null;
		}
	    
	    APPROVAL_STAGE (Integer pCode){
	    	wCode = pCode;
	    }
	
		public Integer getCode() {
			return wCode;
		}
	}
	
	/**
	 * Retorna o somatório dos estágio, conforme o nivel os poderes informados
	 * @param pRegistered Com poder para registrar
	 * @param pConferred Com poder para conferir
	 * @param pVerified Com poder para verificar
	 * @param pApproved Com poder para aprovar
	 * @return
	 */
	public static Integer getApprovalStage(Boolean pRegistered, Boolean pConferred, Boolean pVerified, Boolean pApproved){
		int xApprovalStage = 0x0;
		if (pRegistered){
			xApprovalStage = xApprovalStage + APPROVAL_STAGE.REGISTERED.getCode();
		}
		if (pConferred){
			xApprovalStage = xApprovalStage + APPROVAL_STAGE.CONFERRED.getCode();
		}
		if (pVerified){
			xApprovalStage = xApprovalStage + APPROVAL_STAGE.VERIFIED.getCode();
		}
		if (pApproved){
			xApprovalStage = xApprovalStage + APPROVAL_STAGE.APPROVED.getCode();
		}
		return xApprovalStage;
	}
	
	public static Boolean isRegistered(Integer pApprovalStage){
		if (pApprovalStage == null){return false;}
		return ((pApprovalStage & APPROVAL_STAGE.REGISTERED.getCode()) == APPROVAL_STAGE.REGISTERED.getCode());
	}
	public static Boolean isConferred(Integer pApprovalStage){
		if (pApprovalStage == null){return false;}
		return ((pApprovalStage & APPROVAL_STAGE.CONFERRED.getCode()) == APPROVAL_STAGE.CONFERRED.getCode());
	}
	public static Boolean isVerified(Integer pApprovalStage){
		if (pApprovalStage == null){return false;}
		return ((pApprovalStage & APPROVAL_STAGE.VERIFIED.getCode()) == APPROVAL_STAGE.VERIFIED.getCode());
	}
	public static Boolean isApproved(Integer pApprovalStage){
		if (pApprovalStage == null){return false;}
		return ((pApprovalStage & APPROVAL_STAGE.APPROVED.getCode()) == APPROVAL_STAGE.APPROVED.getCode());
	}	
	
	/**
	 * Retorna o somatório dos próximos estágios que o usuário tem poder.</br>
	 * Caso a etapa atual seja o 'REGISTERED' e o usuário tem poder para 'CONFERRED' e 'VERIFIED',
	 * será retornado o somatório das duas, indicando que o usuário está efetuando 'CONFERRED' e 'VERIFIED' simultaneamente. 
	 * @param pCurrentStage Estágio atual
	 * @param pUserStages O somatório de todos os estágios que o usuário tem poder
	 * @return O somatório dos próximos estágios que serão sensibilizados 
	 */
	public static Integer getNextUserStages(APPROVAL_STAGE pCurrentStage, Integer pUserStages){
		APPROVAL_STAGE xNext = getNextStage(pCurrentStage);
		//Retorna o estágio corrente se usuário não possuir poderes para o próximo estágio
		if ((pUserStages & xNext.getCode()) != xNext.getCode()){
			return pCurrentStage.getCode();
		//Se proximo estágio for 'CONFERRED', verifica se usuário também possui acesso ao 'VERIFIED'
		}else if (xNext==APPROVAL_STAGE.CONFERRED &&
				  isVerified(pUserStages)){
			return APPROVAL_STAGE.CONFERRED.getCode() + APPROVAL_STAGE.VERIFIED.getCode();
		}
		//Retorna próximo estágio, se usuário tem poder de assinatura neste estágio.
		return xNext.getCode();
	}
	
	/**
	 * Retorna o maior estágio dentre os estágios informados.<br/>
	 * @param pStages Somatórios dos estágios
	 * @return
	 */
	public static APPROVAL_STAGE getMaxStage(Integer pStages){
		if (isApproved(pStages)){
			return APPROVAL_STAGE.APPROVED;
		}else if (isVerified(pStages)){
			return APPROVAL_STAGE.VERIFIED;
		}else if (isConferred(pStages)){
			return APPROVAL_STAGE.CONFERRED;
		}
		return APPROVAL_STAGE.REGISTERED;
	}

//	public static Integer getNextStageToUser(APPROVAL_STAGE pCurrentStage, Integer pUserStages){
//		APPROVAL_STAGE xNext = getNextStage(pCurrentStage);
//		//Retorna o estágio corrente se usuário não possuir poderes para o próximo estágio
//		if ((pUserStages & xNext.getCode()) != xNext.getCode()){
//			return pCurrentStage.getCode();
//		}else{
//			//Retorna próximo estágio, se usuário tem poder de assinatura neste estágio.
//			return getNextStageToUser(xNext, pUserStages);
//		}
//	}
	
	/**
	 * Retorna estágio imediatamente posterior
	 * @param pCurrentStage
	 * @return
	 */
	public static APPROVAL_STAGE getNextStage(APPROVAL_STAGE pCurrentStage){
		if (pCurrentStage == null){
			return APPROVAL_STAGE.REGISTERED;
		}
		//Calcula próximo estágio
		Double xOrdinal = DBSNumber.exp(2D,  DBSNumber.toDouble(pCurrentStage.ordinal() - 1)).doubleValue();
		APPROVAL_STAGE xNext = APPROVAL_STAGE.get(xOrdinal);
		if (xNext == null){
			return APPROVAL_STAGE.APPROVED;
		}else{
			return xNext;
		}
	}
	
	/**
	 * Retorna estágio imediatamente anterior
	 * @param pCurrentStage
	 * @return
	 */
	public static APPROVAL_STAGE getPreviousStage(APPROVAL_STAGE pCurrentStage){
		if (pCurrentStage == null){
			return APPROVAL_STAGE.REGISTERED;
		}
		Double xOrdinal = DBSNumber.exp(2D,  DBSNumber.toDouble(pCurrentStage.ordinal() + 1)).doubleValue();
		APPROVAL_STAGE xNext = APPROVAL_STAGE.get(xOrdinal);
		if (xNext == null){
			return APPROVAL_STAGE.REGISTERED;
		}else{
			return xNext;
		}
	}
	
}
