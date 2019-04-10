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
		if(!(this instanceof EscalonadorPrioridade)) throw new ProcessoInvalidoException("Somente EscalonadorPrioridade pode criar processo com prioridade");
		this.fila.add(this.criarProcesso(processoID, StatusProcesso.WAITING, prioridade));
	}

	public void addProcesso(String processoID) {
		if(this instanceof EscalonadorPrioridade) throw new ProcessoInvalidoException("Escalonador de Prioridade só pode ter processos com prioridade");
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
		// O processo pode nï¿½o estar na filaIO, entï¿½o ï¿½ bom verificar se ï¿½ null de novo.
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
			processo.setStatus(StatusProcesso.WAITING);
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
	
	public void tick() {
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
				statusProcessos += i.getProcessoID() + " - " + StatusProcesso.BLOCKED;
				
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