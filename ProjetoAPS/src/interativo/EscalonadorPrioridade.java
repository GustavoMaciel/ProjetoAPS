package interativo;

import java.util.Collections;
import java.util.Comparator;


import framework.StatusProcesso;

public class EscalonadorPrioridade extends RoundRobinInterativo{
	
	public EscalonadorPrioridade() {
		super();
	}
	
	public EscalonadorPrioridade(int quantum) {
		super(quantum);
	}
	
	@Override
	public void tick() {
		boolean continuarTick = true;
		this.ordenarFila();
		if(this.processoNaCPU == null) {
			continuarTick = this.alterarProcessoNaCPU();
		}
		
		if(continuarTick) {
			if(this.tempoRodadoProcessoAtual < this.getQuantum()) {
				this.tempoRodadoProcessoAtual += 1;
			}else {
				this.ordenarFila();
				
				//Eu tenho em minha fila um processo que tenha a mesma prioridade que o proceso atual na CPU?
				//Porque se eu tenho, quer dizer que eu devo trocar
				//Se não tenho, não devo trocar de processo
				//Por algum motivo se eu colocar o this.tempoRodadoProcessoAtual = 1; antes do if else dá erro, vai entender
				if(this.fila.get(0).getPrioridade() == this.processoNaCPU.getPrioridade()) {
					this.processoNaCPU.setStatus(StatusProcesso.WAITING);
					this.fila.add(this.processoNaCPU);
					this.alterarProcessoNaCPU();
					this.tempoRodadoProcessoAtual = 1;
				}else {
					this.tempoRodadoProcessoAtual = 1;
				}
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
				return p1.getPrioridade() < p2.getPrioridade() ? -1 : (p1.getPrioridade() > p2.getPrioridade() ? +1 : 0);
			}
		});
		Collections.reverse(this.fila);
	}

}
