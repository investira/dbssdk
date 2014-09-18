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
	
//	Private Function mtAprovaReprova(pAprova As Boolean, pShowMsg As Boolean) As Boolean
//	Dim xCancela        As Boolean
//	Dim xNivel          As Integer
//	Dim xReadNivel      As Integer
//	Dim xRegistrador    As Boolean
//	Dim xUsado(4)   As Long
//	Dim xUsadoField As String
//	Dim xUsadoID    As Long
//	Dim xDiferentes As Long
//	    
//	    On Error Resume Next
//	    m_DBAprovando = True
//	    If DBUsuarioIDField <> "" And DBAprovadorField <> "" Then
//	        If DBAprovacaoVerificaIguais Then
//	            If m_DBRecordSet(DBUsuarioIDField) = m_DBUsuarioID Then
//	                xRegistrador = True
//	            End If
//	        End If
//	    End If
//	    
//	    RaiseEvent DBSCanEdit(xCancela)
//	    If Not xCancela Then
//	        RaiseEvent DBSValidateAprova(xCancela)
//	    End If
//	    If Not xCancela Then
//	        Set xSql = New DBSSql
//	        If DBAprovaField <> "" Then
//	            xReadNivel = GetNotNull(m_DBRecordSet(DBAprovaField), DBCnsAprova.Registrado)
//	            RaiseEvent DBSReadAprovaNivel(m_DBRecordSet, xReadNivel)
//	            Err.Clear
//	        End If
//	        If pAprova Then
//	            xNivel = 0
//	            If (xReadNivel And DBCnsAprova.Registrado) = DBCnsAprova.Registrado Then
//	                xNivel = GetAprovaNivelMaior(DBAprovaNivel, DBCnsAprova.Conferido, DBAprovacaoQuantidade, xRegistrador)
//	                GoSub TestaSeIgual
//	            ElseIf (xReadNivel And DBCnsAprova.Conferido) = DBCnsAprova.Conferido Then
//	                xNivel = GetAprovaNivelMaior(DBAprovaNivel, DBCnsAprova.Verificado, DBAprovacaoQuantidade, xRegistrador)
//	                GoSub TestaSeIgual
//	            ElseIf (xReadNivel And DBCnsAprova.Verificado) = DBCnsAprova.Verificado Then
//	                xNivel = GetAprovaNivelMaior(DBAprovaNivel, DBCnsAprova.Aprovado, DBAprovacaoQuantidade, xRegistrador)
//	                GoSub TestaSeIgual
//	            Else
//	                xCancela = True
//	            End If
//	        Else
//	            xNivel = DBCnsAprova.Registrado
//	            xCancela = TestaSeIgualReprova("Este comando não pode ser efetuado pelo mesmo usuário que incluiu o registro!")
//	        End If
//	        If Not xCancela Then
//	            If pShowMsg Then
//	                If pAprova Then
//	                    If Not DBMsgAprova = "" Then
//	                        If xNivel = DBCnsAprova.Aprovado Then
//	                            xCancela = Not CBool(ShowMsg(AtencaoYesNo, DBMsgAprova, 2) = vbYes)
//	                        ElseIf xNivel = DBCnsAprova.Verificado Then
//	                            xCancela = Not CBool(ShowMsg(AtencaoYesNo, "Confirma comando de Verificação ?", 2) = vbYes)
//	                        ElseIf xNivel = DBCnsAprova.Conferido Then
//	                            xCancela = Not CBool(ShowMsg(AtencaoYesNo, "Confirma comando de Conferição ?", 2) = vbYes)
//	                        End If
//	                    End If
//	                Else
//	                    If Not DBMsgReprova = "" Then
//	                        Beep
//	                        xCancela = Not CBool(ShowMsg(AtencaoYesNo, DBMsgReprova, 2) = vbYes)
//	                    End If
//	                End If
//	            End If
//	            mtAprovaReprova = True
//	            mtBeginTransaction
//	            If Not xCancela Then
//	                RaiseEvent DBSBeforeAprova(m_DBRecordSet, pAprova, xCancela, xNivel)
//	                If xCancela Then
//	                    mtAprovaReprova = False
//	                Else
//	                    mtAprovaReprova = mtAprovaUpdate(xNivel)
//	                End If
//	                'If xNivel = DBCnsAprova.Aprovado Then
//	                '    GoSub TestaSeIgual
//	                'End If
//	                'GoSub TestaSeIgual
//	            Else
//	                mtAprovaReprova = False
//	            End If
//	            If mtAprovaReprova Then
//	                pAprova = True
//	                RaiseEvent DBSAfterAprova(m_DBRecordSet, pAprova)
//	                mtAprovaReprova = pAprova
//	            End If
//	            DBOk = mtAprovaReprova
//	            mtEndTransaction
//	        End If
//	    End If
//	    GetErro Err
//	    mtAprovaReprova = Not xCancela
//	    m_DBAprovando = False
//	    Exit Function
//
//	TestaSeIgual:
//	    If DBAprovacaoVerificaIguais Then
//	        If xNivel = DBCnsAprova.Aprovado Then
//	            If Not DBUsuarioIDField = "" And Not DBAprovadorField = "" Then
//	                If m_DBRecordSet(DBUsuarioIDField) = m_DBUsuarioID Then
//	                    If Err = 3265 Then
//	                        ShowMsg ErroOk, "Não foi encontrado o campo " & DBUsuarioIDField & " !"
//	                    Else
//	                        ShowMsg ErroOk, "A aprovação não pode ser realizada pelo mesmo usuário que incluio o registro!"
//	                    End If
//	                    xCancela = True
//	                End If
//	            End If
//	        End If
//	        If xNivel = DBCnsAprova.Verificado Then
//	            If Not DBUsuarioIDField = "" And Not DBVerificadorField = "" And Not DBConferidorField = "" Then
//	                If m_DBRecordSet(DBUsuarioIDField) = m_DBUsuarioID And m_DBRecordSet(DBConferidorField) = m_DBUsuarioID Then
//	                    If Err = 3265 Then
//	                        ShowMsg ErroOk, "Não foi encontrado o campo " & DBUsuarioIDField & " !"
//	                    Else
//	                        ShowMsg ErroOk, "A verificação não pode ser realizada pelo mesmo usuário que incluio o registro!"
//	                    End If
//	                    xCancela = True
//	                End If
//	            End If
//	        End If
//	    Else
//	        RaiseEvent DBSAprovacaoVerificaIguais(xNivel, xCancela)
//	    End If
//	    If xNivel = DBCnsAprova.Aprovado Then
//	        If Not DBUsuarioIDField = "" And Not DBVerificadorField = "" And Not DBConferidorField = "" And Not DBVerificadorField = "" Then
//	            xDiferentes = 0
//	            For x = 1 To 4
//	                xUsadoID = -1
//	                If x = 1 Then
//	                    xUsadoField = DBUsuarioIDField
//	                ElseIf x = 2 Then
//	                    xUsadoField = DBConferidorField
//	                ElseIf x = 3 Then
//	                    xUsadoField = DBVerificadorField
//	                ElseIf x = 4 Then
//	                    xUsadoField = ""
//	                    xUsadoID = DBUsuarioId
//	                End If
//	                If Not xUsadoField = "" Then
//	                    xUsadoID = GetNotNull(m_DBRecordSet(xUsadoField), 0)
//	                End If
//	                If Err = 3265 Then
//	                    ShowMsg ErroOk, "Não foi encontrado o campo " & xUsadoField & " !"
//	                    xCancela = True
//	                    Exit For
//	                End If
//	                xAchou = False
//	                For Y = 1 To x
//	                    If xUsadoID = xUsado(Y) Then
//	                        xAchou = True
//	                        Exit For
//	                    End If
//	                Next
//	                If Not xAchou Then
//	                    xUsado(x) = xUsadoID
//	                    xDiferentes = xDiferentes + 1
//	                End If
//	            Next
//	            If Not xCancela Then
//	                If xDiferentes < DBAprovacaoQuantidade Then
//	                    RaiseEvent DBSErroAprovacao(m_DBRecordSet)
//	                    xCancela = True
//	                End If
//	            End If
//	        End If
//	    End If
//	    Err.Clear
//	    Return
//
//	End Function
	
