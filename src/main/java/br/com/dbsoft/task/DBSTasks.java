package br.com.dbsoft.task;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbsoft.error.DBSIOException;

/**
 * Armazenar e controlar uma lista de tarefas
 * @param <TaskClass> Tipo de tarefa
 */
public class DBSTasks<TaskClass extends DBSTask<?>> {

	protected static Logger				wLogger = Logger.getLogger(DBSTasks.class);

	protected LinkedHashMap<String, TaskClass> 		wTasks = new LinkedHashMap<String, TaskClass>();
	private ArrayList<TaskClass> 					wTasksList = new ArrayList<TaskClass>();
	private List<IDBSTaskEventsListener>			wEventListeners = new ArrayList<IDBSTaskEventsListener>();
	private Integer									wTasksEndedCount = 0;
	private Comparator<TaskClass>					wComparator = null;

	public Comparator<TaskClass> getComparator() {
		return wComparator;
	}

	public void setComparator(Comparator<TaskClass> pComparator) {
		wComparator = pComparator;
	}

	/**
	 * Local para configura a lista inicial das tarefas
	 * Chamado sempre após a classe ser criada
	 */
	public void loadTasks(){};
	
	/**
	 * Classe que receberá as chamadas dos eventos quando ocorrerem.
	 * Para isso, classe deverá implementar a interface DBSTarefa.TarefaEventos
	 * @param pEventListener 
	 */	
	public void addEventListener(IDBSTaskEventsListener pEventListener) {
		if (!wEventListeners.contains(pEventListener)){
			wEventListeners.add(pEventListener);
			pvAddEventListenersToTasks();
		}
	}

	/**
	 * Remove listener 
	 * @param pEventListener
	 */
	public void removeEventListener(IDBSTaskEventsListener pEventListener) {
		if (wEventListeners.contains(pEventListener)){
			wEventListeners.remove(pEventListener);
			pvRemoveEventListenersFromTasks();
		}
	}
	
//	/**
//	 * Ativa os agendamentos de forma que possam ser executados na data definida na data do agendamento
//	 */
//	public final void enableNextSchedules(){
//		pvSetGetNextScheduleEnabled(true);	
//	}
	
//	/**
//	 * Desativa os agendamentos sem alterar a data do agendamento e a habilitação(Agendamento Habilitado)
//	 */
//	public final void disableNextSchedules(){
//		pvSetGetNextScheduleEnabled(false);
//	}

	/**
	 * Adiciona uma nova tarefa a lista
	 * @param pId Chave que indentificará a tarefa
	 * @param pTask
	 */
	public final void addTask(String pId, TaskClass pTask){
		//Verifica se eventos já existe na lista e adiciona se não existir
		if (!wTasks.containsKey(pId)){
			pvAddEventListenersToTask(pTask);
//			pTask.setAllSet(true);
			wTasks.put(pId, pTask);
			pvUpdateList();
		}
	}
	
	/**
	 * Remove a tarefa da lista
	 * @param pId Chave que indentifica a tarefa
	 */
	public final void removeTask(String pId){
		if (wTasks.containsKey(pId)){
			//Termina execução se estiver sendo executada
			this.interruptTask(pId);
			//Remove os eventListeners
			pvRemoveEventListenersFromTask(wTasks.get(pId));
			//Remove da lista
			wTasks.remove(pId);
			pvUpdateList();
		}
	}

	/**
	 * Retorna tarefa a partir do id
	 * @param pId Chave que indentifica a tarefa
	 * @return
	 */
	public final TaskClass getTask(String pId){
		if (wTasks.containsKey(pId)){
			return wTasks.get(pId);
		}else{
			return null;
		}
	}

	/**
	 * Retorna lista ordenada de tarefas
	 * A lista de tarefa ordenada é criada no momento do addTask()
	 * @return
	 */
	public final List<TaskClass> getTasksList(){
		return wTasksList;
	}

