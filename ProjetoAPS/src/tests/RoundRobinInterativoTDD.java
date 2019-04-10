package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interativo.*;

class RoundRobinInterativoTDD {
	// Metodos e Cen�rios
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
	
	/**
	 * T3
	 * Adiciona um processo (p1) no tick 0
	 * Chamar tick para ver se P1 continua Executanto
	 * 
	 * */
	@Test
	void adicionaP1NoTick0EVerificaSeEleContinuaExecutanto() {
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.tick();
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
	}

	/**
	 * T4
	 * A partir do T3. Finalizar P1 e verificar se ele ainda executa mais um tick e finaliza
	 * 
	 * */
	@Test
	void finalizarProcessoEVerificarSeAindaExecutaUmTick(){
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.tick();
		
//		esca.getFila().add(new ProcessoInterativo("P2", StatusProcesso.WAITING, 1));
		
//		System.out.println(esca.getFila());
		
		// assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		
		esca.finalizarProcesso("P1");
		assertEquals("P1 - RUNNING\n"
				+ "Quantum: 3\n"
				+ "Tick: 1", esca.getStatusEscalonador());
		esca.tick();
		esca.tick();
		esca.tick();
		assertEquals("Nenhum Processo\n"
				+ "Quantum: 3\n"
				+ "Tick: 2", esca.getStatusProcessos());
		}

}