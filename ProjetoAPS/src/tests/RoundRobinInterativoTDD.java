package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interativo.*;

class RoundRobinInterativoTDD {
	// Metodos e Cenários
	public void rodaTickNVezes(EscalonadorInterativo esca, int n, boolean printForDebug) {
		for (int i = 0; i < n; i++) {
			esca.tick();
			if(printForDebug) System.out.println(esca.getStatusEscalonador());
			}
	}
	
	public EscalonadorInterativo cenarioNProcessos(int n, int quantum) {
		EscalonadorInterativo esca = new RoundRobinInterativo(quantum);
		for(int i=0; i<n; i++) {
			esca.addProcesso("P"+String.valueOf(i+1));
		}
		return esca;
	}
	
	public void addNProcessos(int n, EscalonadorInterativo esca, int ultimoNumeroDeProcesso) {
		for(int i=0; i<n; i++) {
			esca.addProcesso("P" + String.valueOf(++ultimoNumeroDeProcesso));
		}
	}
	//criar metodo pra add n processos
	
	
	// Testes

	// Escalonador Vazio
	@Test
	void escalonadorVazio() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		assertEquals("Nenhum Processo\nQuantum: 3\nTick: 0", esca.getStatusEscalonador());
	}
	
	//Testar se Tick Incrementa
	@Test
	void incrementarTick() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.tick();
		assertEquals(1, esca.getTickAtual());
	}
	
	//Testar dois processos acontecendo o estouro de quantum
	@Test
	void trocaDoisProcessos() {
		EscalonadorInterativo esca = cenarioNProcessos(2, 3);
		esca.tick();
		assertEquals("P1 - RUNNING\nP2 - WAITING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 3, false);
		assertEquals("P2 - RUNNING\nP1 - WAITING\nQuantum: 3\nTick: 4", esca.getStatusEscalonador());
	}

}