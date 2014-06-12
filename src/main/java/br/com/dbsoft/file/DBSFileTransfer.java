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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDKMessages;
import br.com.dbsoft.error.DBSException;
import br.com.dbsoft.task.IDBSTaskEventsListener;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;

/**
 * @author ricardo.villar
 *
 */
public class DBSFileTransfer{

	
	public enum TransferState{
		NOTTRANSFERING 	("Not Transfering", 0),
		TRANSFERING 	("Transfering", 1); 

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
			case 0:
				return TransferState.NOTTRANSFERING;
			case 1:
				return TransferState.TRANSFERING;
			default:
				return TransferState.NOTTRANSFERING;
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
	
	public enum LocalFileNameOrigin{
		HEADER 			("Header", 1),
		URL 			("URL", 2), 
		USER 			("User", 3); 
		
		private String 	wName;
		private int 	wCode;
		
		private LocalFileNameOrigin(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static LocalFileNameOrigin get(int pCode) {
			switch (pCode) {
			case 1:
				return LocalFileNameOrigin.HEADER;
			case 2:
				return LocalFileNameOrigin.URL;
			case 3:
				return LocalFileNameOrigin.USER;
			default:
				return LocalFileNameOrigin.USER;
			}
		}			
	}
	
	
	protected static Logger					wLogger = Logger.getLogger(DBSFileTransfer.class);
	
	private String 							wURL;
	private String 							wLocalFileName = null;
	private Timestamp						wVersion;
	private List<IDBSFileTransferEvents>	wEventListeners = new ArrayList<IDBSFileTransferEvents>();
	private boolean							wInterrupted; 
	private Long							wTimeStarted = 0L;
	private Long							wTimeEnded = 0L;
	private Long							wTimeOut = 0L;
	private TransferState					wTransferState;
	private LocalFileNameOrigin				wLocalFileNameOrigin = LocalFileNameOrigin.USER;
	private String							wRemoteServer;
	private String	 						wMsgErro;

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

