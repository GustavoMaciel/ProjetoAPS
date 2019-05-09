package interativo;

import java.util.Collections;
import java.util.Comparator;

import exceptions.ProcessoInvalidoException;

public class RoundRobinInterativo extends EscalonadorInterativo{
	private int quantum;
	
	public RoundRobinInterativo() {
		super();
		this.quantum = 3;
	}

	public RoundRobinInterativo(int quantum) {
		super();
		this.quantum = quantum;
	}

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) {
		this.quantum = quantum;
	}
	
	@Override
	public String getStatusMiddlePart() {
		return "Quantum: " + this.getQuantum() + "\n";
	}
	
	@Override
	protected void tickTemplateProcesso() {
		if(this.tempoRodadoProcessoAtual < this.quantum) {
			this.tempoRodadoProcessoAtual += 1;
		}else {
			trocaDeProcesso();
		}
	}
	
	@Override
	public void addProcesso(String processoID, int prioridade) {
		throw new ProcessoInvalidoException("Somente EscalonadorPrioridade pode criar processo com prioridade");
	}
	
	@Override
	public void ordenarFila() {
		Collections.sort(this.fila, new Comparator<ProcessoInterativo>() {
			public int compare(ProcessoInterativo processo1, ProcessoInterativo processo2) {
				ProcessoInterativo p1 = processo1;
				ProcessoInterativo p2 = processo2;
				return p1.getTempoChegada() < p2.getTempoChegada() ? -1 : (p1.getTempoChegada() > p2.getTempoChegada() ? +1 : 0);
			}
		});
	}
	
	
}
