package br.com.dbsoft.task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;
import br.com.dbsoft.message.DBSMessage;
import br.com.dbsoft.message.DBSMessage.MESSAGE_TYPE;
import br.com.dbsoft.message.DBSMessages;
import br.com.dbsoft.util.DBSBoolean;
import br.com.dbsoft.util.DBSDate;
import br.com.dbsoft.util.DBSFormat;
import br.com.dbsoft.util.DBSIO;
import br.com.dbsoft.util.DBSNumber;

/**
 * Class para controle de tarefa.<br/>
 * Uma terefa precisa não pode depender de valores que não sejam applicationScoped ou estáticos, já que sua execução poderá ser efetuado em momento que o escopo não esteja disponível.<br/>
 * É importante implementar o método createConnection e ter uma conexão factory declarada na classe que extend esta, para que a variável local de conexão <b>wConnection</b> possar ser setada no openConnection e closeConnection.
 * @param <DataModelClass> Armazenamento auxiliar de dados vinculados a tarefa. 
 * Os respectivos atributos da classe DataModel deverão ser preenchidos pelo usuário oportunamente nas chamadas dos eventos
 */
/**
 * @author ricardo.villar
 *
 * @param <DataModelClass>
 */
/**
 * @author ricardo.villar
 *
 * @param <DataModelClass>
 */
public class DBSTask<DataModelClass> implements IDBSTaskEventsListener {

	public static enum TaskState {
		STOPPED 		("Stopped", 1), 
		RUNNING 		("Running", 2),
		SCHEDULED 		("Scheduled", 3);
		
		private String 	wName;
		private int 	wCode;
		
		private TaskState(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static TaskState get(int pCode) {
			switch (pCode) {
			case 1:
				return TaskState.STOPPED;
			case 2:
				return TaskState.RUNNING;
			case 3:
				return TaskState.SCHEDULED;
			default:
				return TaskState.STOPPED;
			}
		}		
	}
	
	public static enum RunStatus{
		EMPTY			("Empty", 0),
		SUCCESS 		("Success", 1),
		INTERRUPTED 	("Interrupted", 2), 
		ERROR 			("Error", 3); 
		
		private String 	wName;
		private int 	wCode;
		
		private RunStatus(String pName, int pCode) {
			this.wName = pName;
			this.wCode = pCode;
		}

		public String getName() {
			return wName;
		}

		public int getCode() {
			return wCode;
		}
		
		public static RunStatus get(Integer pCode) {
			switch (pCode) {
			case 0:
				return RunStatus.EMPTY;
			case 1:
				return RunStatus.SUCCESS;
			case 2:
				return RunStatus.INTERRUPTED;
			case 3:
				return RunStatus.ERROR;
			default:
				return RunStatus.EMPTY;
			}
		}		
	}

	protected Logger			wLogger = Logger.getLogger(this.getClass());
	protected Connection 		wConnection;
	protected static DBSMessage	wMessageError = new DBSMessage(MESSAGE_TYPE.ERROR,"Erro: %s");
	
	protected DBSMessages<DBSMessage>		wMessages = new DBSMessages<DBSMessage>(DBSMessage.class);
	
	private List<IDBSTaskEventsListener>	wEventListeners = new ArrayList<IDBSTaskEventsListener>();
	
	private int						wId;
	private String					wName = "";
	private int 					wSteps = 1; //Configura com o mínimo de uma etapa
	private int						wSubSteps = 1;
	private String					wCurrentStepName = "";
	private int						wCurrentSubStep = 1;
	private int 					wCurrentStep;
	private TaskState 				wTaskState = TaskState.STOPPED;
	private RunStatus				wRunStatus = RunStatus.EMPTY;
	private RunStatus				wLastRunStatus = RunStatus.EMPTY;
	private Long					wTimeOut = 0L;
	private Long					wTimeStarted = 0L;
	private Long					wTimeEnded = 0L;
	private Date					wScheduleDate;
	private	int						wRetryOnErrorSeconds = 0;
	private int						wRetryOnErrorTimes = 0;
	private Timer					wTimer;// = new Timer();
	private RunByThread				wRunThread;
	private boolean					wMultiTask = true;
	private DataModelClass			wDataModel; //Armazenamento auxiliar de dados vinculados a esta tarefa
	private DataModelClass			wDataModelValueOriginal; //Armazenamento auxiliar de dados vinculados a esta tarefa
	private Double					wPercentageCompleted = 0D;
	private String 					wUserName;
	private String 					wUserPassword;
	private boolean					wReRun = true;
	private int						wRetryOnErrorCount = 0;
	private	boolean					wTransactionEnabled = true;
	private boolean					wRunningScheduled = false; 
	private Integer					wTaskUpdateMilliseconds = 2000;
	private Long					wTaskUpdateLastTime = 0L;


	public DBSTask(){
		pvFireEventInitializeTask();
	}
	
	public DBSTask(Connection pConnection) {
		wConnection = pConnection;
		pvFireEventInitializeTask();
	}
	
	public DBSTask(IDBSTaskEventsListener pEventListener) {
		addEventListener(pEventListener);
		pvFireEventInitializeTask();
	}

	public DBSTask(IDBSTaskEventsListener pEventListener, Connection pConnection) {
		wConnection = pConnection;
		addEventListener(pEventListener);
		pvFireEventInitializeTask();
	}
	
	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.<br/>
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos<br/>
	 * Lembre-se de remove-la utilizando removeEventListener quando a classe for destruida, para evitar que ela seja chamada quando já não deveria. 
	 * @param pEventListener Classe
	 */
	public final void addEventListener(IDBSTaskEventsListener pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
		}
	}

