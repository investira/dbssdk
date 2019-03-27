package br.com.investira.access;

import javax.servlet.http.HttpServletResponse;

import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.IDBSMessage;
import br.com.dbsoft.message.IDBSMessageBase.MESSAGE_TYPE;

public class AccessMessages {

	//GENERICO E VALIDACAO - 00XX =================================================================================
	//200 - Sucess
	public static IDBSMessage EmailEnviado = 
			new DBSMessage(MESSAGE_TYPE.SUCCESS,	2000001, HttpServletResponse.SC_OK, "E-mail enviado.");
	
	//400 - Bad Request
	public static IDBSMessage ParametrosInvalidos =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000001, HttpServletResponse.SC_BAD_REQUEST, "Parâmetros inválidos ou não encontrados.");
	
	public static IDBSMessage EmailInvalido =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000002, HttpServletResponse.SC_BAD_REQUEST, "E-mail inválido.");
	
	//500 - Internal Server Error
	public static IDBSMessage ErroGenerico =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		5000001, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro processando a informação.");
	
	//AUTH - 01XX =================================================================================
	//200 - Sucess
	public static IDBSMessage LoginOK =
			new DBSMessage(MESSAGE_TYPE.SUCCESS, 	2000101, HttpServletResponse.SC_OK, "Login efetuado.");
	
	public static IDBSMessage TokenRenovado = 
			new DBSMessage(MESSAGE_TYPE.SUCCESS,	2000102, HttpServletResponse.SC_OK, "Token Renovado.");
	
	public static IDBSMessage CodeValido = 
			new DBSMessage(MESSAGE_TYPE.SUCCESS,	2000103, HttpServletResponse.SC_OK, "O Código está válido.");
	
	public static IDBSMessage CadastroConfirmado =
			new DBSMessage(MESSAGE_TYPE.SUCCESS, 	2000104, HttpServletResponse.SC_OK, "Seu cadastro foi confirmado com sucesso.");
	
	public static IDBSMessage SenhaAlterada = 
			new DBSMessage(MESSAGE_TYPE.SUCCESS,	2000105, HttpServletResponse.SC_OK, "Senha alterada com sucesso.");
	
	public static IDBSMessage UsuarioEncontrado =
			new DBSMessage(MESSAGE_TYPE.SUCCESS, 	2000106, HttpServletResponse.SC_OK, "Usuário encontrado.");
	
	//201 - Created
	public static IDBSMessage CadastroCriarSucesso =
			new DBSMessage(MESSAGE_TYPE.SUCCESS, 	2010101, HttpServletResponse.SC_CREATED, "Seu cadastro foi registrado e você receberá um e-mail de confirmação.");
	
	public static IDBSMessage CodigoCriarSucesso = 
			new DBSMessage(MESSAGE_TYPE.SUCCESS,	2010102, HttpServletResponse.SC_CREATED, "Código criado com sucesso.");
	
	//400 - Bad Request
	public static IDBSMessage CodigoCriarErro =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000101, HttpServletResponse.SC_BAD_REQUEST, "Erro criando código.");
	
	public static IDBSMessage CadastroCriarErro =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000102, HttpServletResponse.SC_BAD_REQUEST, "Ocorreu um erro cadastrando usuário.");
	
	public static IDBSMessage ErroRequisitandoCodigo =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000103, HttpServletResponse.SC_BAD_REQUEST, "Erro requisitando Código de Confirmação/Reset de senha.");
	
	public static IDBSMessage SenhaInvalida =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000104, HttpServletResponse.SC_BAD_REQUEST, "Senha inválida.");

	public static IDBSMessage SenhaNaoConfirma =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4000105, HttpServletResponse.SC_BAD_REQUEST, "Senha e Confirmação de Senha devem ser iguais.");
	
	//401 - Unauthorized
	public static IDBSMessage TokenInvalido = 
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4010101, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
	
	public static IDBSMessage UsuarioNaoAutorizado =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4010202, HttpServletResponse.SC_UNAUTHORIZED, "Usuário não autorizado.");
	
	public static IDBSMessage CodeInvalido = 
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4010103, HttpServletResponse.SC_UNAUTHORIZED, "O Código está inválido.");
	
	//404 - Not Found
	public static IDBSMessage UsuarioNaoEncontrado =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4040201, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
	
	//403 - FORBIDDEN
	public static IDBSMessage UsuarioUsernameOuSenhaInvalida =  
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4030101, HttpServletResponse.SC_FORBIDDEN, "Senha incorreta. Caso tenha esquecido a senha, use a opção Esqueci a senha.");
	
	//412 - Precondition Failed
	public static final IDBSMessage TokenNaoEncontrado =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4120101, HttpServletResponse.SC_PRECONDITION_FAILED, "Token não encontrado no Header da requisição.");

//	public static final IDBSMessage TokenUsernameNaoEncontrados =
//			new DBSMessage(MESSAGE_TYPE.ERROR, 		4120101, HttpServletResponse.SC_PRECONDITION_FAILED, "Token ou Username não encontrados no Header da requisição.");
	
	//423 - Locked
	public static IDBSMessage UsuarioBloqueadoAte = 
			new DBSMessage(MESSAGE_TYPE.ERROR, 		4232001, 423, "Usuário Bloqueado até ");
	
	//500 - Internal Server Error
	public static IDBSMessage ErroGerandoTokenClient =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		5001001, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro gerando Token de Client.");
	
	//503 - Service Unavaliable
	public static IDBSMessage ServicoIndisponivel =
			new DBSMessage(MESSAGE_TYPE.ERROR, 		5031001, HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Serviço indisponível. Tente novamente mais tarde.");
	
}
