package br.com.dbsoft.push;

import javax.annotation.PreDestroy;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dbsoft.core.DBSSDK.CONTENT_TYPE;
import br.com.dbsoft.core.DBSSDK.ENCODE;
import br.com.dbsoft.core.DBSServlet;
import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.util.DBSSession;

/**
 * Servlet que faz a conexão com o SSE no xhtml<br/>
 * Recebe os requests e armazena em um DBSPushBean para posteriormente ser utilizado para disparar o response.<br/>
 * É necessário que a class que extenderá esta possua a anotação @WebServlet(value="/??", asyncSupported=true) 
 *
 */
public abstract class DBSPushServlet extends DBSServlet {

	private static final long serialVersionUID = -7513051390653245604L;
	
	public static final String USER_ID = "USER_ID";
	
	/**
	 * Retorna o bean que armazenará os requests(clients) abertos.
	 * Os request serão utilzado posteriormente para enviar mensagens de volta ao cliente(browser)
	 * @return
	 */
	protected abstract DBSPushBean getDBSPushBean();
	
	/**
	 * Retorna o Id do usuário(Ou qualquer identificação que represente o indivíduo)<br/>
	 * para vinculá-lo ao respectivo request e posteriormente saber a quem pertence cada request
	 * @return
	 */
	protected String getUserId(){
		return "";
	};

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		//System.out.println("DBSPushServlet: DESTRO");
		if (getDBSPushBean().getOnGoingRequests()!=null){
			getDBSPushBean().finalizeClass(); 
		}
		super.destroy();
	}

	//	@Override
//	protected void service(HttpServletRequest pReq, HttpServletResponse pRes)  {

	@Override
	protected void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException {
		try {
			pResponse.setContentType(CONTENT_TYPE.TEXT_EVENT_STREAM);
			pResponse.setCharacterEncoding(ENCODE.UTF_8);
			pResponse.setHeader("Cache-Control", "no-cache");
			pResponse.setHeader("Connection", "keep-alive");

			//Vincula o usuário ao request para posteriormente pode identifica o usuário de cada request
			pRequest.setAttribute(USER_ID, getUserId().trim().toUpperCase());

			final AsyncContext xAC = pRequest.startAsync();
			
			xAC.setTimeout(60000); //1 minuto 60.000 milisegundos
			xAC.addListener(new AsyncListener() {
				@Override 
				public void onComplete(AsyncEvent pEvent) {
//						System.out.println("COMPLETE");
				}
				
				@Override 
				public void onTimeout(AsyncEvent pEvent) {
//						System.out.println("TIMEOUT");
					try{
						if (pEvent == null){
//								System.out.println("TIMEOUT NULL");
						}
						pvOnTimeout(pEvent);
					}catch(Exception ignore){
//							System.out.println("TIMEOUT IGNORE");
					}
				}
				
				@Override 
				public void onError(AsyncEvent pEvent) {
//						System.out.println("ERROR");
//						pvOnError(pEvent);
				}
				
				@Override 
				public void onStartAsync(AsyncEvent pEvent) {
//						System.out.println("START");
				}
		    });
			//Envia resposta vázia
			DBSSession.writeServletEventSourceResponse(xAC.getResponse(), "init", null);
			pvOnNewRequest(xAC);			
			
//			super.service(pReq, pRes);
		} catch(Exception e){
//				DBSIO.throwIOException(e);
		} finally {
//				System.out.println("REQUEST");
//			this.destroy();
		}
//		System.out.println("-------------------------------------");

	}
	
	@Override
	@PreDestroy
	public void finalize(){
//		System.out.println("DBSPUSHSERVLET DESTROY");
	}

	@SuppressWarnings("unused")
	public void onNewRequest(AsyncContext pAsyncContext) {}
	
	/**
	 * Armazena o resquest para poder ser utilizado posteriormente para enviar mensagens
	 * @param pAsyncContext
	 */
	private void pvOnNewRequest(AsyncContext pAsyncContext) {
//		System.out.println("pvOnNewRequest #1");
		getDBSPushBean().getOnGoingRequests().add(pAsyncContext);
//		System.out.println("REQUEST: ARMAZENADO:" + getDBSPushBean().getOnGoingRequests().size());
//		System.out.println("pvOnNewRequest #2");
		onNewRequest(pAsyncContext);
	};	


	
	private void pvOnTimeout(AsyncEvent pEvent) {
		try {
//			System.out.println("PVON TIMEOUT #1");
			if (getDBSPushBean().getOnGoingRequests()!=null){
//				System.out.println("PVON TIMEOUT #2");
				((HttpServletResponse) pEvent.getAsyncContext().getResponse()).setStatus(500);
				pEvent.getAsyncContext().complete();
				pEvent.getAsyncContext().dispatch();
				getDBSPushBean().getOnGoingRequests().remove(pEvent.getAsyncContext()); 
			}
//			System.out.println("PVON TIMEOUT #3");
		}catch(Exception ignore){
//			System.out.println("PVON TIMEOUT");
		}
	};

}
