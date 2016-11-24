package br.com.dbsoft.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import br.com.dbsoft.core.DBSSDK.ENCODE;
import br.com.dbsoft.core.DBSSDK.NETWORK.METHOD;
import br.com.dbsoft.core.DBSSDKMessages;
import br.com.dbsoft.error.DBSException;
import br.com.dbsoft.task.IDBSTaskEventsListener;
import br.com.dbsoft.util.DBSFile;
import br.com.dbsoft.util.DBSNumber;
import br.com.dbsoft.util.DBSObject;
import br.com.dbsoft.util.DBSString;

/**
 * @author ricardo.villar
 *
 */
public class DBSFileTransfer{

	private static Integer wBufferSize  = 153600;
	
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
	private String 							wLocalFileNameOnly = null;
	private String 							wLocalPath = null;
	private List<IDBSFileTransferEventsListener>	wEventListeners = new ArrayList<IDBSFileTransferEventsListener>();
	private boolean							wInterrupted; 
	private Long							wTimeStarted = 0L;
	private Long							wTimeEnded = 0L;
	private Long							wTimeOut = 0L;
	private TransferState					wTransferState;
	private LocalFileNameOrigin				wLocalFileNameOrigin = LocalFileNameOrigin.USER;
	private Timestamp						wLastModified;
	private String							wRemoteServer;
	private String	 						wMsgErro;
	private METHOD	 						wMethod = METHOD.GET;
	private List<String>					wRequestPropertys;
	private byte[]							wFileContent = null;
	private Boolean							wOk = true;

	/**
	 * @param pURL do arquivo que se deseja baixar 
	 */
	public DBSFileTransfer(String pURL) {
		setURL(pURL);
	}

	/**
	 * @param pURL 
	 * @param pMethod
	 * @param pRequestPropertys
	 */
	public DBSFileTransfer(String pURL, METHOD pMethod) {
		setURL(pURL);
		this.wMethod = pMethod;
		this.wRequestPropertys = null;
	}

	/**
	 * @param pURL
	 * @param pMethod
	 * @param pRequestPropertys
	 */
	public DBSFileTransfer(String pURL, METHOD pMethod, List<String> pRequestPropertys) {
		setURL(pURL);
		this.wMethod = pMethod;
		this.wRequestPropertys = pRequestPropertys;
	}


	/**
	 * @param pURL do arquivo que se deseja baixar 
	 * @param pLocalPath Caminho local
	 */
	public DBSFileTransfer(String pURL, String pLocalPath) {
		setURL(pURL);
		setLocalPath(pLocalPath);
	}

	
	/**
	 * @param pURLURL do arquivo que se deseja baixar 
	 * @param pLocalPath Caminho local
	 * @param pLocalFileNameOnly Nome do arquivo local, caso não desejar alterar o nome original contido ou retornado na URL
	 */
	public DBSFileTransfer(String pURL, String pLocalPath, String pLocalFileNameOnly) {
		setURL(pURL);
		setLocalPath(pLocalPath);
		setLocalFileNameOnly(pLocalFileNameOnly);
	}


	/**
	 * Construtor que configura os parametros para efetuar a transferencia.<br/>
	 * @param pURLURL do arquivo que se deseja baixar 
	 * @param pLocalPath Caminho local
	 * @param pMethod
	 * @param pRequestPropertys
	 */
	public DBSFileTransfer(String pURL, String pLocalPath, METHOD pMethod, List<String> pRequestPropertys) {
		setURL(pURL);
		setLocalPath(pLocalPath);
		this.wMethod = pMethod;
		this.wRequestPropertys = pRequestPropertys;
	}
	
	/**
	 * Construtor que configura os parametros para efetuar a transferencia.<br/>
	 * @param pURLURL do arquivo que se deseja baixar 
	 * @param pLocalPath Caminho local
	 * @param pLocalFileName Nome do arquivo local, caso não desejar alterar o nome original contido ou retornado na URL
	 * @param pMethod
	 * @param pRequestPropertys
	 */
	public DBSFileTransfer(String pURL, String pLocalPath, String pLocalFileNameOnly, METHOD pMethod, List<String> pRequestPropertys) {
		setURL(pURL);
		setLocalPath(pLocalPath);
		setLocalFileNameOnly(pLocalFileNameOnly);
		this.wMethod = pMethod;
		this.wRequestPropertys = pRequestPropertys;
	}