	public DBSFileTransfer(String pURL, Timestamp pVersion) {
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
	 * Retorna se tarefa ultrapassou o tempo permitido.
	 * @return
	 */
	public boolean isTimeOut(){
		if (wTimeOut == 0L){
			return false;
		}
		return ((System.currentTimeMillis() - wTimeStarted) < wTimeOut);
	}
	/**
	 * Retorna date/hora que foi iniciado o transfer
	 * @return
	 */
	public final Long getTimeStarted(){
		return wTimeStarted;
	}

	/**
	 * Retorna data/hora que foi finalizado o transfer ou a hora atual caso esteja em processo de transferencia
	 * @return
	 */
	public final Long getTimeEnded(){
		return wTimeEnded;
	}

	/**
	 * Retorna o tempo de corrido
	 * @return
	 */
	public final Long getTimeElapsed(){
		return wTimeEnded - wTimeStarted;
	
	}

	/**
	 * Retorna da onde se originou o nome do arquivo local.
	 * @return
	 */
	public LocalFileNameOrigin getLocalFileNameOrigin() {
		return wLocalFileNameOrigin;
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
	 * Nome do arquivo local que será criado em função do download, 
	 * caso esta informação não seja fornecida do próprio request.  
	 * @param pLocalFileName
	 */
	public final void setLocalFileName(String pLocalFileName) {
		wLocalFileName = pLocalFileName;
	}

	/**
	 * Nome do arquivo local que será criado em função do download, 
	 * caso esta informação não seja fornecida do próprio request.  
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
	 * Retorna o servidor utilizado na URL remota
	 * @return
	 */
	public String getRemoteServer() {
		return wRemoteServer;
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
	* Retorna a mensagem de erro, caso tenha.
	* @return Mensagem String
	*/
	public String getMsgErro() {
		return wMsgErro;
	}
	
	/**
	* Configura a mensagem de erro a ser recuperada por quem executar o download.
	* @param pMsgErro
	*/
	public void setMsgErro(String pMsgErro) {
		wMsgErro = pMsgErro;
	}
	public void setMsgErro(String pMsgErro, Level pLevel) {
		setMsgErro(pMsgErro);
		if (!DBSObject.isEmpty(pLevel)) {
			if (pLevel == Level.ERROR) {
				wLogger.error(pMsgErro);
			} else if (pLevel == Level.WARN) {
				wLogger.warn(pMsgErro);
			} else if (pLevel == Level.INFO) {
				wLogger.info(pMsgErro);
			} else if (pLevel == Level.DEBUG) {
				wLogger.debug(pMsgErro);
			}
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
	 * Executa o download do arquivo,
	 * 
	 * @return boolean True se conseguir realizar a operação.
	 */
	public final synchronized File transfer() {
		if (wURL == null){
			setMsgErro(DBSSDKMessages.URLRemotaNaoInformada, Level.ERROR);
		}

		//---- chama evento -----------------------
		pvFireEventStarted(); //Chama evento
		//-----------------------------------------

		URL xURL = null;
		File xFile = null;
		
		try {
			xURL = new URL(wURL);
		} catch (MalformedURLException e1) {
//			wLogger.error("Não foi possível converter a URL: " + wURL, e1);
			setMsgErro(DBSSDKMessages.ErroConvertendoURL + wURL, Level.ERROR);
			xURL = null;
		}

		try {
			if (xURL != null) {
				xFile = pvDownloadFile(xURL);
			} else {
				xFile = pvDownloadFile(wURL);
			}
		} catch (FileNotFoundException e) {
//			wLogger.error("Arquivo não encontrado:" + wURL);
			setMsgErro(DBSSDKMessages.ArquivoNaoEncontrado + wURL, Level.ERROR);
		} catch (IOException e) {
//			wLogger.error("Erro ao tentar efetuar o Download do arquivo", e);
			setMsgErro(DBSSDKMessages.ErroGenerico + e.getLocalizedMessage(), Level.ERROR);
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
	 * Executa o download do arquivo.
	 * Caso a pasta de destino não exista, este método tenta criá-la. <br/>
	 * Tenta recuperar o nome do arquivo, atráves do cabeçalho das resposta da conexão com a URL. Caso não seja possível,
	 * utilizar o nome informado pelo usuário em <b>pLocalFileName</b>, caso não tenha sido informado, tenta recuperar através do texto da URL.
	 * Durante o download é feita a verificação do timeout, que caso 
	 * seja excedido, o método retorna null.
	 * 
	 * @param pURL URL da origem do arquivo.
	 * @param pLocalFileName Caminho local onde será gravado o arquivo recebido. 
	 * Caminho deve conter também o nome do arquivo local, caso este não seja informado automaticamente na conexão.
	 * @return File Retorna o arquivo transferido.
	 * @throws IOException
	 */
	private final File pvDownloadFile(URL pURL) throws IOException {
		setTransferState(TransferState.TRANSFERING);
		
		HttpURLConnection xConnection = (HttpURLConnection) pURL.openConnection();
		xConnection.setRequestMethod("GET");
		xConnection.setAllowUserInteraction(false);
		xConnection.setDoInput(true);
		xConnection.setDoOutput(false);
		xConnection.setConnectTimeout(DBSNumber.toInteger(wTimeOut)); //DEFINE O TIMEOUT DE CONEXAO
		xConnection.connect();
		
		
		//Recupera nome do arquivo
		String xContent = xConnection.getHeaderField("Content-Disposition");
		
		String xRemoteFileName = DBSFile.getFileNameFromPath(wLocalFileName);
		//Recupera nome do arquivo enviado pela conexão
		if(xContent != null 
		&& xContent.indexOf("=") != -1) {
		    String[] xFileName = xContent.split("=");
		    xRemoteFileName = DBSObject.getNotEmpty(xFileName[1], null);
		    wLocalFileNameOrigin = LocalFileNameOrigin.HEADER;
		}else{
			//Recupera nome da URL se não foi definido o nome pelo usuário
			if (DBSObject.isEmpty(xRemoteFileName)){
				if (pURL.getFile() != null){
				    wLocalFileNameOrigin = LocalFileNameOrigin.URL;
					xRemoteFileName = DBSFile.getFileNameFromPath(pURL.getFile());
				}
			}
		}
		
		if (DBSObject.isEmpty(xRemoteFileName)){
//			wLogger.info("Nome do arquivo local não foi informado.");
			setMsgErro(DBSSDKMessages.ArquivoLocalNaoInformado);
			xConnection.disconnect();
			setTransferState(TransferState.NOTTRANSFERING);
			return null;
		}
		
		if (xConnection.getResponseCode() == 200) {
			//Reconstroi o nome do arquivo local
	    	wLocalFileName = DBSFile.getPathFromFileName(wLocalFileName) + xRemoteFileName;
			//Salva qual o servidor utilizado
			wRemoteServer = xConnection.getHeaderField("Server");
			if (DBSFile.exists(wLocalFileName) && !DBSObject.isEmpty(getVersion())) {
				if (xConnection.getLastModified() != 0 && xConnection.getLastModified() == getVersion().getTime()) {
//					wLogger.info("Arquivo não baixado. Versão atual já é a mais nova.");
					setMsgErro(DBSSDKMessages.ArquivoNaoBaixadoVersaoAtual, Level.INFO);
					setTransferState(TransferState.NOTTRANSFERING);
					return null;
				}
			} 
			setVersion(new Timestamp(xConnection.getLastModified()));
			InputStream xReader = xConnection.getInputStream();
			
			File xInputFile = new File(wLocalFileName);
			if (!xInputFile.isFile()) { //Cria a pasta do arquivo caso ela não exista.
				DBSFile.mkDir(xInputFile);
			}
			FileOutputStream xDownloadedFile = new FileOutputStream(xInputFile); 
			
			byte[] xBuffer = new byte[153600];

			int xBytesReaded = 0;
			
			wInterrupted = false;
			try {
				while ((xBytesReaded = xReader.read(xBuffer)) != -1 && 
						!isTimeOut() && 
						!wInterrupted) { //Se o timeout for 0 ele irá ler até acabar.
					xDownloadedFile.write(xBuffer, 0, xBytesReaded);
					xBuffer = new byte[153600];
					//xBytesReadedTotal += xBytesReaded;
					pvFireEventTransferring();//EVENTO
				}
			} catch (IOException e) {
				wLogger.warn(e);
			} finally{
				xDownloadedFile.close();
				xReader.close();
				xConnection.disconnect();
				setTransferState(TransferState.NOTTRANSFERING);
			}
			
			if (getTimeElapsed() > wTimeOut && wTimeOut != 0L) {
//				wLogger.warn("Erro de Timeout.");
				setMsgErro(DBSSDKMessages.ErroTimeout, Level.WARN);
				return null;
			} else if (wInterrupted) {
//				wLogger.warn("Processo interrompido pelo usuário.");
				setMsgErro(DBSSDKMessages.ProcessoInterrompidoUsuario, Level.WARN);
				return null;
			} else {
				return xInputFile;
			}
		} else {
//			wLogger.error("Erro tentando baixar aquivo: " + xConnection.getResponseMessage());
			setMsgErro(DBSSDKMessages.ErroGenerico + xConnection.getResponseMessage(), Level.ERROR);
			return null;
		}
	}
	
	private File pvDownloadFile(String pURL) throws IOException {

		File xSource = new File(pURL);
		File xLocalFile = new File(wLocalFileName);
		
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
		
		if (getTimeElapsed() > wTimeOut && wTimeOut != 0L) {
//			wLogger.warn("Erro de Timeout.");
			setMsgErro(DBSSDKMessages.ErroTimeout);
			return null;
		} else {
			setVersion(new Timestamp(xSource.lastModified()));
			return xLocalFile;
		}

	}
	
	//EVENTOS =======================================================================
	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventStarted(){
		wTimeStarted = System.currentTimeMillis();
		wTimeEnded = wTimeStarted;

		setTransferState(TransferState.TRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).started(this);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventEnded(){
		wTimeEnded = System.currentTimeMillis();

		setTransferState(TransferState.NOTTRANSFERING);
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
