package tests;
import interativo.*;
import framework.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EscalonadorPrioridadeTDD {
	// Metodos e Cenários
	public void rodaTickNVezes(EscalonadorInterativo esca, int n, boolean printForDebug) {
		for (int i = 0; i < n; i++) {
			esca.tick();
			if(printForDebug) System.out.println(esca.getStatusEscalonador());
			}
	}
	
	public EscalonadorInterativo cenarioNProcessos(int n, int quantum) {
		EscalonadorInterativo esca = new EscalonadorPrioridade(quantum);
		for(int i=0; i<n; i++) {
			esca.addProcesso("P"+String.valueOf(i+1), 1);
		}
		return esca;
	}
	
	public void addNProcessos(int n, EscalonadorInterativo esca, int ultimoNumeroDeProcesso, int prioridade) {
		for(int i=0; i<n; i++) {
			esca.addProcesso("P" + String.valueOf(++ultimoNumeroDeProcesso), prioridade);
		}
	}

	@Test
	void test() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		addNProcessos(2, esca, 2, 2);
		rodaTickNVezes(esca, 15, true);
	}

}