	public final void removeEventListener(IDBSTaskEventsListener pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
		}
	}
	/**
	 * Excluir todos os eventListeners vinculados a esta tarefa.
	 */
	public final void removeAllEventListeners() {
		wEventListeners.clear();
	}

	/**
	 * Retorna a thread do sistema que está vinculada esta tarefa
	 * @return
	 */
	public Thread getThread(){
		return wRunThread;
	}

	public Connection getConnection() {return wConnection;}
	public void setConnection(Connection pConnection) {wConnection = pConnection;}	

	/**
	 * Usuário que será utilizado para fazer a conexão com o banco de dados.<br/>
	 * Caso não seja informado, é assumido que não haverá conexão.
	 * @return
	 */
	public String getUserName() {return wUserName;}
	/**
	 * Usuário que será utilizado para fazer a conexão com o banco de dados.<br/>
	 * Caso não seja informado, é assumido que não haverá conexão.
	 * @return
	 */
	public void setUserName(String pUserName) {
		wUserName = pUserName;
		if (pUserName == null){
			wUserPassword = null;
		}
	}
	
	public String getUserPassword() {return wUserPassword;}
	public void setUserPassword(String pUserPassword) {wUserPassword = pUserPassword;}

	/**
	 * Retorna o tempo atual de execução, caso a tarefa esteja sendo executado 
	 * ou o tempo da última execução, caso já tenha sido finalizada.<br/>
	 * Retorna 0 caso a tarefa ainda não tenha sido executada.
	 * @return
	 */
	public Long getTimeElapsed() {
		if (getTaskState() == TaskState.RUNNING){
			wTimeEnded = System.currentTimeMillis();
		}
		return wTimeEnded - wTimeStarted; 
	}

	/**
	 * Tempo em millisegundos para interromper a tarefa, caso seja ultrapassado.<br/>
	 * 0 não há timeout.
	 * @return
	 */
	public Long getTimeOut() {
		return wTimeOut;
	}

	/**
	 * Tempo em millisegundos para interromper a tarefa, caso seja ultrapassado.<br/>
	 * 0 não há timeout.
	 * @param pTimeOut
	 */
	public void setTimeOut(Long pTimeOut) {
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
	 * Retorna a hora iniciada
	 * @return
	 */
	public Long getTimeStarted(){
		return wTimeStarted;
	}

	/**
	 * Identificador auxiliar
	 * @return
	 */
	public final int getId() {
		return wId;
	}

	/**
	 * Identificardos auxiliar
	 * @param wId
	 */
	public final void setId(int wId) {
		this.wId = wId;
	}

	/**
	 * Indica se será efetuado o controle de transação.<br/>.
	 * Como padrão, cada etapa é executada dentro de uma transação(begin,commit(com sucesso) e rollback(com erro).<br/>
	 * Será considerado erro no caso de <b>exception</b> e quando for for utilizado o <b>setOk(False)</b> no evento.
	 * @param pTransactionEnabled
	 */
	public final void setTransationEnabled(Boolean pTransactionEnabled){
		wTransactionEnabled = DBSBoolean.toBoolean(pTransactionEnabled);
	}

	/**
	 * Indica se será efetuado o controle de transação.<br/>.
	 * Como padrão, cada etapa é executada dentro de uma transação(begin,commit(com sucesso) e rollback(com erro).<br/>
	 * Será considerado erro no caso de <b>exception</b> e quando for for utilizado o <b>setOk(False)</b> no evento.
	 * @param pTransactionEnabled
	 */
	public final Boolean getTransationEnabled(){
		return wTransactionEnabled;
	}
	
	/**
	 * Retorna nome da tarefa conforme definição do usuário
	 * @return
	 */
	public final String getName() {return wName;}

	/**
	 * Configura o nome da tarefa
	 * @param pName
	 */
	public final void setName(String pName) {this.wName = pName;}

	/**
	 * Retorna nome da etapa conforme definição do usuário
	 * @return
	 */
	public final String getCurrentStepName() {return wCurrentStepName;}

	/**
	 * Configura o nome da etapa
	 * @param pName
	 * @throws DBSIOException 
	 */
	public final void setCurrentStepName(String pCurrentStepName) throws DBSIOException {
		if (wCurrentStepName != pCurrentStepName){
			wCurrentStepName = pCurrentStepName;
			pvFireEventTaskUpdated();
		}
	}

	/**
	 * Retorna se é multitarefa.
	 * Sendo multitarefa, é criado uma thread para execucão em paralelo, fazendo com que
	 * não seja necessário aguarda a finaliação da execução para retornar o controle para
	 * a rotina chamadora
	 * @return
	 */
	public final boolean isMultiTarefa() {return wMultiTask;}

	/**
	 * Configura se é multitarefa
	 * Sendo multitarefa, é criado uma thread para execucão em paralelo, fazendo com que
	 * não seja necessário aguarda a finaliação da execução para retornar o controle para
	 * a rotina chamadora
	 * @param wMultiTask
	 */
	public final void setMultiTask(boolean pMultiTask) {wMultiTask = pMultiTask;}

	/**
	 * Quantidade de segundos para efetuar nova tentativa em caso de erro.<br/>
	 * Valor <b>0</b> indica que <b>não</b> será efetuada nova tentativa.
	 * O padrão é <b>0</b>.<br/>
	 * É necessário que a tarefa seja multitarefa, para isso deve-se setar <b>setMultiTask(true)</b>.
	 * @return
	 */
	public final int getRetryOnErrorSeconds() {return wRetryOnErrorSeconds;}
	/**
	 * Quantidade de segundos para efetuar nova tentativa em caso de erro.<br/>
	 * Valor <b>0</b> indica que <b>não</b> será efetuada nova tentativa.<br/>
	 * O padrão é <b>0</b>.<br/>
	 * É necessário que a tarefa seja multitarefa, para isso deve-se setar <b>setMultiTask(true)</b>.
	 * @return
	 */
	public final void setRetryOnErrorSeconds(int pRetryOnErrorSeconds) {wRetryOnErrorSeconds = pRetryOnErrorSeconds;}

	/**
	 * Número máximo de tentativas em caso de erros sucessivos.<br/>
	 * Valor <b>0</b> indica que não há limite de tentativas.<br/>
	 * O padrão é 0.<br/>
	 * É necessário que a tarefa seja multitarefa, para isso deve-se setar <b>setMultiTask(true)</b>.
	 * @return
	 */
	public final int getRetryOnErrorTimes() {return wRetryOnErrorTimes;}

	/**
	 * Número máximo de tentativas em caso de erros sucessivos.<br/>
	 * Valor <b>0</b> indica que não há limite de tentativas.<br/>  
	 * O padrão é 0.<br/>
	 * É necessário que a tarefa seja multitarefa, para isso deve-se setar <b>setMultiTask(true)</b>.
	 * @return
	 */
	public final void setRetryOnErrorTimes(int pRetryOnErrorTimes) {
		wRetryOnErrorTimes = pRetryOnErrorTimes;
		//For tempo mínimo de 1 segundo, caso ainda não tenha sido configurado
		if (wRetryOnErrorSeconds == 0){
			wRetryOnErrorSeconds = 1;
		}
	}


	
	/**
	 * Retorna o datamodel vinculado a esta tarefa
	 * O datamodel é uma forma auxiliar(opcional) de armazenar dados vinculados a esta tarefa
	 * @return
	 */
	public final DataModelClass getDataModel() {
		return wDataModel;
	}

	/**
	 * Configura o datamodel vinculado a esta tarefa
	 * O datamodel é uma forma auxiliar(opcional) de armazenar dados vinculados a esta tarefa
	 * @param pDataModel
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public final void setDataModel(DataModelClass pDataModel) throws InstantiationException, IllegalAccessException {
		wDataModelValueOriginal = (DataModelClass) pDataModel.getClass().newInstance();
		DBSIO.copyDataModelFieldsValue(pDataModel, wDataModelValueOriginal);
		wDataModel = pDataModel;
	}

	/**
	 * Retorna o datamodel vinculado a esta tarefa inicialmente. 
	 * O datamodel é uma forma auxiliar(opcional) de armazenar dados vinculados a esta tarefa
	 * @return
	 */
	public final DataModelClass getDataModelValueOriginal() {
		return wDataModelValueOriginal;
	}

	
	/**
	 * Deverá retornar o status da última execução da tarefa
	 * @return
	 */
	public final RunStatus getLastRunStatus() {
		return wLastRunStatus;
	}

	public final void setLastRunStatus(RunStatus pLastRunStatus) {
		wLastRunStatus = pLastRunStatus;
	}

	public final void setLastRunStatus(Integer pLastRunStatus) {
		setLastRunStatus(RunStatus.get(pLastRunStatus));
	}


	/**
	 * Deverá retornar o status da execução atual da tarefa
	 * @return
	 */
	public final RunStatus getRunStatus() {
		return wRunStatus;
	}

	/**
	 * Retorna a situação da execução
	 * @return
	 */
	public final TaskState getTaskState() {
		return wTaskState;
	}

	/**
	 * Retorna a quantidadede de etapas que contém esta tarefa
	 * Esta informação é utilizada posteriormente para acompanhar a evolução.
	 * @return
	 */
	public final Integer getSteps() {
		return wSteps;
	}

	/**
	 * Configura a quantidade de etapas que conterá esta tarefa.
	 * Esta informação será utilizada posteriormente para acompanhar a evolução
	 * @return
	 */
	public final void setSteps(Integer pSteps) {
		if(pSteps>0){
			wSteps = pSteps;
			pvSetCurrentStep(1);
		}
	}
	
	/**
	 * Retorna a etapas que esta em execução no momento 
	 * @return
	 */
	public final Integer getCurrentStep() {
		return wCurrentStep;
	}
	/**
	 * Retorna a quantidadede de etapas que contém esta tarefa
	 * Esta informação é utilizada posteriormente para acompanhar a evolução
	 * @return
	 */
	public final Integer getSubSteps() {
		return wSubSteps;
	}

	/**
	 * Configura a quantidade de etapas que conterá esta tarefa.
	 * Esta informação será utilizada posteriormente para acompanhar a evolução
	 * @return
	 */
	public final void setSubSteps(int pSubSteps) {
		if(pSubSteps>0){
			wSubSteps = pSubSteps;
			pvSetCurrentSubStep(0); 
		}
	}

	/**
	 * Indica a finalização do subStep atual para passar para o próximo subSteps
	 * @throws DBSIOException 
	 */
	public final void endSubStep() throws DBSIOException{
		int xNextSubStep = wCurrentSubStep+1;
		if (xNextSubStep > wSubSteps){
			wLogger.error(getName() + ":Quantidade de SubsSteps total de " +  wSubSteps + " é inferior a " + xNextSubStep);
			error();
		}else{
			pvSetCurrentSubStep(xNextSubStep);
			//Calcula percentual atual e dispara evento que percentual mudou
			pvCalcPercentageCompleted();
		}
	}
	
	/**
	 * Retorna a etapas que esta em execução no momento 
	 * @return
	 */
	public final Integer getCurrentSubStep() {
		return wCurrentSubStep;
	}
	/**
	 * Retorna percentual total de conclusão das etapas. 
	 * @return
	 */
	public final Double getPercentageCompleted(){
		return wPercentageCompleted;
	}
	
	/**
	 * Inibe disparos do evento <b>taskUpdated</b> em tempo 
	 * inferior ao informado durante a execução das etapas.<br/>
	 * Isto evita disparos constantes quando a execução da etapa for muita rápida.<br/>
	 * O padrão são 2000 milisegundos(2 segundos).
	 * O valor <b>0<b/> desabilita a inibição.
	 * @return
	 */
	public Integer getTaskUpdateMilliseconds() {
		return wTaskUpdateMilliseconds;
	}

	/**
	 * Inibe disparos do evento <b>taskUpdated</b> em tempo 
	 * inferior ao informado durante a execução das etapas.<br/>
	 * Isto evita disparos constantes quando a execução da etapa for muita rápida.<br/>
	 * O padrão são 500 milisegundos(0,5 segundo).
	 * O valor <b>0<b/> desabilita a inibição.
	 * @return
	 */
	public void setTaskUpdateMilliseconds(Integer pTaskUpdateMilliseconds) {
		wTaskUpdateMilliseconds = pTaskUpdateMilliseconds;
	}


	/**
	 * Normalmente as etapas de uma tarefa são executadas uma única vez.<br/>
	 * Este indicador possiblita executar as etapas mais uma vez, desde o ínicio, logo após a finalização da última etapa.<br/>
	 * Este valor só deve ser alterado após a tarefa inicia durante a chamada de qualquer um dos eventos.<br/>
	 * Os eventos <b>beforeRun</b> e <b>afterRun</b> serão disparados sempre respectivamentes antes e depois de execução das etapas.<br/>
	 * @param pReRun
	 */
	public void setReRun(Boolean pReRun){
		wReRun = pReRun;
	}

	/**
	 * Normalmente as etapas de uma tarefa são executadas uma única vez.<br/>
	 * Este indicador possiblita executar as etapas mais uma vez, desde o ínicio, logo após a finalização da última etapa.<br/>
	 * Este valor só deve ser alterado durante o processamento da tarefa.<br/>
	 * O evento <b>afterRun</b> será disparado sempre após a finalização da última etapa e antes de executar mais uma vez as estapas.<br/>
	 * @param pReRun
	 */
	public Boolean getReRun(){
		return wReRun;
	}
	

	
	/**
	 * Retorna Data e Hora que será disparada a tarefa
	 * @return
	 */
	public final Date getScheduleDate() {return wScheduleDate;}

	/**
	 * Data e Hora que será disparada a tarefa
	 * @param pScheduleDate
	 * @throws DBSIOException 
	 */
	public final void setScheduleDate(Date pScheduleDate) throws DBSIOException {
		pvRetryReset();
		pvScheduleDate(pScheduleDate);
	}

	/**
	 * Indica se tarefa foi interrompida
	 * @return
	 */
	public final Boolean isInterrupted() {
		return (wRunStatus == RunStatus.INTERRUPTED &&
				wTaskState != TaskState.RUNNING);
	}

	/**
	 * Indicador se a tarefa está em execução.
	 * @return
	 */
	public final boolean isRunning(){
		return (wTaskState==TaskState.RUNNING);
	}
	
	/**
	 * Indicador se a tarefa está ativa.<br/>
	 * Tarefas em execução e agendadas são consderadas ativas.
	 * @return
	 */
	public final boolean isActive(){
		return (wTaskState!=TaskState.STOPPED);
	}
	
	/**
	 * Retorna se quem iniciou a tarefa foi um agendamento.<br/>
	 * @return true : Tarefa iniciada por agendamento
	 * false: Tarefa iniciada manualmente ou tarefa estiver parada.
	 */
	public final boolean isRunningScheduled(){
		return wRunningScheduled;
	}
	
	/**
	 * Executa a tarefa se não estiver em execução.<br/>
	 * Caso não sejá multifarefa, este metodo só terminará ao final da execução da tarefa. 
	 * @throws Exception 
	 */
	public final void run() throws DBSIOException{
		try {
			pvRetryReset();
			pvRunTask();
		} catch (Exception e) {
			wLogger.error(getName(),e);
			error();
			DBSIO.throwIOException(e.getMessage());
		}
	}

	/**
	 * Interrompe e finaliza a tarefa.
	 * Destativa o agendamento(se houver) e exclui os listeners.
	 * Para ativar novamente esta tarefa, será necessário criar uma nova instancia.
	 */
	public final void kill() {
		try{
			if (wRunThread != null){
				wRunThread.kill();
			}
			interrupt();
			pvDesativaAgendamento();
			pvSetTaskStateToNotRunnig();
			removeAllEventListeners();
		}catch(Exception e){
			wLogger.error(getName(), e);
		}
	}
	
	/**
	 * Troca o RunStatus para interrompido. 
	 * A interrupção não acaba com o thread da tarefa, 
	 * somente seta a variável local indicando que a tarefa foi interrompida.
	 * Cabe ao usuário testar se a tarefa foi interrompida dentro da etapa 
	 * que estiver em execução.
	 * @throws DBSIOException 
	 */	
	public final void interrupt() throws DBSIOException{
		pvInterrupt(RunStatus.INTERRUPTED);
	}

	/**
	 * Troca o RunStatus para interrompido com erro. 
	 * A interrupção não acaba com o thread da tarefa, 
	 * somente seta a variável local indicando que a tarefa foi interrompida por erro.
	 * Cabe ao usuário testar se a tarefa foi interrompida dentro da etapa 
	 * que estiver em execução.
	 * @throws DBSIOException 
	 */	
	public final void error() throws DBSIOException{
		pvInterrupt(RunStatus.ERROR);
	}

	/**
	 * Configura tarefa para valores antes da execução.
	 * @throws DBSIOException 
	 */	
	public synchronized final void reset() throws DBSIOException{
		//Reseta o estados da finalização da execução anterior
		pvSetRunStatus(RunStatus.EMPTY);
		setSteps(getSteps());
		wPercentageCompleted = 0D;
	}

	//Objeto somente para acordar a tarefa após o wait
	public class NotifyObject{}
	
	/**
	 * 
	 * Classe multi tarefa que será chamda quando a execução for iniciada
	 */
	private class RunByThread extends Thread{
		
		private Boolean wKeepAlive = true;
		
		//Objeto somente para acordar a tarefa após o wait
		private NotifyObject  wNO = new NotifyObject();

		/**
		 * Evita que tarefa fique em espera.<br/>
		 * Ao final do <b>run<b/> a tarefa, não será mais possível reativa-lá.<br/>
		 * Será necessário criar uma nova instancia.
		 */
		public void kill(){
			wKeepAlive = false;
			synchronized(wNO){
				wNO.notify();
			}
		}
		
		@Override
		public void start() {
			//Se tarefa for nova
			if (getState() == State.NEW){
				//Inicia pela primeira vez a tarefa
				super.start();
			}else{
				//Acorda a tarefa que estava em espera
				synchronized(wNO){
					wNO.notify();
				}
			}
		}
		
		@Override
		public void run() {
			//Executa as etapas novamente enquando a tarefa estiver ativa(on)
			while (wKeepAlive){
				//Executa as estapas
				try {
					pvRunTaskSteps();
					synchronized(wNO){
						//Suspende a tarefa até receber uma notificação for wOn.notify. Isto mantém ela válida, sem a necessida de criar uma nova 
						wNO.wait();
					}
				} catch (InterruptedException | DBSIOException e) {
					wLogger.error(getName(), e);
					try {
						error();
					} catch (DBSIOException e1) {
						wLogger.error(getName(), e);
					}
				}
			}
		}
	}

	//=====================================================================
	
	//=====================================================================
	/**
	 * Classe que será chamada quando o timer for disparado, para iniciar a execução
	 *
	 */
	private class RunByTimer extends TimerTask{
		@Override
		public void run() {
			try{
				wLogger.info(getName() + ":Inicio:Timer");
				wRunningScheduled = true;
				pvRunTask();
			} catch (Exception e) {
				wLogger.error(getName(),e);
			}
		}
		
	}

	@Override
	public void initializeTask(DBSTaskEvent pEvent){
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}
	
	@Override
	public void finalizeTask(DBSTaskEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}
	
	@Override
	public void beforeRun(DBSTaskEvent pEvent) throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	@Override
	public void afterRun(DBSTaskEvent pEvent) throws DBSIOException {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise		
	}

	@Override
	public void ended(DBSTaskEvent pEvent) throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	@Override
	public void interrupted(DBSTaskEvent pEvent) throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	@Override
	public void error(DBSTaskEvent pEvent) throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	@Override
	public void step(DBSTaskEvent pEvent) throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	@Override
	public void taskStateChanged(DBSTaskEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise		
	}

	@Override
	public void runStatusChanged(DBSTaskEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}


	@Override
	public void taskUpdated(DBSTaskEvent pEvent) {
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	/**
	 * Método que deverá ser implementado, caso queira utilizar a conexão local com o banco de dados(wConnection).<br/>
	 * É importante ter uma conexão factory declarada na classe que extend esta.
	 * ex:wConnection = wUsuario.getCnFactory().getConnection().<br/>
	 */
	protected void createConnection() throws DBSIOException{
		// Manter vazio. Quem extender esta classe fica responável de sobreescrever este métodos, caso precise
	}

	/**
	 * Método para fechar a wConnection local.<br/>
	 * ex: DBSIO.closeConnection(wConnection);
	 */
	protected void destroyConnection() throws DBSIOException{
		DBSIO.closeConnection(wConnection);
	}
	
	/**
	 * Método para setar a wConnection local com uma conexão válida
	 * @throws SQLException 
	 */
	public boolean openConnection() {
		try {
			//Cria nova conexão se a conexão local for nula ou se estiver fechada.
			if ((wConnection!=null && wConnection.isClosed()) ||
				wConnection==null){
				try {
					createConnection();
					return true;
				} catch (DBSIOException e) {
					wMessageError.setMessageText(e.getLocalizedMessage());
					addMessage(wMessageError);
				}
			}
		} catch (SQLException e) {
			wMessageError.setMessageTextParameters(e.getLocalizedMessage());
			addMessage(wMessageError);
		}
		return false;
	}
	
	/**
	 * Método para fechar a wConnection local
	 */
	public void closeConnection(){
		if (wConnection != null){
			try {
				if (!wConnection.isClosed()){
					try {
						destroyConnection();
					} catch (DBSIOException e) {
						wMessageError.setMessageText(e.getLocalizedMessage());
						addMessage(wMessageError);
					}
				}
			} catch (SQLException e) {
				wMessageError.setMessageTextParameters(e.getLocalizedMessage());
				addMessage(wMessageError);
			}
		}
	}
	
	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	public String getMessageText(){
//		return wMessages.getMessageText(); //Comentado em 16/01/14 - Ricardo. Aparentemente não fazia sentido
		return wMessages.getCurrentMessageText();
	}
	
	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	public String getMessageTooltip(){
		return wMessages.getCurrentMessageTooltip();
	}
	
	/**
	 * Retorna texto da mensagem que está na fila
	 * @return
	 */
	public DBSMessages<DBSMessage> getMessages(){
		return wMessages;
	}


	/**
	 * Retorna se há alguma mensagem na fila
	 * @return
	 */
	/**
	 * @return
	 */
	public Boolean getHasMessage(){
		if (wMessages.getCurrentMessageKey()!=null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Valida mensagem corrente. <br/>
	 * Se for Warning chama o método warningMessageValidated passando o opção que o usuário escolheu.<br/>
	 * Para implementar algum código após a confirmação, este método(warningMessageValidated) deverá ser sobreescrito. 
	 * @param pIsValidated
	 * @return
	 */
	public void setMessageValidated(Boolean pIsValidated){
		if (wMessages!=null){
			String xMessageKey = wMessages.getCurrentMessageKey();
			wMessages.setValidated(pIsValidated);
			messageValidated(xMessageKey, pIsValidated);
		}
	}
	
	/**
	 * Método após a validação de qualquer mensagem.
	 * @param pMessageKey
	 * @param pIsValidated
	 * @throws DBSIOException 
	 */
	protected void messageValidated(String pMessageKey, Boolean pIsValidated){}

	// Protected ------------------------------------------------------------------------
	/**
	 * Limpa fila de mensagens
	 */
	protected void clearMessages(){
		wMessages.clear();
	}
	
	/**
	 * Adiciona uma mensagem a fila
	 * @param pMessageKey Chave da mensagem para ser utilizada quando se quer saber se a mensagem foi ou não confirmada pelo usuário
	 * @param pMessageType Tipo de mensagem. Messagem do tipo warning requerem a confirmação do usuário
	 * @param pMessageText Texto da mensagem
	 */
	protected void addMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText){
		addMessage(pMessageKey, pMessageType, pMessageText, "");
	}

	/**
	 * Adiciona uma mensagem a fila
	 * @param pMessageKey Chave da mensagem para ser utilizada quando se quer saber se a mensagem foi ou não confirmada pelo usuário
	 * @param pMessageType Tipo de mensagem. Messagem do tipo warning requerem a confirmação do usuário
	 * @param pMessageText Texto da mensagem
	 */
	protected void addMessage(MESSAGE_TYPE pMessageType, String pMessageText){
		wMessages.add(pMessageType, pMessageText);
	}

	/**
	 * Adiciona uma mensagem a fila
	 * @param pMessage
	 */
	protected void addMessage(DBSMessage pMessage){
		addMessage(pMessage.getMessageText(), pMessage.getMessageType(), pMessage.getMessageText(), pMessage.getMessageTooltip());
	}

	
	/**
	 * Adiciona uma mensagem a fila
	 * @param pMessage
	 */
	protected void addMessage(String pMessageKey, MESSAGE_TYPE pMessageType, String pMessageText, String pMessageTooltip){
		//Configura o icone do dialog confome o tipo de mensagem
		wMessages.add(pMessageKey, pMessageType, pMessageText, pMessageTooltip);
	}
	/**
	 * Remove uma mensagem da fila
	 * @param pMessageKey
	 */
	protected void removeMessage(String pMessageKey){
		wMessages.remove(pMessageKey);
	}

	/**
	 * Retorna se mensagem foi validada
	 * @param pMessageKey
	 * @return
	 */
	protected boolean isMessageValidated(String pMessageKey){
		return wMessages.isValidated(pMessageKey);
	}
	
	protected boolean isMessageValidated(DBSMessage pMessage){
		return isMessageValidated(pMessage.getMessageText());
	}

	/**
	 * Inicia execução da tarefa
	 * @throws Exception
	 */
	private void pvRunTask() throws DBSIOException{
		if (!isRunning()){
			try{
				if (wMultiTask){
					//Cria multitarefa e inicia. A Thread chamará o pvRunMain
					if (wRunThread == null || !wRunThread.isAlive()){
						wRunThread = new RunByThread();
						wRunThread.setName(wName);
					}
					wRunThread.start();
				}else{
					//Executa sem multitarefa
					pvRunTaskSteps();
				}
			}catch(Exception e){
				wRunningScheduled = false;
				wLogger.error(getName(), e);
				error();
				throw new DBSIOException(e);
			}
		}else{
			wRunningScheduled = false;
			wLogger.warn(getName() + ":Tarefa já em execução. Nova solicitação de execução foi ignorada.");
		}
	}

	/**
	 * Loop para execução de todas as etapas da tarefas conforme 
	 * o número de estapada definidas em <b>steps</b>.  
	 * @throws DBSIOException 
	 */
	private synchronized void pvRunTaskSteps() throws DBSIOException{
		wTimeStarted = System.currentTimeMillis();
		wTimeEnded = wTimeStarted;
		while (wReRun){
			wReRun = false;
			//Verifica se está em execução antes de iniciar
			try{
				if (!isRunning()){
					pvSetTaskState(TaskState.RUNNING);
					//Reseta para a etapa inicial
					reset(); 
					//Dispara evento e verifica se pode iniciar
					if (pvFireEventBeforeRun()){
						//Loop até atingir a quantidade de tarefas informadas
						for (int xStep = 1; xStep < wSteps + 1; xStep++){
							//Para dar oportunidade de processar algum método que tenha sido chamado durante o processamento desta thread
							Thread.yield();
							
							pvSetCurrentStep(xStep); 
	
							pvCalcPercentageCompleted();
	
							/* Dispara evento de step e verifica se houve erro.
							 * A o statu de interrupção é priopritário.  
							 */
							if (!pvFireEventStep()
							  && getRunStatus() != RunStatus.INTERRUPTED){
								error();
								break;
							}
	
							//Finaliza se foi interrompido
							if (getRunStatus() == RunStatus.INTERRUPTED){
								break;
							}
						}
						//Configura
						if (getRunStatus() == RunStatus.EMPTY){
							pvSetRunStatus(RunStatus.SUCCESS);
						}
						setLastRunStatus(getRunStatus());
						wTimeEnded = System.currentTimeMillis();
						if (getRunStatus() != RunStatus.INTERRUPTED
						 && getRunStatus() != RunStatus.ERROR){
							pvFireEventAfterRun();
						}
					}else{
						pvFireEventInterrupted();
					}
				}else{
					wLogger.error(getName() + ":Tarefa já se contra em execução:" + wRunThread.getId());
				}
			}catch(Exception e){
				wLogger.error(getName(), e);
				error();
				setLastRunStatus(getRunStatus());
				throw new DBSIOException(e);
			}finally{
				pvSetTaskStateToNotRunnig();
				//Se foi configurado a quantidade de segundos para uma nova tentativa...
				if (wRetryOnErrorSeconds > 0 
				 && wRetryOnErrorTimes > 0){
					//Se a execução terminou com erro...
					if (getRunStatus() == RunStatus.ERROR){
						//Incrementa contador de erro
						wRetryOnErrorCount++; 
						//Desativa ReRun, caso tenha sido ativado pelo usuário, pois a prioridade é agendar a nova tentativa
						wReRun = false;
						if (wRetryOnErrorTimes != 0 
						 && wRetryOnErrorCount > wRetryOnErrorTimes){
							wLogger.warn(getName() + ":Erro permaneceu após " + (wRetryOnErrorTimes + 1) + " tentativas de execução.");
							//Zera qualquer agendamente anterior
							setScheduleDate(null);
						}else{
							if (wMultiTask){
								pvRetrySchedule();
							}else{
								wReRun = true;
							}
						}
					}else{
						//Zera contador de erro
						wRetryOnErrorCount = 0; 
					}
				}
				//dispara evento 'ended' se não houver uma nova tentativa
				if (getScheduleDate() == null
				 && wReRun == false){
					pvFireEventTaskUpdated();
					pvFireEventEnded();
				}
			}
		}
		//Reseta para os valores inicias
		wRunningScheduled = false;
		wReRun = true;
	}

	/**
	 * Zera controle de tentativas por erro
	 */	
	private final void pvRetryReset(){
		wRetryOnErrorCount = 0;
	}

	/**
	 * Agenda a tarefa no timer caso a tarefa e busca pelo próximo agendamento estajam habilitados
	 * @throws DBSIOException 
	 */
	private void pvAtivaAgendamento() throws DBSIOException{
		//Cancela último timer se houver;
		pvKillTimer();
		//Cria novo agendamento
		if (wScheduleDate != null){
			Timestamp xNow = DBSDate.getNowTimestamp();
			if (!wScheduleDate.before(xNow)){
				//Cria timer
				wTimer = new Timer("Timer - " + getName());
				wTimer.schedule(new RunByTimer(), wScheduleDate);
				//COnfigura como agendado.
				pvSetTaskState(TaskState.SCHEDULED);
				wLogger.info(getName() + " agendada para: " + DBSFormat.getFormattedDateTime(wScheduleDate));
			}else{
				wLogger.error(getName() + ":Data/Hora[" + DBSFormat.getFormattedDateTime(wScheduleDate) + "]" +
											" menor que a data/hora[" + DBSFormat.getFormattedDateTime(xNow) + "] atual.");
			}
		}
	}

	/**
	 * Desativa o agendamento e cancela o timer se estiver ativo
	 */
	private void pvDesativaAgendamento(){
		//Cancela último timer se houver;
		wScheduleDate = null;
		pvKillTimer();
	}

	/**
	 * Cancela o timer se estiver ativo
	 */
	private void pvKillTimer(){
		//Cancela último timer se houver;
		if (wTimer != null) {
			wTimer.cancel();
			wTimer.purge();
		}
	}

	/**
	 * Calcula percentual total de conclusão das etapas.
	 * @throws DBSIOException 
	 */
	private void pvCalcPercentageCompleted() throws DBSIOException{
		if (wSteps != 0){
			Double xCurrentStep = (double) (wCurrentStep -1);
			Double xCurrentSubStep = (double) wCurrentSubStep;
			//Se estiver iniciado o processamento
			if (getTaskState() == TaskState.RUNNING){
				//Se for inicio
				if (xCurrentStep==0 && 
					xCurrentSubStep==0){
					wPercentageCompleted = 0.001; //Envia valor mínimo somente para indicar que foi dado inicio ao processamento
				}else{
					//Calcula percentual com relação a etapa principal
					double xP = DBSNumber.divide(xCurrentStep, wSteps).doubleValue();
					//Calcula percentual com relação a subetapa(proporcional a quantidade de etapas) e adiciona a etapa principal
					xP += ((1 / (double) wSteps) * xCurrentSubStep / wSubSteps);
					wPercentageCompleted =  xP * 100;
				}
			//Se processamento estiver parado
			}else{
				//Se finalizado com sucesso
				if (wCurrentStep == wSteps &&
					getRunStatus() == RunStatus.SUCCESS){
					wPercentageCompleted = 100D;
				//Se for inicio ou não foi finalizado com sucesso
				}else{
					wPercentageCompleted =  0D;
				}
			}
		//Se quantida de estapas for 0.
		}else{
			wPercentageCompleted = 100D;
		}
		wPercentageCompleted = DBSNumber.round(wPercentageCompleted, 3);
		pvFireEventTaskUpdated();
	}

	/**
	 * Seta o número da etapa atual
	 * @param pCurrentStep
	 */
	private final void pvSetCurrentStep(Integer pCurrentStep) {
		wCurrentStep = pCurrentStep;
		setSubSteps(1);//Reseta a quantidade de substeps sempre que o step for trocado. O substep deve ser definido dinamicamente dentro do evento step
	}

	/**
	 * Seta o número da subetapa.
	 * @param pCurrentSubStep
	 */
	private final void pvSetCurrentSubStep(int pCurrentSubStep) {
		wCurrentSubStep = pCurrentSubStep;
	}

	/**
	 * Configura a situação da execução (Método PRIVADO) e 
	 * calcula percentual de execução atual.
	 * @param pRunState
	 * @throws DBSIOException 
	 */
	private final void pvSetTaskState(TaskState pRunState) throws DBSIOException {
		if (wTaskState != pRunState){
			wTaskState = pRunState;
			pvFireEventTaskStateChanged();
			//Calcula a finalização da terafa
			pvCalcPercentageCompleted();
	
		}
	}

	/**
	 * Configura o status da execução atual 
	 * @param pRunStatus
	 * @throws DBSIOException 
	 */
	private final void pvSetRunStatus(RunStatus pRunStatus) throws DBSIOException {
		if (wRunStatus != pRunStatus){
			wRunStatus = pRunStatus;
			pvFireEventRunStatusChanged();
			pvTaskUpdateLastTimeReset();
			pvFireEventTaskUpdated();
		}
	}

	//============ PRIVATES ==========================================================================
	/**
	 * Interrompe execução.
	 * @param pInterrupt
	 * @throws DBSIOException 
	 */
	private final void pvInterrupt(RunStatus pRunStatus) throws DBSIOException{
		if (isRunning()){
			pvSetRunStatus(pRunStatus);
			//Eventos ----------------------------
			if (pRunStatus == RunStatus.ERROR){
				pvFireEventError();
			}
			pvFireEventInterrupted();
		}
	}

	@PreDestroy
	private void pvFinalize() {
		pvFireEventFinalizeTask();
		kill();
		try {
			super.finalize();
		} catch (Throwable ignore) {}
	}

	/**
	 * Programa um novo agendamento a partir da quantidade de segundos definida em getSecondsToRetry, 
	 * caso a seja uma tarefa agendada e houve erro.
	 * @throws DBSIOException 
	 */
	private void pvRetrySchedule() throws DBSIOException{
		if (wRetryOnErrorSeconds > 0
		 && wRetryOnErrorTimes > 0){
			Date xData = DBSDate.getNowDate(true);
			xData = DBSDate.getDateAddSeconds(xData, wRetryOnErrorSeconds);
			wLogger.warn(getName() + ":Tentativa " + wRetryOnErrorCount + " de " + wRetryOnErrorTimes + " será executada em: " + DBSFormat.getFormattedDateCustom(xData, "dd/MM/yyyy HH:mm:ss"));
			pvScheduleDate(xData);
		}
	}
	
	/**
	 * Reseta o hora da última chamada para não inibir 
	 * a próxima chamada do evento taskUpdate, se e quando houver.
	 */
	private void pvTaskUpdateLastTimeReset(){
		wTaskUpdateLastTime = 0L;
	}

	/**
	 * Ativa agendamento
	 * @param pScheduleDate
	 * @throws DBSIOException 
	 */
	private final void pvScheduleDate(Date pScheduleDate) throws DBSIOException {
		wScheduleDate = pScheduleDate;
		pvAtivaAgendamento();
	}

	/**
	 * Seta a situação do Status considerando as tarefas agendadas estarão como "aguardando". Tarefas sem agendamento estão "Paradas".
	 * @return
	 * @throws DBSIOException 
	 */
	private void pvSetTaskStateToNotRunnig() throws DBSIOException{
		if (wScheduleDate != null
		 && wScheduleDate.after(DBSDate.getNowTimestamp())){
			pvSetTaskState(TaskState.SCHEDULED);
		}else{
			pvSetTaskState(TaskState.STOPPED);
		}
	}

	//=== EVENTOS =====================================================================================
	/**
	 * Evento disparado quando é iniciada a execução
	 * @throws DBSIOException 
	 */
	private boolean pvFireEventInitializeTask() {
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		initializeTask(xE);
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).initializeTask(xE);
	    }
		return xE.isOk();
	}

	private void pvFireEventFinalizeTask(){
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		finalizeTask(xE);
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).finalizeTask(xE);
	    }
	}

	private boolean pvFireEventBeforeRun() throws DBSIOException{
		DBSTaskEvent xE = new DBSTaskEvent(this);
		try{
			openConnection();
			
			//Chame o metodo(evento) local para quando esta classe for extendida
			beforeRun(xE);
			if (xE.isOk()){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).beforeRun(xE);
					//Sai em caso de erro
					if (!xE.isOk()){break;}
		        }
			}
			return xE.isOk();
		}catch(Exception e){
			wLogger.error(getName() + ":BeforeRun:", e);
			error();
			throw e;
		}finally{
			closeConnection();
		}
	}

	/**
	 * Dispara evento informando a execução terminou.<br/>
	 * Situação do termino pode ser verificada com getLastRunStatus().
	 * @throws DBSIOException 
	 */
	private void pvFireEventAfterRun() throws DBSIOException{
		DBSTaskEvent xE = new DBSTaskEvent(this);
		try{
			openConnection();
			//Chame o metodo(evento) local para quando esta classe for extendida
			afterRun(xE);
			//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
			for (int xX=0; xX<wEventListeners.size(); xX++){
				wEventListeners.get(xX).afterRun(xE); 
	        }
		}catch(Exception e){
			wLogger.error(getName() + ":AfterRun", e);
			error();
			throw e;
		}finally{
			closeConnection();
		}
	}

	/**
	 * Dispara evento a cada nova etapa da tarefa
	 * @return
	 * @throws DBSIOException 
	 */
	private boolean pvFireEventStep() throws DBSIOException{
		DBSTaskEvent xE = new DBSTaskEvent(this);
		Boolean xOk = true;
		try{
			openConnection();
			//Salva valor do transationEnabled para evitar erro caso o atributo seja alterado antes de finalizada a execução da etapa
			Boolean xTransationEnabled = getTransationEnabled();
			if (xTransationEnabled){
				DBSIO.beginTrans(wConnection);
			}
			//Chame o metodo(evento) local para quando esta classe for extendida
			step(xE);
			xOk = xE.isOk() && getRunStatus() == RunStatus.EMPTY;
			if (xOk){
				//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
				for (int xX=0; xX<wEventListeners.size(); xX++){
					wEventListeners.get(xX).step(xE);
					xOk = (xE.isOk() && getRunStatus() == RunStatus.EMPTY);
					if (!xOk){break;} //Em caso de solicitação de interrupção(interrupt() ou error()), sai do loop.
		        }
			}
			//Para dar oportunidade de processar algum método que tenha sido chamado durante o processamento desta thread
			Thread.yield();
			if (xTransationEnabled){
				DBSIO.endTrans(wConnection, xOk);
			}
			return xE.isOk();
		}catch(Exception e){
			wLogger.error(getName() + ":Error Step:" + getCurrentStep() + ":" + getCurrentStepName(), e);
			DBSIO.endTrans(wConnection, false);
			error();
			throw e;
		}finally{
			closeConnection();
		}
	}

	/**
	 *Dispara evento informando que a execução mudou de situação(Executando, Parado ou Agendada)
	 * @throws DBSIOException 
	 */
	private void pvFireEventTaskStateChanged() throws DBSIOException{
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		taskStateChanged(xE);
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).taskStateChanged(xE);
	    }		
	}

	/**
	 * Dispara evento informando que o atributo 'status' foi alterado.
	 * Local onde deverá se implementada a conversão do valor deste atributo para 
	 * o valor correspondente no datamodel, se for o caso.(Exemplo enum)
	 * @throws DBSIOException 
	 */
	private void pvFireEventRunStatusChanged() throws DBSIOException{
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		runStatusChanged(xE);
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).runStatusChanged(xE);
	    }
	}

	/**
	 * Dispara evento quando tarefa for finalizada. 
	 * @throws DBSIOException 
	 */
	private void pvFireEventEnded() throws DBSIOException {
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		ended(xE);
		wLogger.info(getName() + ":Ended:Step:" + getCurrentStep() + ":" + getCurrentStepName());
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).ended(xE);
	    }
	}
	
	/**
	 * Dispara evento informando que foi interrompida execução 
	 * @throws DBSIOException 
	 */
	private void pvFireEventInterrupted() throws DBSIOException {
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		interrupted(xE);
		wLogger.warn(getName() + ":Interrupt:Step:" + getCurrentStep() + ":" + getCurrentStepName());
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).interrupted(xE);
	    }
	}

	/**
	 * Dispara evento informando que foi interrompida execução 
	 * @throws DBSIOException 
	 */
	private void pvFireEventError() throws DBSIOException {
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		error(xE);
		wLogger.error(getName() + ":Interrupt with error:Step:" + getCurrentStep() + ":" + getCurrentStepName());
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).error(xE);
	    }
	}

	/**
	 * Diapara evendo quando houver alguma evolução no percentual ou alguma modificação no nome da etapa.
	 * @throws DBSIOException 
	 */
	private void pvFireEventTaskUpdated() throws DBSIOException{
		Long xCurrentTime = Calendar.getInstance().getTimeInMillis();
		if (wTaskUpdateMilliseconds != 0){
			if (xCurrentTime - wTaskUpdateLastTime > wTaskUpdateMilliseconds){
				wTaskUpdateLastTime = xCurrentTime;
			}else{
				return;
			}
		}
		DBSTaskEvent xE = new DBSTaskEvent(this);
		//Chame o metodo(evento) local para quando esta classe for extendida
		taskUpdated(xE);
		//Chama a metodo(evento) dentro das classe foram adicionadas na lista que possuem a implementação da respectiva interface
		for (int xX=0; xX<wEventListeners.size(); xX++){
			wEventListeners.get(xX).taskUpdated(xE);
	    }
	}

}
