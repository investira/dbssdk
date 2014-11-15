package br.com.dbsoft.task;

import br.com.dbsoft.error.DBSIOException;

public interface IDBSTaskEventsListener {
	/**
	 * Evento disparado quando a classe é criada.
	 * Neste evento, deve-se definir a quantidade de etapas da tarefa por setSteps.
	 * Caso não seja, informado, será considerado somente um única etapa.
	 */
	public void initializeTask(DBSTaskEvent pEvent);

	/**
	 * Evento disparado antes da classe ser destruida<br/>.
	 * Fecha a conexão se houver e estiver aberta.
	 */
	public void finalizeTask(DBSTaskEvent pEvent);

	/**
	 * Evento disparado antes de iniciar execução.<br/>
	 * Podendo-se inibir a execução informando setOk(false) no evento. 
	 * Neste caso o evento <b>interrupted</b> será disparado em seguida.<br/>
	 * Conexão com o banco encontra-se aberta.
	 */
	public void beforeRun(DBSTaskEvent pEvent) throws DBSIOException;

	/**
	 * Evento disparado após finalizada a execução.<br/>
	 * Evento <b>não</b> é disparado em caso de interrupção do usuário ou erro. 
	 * Utilize os eventos <b>interrupted</b> e/ou <b>error</b> nestes casos.<br/>
	 * Para identificar qual o status da execução deve-se perquisar <b>getObject().getRunStatus()</b>.<br/>
	 * Conexão com o banco encontra-se aberta.
	 */
	public void afterRun(DBSTaskEvent pEvent) throws DBSIOException;

	/**
	 * Evento disparado quando a terafa é interrompida pelo usuário ou por erro.<br/>
	 * No caso de erro o evento <b>error<b/> é disparado antes.<br/>
	 * Para identificar qual o status da interrupção, deve-se perquisar <b>getObject().getRunStatus()</b>.
	 */
	public void interrupted(DBSTaskEvent pEvent) throws DBSIOException;

	/**
	 * Evento disparado quando a terafa é interrompida por erro.<br/>
	 * Evento <b>interrupted</b> também é disparado em seguida.<br/>
	 * Para identificar qual o status da interrupção, deve-se perquisar <b>getObject().getRunStatus()</b>.
	 */
	public void error(DBSTaskEvent pEvent) throws DBSIOException;

	/**
	 * Evento disparado a cada etapa.<br/>
	 * Neste evento, deve-se informar a quantidadede de subetapas contém 
	 * a etapa em execução informada por <b>getCurrentStep()</b>, utilizando <b>setSubSteps</b> 
	 * e chamar <b>endSubStep()</b> ao final de cada subetapa.<br/>
	 * As subestapas deverão ser implementadas/chamadas dentro deste evento(step).<br/>
	 * Caso não existam subetapas, será considerado uma única subetapa.<br/>
	 * Deve-se chamar o método <b>setStepName</b> para definir o nome da etapa para posterior controle.<br/>
	 * A etapa é efetuada dentro de uma transação com commit e rollback automático, 
	 * a depender do status isOk() do evento.<br/>
	 * Para desativar o controle automático de transação, configure o atributo <b>setTransactionEnabled(false)</b>.<br/> 
	 * Conexão com o banco encontra-se aberta.<br/>
	 */
	public void step(DBSTaskEvent pEvent) throws DBSIOException;


	/**
	 * Evento disparado quando houver alguma evolução no percentual, modificação no nome da etapa ou status da tarefa <b>RunStatus</b>.<br/>
	 * Evento normalmente utilizado para informar ao usuário a situação da tarefa.<br/>
	 * Se precisar utilizar a conexão e ela já estiver aberta automaticamente pela terafa, <b>NÃO</b> feche.
	 * Se ela não estiver aberta, <b>abra e fecha</b> após a utilização.
	 */
	public void taskUpdated(DBSTaskEvent pEvent) throws DBSIOException;

	/**
	 * Evento disparado quando a execução mudou de situação(Executando, Parado ou Agendada).<br/>
	 * Se precisar utilizar a conexão e ela já estiver aberta automaticamente pela terafa, <b>NÃO</b> feche.
	 * Se ela não estiver aberta, <b>abra e fecha</b> após a utilização.<br/>
	 * Local onde poderá ser implementado a atualização deste dado no banco de dados, se for o caso.
	 */
	public void taskStateChanged(DBSTaskEvent pEvent) throws DBSIOException;

//	/**
//	 * Evento disparado quando o atributo 'Agendada' foi alterado.<br/>
//	 * Se precisar utilizar a conexão e ela já estiver aberta automaticamente pela terafa, <b>NÃO</b> feche.
//	 * Se ela não estiver aberta, <b>abra e fecha</b> após a utilização.<br/>
//	 * Local onde poderá ser implementado a atualização deste dado no banco de dados, se for o caso.
//	 */
//	public void scheduleEnabledChanged(DBSTaskEvent pEvent);

	/**
	 * Evento disparado quando o atributo 'status' foi alterado.<br/>
	 * Se precisar utilizar a conexão e ela já estiver aberta automaticamente pela terafa, <b>NÃO</b> feche.
	 * Se ela não estiver aberta, <b>abra e fecha</b> após a utilização.<br/>
	 * Local onde poderá ser implementado a atualização deste dado no banco de dados, se for o caso.
	 */
	public void runStatusChanged(DBSTaskEvent pEvent) throws DBSIOException;



}
