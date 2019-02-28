package framework;

import java.util.Collections;
import java.util.Comparator;

import org.junit.runner.manipulation.Sortable;

import exceptions.FilaInvalidaException;
import exceptions.QuantumMenorQueUmException;

public class EscalonadorRoundRobin extends Escalonador {

	private int quantum;

	public EscalonadorRoundRobin() {
		this.quantum = 3;
	}

	public EscalonadorRoundRobin(int quantum) throws QuantumMenorQueUmException {
		if (quantum < 1) {
			throw new QuantumMenorQueUmException("Quantum inválido");
		}
		this.quantum = quantum;
	}

	public int getQuantum() {
		return quantum;
	}

	public void setQuantum(int quantum) throws QuantumMenorQueUmException {
		if (quantum < 1) {
			throw new QuantumMenorQueUmException("Quantum inválido");
		}
		this.quantum = quantum;
	}

	@Override
	public TabelaResultante rodar() throws FilaInvalidaException {
		if (this.fila.size() < 1) {
			throw new FilaInvalidaException("Fila do escalonador vazia");
		}
		// Primeiro passo: ordenar a fila de processos de acordo com o tempo de inicio.
		this.odenarFila();

		TabelaResultante tabelaFinal = new TabelaResultante();
		// Segundo passo: adicionar uma nova linha para cada processo na tabela
		// resultante do escalonador.
		for (Processo i : this.fila) {
			tabelaFinal.addLinha(i.getProcessoID(), new Linha(i));
		}

		int tempoAtual = 0;
		boolean parar = false;
		
		// --Essa sessão só serve pra saber se o primeiro processo da fila não inicia no
		// tempo 0
		Processo primeiroNaFila = this.fila.get(0);
		if (primeiroNaFila.getTempoInicio() > tempoAtual) {
			for (int i = 0; i < primeiroNaFila.getTempoInicio(); i++) {
				for (Processo atual : this.fila) {
					tabelaFinal.addStatus(atual.getProcessoID(), StatusProcesso.NAOEXISTE, tempoAtual);
				}
				tempoAtual++;
			}
		}
		// --
		
		while (!parar) {
			//Remove o primeiro processo da fila
			Processo processoNaCPU = this.fila.remove(0);
			
			// Se o processo que está atualmente na CPU ainda não está no tempo de iniciar ele deve ganhar o status de não existe
			if (processoNaCPU.getTempoInicio() > tempoAtual) {
				tabelaFinal.addStatus(processoNaCPU.getProcessoID(), StatusProcesso.NAOEXISTE, tempoAtual);
				tempoAtual++;
				this.fila.add(processoNaCPU);
				continue;
			}

			//Rodar o processo na CPU de acordo com o quantum
			for (int i = 0; i < this.quantum; i++) {
				
				tabelaFinal.addStatus(processoNaCPU.getProcessoID(), StatusProcesso.RUNNING, tempoAtual);
				processoNaCPU.setTempoExec(processoNaCPU.getTempoExec() - 1);

				for (Processo atual : this.fila) {
					if (tempoAtual < atual.getTempoInicio()) {
						tabelaFinal.addStatus(atual.getProcessoID(), StatusProcesso.NAOEXISTE, tempoAtual);
					} else {
						tabelaFinal.addStatus(atual.getProcessoID(), StatusProcesso.WAITING, tempoAtual);
					}
				}
				tempoAtual++;
				// -- Se o processo terminou antes do Quantum, ele será removido da fila
				if (processoNaCPU.getTempoExec() <= 0) {
					tabelaFinal.addStatus(processoNaCPU.getProcessoID(), StatusProcesso.FINISHED, tempoAtual);
					break;
				}
				// --
			}
			// -- Se o processo ainda precisa executar, ele tem que ir pro final da fila
			// novamente

			if (processoNaCPU.getTempoExec() > 0) {
				this.fila.add(processoNaCPU);
			}

			if (this.fila.size() == 0) {
				parar = true;
			}

			// --

		}
		return tabelaFinal;
	}

}
