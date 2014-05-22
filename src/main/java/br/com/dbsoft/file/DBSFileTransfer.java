package br.com.dbsoft.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSException;
import br.com.dbsoft.task.IDBSTaskEventsListener;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;

public class DBSFileTransfer{

	
	public enum TransferState{
		NOTSTARTED 		("Not Started", 1),
		STARTED 		("Started", 2),
		ENDED  		    ("Ended", 3),
		CONNECTING 		("Connecting", 4), 
		TRANSFERING 	("Transfering", 5), 
		COMPRESSING 	("Compressing", 6),
		ERROR 			("Error", 7),
		SUCESS 			("Sucess", 8); 

		private String 	wName;
		private int 	wCode;
		
		private TransferState(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static TransferState get(int pCode) {
			switch (pCode) {
			case 1:
				return TransferState.NOTSTARTED;
			case 2:
				return TransferState.STARTED;
			case 3:
				return TransferState.ENDED;
			case 4:
				return TransferState.CONNECTING;
			case 5:
				return TransferState.TRANSFERING;
			case 6:
				return TransferState.COMPRESSING;
			case 7:
				return TransferState.ERROR;
			case 8:
				return TransferState.SUCESS;
			default:
				return TransferState.NOTSTARTED;
			}
		}			
	}
	

	public enum Compress{
		COMPRESS 		("Compress", 1),
		DECOMPRESS 		("Decompress", 2), 
		ORIGINAL 		("Original", 3); 
		
		private String 	wName;
		private int 	wCode;
		
		private Compress(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static Compress get(int pCode) {
			switch (pCode) {
			case 1:
				return Compress.COMPRESS;
			case 2:
				return Compress.DECOMPRESS;
			case 3:
				return Compress.ORIGINAL;
			default:
				return Compress.ORIGINAL;
			}
		}			
	}	
	
	protected static Logger					wLogger = Logger.getLogger(DBSFileTransfer.class);
	
	private String 							wURL;
	private String 							wDestino;
	private Timestamp						wVersao;
	private Long							wTimeOut = 0L;
	private List<IDBSFileTransferEvents>	wEventListeners = new ArrayList<IDBSFileTransferEvents>();
	private boolean							wInterrupted; 
	private long							wTimeStarted;
	private long							wTimeEnded;
	private TransferState					wTransferState;

	/**
	 * Construtor que recebe a URL do arquivo fonte e o arquivo de destino.
	 * 
	 * @param pURL URL do arquivo fonte
	 * @param pDestino Arquivo de destino
	 */
	public DBSFileTransfer(String pURL, String pDestino, Timestamp pVersao) {
		this.wDestino = pDestino;
		this.wURL = pURL;
		this.wVersao = pVersao;
	}


	/**
	 * @param pEventListener Classe que receberá as chamadas dos eventos quando ocorrerem.
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos
	 */
	public final void addEventListener(IDBSFileTransferEvents pEventListener) {
		wEventListeners.add(pEventListener);
	}

	public final void removeEventListener(IDBSTaskEventsListener pEventListener) {
		wEventListeners.remove(pEventListener);
	}

	public final Long getTimeOut() {
		return wTimeOut;
	}

	public final void setTimeOut(Long pTimeOut) {
		this.wTimeOut = pTimeOut;
	}

	public final String getURL() {
		return wURL;
	}
	
	public final void setURL(String pURL) {
		this.wURL = pURL;
	}

	public final void setDestino(String pDestino) {
		this.wDestino = pDestino;
	}

	public final String getDestino() {
		return wDestino;
	}

	public final Timestamp getVersao() {
		return wVersao;
	}

	public final void setVersao(Timestamp wVersao) {
		this.wVersao = wVersao;
	}

	/**
	 * Retorna a situação da execução
	 * @return
	 */
	public final TransferState getRunningState() {
		return wTransferState;
	}

	/**
	 * Configura a situação da execução (Método PRIVADO)
	 * @param pRunningState
	 */
	private final void setTransferState(TransferState pTransferState) {
		if (wTransferState != pTransferState){
			wTransferState = pTransferState;
			pvEventTransferStateChanged();
		}
	}

	/**
	 * Retornar se o transfer foi interrompido
	 * @return boolean
	 */
	public final boolean isInterrupted() {
		return wInterrupted;
	}
	
	/**
	 * Retorna date/hora que foi iniciado o transfer
	 * @return
	 */
	public final Timestamp getTimeStarted(){
		return DBSDate.toTimestamp(wTimeStarted);
	}
	
	public final void setTimeStarted(){
		wTimeStarted = System.currentTimeMillis();
		wTimeEnded = wTimeStarted;

	}
		
