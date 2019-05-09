package interativo;

import java.util.Collections;
import java.util.Comparator;

import exceptions.ProcessoInvalidoException;
import framework.StatusProcesso;

public class EscalonadorPrioridade extends RoundRobinInterativo {

	public EscalonadorPrioridade() {
		super();
	}

	public EscalonadorPrioridade(int quantum) {
		super(quantum);
	}
	
	@Override
	public void addProcesso(String processoID) {
		throw new ProcessoInvalidoException("Escalonador de Prioridade só pode ter processos com prioridade");
	}
	@Override
	public void addProcesso(String processoID, int prioridade) {
		this.fila.add(this.criarProcesso(processoID, StatusProcesso.WAITING, prioridade));
	}
	
	@Override
	protected void tickTemplateProcesso() {
		try {
			if(this.fila.get(0).getPrioridade() < this.processoNaCPU.getPrioridade()) {
				trocaDeProcesso();
			}
		}catch(IndexOutOfBoundsException e) {
			// Esperado se não houver processos na fila, não precisa fazer nada
		}
		if (this.tempoRodadoProcessoAtual < this.getQuantum()) {
			this.tempoRodadoProcessoAtual += 1;
		} else {
			this.ordenarFila();

			try {
				if (this.fila.get(0).getPrioridade() <= this.processoNaCPU.getPrioridade()) {
					trocaDeProcesso();
				} else {
					this.tempoRodadoProcessoAtual = 1;
				}
			} catch (IndexOutOfBoundsException e) {
				this.tempoRodadoProcessoAtual = 1;
			}
		}
	}

	@Override
	public void ordenarFila() {
		Collections.sort(this.fila, new Comparator<ProcessoInterativo>() {
			public int compare(ProcessoInterativo processo1, ProcessoInterativo processo2) {
				ProcessoInterativo p1 = processo1;
				ProcessoInterativo p2 = processo2;
				return p1.getPrioridade() < p2.getPrioridade() ? -1
						: (p1.getPrioridade() > p2.getPrioridade() ? +1 : 0);
			}
		});
	}

}
