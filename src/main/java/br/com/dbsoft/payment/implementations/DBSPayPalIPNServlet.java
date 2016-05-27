package br.com.dbsoft.payment.implementations;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.paypal.ipn.IPNMessage;

import br.com.dbsoft.error.DBSIOException;

/**
 * Implementação Básica de uma Servlet para o IPN (Instant Payment Notification) do PayPal
 * @author jose.avila@dbsoft.com.br
 *
 */
public abstract class DBSPayPalIPNServlet extends HttpServlet {

	private static final long 	serialVersionUID = -6030212068114900098L;
	protected 	Logger			wLogger = Logger.getLogger(this.getClass());
	private 	IPNMessage 		wIPNMessage;
	
	public IPNMessage getIPNMessage() {
		return wIPNMessage;
	}

	public void setIPNMessage(IPNMessage pIPNMessage) {
		wIPNMessage = pIPNMessage;
	}
	
	/* 
	 * Recebe a chamada IPN do PayPal
	 * Preenche o IPNMessege com a mensagem enviada do PayPal <br/>
	 * e chama o método onRequest.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			setIPNMessage(new IPNMessage(request));
			onRequest(request, response);
		} catch (DBSIOException e) {
			wLogger.error(e);
		}
	}
	
	protected abstract void onRequest(HttpServletRequest pRequest, HttpServletResponse pResponse) throws DBSIOException;

}
