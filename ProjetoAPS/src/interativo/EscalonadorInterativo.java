package interativo;

import framework.StatusProcesso;
import java.util.ArrayList;

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

	public String getStatusProcessos() {
		String statusProcessos = "";
		if(this.processoNaCPU != null) {
			statusProcessos += this.processoNaCPU.getProcessoID() + " - " + this.processoNaCPU.getStatus().toString() + "\n";
		}
		if(this.fila.size() > 0 || this.filaIO.size() > 0) {
			for(ProcessoInterativo i: this.fila) {
				statusProcessos += i.getProcessoID() + " - " + i.getStatus().toString() + "\n";
			}
			for(ProcessoInterativo i: this.filaIO) {
				statusProcessos += i.getProcessoID() + " - " + "BLOQUEADO\n";
			}
		}else {
			statusProcessos = "Nenhum Processo\n";
		}
		return statusProcessos;
	}

	public int getTickAtual() {
		return tickAtual;
	}

	public void setTickAtual(int tickAtual) {
		this.tickAtual = tickAtual;
	}

	public void addProcesso(String processoID) {
		StatusProcesso status = StatusProcesso.WAITING;
		this.fila.add(this.criarProcesso(processoID, status));
	}
	
	public void finalizarProcesso(String processoID) {
		ProcessoInterativo processo = procurarProcesso(processoID, this.fila);
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

	private ProcessoInterativo criarProcesso(String processoID, StatusProcesso status) {
		return new ProcessoInterativo(processoID, status, this.tickAtual);
	}
	
	public void bloquearProcesso(String processoID) {
		ProcessoInterativo processo = this.procurarProcesso(processoID, this.fila);
		if(processo != null) {
			this.fila.remove(processo);
			this.filaIO.add(processo);
		}
	}
	
	public void retomarProcesso(String processoID) {
		ProcessoInterativo processo = this.procurarProcesso(processoID, this.filaIO);
		if(processo != null) {
			this.filaIO.remove(processo);
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
		//TODO ON CHILD
		return null;
	}

	public void ordenarFila() {
		//TODO ON CHILD
	}
	
}
