package br.com.dbsoft.push;

import javax.annotation.PreDestroy;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.dbsoft.util.DBSSession;

/**
 * Servlet que faz a conexão com o SSE no xhtml<br/>
 * Recebe os requests e armazena em um DBSPushBean para posteriormente ser utilizado para disparar o response. 
 *
 */
public abstract class DBSPushServlet extends HttpServlet {

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
	
	@Override
	protected void service(HttpServletRequest pReq, HttpServletResponse pRes)  {
		try {
				pRes.setContentType("text/event-stream");
				pRes.setCharacterEncoding("UTF-8");
				pRes.setHeader("Cache-Control", "no-cache");
				pRes.setHeader("Connection", "keep-alive");
	
				//Vincula o usuário ao request para posteriormente pode identifica o usuário de cada request
				pReq.setAttribute(USER_ID, getUserId().trim().toUpperCase());
	
				final AsyncContext xAC = pReq.startAsync();
				
				xAC.setTimeout(60000); //10 minutos = 600.000 milisegundos
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
//				System.out.println("REQUEST EXCEPTION ");
//				e.printStackTrace();
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
