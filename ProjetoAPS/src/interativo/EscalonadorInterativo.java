package interativo;

import framework.StatusProcesso;
import java.util.ArrayList;

import exceptions.ProcessoInvalidoException;

public class EscalonadorInterativo {

	protected ArrayList<ProcessoInterativo> fila;
	protected ArrayList<ProcessoInterativo> filaIO;
	protected ProcessoInterativo processoNaCPU;
	protected int tempoRodadoProcessoAtual;
	protected int tickAtual;

	public EscalonadorInterativo() {
		fila = new ArrayList<ProcessoInterativo>();
		filaIO = new ArrayList<>();
		processoNaCPU = null;
		tempoRodadoProcessoAtual = 0;
		tickAtual = 0;
	}
	
	public ProcessoInterativo getProcessoNaCPU() {
		return processoNaCPU;
	}

	public void setProcessoNaCPU(ProcessoInterativo processoNaCPU) {
		this.processoNaCPU = processoNaCPU;
	}

	public int getTempoRodadoProcessoAtual() {
		return tempoRodadoProcessoAtual;
	}

	public void setTempoRodadoProcessoAtual(int tempoRodadoProcessoAtual) {
		this.tempoRodadoProcessoAtual = tempoRodadoProcessoAtual;
	}

	public ArrayList<ProcessoInterativo> getFila() {
		return fila;
	}

	public void setFila(ArrayList<ProcessoInterativo> fila) {
		this.fila = fila;
	}

	public ArrayList<ProcessoInterativo> getFilaIO() {
		return filaIO;
	}

	public void setFilaIO(ArrayList<ProcessoInterativo> filaIO) {
		this.filaIO = filaIO;
	}

	public int getTickAtual() {
		return tickAtual;
	}

	public void setTickAtual(int tickAtual) {
		this.tickAtual = tickAtual;
	}
	
	public void addProcesso(String processoID, int prioridade) {
		this.fila.add(this.criarProcesso(processoID, StatusProcesso.WAITING, prioridade));
	}

	public void addProcesso(String processoID) {
		StatusProcesso status = StatusProcesso.WAITING;
		this.fila.add(this.criarProcesso(processoID, status));
	}
	
	public void finalizarProcesso(String processoID) {
		ProcessoInterativo processo = procurarProcesso(processoID, this.fila);
		
		if(processo == null && this.processoNaCPU.getProcessoID().equals(processoID)) {
			processo = this.processoNaCPU;
			this.processoNaCPU = null;
		}
		
		if(processo == null) {
			processo = procurarProcesso(processoID, this.filaIO);
		}
		// O processo pode n�o estar na filaIO, ent�o � bom verificar se � null de novo.
		if(processo != null) {
			processo.setStatus(StatusProcesso.FINISHED);
			if(!this.fila.remove(processo)) {
				this.filaIO.remove(processo);
			}
		}
	}
	
	private ProcessoInterativo criarProcesso(String processoID, StatusProcesso status, int prioridade) {
		return new ProcessoInterativo(processoID, status, this.tickAtual, prioridade);
	} 

	private ProcessoInterativo criarProcesso(String processoID, StatusProcesso status) {
		return new ProcessoInterativo(processoID, status, this.tickAtual);
	}
	
	public void bloquearProcesso(String processoID) {
		ProcessoInterativo processo = this.procurarProcesso(processoID, this.fila);
		if(processo == null && this.processoNaCPU.getProcessoID().equals(processoID)) {
			processo = this.processoNaCPU;
			this.processoNaCPU = null;
		}
		if(processo != null) {
			this.fila.remove(processo);
			processo.setStatus(StatusProcesso.BLOCKED);
			this.filaIO.add(processo);
		}
	}
	
	public void retomarProcesso(String processoID) {
		ProcessoInterativo processo = this.procurarProcesso(processoID, this.filaIO);
		if(processo != null) {
			this.filaIO.remove(processo);
			//CORRIGIR PRO BUG DO test32() onde ele volta waiting quando deveria estar rodando
			if(this.processoNaCPU == null) {
				processo.setStatus(StatusProcesso.RUNNING);
			}else {
				processo.setStatus(StatusProcesso.WAITING);
			}
			this.fila.add(processo);
		}
	}
	
	private ProcessoInterativo procurarProcesso(String processoID, ArrayList<ProcessoInterativo> fila) {
		for(ProcessoInterativo i: fila) {
			if(i.getProcessoID().equals(processoID)) {
				return i;
			}
		}
		return null;
	}
	
	//Refactoring
	protected void trocaDeProcesso() {
		this.processoNaCPU.setStatus(StatusProcesso.WAITING);
		this.fila.add(this.processoNaCPU);
		this.alterarProcessoNaCPU();
		this.tempoRodadoProcessoAtual = 1;
	}
	
	/**
	 * Metodo utilizado para trocar o processo atual na CPU.
	 * @return false se n�o houver nenhum processo na fila, true se a troca ocorreu.
	 */
	protected boolean alterarProcessoNaCPU() {
		try {
			this.processoNaCPU = this.fila.remove(0);
			this.processoNaCPU.setStatus(StatusProcesso.RUNNING);
			this.tempoRodadoProcessoAtual = 0;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	public void tick() {
		boolean continuarTick = true;
		ordenarFila();
		
		// Ver se temos algum processo na CPU, d� pra usar essa parte aqui no caso de ser chamado o tick e ainda n�o ter nenhum processo
		// Essa � a raz�o do boolean
		if(this.processoNaCPU == null) {
			continuarTick = this.alterarProcessoNaCPU();
		}
		
		if(continuarTick) {
			tickTemplateProcesso();
		}
		this.tickAtual++;
	}
	
	protected void tickTemplateProcesso() {
		//TODO ON CHILD
	}
	
	public String getStatusEscalonador() {
		String status = "";
		status += this.getStatusProcessos();
		status += this.getStatusMiddlePart();
		status += "Tick: " + this.getTickAtual();
		return status;
	}
	
	public String getStatusProcessos() {
		String statusProcessos = "";
		if(this.processoNaCPU != null) {
			statusProcessos += this.processoNaCPU.getProcessoID() + " - " + this.processoNaCPU.getStatus().toString() + "\n";
		}
		if(this.fila.size() > 0) {
			for(ProcessoInterativo i: this.fila) {
				statusProcessos += i.getProcessoID() + " - " + i.getStatus().toString() + "\n";
			}
		}
		if(this.filaIO.size() > 0) {
			for(ProcessoInterativo i: this.filaIO) {
				statusProcessos += i.getProcessoID() + " - " + StatusProcesso.BLOCKED + "\n";
				
			}
		}
		if(statusProcessos.equals("")){
			statusProcessos = "Nenhum Processo\n";
		}
		return statusProcessos;
	}

	protected String getStatusMiddlePart() {
		// TODO On Child
		return null;
	}

	public void ordenarFila() {
		//TODO ON CHILD
	}
	
}