	/**
	 * Executa a tarefa indicada pelo pId
	 * @param pId Chave que indentifica a tarefa
	 */
	public synchronized final void runTask(String pId){
		try{
			if (wTasks.containsKey(pId)){
				wTasks.get(pId).run();
			}else{
				wLogger.error("Tarefa #" + pId + " inexistente");
			}
//			wTasksEndedCount++;
		}catch(Exception e){
			wLogger.error(e);
		}
	}

	/**
	 * Interrompe e finaliza a tarefa.
	 * Destativa o agendamento(se houver) e exclui os listeners.
	 * Para ativar novamente esta tarefa, será necessário criar uma nova instancia.
	 * @param pId Chave que indentifica a tarefa
	 */
	public synchronized final void killTask(String pId){
		try{
			if (wTasks.containsKey(pId)){
				wTasks.get(pId).kill();
			}else{
				wLogger.error("Tarefa #" + pId + " inexistente");
			}
		}catch(Exception e){
			wLogger.error(e);
		}
	}

	/**
	 * Finaliza a tarefa indicada pelo pId
	 * @param pId Chave que indentifica a tarefa
	 */
	public synchronized final void interruptTask(String pId){
		try{
			if (wTasks.containsKey(pId)){
				wTasks.get(pId).interrupt();
			}else{
				wLogger.error("Tarefa #" + pId + " inexistente");
			}
		}catch(Exception e){
			wLogger.error(e);
		}
	}
	
//	/**
//	 * Finaliza a tarefa indicada pelo pId
//	 * @param pId Chave que indentifica a tarefa
//	 */
//	public synchronized final void destroydTask(String pId){
//		try{
//			if (wTasks.containsKey(pId)){
//				wTasks.get(pId).destroy();
//			}else{
//				wLogger.error("Tarefa #" + pId + " inexistente");
//			}
//		}catch(Exception e){
//			wLogger.error(e);
//		}
//	}
	
	/**
	 * Executar todas as tarefas
	 * @throws Exception 
	 */
	public synchronized final void runTasks() throws Exception{
		loadTasks();
		for (TaskClass xTarefa : wTasks.values()) {
			xTarefa.run();
//			wTasksEndedCount++;
		}
	}

	/**
	 * Interrompe e finaliza todas tarefas.
	 * Destativa o agendamento(se houver) e exclui os listeners.
	 * Para ativar novamente esta tarefa, será necessário criar uma nova instancia.
	 */
	public synchronized final void killTasks(){
		for (TaskClass xTarefa : wTasks.values()) {
			xTarefa.kill();
		}
	}
	
	/**
	 * Interrompter todas as tarefas, não executando a próxima etapa(se houve).
	 * A interrupção não acaba com o thread da tarefa, 
	 * somente seta a variável local indicando que a tarefa foi interrompida.
	 * Cabe ao usuário testar se a tarefa foi interrompida dentro da etapa 
	 * que estiver em execução.
	 * @throws DBSIOException 
	 */
	public synchronized final void interruptTasks() throws DBSIOException{
		for (TaskClass xTarefa : wTasks.values()) {
			xTarefa.interrupt();
		}
	}
	
//	/**
//	 * Destroi todas as tarefas
//	 */
//	public synchronized final void destroyTasks(){
//		for (TaskClass xTarefa : wTasks.values()) {
//			xTarefa.destroy();
//		}
//	}

