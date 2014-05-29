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

/**
 * @author ricardo.villar
 *
 */
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
	private String 							wLocalFileName;
	private Timestamp						wVersion;
	private Long							wTimeOut = 0L;
	private List<IDBSFileTransferEvents>	wEventListeners = new ArrayList<IDBSFileTransferEvents>();
	private boolean							wInterrupted; 
	private long							wTimeStarted;
	private long							wTimeEnded;
	private TransferState					wTransferState;

	/**
	 * Construtor que configura os parametros para efetuar a transferencia.<br/>
	 * Para iniciar o download utilize o método <b>transfer<b/>.
	 * @param pURL
	 * @param pLocalFileName
	 * @param pVersion
	 */
	public DBSFileTransfer(String pURL, String pLocalFileName, Timestamp pVersion) {
		this.wLocalFileName = pLocalFileName;
		this.wURL = pURL;
		this.wVersion = pVersion;
	}


	/**
	 * Adiciona uma classe que receberá as chamadas dos eventos deste transferencia quando ocorrerem.
	 * Classe deve implementar <b>IDBSFileTransferEvents</b>.
	 * @param pEventListener 
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos
	 */
	public final void addEventListener(IDBSFileTransferEvents pEventListener) {
		wEventListeners.add(pEventListener);
	}

	/**
	 * Remove classe listener.
	 * @param pEventListener
	 */
	public final void removeEventListener(IDBSTaskEventsListener pEventListener) {
		wEventListeners.remove(pEventListener);
	}

	/**
	 * Tempo de espera para finalizar o download.
	 * Após este tempo, será emitida mensagem de erro.
	 * @return
	 */
	public final Long getTimeOut() {
		return wTimeOut;
	}

	/**
	 * Tempo de espera para finalizar o download.
	 * Após este tempo, será emitida mensagem de erro.
	 */
	public final void setTimeOut(Long pTimeOut) {
		wTimeOut = pTimeOut;
	}

	/**
	 * URL remota de onde será efetuado o download.
	 * @return
	 */
	public final String getURL() {
		return wURL;
	}
	
	/**
	 * URL remota de onde será efetuado o download.
	 * @return
	 */
	public final void setURL(String pURL) {
		wURL = pURL;
	}

	/**
	 * Nome do arquivo local que será criado em função do download.
	 * @param pLocalFileName
	 */
	public final void setLocalFileName(String pLocalFileName) {
		wLocalFileName = pLocalFileName;
	}

	/**
	 * Nome do arquivo local que será criado em função do download.
	 */
	public final String getLocalFileName() {
		return wLocalFileName;
	}

	public final Timestamp getVersion() {
		return wVersion;
	}

	public final void setVersion(Timestamp pVersion) {
		wVersion = pVersion;
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
			pvFireEventTransferStateChanged();
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
	 * Executa o download do arquivo,
	 * 
	 * @return boolean True se conseguir realizar a operação.
	 */
	public final synchronized File transfer() {
		//---- chama evento -----------------------
		pvFireEventStarted(); //Chama evento
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
				xFile = pvDownloadFile(xURL, wLocalFileName);
			} else {
				xFile = pvDownloadFile(wURL, wLocalFileName);
			}
		} catch (FileNotFoundException e) {
			wLogger.error("Arquivo não encontrado:" + wURL);
		} catch (IOException e) {
			wLogger.error("Erro ao tentar efetuar o Download do arquivo", e);
		}
		
		//---- chama evento -----------------------
		
		pvFireEventEnded();
		//-----------------------------------------
		return xFile;
	}
	
	
	/**
	 * Interrmpo o processo de download
	 */
	public final synchronized void interrupt() {
		this.wInterrupted = true;
		pvFireEventInterrupted();
	}
	

	/**
	 * Renomeia o arquivo atual(localfile) adicionando a versão para efeito de histórico.
	 * @param String pVersion no formato yyyy-MM-dd_HH-mm-ss
	 * @throws DBSException 
	 */
	public final boolean addVersionToLocalFile(String pVersion) {
		if (wLocalFileName.lastIndexOf(".") != -1) {
			File xArquivoAtual = new File(wLocalFileName);
			File xArquivoAntigo = new File(wLocalFileName.substring(0, wLocalFileName.lastIndexOf(".")) + "_" + pVersion.trim() + wLocalFileName.substring(wLocalFileName.lastIndexOf(".")));
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
	 * @param pLocalFileName String do destino da transferência.
	 * @return File Retorna o arquivo transferido.
	 * @throws IOException
	 */
	private final File pvDownloadFile(URL pURL, String pLocalFileName) throws IOException {
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
			if (DBSFile.exists(pLocalFileName) && !DBSObject.isEmpty(getVersion())) {
				if (xConnection.getLastModified() != 0 && xConnection.getLastModified() == getVersion().getTime()) {
					wLogger.info("Arquivo não baixado. Versão atual já é a mais nova.");
					setTransferState(TransferState.NOTSTARTED);
					return null;
				}
			} 
			setVersion(new Timestamp(xConnection.getLastModified()));
			InputStream xReader = xConnection.getInputStream();
			
			File xInputFile = new File(pLocalFileName);
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
					pvFireEventTransferring();//EVENTO
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
	
	private File pvDownloadFile(String pURL, String pLocalFileName) throws IOException {
		setTransferState(TransferState.CONNECTING);
		File xSource = new File(pURL);
		File xLocalFile = new File(pLocalFileName);
		
		if (!xLocalFile.isFile()) { //Cria a pasta do arquivo caso ela não exista.
			DBSFile.mkDir(xLocalFile);
		}
		
		if (xLocalFile.exists()) { //Deleta o arquivo caso ele já exista.
			xLocalFile.delete();
		}
		xLocalFile.createNewFile();
	  
		FileChannel xSourceChannel = null;   
		FileChannel xDestinationChannel = null;   
		try {   
			xSourceChannel = new FileInputStream(xSource).getChannel();   
			xDestinationChannel = new FileOutputStream(xLocalFile).getChannel();   

			xSourceChannel.transferTo(0, xSourceChannel.size(), xDestinationChannel);
//	         destinationChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
			pvFireEventTransferring(); //EVENTO
	    }finally {   
	    	if (xSourceChannel != null && xSourceChannel.isOpen()){   
	    		xSourceChannel.close();   
	    	}
	    	if (xDestinationChannel != null && xDestinationChannel.isOpen()){   
	             xDestinationChannel.close();   
	    	}
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
			setVersion(new Timestamp(xSource.lastModified()));
			return xLocalFile;
		}

	}
	
	//EVENTOS =======================================================================
	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventStarted(){
		setTimeStarted();
		setTransferState(TransferState.STARTED);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).started(this);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventEnded(){
		setTimeEnded();
		setTransferState(TransferState.ENDED);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).ended(this);
        }		
	}

	/**
	 * Chamada durante a execução
	 */
	private void pvFireEventTransferring(){
		getTimeEnded();
		setTransferState(TransferState.TRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferring(this);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventInterrupted(){
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).interrupted(this);
        }		
	}

	/**
	 * Chamada quando a execução mudou de situação(Executando, Parado ou Agendada)
	 * Local onde deverá ser implementado a atualização deste dado no banco de dados, se for o caso
	 */
	private void pvFireEventTransferStateChanged(){
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferStateChanged(this);
        }		
	}


}
