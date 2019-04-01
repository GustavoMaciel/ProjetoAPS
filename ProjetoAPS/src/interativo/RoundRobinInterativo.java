package interativo;

import java.util.Collections;
import java.util.Comparator;
import framework.StatusProcesso;

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
	
	/**
	 * Metodo utilizado para trocar o processo atual na CPU.
	 * @return false se não houver nenhum processo na fila, true se a troca ocorreu.
	 */
	private boolean alterarProcessoNaCPU() {
		try {
			this.processoNaCPU = this.fila.remove(0);
			this.processoNaCPU.setStatus(StatusProcesso.RUNNING);
			this.tempoRodadoProcessoAtual = 0;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public void tick() {
		this.ordenarFila();
		
		boolean continuarTick = true;
		// Ver se temos algum processo na CPU, dá pra usar essa parte aqui no caso de ser chamado o tick e ainda não ter nenhum processo
		// Essa é a razão do boolean
		if(this.processoNaCPU == null) {
			continuarTick = this.alterarProcessoNaCPU();
		}
		if(continuarTick) {
			if(this.tempoRodadoProcessoAtual < this.quantum) {
				this.tempoRodadoProcessoAtual += 1;
			}else {
				this.processoNaCPU.setStatus(StatusProcesso.WAITING);
				this.fila.add(this.processoNaCPU);
				this.alterarProcessoNaCPU();
				this.tempoRodadoProcessoAtual = 1;
			}
		}
		this.tickAtual += 1;
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