//	Function GetAprovaNivelMaior(pAprovaNivel As Integer, pNivelInicio As DBCnsAprova, pAprovacaoQuantidade As Long, Optional pRegistrador As Boolean) As Integer
//    If (pAprovaNivel And DBCnsAprova.Registrado) = DBCnsAprova.Registrado Then
//        GetAprovaNivelMaior = DBCnsAprova.Registrado
//    Else
//        If Not GetAprovaNivelMaior = 0 Then
//            Exit Function
//        End If
//    End If
//    If (pAprovaNivel And DBCnsAprova.Conferido) = DBCnsAprova.Conferido Then
//        GetAprovaNivelMaior = DBCnsAprova.Conferido
//    Else
//        If Not GetAprovaNivelMaior = 0 And (pNivelInicio = 0 Or Not ((pAprovaNivel And pNivelInicio) = pNivelInicio)) Then
//            Exit Function
//        End If
//    End If
//    If (pAprovaNivel And DBCnsAprova.Verificado) = DBCnsAprova.Verificado Then
//        GetAprovaNivelMaior = DBCnsAprova.Verificado
//    Else
//        If Not GetAprovaNivelMaior = 0 And (pNivelInicio = 0 Or Not ((pAprovaNivel And pNivelInicio) = pNivelInicio)) Then
//            Exit Function
//        End If
//    End If
//    If (pAprovaNivel And DBCnsAprova.Aprovado) = DBCnsAprova.Aprovado And Not pRegistrador Then
//        If Not (pNivelInicio = 0 And ((pAprovaNivel And DBCnsAprova.Registrado) = DBCnsAprova.Registrado)) Then
//            GetAprovaNivelMaior = DBCnsAprova.Aprovado
//        End If
//'        If pNivelInicio = 0 And Not ((pAprovaNivel And DBCnsAprova.Registrado) = DBCnsAprova.Registrado) Then 'Teste se usuário tem também permissão para registrar
//'            GetAprovaNivelMaior = DBCnsAprova.Aprovado
//'        End If
//    Else
//        If Not GetAprovaNivelMaior = 0 And (pNivelInicio = 0 Or Not ((pAprovaNivel And pNivelInicio) = pNivelInicio)) Then
//            Exit Function
//        End If
//    End If
//    If pAprovacaoQuantidade > 2 Then
//        'Verifica intervalo entre o nivel de aprovacao que esta e o perfil e compara com a diferença necessária
//        If Abs(Log(GetAprovaNivelMaior) / Log(2) - (Log(pNivelInicio) / Log(2) + 1)) > (pAprovacaoQuantidade - 2) Then
//            GetAprovaNivelMaior = 2 ^ ((Log(GetAprovaNivelMaior) / Log(2)) + (pAprovacaoQuantidade - 2))
//        End If
//        
//    End If
//End Function

}
