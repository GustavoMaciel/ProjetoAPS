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
	 * 
	 * P8 - RUNNING
	 * P1 - WAITING
	 * P2 - WAITING
	 * TICK: 5
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
	
	/**Concorrencia, processo finaliza quando tava executando
	 * T8
	 * Cria dois processos, ambos vão brigar pela cpu, mas P1 finaliza quando estava executando
	 * Ou seja, quando ele finalizar, no próximo tick precisa estar P2 executando
	 * 
	 * */
	@Test
	void concorrenciaProcessoFinalizaQuandoEstavaExecutando() {
		EscalonadorInterativo esca = new RoundRobinInterativo(2);
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		
		esca.tick(); // P1 precisa tah executando
		
		esca.finalizarProcesso("P1");
		esca.tick();
		
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "Quantum: 2\n"
				+ "Tick: 2", esca.getStatusEscalonador());
		
	}
	
	/**Concorrencia, processo finaliza quando tava esperando
	 * T9
	 * Cria dois processos, ambos brigam pela cpu, mas P1 finaliza quando estava esperando
	 * Ou seja, quando ele finalizar, no próximo tick precisa estar P2 executando
	 * Quando o quantum de P2 estourar ele precisa ainda continuar executando
	 * 
	 * */
	@Test
	void concorrenciaProcessoFinalizaQuandoEstavaEsperando() {
		EscalonadorInterativo esca = new RoundRobinInterativo(2);
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		
		rodaTickNVezes(esca, 3, false); // esca.tick(); esca.tick(); esca.tick();
		
		
		esca.finalizarProcesso("P1");
		
		rodaTickNVezes(esca, 3, false); // esca.tick(); esca.tick(); esca.tick();
		
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "Quantum: 2\n"
				+ "Tick: 6", esca.getStatusEscalonador());
		
	}
	
	/**Concorrencia, processo finaliza quando tava esperando
	 * T10 => T5 com quantum default
	 * Cria dois processos no tick 0 e chama o tick até estourar o quantum
	 * */
	@Test
	void criaDoisProcessosEMandaEstourarOQuantumDefault() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		
		rodaTickNVezes(esca, 4, false); // esca.tick(); esca.tick(); esca.tick();
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "P1 - WAITING\n"
				+ "Quantum: 3\n" // quantum default
				+ "Tick: 4", esca.getStatusEscalonador());
		
	}
	
	/**
	 * T11  - Não entendi
	 * */
	
	
	/**
	 * T12
	 * A partir de T6, o processo executando bloqueia
	 * P2 - RUNNING
	 * P3 - WAITNG
	 * P1 - BLOCKED
	 * 
	 * Quando estoura apenas entre P2 e P3
	 * 
	 * */
	@Test
	void processoExecutandoBloqueiaFicandoApenasOutrosProcessosNaCPU() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		esca.addProcesso("P3");
		
		esca.tick();
		esca.bloquearProcesso("P1");
		esca.tick();
		esca.tick();
		
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "P3 - WAITING\n"
				+ "P1 - BLOCKED", esca.getStatusProcessos());
	}
	
	/**
	 * TInventado a partir do T12
	 * A partir de T6, o processo executando bloqueia
	 * Mas volta a fila de execução após P3 estourar o quantum
	 * 
	 * Status normal
	 * P2 - RUNNING
	 * P3 - WAITNG
	 * P1 - BLOCKED
	 * */
	@Test
	void processoExecutandoBloqueiaVoltaAFilaAposP3EstourarOQuantum() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		esca.addProcesso("P3");
		
		esca.tick();
		esca.bloquearProcesso("P1");
		esca.tick();
		esca.tick();
		
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "P3 - WAITING\n"
				+ "P1 - BLOCKED", esca.getStatusProcessos());
	}
	
	/**
	 *  T13
	 * A partir de T12, P1 é retornado quando P2 está executando
	 * Status normal
	 * P2 - RUNNING
	 * P3 - WAITING
	 * P1 - WAITING
	 * */
	@Test
	void p1RetornaQuandoP2EstaExecutando() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		esca.addProcesso("P3");
		
		esca.tick();
		esca.bloquearProcesso("P1");
		esca.tick();
		esca.retomarProcesso("P1"); // ERRO Deveria voltar como WAITING
//		esca.tick();
		
		assertEquals(""
				+ "P2 - RUNNING\n"
				+ "P3 - WAITING\n"
				+ "P1 - WAITING", esca.getStatusProcessos());
	}
	
	// NÃO ENTENDI BEM O TESTE	
	/**
	 * T14
	 * P2 - BLOCKED
	 * P3 - BLOCKED
	 * P1 - BLOCKED
	 * Retornam na ordem que pararam
	 * */
	@Test
	void osProcessosBloqueamERetormaNaOrdemP1_P2_P3() {
		EscalonadorInterativo esca = new RoundRobinInterativo();
		esca.addProcesso("P1");
		esca.addProcesso("P2");
		esca.addProcesso("P3");
		
		esca.bloquearProcesso("P1");
		esca.bloquearProcesso("P2");
		esca.bloquearProcesso("P3");
		
		
		assertEquals(""
				+ "P1 - BLOCKED\n"
				+ "P2 - BLOCKED\n"
				+ "P3 - BLOCKED", esca.getStatusProcessos());
		
		esca.retomarProcesso("P1");
		esca.retomarProcesso("P2");
		esca.retomarProcesso("P3");
		
		assertEquals(""
				+ "P1 - WAITING\n"
				+ "P2 - WAITING\n"
				+ "P3 - WAITING\n", esca.getStatusProcessos());
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}