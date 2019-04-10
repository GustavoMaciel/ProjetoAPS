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
	 * A partir do T3. Finalizar P1 e verificar se ele foi removido da CPU
	 * */
	@Test
	void finalizarProcessoEVerificarSeAindaExecutaUmTick(){
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.tick();
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		
		esca.finalizarProcesso("P1");
		assertEquals("Nenhum Processo\n", esca.getStatusProcessos());
	}
	
	/**
	 * T5
	 * Cria dois processos no tick 0 e chama o tick até estourar o quantum
	 * 
	 * P1 - WAITING
	 * P2 - WAITING
	 * 
	 * tick
	 * tick
	 * tick
	 * 
	 * P1 - RUNNING
	 * P2 - WAITING
	 * tick: 3
	 * quantum: 3
	 * */
	@Test
	void cria2ProcessosEmTick0EChamarTickAteEstourarOQauntum(){
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		
		esca.tick();
		assertEquals("P1 - RUNNING\nP2 - WAITING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		
		
		esca.tick();
		esca.tick();
		
		assertEquals("P1 - RUNNING\nP2 - WAITING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		
	}
	
	
	/**
	 * T6
	 * Repetir o T5 com 3 processos
	 * */
	@Test
	void cria3ProcessosEmTick0EChamarTickAteEstourarOQauntum(){
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		esca.addProcesso("P8");
		
		esca.tick();
		assertEquals(""
				+ "P1 - RUNNING\n"
				+ "P2 - WAITING\n"
				+ "P8 - WAITING\n"
				+ "Quantum: 3"
				+ "\nTick: 1", esca.getStatusEscalonador());
		
		esca.tick();
		esca.tick();
		
		assertEquals(""
				+ "P1 - RUNNING\n"
				+ "P2 - WAITING\n"
				+ "P8 - WAITING\n"
				+ "Quantum: 3\n"
				+ "Tick: 3", esca.getStatusEscalonador());
	}
	
	
	/**
	 * P7
	 * Parecido com T5, P2 entra no tick 3
	 * mas a concorrência só começa em TICK 3
	 * 
	 * P1 - WAITING
	 * P8 - WAITING
	 * P2 - WAITING
	 * 
	 * tick
	 * tick
	 * tick
	 * 
	 * P1 - RUNNING
	 * P2 - WAITING
	 * tick: 3
	 * quantum: 3
	 * 
	 * */
	@Test
	void cria3ProcessosMasP2SoEhCriadoNoTick3(){
		EscalonadorInterativo esca = new RoundRobinInterativo(3);
		esca.addProcesso("P1");
		esca.addProcesso("P8");
		
		esca.tick();
		esca.tick();
		assertEquals(""
				+ "P1 - RUNNING\n"
				+ "P8 - WAITING\n"
				+ "Quantum: 3"
				+ "\nTick: 2", esca.getStatusEscalonador());
		
		esca.tick();
		esca.addProcesso("P2");
		
		esca.tick();
		esca.tick();
		
		assertEquals(""
				+ "P8 - RUNNING\n"
				+ "P1 - WAITING\n"
				+ "P2 - WAITING\n"
				+ "Quantum: 3\n"
				+ "Tick: 5", esca.getStatusEscalonador());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}