	/**
	 * Retorna data/hora que foi finalizado o transfer ou a hora atual caso esteja em processo de transferencia
	 * @return
	 */
	public final Timestamp getTimeEnded(){
		return DBSDate.toTimestamp(wTimeEnded);
	}
	
	public final void setTimeEnded(){
		wTimeEnded = System.currentTimeMillis();
	}
	
	/**
	 * Retorna o tempo de corrido
	 * @return
	 */
	public final Long getElapsedTime(){
		return wTimeEnded - wTimeStarted;

	}

	/**
	 * Método público e synchronized (?) que executa o download do arquivo.
	 * 
	 * @return boolean True se conseguir realizar a operação.
	 */
	public final synchronized File transfer() {
		//---- chama evento -----------------------
		pvEventStarted(); //Chama evento
		//-----------------------------------------

		URL xURL = null;
		File xFile = null;
		
		try {
			xURL = new URL(wURL);
		} catch (MalformedURLException e1) {
//			wLogger.error("Não foi possível converter a URL: " + e1.getMessage(),e1);
			xURL = null;
		}

		try {
			if (xURL != null) {
				xFile = pvDownloadFile(xURL, wDestino);
			} else {
				xFile = pvDownloadFile(wURL, wDestino);
			}
		} catch (FileNotFoundException e) {
			wLogger.error("Arquivo não encontrado:" + wURL);
		} catch (IOException e) {
			wLogger.error("Erro ao tentar efetuar o Download do arquivo", e);
		}
		
		//---- chama evento -----------------------
		
		pvEventEnded();
		//-----------------------------------------
		return xFile;
	}
	
	
	/**
	 * Altera o valor da variável wInterromper para interromper o processo.
	 */
	public final void interrupt() {
		this.wInterrupted = true;
		pvEventInterrupted();
	}
	

	/**
	 * Renomeia o arquivo atual adicionando a versão para efeito de histórico.
	 * @param String Versao no formato yyyy-MM-dd_HH-mm-ss
	 * @throws DBSException 
	 */
	public final boolean renomearArquivoAtual(String pVersao) {
		if (wDestino.lastIndexOf(".") != -1) {
			File xArquivoAtual = new File(wDestino);
			File xArquivoAntigo = new File(wDestino.substring(0, wDestino.lastIndexOf(".")) + "_" + pVersao.trim() + wDestino.substring(wDestino.lastIndexOf(".")));
			xArquivoAtual.renameTo(xArquivoAntigo);
			return true;
		} else {
			interrupt();
			return false;
		}
	}

	
	//Private =======================================================================
	/**
	 * Método privado que executa o download do arquivo.
	 * Caso a pasta de destino não exista, este método tenta cria-la.
	 * Durante o download é feita a verificação do timeout, que caso 
	 * seja excedido, o método retorna null.
	 * 
	 * @param pURL URL da fonte do arquivo.
	 * @param pDestino String do destino da transferência.
	 * @return File Retorna o arquivo transferido.
	 * @throws IOException
	 */
	private final File pvDownloadFile(URL pURL, String pDestino) throws IOException {
		//System.out.println("Abrindo conexão para Download");
		setTransferState(TransferState.CONNECTING);
		HttpURLConnection xConnection = (HttpURLConnection) pURL.openConnection();
		xConnection.setRequestProperty("Request-Method", "POST");
		xConnection.setDoInput(true);
		xConnection.setDoOutput(false);
		xConnection.setConnectTimeout(DBSNumber.toInteger(wTimeOut)); //DEFINE O TIMEOUT DE CONEXAO
		xConnection.connect();
		
//		xConnection.setChunkedStreamingMode(0);
//		System.out.println(xConnection.getLastModified());
//		System.out.println(xConnection.getContentLength());
		
		if (xConnection.getResponseCode() == 200) {
			if (DBSFile.exists(pDestino) && !DBSObject.isEmpty(getVersao())) {
				if (xConnection.getLastModified() != 0 && xConnection.getLastModified() == getVersao().getTime()) {
					wLogger.info("Arquivo não baixado. Versão atual já é a mais nova.");
					setTransferState(TransferState.NOTSTARTED);
					return null;
				}
			} 
			setVersao(new Timestamp(xConnection.getLastModified()));
			InputStream xReader = xConnection.getInputStream();
			
			File xInputFile = new File(pDestino);
			if (!xInputFile.isFile()) { //Cria a pasta do arquivo caso ela não exista.
				//System.out.println("Criando pasta de download");
				DBSFile.mkDir(xInputFile);
//			} else {
//				File xFile = new File(pDestino);
//				Date xData = new Date(xFile.lastModified());
//				String xNome = pDestino;
//				String xNovoNome = DBSString.getSubString(xNome, 1, xNome.length() - 4) + "_" + xData.toString() + DBSString.getSubString(xNome, xNome.length() - 3, xNome.length());
//				xFile.renameTo(new File(xNovoNome));
			}
			FileOutputStream xDownloadedFile = new FileOutputStream(xInputFile);
			
			byte[] xBuffer = new byte[153600];
			//int xBytesReadedTotal = 0;
			int xBytesReaded = 0;
			
//				System.out.println("Lendo arquivo.\n");
			
			wInterrupted = false;
			try {
				while ((xBytesReaded = xReader.read(xBuffer)) != -1 && 
						(getElapsedTime() < wTimeOut || wTimeOut == 0L) && 
						!wInterrupted) { //Se o timeout for 0 ele irá ler até acabar.
					xDownloadedFile.write(xBuffer, 0, xBytesReaded);
					xBuffer = new byte[153600];
					//xBytesReadedTotal += xBytesReaded;
					pvEventTransferring();//EVENTO
				}
			} catch (IOException e) {
				wLogger.warn(e);
			}
			
			xDownloadedFile.close();
			xReader.close();
			xConnection.disconnect();
			
			if (getElapsedTime() > wTimeOut && wTimeOut != 0L) {
				wLogger.warn("Erro de Timeout.");
				setTransferState(TransferState.ERROR);
				return null;
			} else if (wInterrupted) {
				wLogger.warn("Processo interrompido pelo usuário.");
				setTransferState(TransferState.ERROR);
				return null;
			} else {
				//			System.out.println("Executado. "
				//					+ (new Integer(xBytesReadedTotal).toString()) + " bytes read ("
				//					+ (new Long(xEndTime - xStartTime).toString())
				//					+ " millseconds).\n");
				setTransferState(TransferState.SUCESS);
				return xInputFile;
			}
		} else {
			wLogger.error("Erro tentando baixar aquivo: " + xConnection.getResponseMessage());
			setTransferState(TransferState.ERROR);
			return null;
		}
	}
	