	/**
	 * Indica se existe alguma tarefa em execução
	 * @return
	 */
	public final boolean isTasksRunning(){
		for (TaskClass xTarefa : wTasks.values()) {
			if (xTarefa.isRunning()){
				return true;
			}
		}
		return false;
	}

//	/**
//	 * Indica se tem alguma tarefa agendada no timer
//	 * @return
//	 */
//	public final boolean isTasksScheduled(){
//		for (TaskClass xTarefa : wTasks.values()) {
//			if (xTarefa.isScheduled()){
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * Indica se tem alguma tarefa agendada no timer
	 * @return
	 */
	public final boolean isTasksActive(){
		for (TaskClass xTarefa : wTasks.values()) {
			if (xTarefa.isActive()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna true se todos as tarefas tiverem sido interrompidas
	 * @return
	 */
	public final boolean isTasksInterrupted(){
		for (TaskClass xTarefa : wTasks.values()) {
			if (!xTarefa.isInterrupted()){
				return false;
			}
		}
		if (isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Indica se tem algum agendamento ativo
	 * @return
	 */
	public final boolean isEmpty(){
		return wTasks.size() == 0;
	}
	
//	/**
//	 * @param pId Chave que indentifica a tarefa
//	 * @param pEnabled
//	 */
//	public final void scheduleEnabled(String pId, boolean pEnabled){
//		try{
//			wTasks.get(pId).setScheduleEnabled(pEnabled);
//		}catch(Exception e){
//			wLogger.error(e);
//		}
//	}


	/**
	 * Retorna a quantidade de tarefas em execução.
	 * @return
	 */
	public int getTasksActiveCount() {
		int xCount = 0;
		for (TaskClass xTarefa : wTasks.values()) {
			if (xTarefa.isActive()) {
				xCount++;
			}
		}
		return xCount;
	}
	
	
	/**
	 * Retorna a quantidade de tarefas em execução.
	 * @return
	 */
	public int getTasksRunningCount() {
		int xCount = 0;
		for (TaskClass xTarefa : wTasks.values()) {
			if (xTarefa.isRunning()) {
				xCount++;
			}
		}
		return xCount;
	}

	/**
	 * Retorna a quantidade de tarefas executadas.
	 * @return
	 */
	public Integer getTasksEndedCount() {
		return wTasksEndedCount;
	}

	
	//private -----------------------------------------------------------------------
	
	/**
	 * Cria lista ordenada 
	 */
	private final void pvUpdateList(){
		if (wTasks != null){
			if (wComparator != null) {
				wTasksList = new ArrayList<TaskClass>(wTasks.values());
				Collections.sort(wTasksList, wComparator);
			} else {
				wTasksList = new ArrayList<TaskClass>(wTasks.values());
			}
		}else{
			wTasksList = new ArrayList<TaskClass>();
		}
	}

//	/** 
//	 * Indica se ativará novos agendamentos. Os agendamento que estivem sidos efetuados no timer, não serão efetados
//	 * @return
//	 */
//	private final void pvSetGetNextScheduleEnabled(boolean pNextSchedulesEnabled){
//		for (TaskClass xTarefa : wTasks.values()) {
//			xTarefa.setNextSchedulesEnabled(pNextSchedulesEnabled);
//		}
//	}
//	
	
	/**
	 * Adiciona a tarefa(ptask) todos os listeners já existentes, para que eles também recebem os eventos desta nova tarefa
	 */
	private final void pvAddEventListenersToTask(TaskClass pTask){
		for (IDBSTaskEventsListener xEvent: wEventListeners){
			pTask.addEventListener(xEvent);
		}
	}
	
	/**
	 * Adiciona a tarefa(ptask) todos os listeners já existentes, para que eles também recebem os eventos desta nova tarefa
	 */
	private final void pvAddEventListenersToTasks(){
		for (TaskClass xTarefa : wTasks.values()) {
			pvAddEventListenersToTask(xTarefa);
		}
	}
	
	/**
	 * Adiciona a tarefa(ptask) todos os listeners já existentes, para que eles também recebem os eventos desta nova tarefa
	 */
	private final void pvRemoveEventListenersFromTask(TaskClass pTask){
		for (IDBSTaskEventsListener xEvent: wEventListeners){
			pTask.removeEventListener(xEvent);
		}
	}
	
	/**
	 * Adiciona a tarefa(ptask) todos os listeners já existentes, para que eles também recebem os eventos desta nova tarefa
	 */
	private final void pvRemoveEventListenersFromTasks(){
		for (TaskClass xTarefa : wTasks.values()) {
			pvRemoveEventListenersFromTask(xTarefa);
		}
	}

}