	/**
	 * Adiciona uma classe que receberá as chamadas dos eventos deste transferencia quando ocorrerem.
	 * Classe deve implementar <b>IDBSFileTransferEvents</b>.
	 * @param pEventListener 
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos
	 */
	public final void addEventListener(IDBSFileTransferEventsListener pEventListener) {
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
		if (wTimeOut == null
		 || wTimeOut == 0L){
			return false;
		}
		return ((System.currentTimeMillis() - wTimeStarted) < wTimeOut);
	}
	
	public boolean isOk(){
		return wOk;
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
	 * Data e hora da última modificação do arquivo transferido.
	 * @return
	 */
	public Timestamp getLastModified() {
		return wLastModified;
	}

	/**
	 * Data e hora da última modificação do arquivo transferido.
	 * @return
	 */
	private void setLastModified(Timestamp pLastModified) {
		wLastModified = pLastModified;
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
	 * Nome do arquivo local que será criado em função do download.<br/> 
	 * caso esta informação não seja fornecida do próprio request.  
	 * @param pLocalFileName
	 */
	public final void setLocalPath(String pLocalPath) {
		wLocalPath = DBSFile.getPathFromFolderName(pLocalPath);
	}

	/**
	 * Nome do arquivo local que será criado em função do download, 
	 * caso esta informação não seja fornecida do próprio request.  
	 */
	public final String getLocalPath() {
		return wLocalPath;
	}
	

	/**
	 * Nome do arquivo local que será criado em função do download incluido o caminho da pasta.  
	 */
	public final String getLocalFileName() {
		return wLocalPath + wLocalFileNameOnly;
	}
	
	/**
	 * Nome do arquivo local que será criado em função do download.  
	 * Para nome do arquivo conténdo a caminho completo, incluido a pasta, 
	 * utilize <b>getLocalFileNameFullPath</b>  
	 * @return
	 */
	public final String getLocalFileNameOnly() {
		return wLocalFileNameOnly;
	}

	/**
	 * Nome do arquivo local que será criado em função do download.<br/> 
	 * caso esta informação não seja fornecida do próprio request.  
	 * @param pLocalFileNameOnly
	 */
	public final void setLocalFileNameOnly(String pLocalFileNameOnly) {
		wLocalFileNameOnly = DBSFile.getFileNameFromPath(pLocalFileNameOnly);
	}

	/**
	 * Retorna o servidor utilizado na URL remota
	 * @return
	 */
	public String getRemoteServer() {
		return wRemoteServer;
	}

	/**
	 * Retorna conteúdo do arquivo.<br/>
	 * Caso o encode não seja nulo, converte para o encode informado.
	 * @param pEncode
	 * @return
	 */
	public String getFileContent(String pEncode){
		String xValue = null;
		if (wFileContent != null){
			try {
				if (pEncode == null){
					xValue = new String(wFileContent);
				}else{
					xValue = new String(wFileContent, pEncode);
				}
			} catch (UnsupportedEncodingException e) {
				wLogger.error(e);
			}
		}
		return xValue;
	}

	/**
	 * Retorna conteúdo do arquivo em bytes.
	 * @return
	 */
	public byte[] getFileContent(){
		return wFileContent;
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
//	public void setMsgErro(String pMsgErro) {
//	}
	public void setMsgErro(String pMsgErro, Level pLevel) {
//		setMsgErro(pMsgErro);
		wMsgErro = pMsgErro;
		if (!DBSObject.isEmpty(pLevel)) {
			if (pLevel == Level.ERROR) {
				wLogger.error(pMsgErro);
				wOk = false;
			} else if (pLevel == Level.WARN) {
				wLogger.warn(pMsgErro);
				wOk = false;
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
	 * Executa o download do arquivo.<br/>
	 * Quando não for informado o <b>LocalPath</b> o arquivo será salvo somente em memória
	 * Nesta situação deve-se consultar seu conteúdo através do método <b>getFileContent()</b>.<br/>
	 * Para consultar se houve sucesso na transferência, deve-se consultar o atributo <b>isOk()</b>.
	 * 
	 * @return File Arquivo se conseguir realizar a operação.
	 */
	public final synchronized File transfer() {
		wOk = true;
		wMsgErro = null;
		if (wURL == null){
			setMsgErro(DBSSDKMessages.URLRemotaNaoInformada, Level.ERROR);
			return null;
		}

		//---- chama evento -----------------------
		pvFireEventBeforeFileTransfer(); //Chama evento
		//-----------------------------------------

		URL xURL = null;
		File xFile = null;
		
		try {
			wFileContent = null;
			xURL = new URL(wURL);
		} catch (MalformedURLException e1) {
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
			setMsgErro(DBSSDKMessages.ArquivoNaoEncontrado + wURL, Level.ERROR);
		} catch (IOException e) {
			setMsgErro(DBSSDKMessages.ErroGenerico + e.getLocalizedMessage(), Level.ERROR);
		}
		
		//---- chama evento -----------------------
		
		pvFireEventAfterFileTransfer();
		//-----------------------------------------
		return xFile;
	}
	
	
	/**
	 * Interrmpo o processo de download
	 */
	public final void interrupt() {
		this.wInterrupted = true;
		pvFireEventInterrupted();
	}
	

	/**
	 * Renomeia o arquivo atual(localfile) adicionando a versão para efeito de histórico.
	 * @param String pVersion no formato yyyy-MM-dd_HH-mm-ss
	 * @throws DBSException 
	 */
	public final boolean addVersionToLocalFile(String pVersion) {
		if (wLocalFileNameOnly.lastIndexOf(".") != -1) {
			File xArquivoAtual = new File(getLocalFileName());
			File xArquivoAntigo = new File(wLocalPath + wLocalFileNameOnly.substring(0, wLocalFileNameOnly.lastIndexOf(".")) + "_" + pVersion.trim() + wLocalFileNameOnly.substring(wLocalFileNameOnly.lastIndexOf(".")));
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
		if (pURL.toString().startsWith("ftp://")) {
			return pvDownloadFileFTP(pURL);
		} else {
			return pvDownloadFileHTTP(pURL);
		}
	}
		
	private final File pvDownloadFileHTTP(URL pURL) throws IOException {
		setTransferState(TransferState.TRANSFERING);
		
		HttpURLConnection xConnection = null;
		try{
			//Força a criação de cookie para evitar erro em sites que obriguem um sessionid no request para o download. 
			if (CookieHandler.getDefault() == null){
				CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
			}
			//Abre conexão 
			xConnection = (HttpURLConnection) pURL.openConnection();
		}catch(Exception e){
			wLogger.error(e);
			return null;
		}
		//REQUEST===================================
		xConnection.setRequestMethod(wMethod.getName());
		xConnection.setDoInput(true);
		xConnection.setDoOutput(true);
		//Configura parametros para a request
		if (!DBSObject.isNull(wRequestPropertys) && !wRequestPropertys.isEmpty()) {
			StringBuilder xParams = new StringBuilder();
			boolean first = true;
			for (String xProperty : wRequestPropertys) {
				String xKey = DBSString.getSubString(xProperty, 1, xProperty.indexOf("="));
				String xValue = DBSString.getSubString(xProperty, xProperty.indexOf("=")+2, xProperty.length());

		        if (first) {
		            first = false;
		        } else {
		            xParams.append("&");
		        }
		        xParams.append(URLEncoder.encode(xKey, ENCODE.UTF_8));
		        xParams.append("=");
		        xParams.append(URLEncoder.encode(xValue, ENCODE.UTF_8));
			}
			//Efetua o request
			OutputStream xOs = xConnection.getOutputStream();
			BufferedWriter xWriter = new BufferedWriter(new OutputStreamWriter(xOs, ENCODE.UTF_8));
			xWriter.write(xParams.toString());
			xWriter.flush();
			xWriter.close();
			xOs.close();
		}
		xConnection.setConnectTimeout(DBSNumber.toInteger(wTimeOut)); //DEFINE O TIMEOUT DE CONEXAO
		xConnection.connect();
		
		if (xConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return pvDownload(pURL, xConnection);
		} else {
//			wLogger.error("Erro tentando baixar aquivo: " + xConnection.getResponseMessage());
			setMsgErro(DBSSDKMessages.ErroGenerico + xConnection.getResponseMessage(), Level.ERROR);
			return null;
		}
	}

	private final File pvDownloadFileFTP(URL pURL) throws IOException {
		URLConnection 	xConnection = pURL.openConnection();
		return pvDownload(pURL, xConnection);
	}

	private File pvDownloadFile(String pURL) throws IOException {

		if (!pvFireEventBeforeSave()){
			return null;
		}
		File xSource = new File(pURL);
		File xLocalFile = new File(wLocalFileNameOnly);

		setLastModified(null);
		if (!xLocalFile.isFile()) { //Cria a pasta do arquivo caso ela não exista.
			DBSFile.mkDir(xLocalFile);
		}
		
		if (xLocalFile.exists()) { //Deleta o arquivo caso ele já exista.
			xLocalFile.delete();
		}
		xLocalFile.createNewFile();
	  
		FileChannel xSourceChannel = null;   
		FileChannel xDestinationChannel = null;  
		FileInputStream xFileSourceStream = null;
		FileOutputStream xFileDestinationStream = null;
		try {   
			xFileSourceStream = new FileInputStream(xSource);
			xFileDestinationStream = new FileOutputStream(xLocalFile);
	    	if (xFileSourceStream != null){   
	    		xSourceChannel = xFileSourceStream.getChannel();   
	    	}
	    	if (xFileDestinationStream != null){
	    		xDestinationChannel = xFileDestinationStream.getChannel();   
	    	}
			xSourceChannel.transferTo(0, xSourceChannel.size(), xDestinationChannel);
//	         destinationChannel.transferFrom(sourceChannel, 0, Long.MAX_VALUE);
			pvFireEventTransferring(); //EVENTO
	    }finally {   
	    	if (xFileSourceStream != null){   
		    	if (xSourceChannel != null && xSourceChannel.isOpen()){   
		    		xSourceChannel.close();   
		    	}
	    		xFileSourceStream.close();   
	    	}
	    	if (xFileDestinationStream != null){   
		    	if (xDestinationChannel != null && xDestinationChannel.isOpen()){   
		             xDestinationChannel.close();   
		    	}
	    		xFileDestinationStream.close();   
	    	}
	    }   
		
		
		if (getTimeElapsed() > wTimeOut && wTimeOut != 0L) {
			setMsgErro(DBSSDKMessages.ErroTimeout, Level.ERROR);
			xLocalFile = null;
		} else if (wInterrupted) {
			setMsgErro(DBSSDKMessages.ProcessoInterrompidoUsuario, Level.WARN);
			xLocalFile = null;
		} else {
			setLastModified(new Timestamp(xSource.lastModified()));
		}

		pvFireEventAfterSave();
		
		return xLocalFile;
	}
	
	private File pvDownload(URL pURL, URLConnection pConnection) throws IOException {
		String 			 xFileFullName;
		File 			 xInputFile = null;
		FileOutputStream xDownloadedFile = null;
		
		//READ RESPONSE=====================================
		//Recupera nome do arquivo
		String xContent = pConnection.getHeaderField("Content-Disposition"); 
		
		//Como default, define o nome remoto como sendo iqual ao local
		String xRemoteFileName = wLocalFileNameOnly;

		//Recupera nome do arquivo enviado pela conexão
		if(xContent != null 
		&& xContent.indexOf("=") != -1) {
		    String[] xFileName = xContent.split("=");
		    xRemoteFileName = DBSObject.getNotEmpty(xFileName[1], null);
		    xRemoteFileName = DBSString.changeStr(xRemoteFileName, "\"", "");
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
		//Não foi encontrado o nome do arquivo a ser baixado
		if (DBSObject.isEmpty(xRemoteFileName)){
			setMsgErro(DBSSDKMessages.ArquivoLocalNaoInformado, Level.ERROR);
			if (pConnection instanceof HttpURLConnection){
				((HttpURLConnection) pConnection).disconnect();
			}
			setTransferState(TransferState.NOTTRANSFERING);
			return null;
		}
		
		//READ FILE=====================================
		//Salva qual o servidor utilizado
		wRemoteServer = pConnection.getHeaderField("Server"); 
		if (wRemoteServer == null){
			wRemoteServer = pConnection.getHeaderField("X-Powered-By");
		}
//			if (DBSFile.exists(wLocalFileName) && !DBSObject.isEmpty(getVersion())) {
//				//Verifica se arquivo a ser baixado contém e mesma data e hora do arquivo local, se existir. 
//				if (xConnection.getLastModified() != 0 && xConnection.getLastModified() == getLastModified().getTime()) {
//					setMsgErro(DBSSDKMessages.ArquivoNaoBaixadoVersaoAtual, Level.INFO);
//					setTransferState(TransferState.NOTTRANSFERING);
//					return null;
//				}
//			} 
		setLastModified(new Timestamp(pConnection.getLastModified()));
		
		if (!pvFireEventBeforeSave()){
			return null;
		}
		
//			InputStreamReader xReader = new InputStreamReader(xConnection.getInputStream(), Charset.forName(ENCODE.ISO_8859_1));
		InputStream xReader = pConnection.getInputStream();
		
		byte[] xBufferRead = new byte[wBufferSize];

		wInterrupted = false;
		try {
			//Le arquivo integralmente
			int xBytesRead = 0;
			 
			while ((xBytesRead = xReader.read(xBufferRead)) != -1 && 
				   !isTimeOut() && //Se o timeout for 0 ele irá ler até acabar.
				   !wInterrupted) {
				wFileContent = DBSString.appendByteArrays(wFileContent, xBufferRead, xBytesRead);
				//Recria buffer vazio
				xBufferRead = new byte[wBufferSize];
				pvFireEventTransferring();
			}
			
			//Grava arquivo local
			if (wLocalPath != null){
				//Defini o nome do arquivo local
				wLocalFileNameOnly = xRemoteFileName;
				//Reconstroi o nome do arquivo local
				xFileFullName = DBSFile.getPathFromFileName(wLocalPath, true) + xRemoteFileName;

				xInputFile = new File(xFileFullName);
				if (!xInputFile.isFile()) { //Cria a pasta do arquivo caso ela não exista.
					DBSFile.mkDir(xInputFile);
				}
				xDownloadedFile = new FileOutputStream(xInputFile);
				xDownloadedFile.write(wFileContent, 0, wFileContent.length);
			}
		} catch (IOException e) {
			wLogger.warn(e);
			wOk = false;
		} finally{
			//Fecha o arquivo local
			if (xDownloadedFile != null){
				xDownloadedFile.close();
			}
			xReader.close();
			if (pConnection instanceof HttpURLConnection){
				((HttpURLConnection) pConnection).disconnect();
			}
			setTransferState(TransferState.NOTTRANSFERING);
		}
		
		if (getTimeElapsed() > wTimeOut && wTimeOut != 0L) {
			setMsgErro(DBSSDKMessages.ErroTimeout, Level.ERROR);
			xInputFile = null;
		} else if (wInterrupted) {
			setMsgErro(DBSSDKMessages.ProcessoInterrompidoUsuario, Level.WARN);
			xInputFile = null;
		}else{
			pvFireEventAfterSave();
		}

		return xInputFile;
	}
	
	//EVENTOS =======================================================================
	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventBeforeFileTransfer(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		
		wTimeStarted = System.currentTimeMillis();
		wTimeEnded = wTimeStarted;

		setTransferState(TransferState.TRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeFileTransfer(xE);
        }		
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventAfterFileTransfer(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		
		wTimeEnded = System.currentTimeMillis();

		setTransferState(TransferState.NOTTRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterFileTransfer(xE);
        }		
	}

	/**
	 * Chamada antes de salvar o arquivo
	 */
	private boolean pvFireEventBeforeSave(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).beforeSave(xE);
			if (!xE.isOk()){break;}
        }
		return xE.isOk();
	}

	/**
	 * Chamada após arquivo salvo
	 */
	private void pvFireEventAfterSave(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).afterSave(xE);
        }		
	}
	
	/**
	 * Chamada durante a execução
	 */
	private boolean pvFireEventTransferring(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		setTransferState(TransferState.TRANSFERING);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferring(xE);
			if (!xE.isOk()){break;}
        }		
		return xE.isOk();
	}

	/**
	 * Chamada quando é iniciada a execução
	 */
	private void pvFireEventInterrupted(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).interrupted(xE);
        }		
	}

	/**
	 * Chamada quando a execução mudou de situação(Executando, Parado ou Agendada)
	 * Local onde deverá ser implementado a atualização deste dado no banco de dados, se for o caso
	 */
	private void pvFireEventTransferStateChanged(){
		DBSFileTransferEvent xE = new DBSFileTransferEvent(this);
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).transferStateChanged(xE);
        }		
	}



}