	private File pvDownloadFile(String pURL, String pDestino) throws IOException {
		//System.out.println("Lendo arquivo.\n");
		
	    setTransferState(TransferState.CONNECTING);
		File xSource = new File(pURL);
		File xDestino = new File(pDestino);
		
		if (!xDestino.isFile()) { //Cria a pasta do arquivo caso ela não exista.
			DBSFile.mkDir(xDestino);
		}
		
		if (xDestino.exists()) { //Deleta o arquivo caso ele já exista.
			xDestino.delete();
		}
		xDestino.createNewFile();
	  
	     FileChannel sourceChannel = null;   
	     FileChannel destinationChannel = null;   
	     try {   
	         sourceChannel = new FileInputStream(xSource).getChannel();   
	         destinationChannel = new FileOutputStream(xDestino).getChannel();   
	         sourceChannel.transferTo(0, sourceChannel.size(),   
	                 destinationChannel);
//	         destinationChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
	         pvEventTransferring(); //EVENTO
	     } finally {   
	         if (sourceChannel != null && sourceChannel.isOpen())   
	             sourceChannel.close();   
	         if (destinationChannel != null && destinationChannel.isOpen())   
	             destinationChannel.close();   
	    }   
		
		if (getElapsedTime() > wTimeOut && wTimeOut != 0L) {
			wLogger.warn("Erro de Timeout.");
			setTransferState(TransferState.ERROR);
			return null;
		} else {
//			System.out.println("Executado em "
//					+ (new Long(xEndTime - xStartTime).toString())
//					+ " millseconds.\n");
			setTransferState(TransferState.SUCESS);
			setVersao(new Timestamp(xSource.lastModified()));
			return xDestino;
		}

	}
	
	//EVENTOS =======================================================================
	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvEventStarted(){
		setTimeStarted();
		setTransferState(TransferState.STARTED);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).started(this);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvEventEnded(){
		setTimeEnded();
		setTransferState(TransferState.ENDED);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).ended(this);
        }		
	}

	/**
	 * Chamada durante a execução
	 */
	private void pvEventTransferring(){
		getTimeEnded();
		setTransferState(TransferState.TRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferring(this);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvEventInterrupted(){
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).interrupted(this);
        }		
	}

	/**
	 * Chamada quando a execução mudou de situação(Executando, Parado ou Agendada)
	 * Local onde deverá ser implementado a atualização deste dado no banco de dados, se for o caso
	 */
	private void pvEventTransferStateChanged(){
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferStateChanged(this);
        }		
	}


}
