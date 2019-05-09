package tests;
import interativo.*;
import framework.*;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.crypto.Data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import exceptions.ProcessoInvalidoException;

class EscalonadorPrioridadeTDD {
	// Metodos e Cen�rios
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
	public EscalonadorInterativo cenarioNProcessos(int n) {
		return cenarioNProcessos(n, 3);
	}
	
	public void addNProcessos(int n, EscalonadorInterativo esca, int ultimoNumeroDeProcesso, int prioridade) {
		for(int i=0; i<n; i++) {
			esca.addProcesso("P" + String.valueOf(++ultimoNumeroDeProcesso), prioridade);
		}
	}
	
	//teste de criação e adição de escalonador
	@Test
	void test() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		addNProcessos(2, esca, 2, 2);
		rodaTickNVezes(esca, 15, true);
	}
	
	/*criar escalonador com prioridade
	 * adicionar processos sem prioridade 
	 * verificar se exceção foi lançada
	 * status -> nenhum processo
	 * 			 Tick = 1
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test15() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		Assertions.assertThrows(ProcessoInvalidoException.class, () -> {
		esca.addProcesso("P1");
		});
		assertEquals("Nenhum Processo\n", esca.getStatusProcessos());
	}
	
	/*criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1 
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 Tick = 1
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test16() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 1, false);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
	}
	/*criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1 
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 
	 *  		 Tick = 1
	 *  		 2 - Nenhum processo
	 * 			 Tick = 2
	 * 			 Quantum = "default" = 3
	 */
	@Test 
	void test17() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 1, false);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		esca.finalizarProcesso("P1");
		rodaTickNVezes(esca, 1, false);
		assertEquals("Nenhum Processo\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
		
	}
	/* O mesmo teste que T5
	 * criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1 em Tick 0
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 *  		 Tick = 1
	 *  		 Chamar Tick até estourar o Quantum 
	 * 			 1 - P2 (EXECUTANDO)
	 * 			 2 - P1 (ESPERANDO)
	 * 			 Chamar Tick até estourar o Quantum 
	 * 			 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test18() {//T5
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		
		
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P2 - RUNNING\n"
				+    "P1 - WAITING\nQuantum: 3\nTick: 6", esca.getStatusEscalonador());
		
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\nQuantum: 3\nTick: 9", esca.getStatusEscalonador());
		
	}
	/* Repetir T5 com 3 Processos
	 * criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1 em Tick 0
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test19() {//6
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+    "P3 - WAITING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 3, true);
		assertEquals("P2 - RUNNING\n"
				+    "P3 - WAITING\n"
				+    "P1 - WAITING\nQuantum: 3\nTick: 6", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 3, true);
		assertEquals("P3 - RUNNING\n"
				+    "P1 - WAITING\n"
				+    "P2 - WAITING\nQuantum: 3\nTick: 9", esca.getStatusEscalonador());
	}
	/* O mesmo teste que T5, mas P2 é criado só no tick 3
	 * criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1 em Tick 1
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Chamar Tick até 3, onde será criado P2 (A concorrência começa em Tick 3)
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test20() {//7
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
		assertEquals("P2 - RUNNING\n"
				+ 	 "P1 - WAITING\nQuantum: 3\nTick: 6", esca.getStatusEscalonador());
		
	}
	
	@Test
	void test21() {//8
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 2, true);
		assertEquals("P1 - RUNNING\nP2 - WAITING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
		esca.finalizarProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		assertEquals("P2 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());

	}
	/* criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Porcesso continua executando mesmo depois de estourar o Quantum
	 *  		 Matar P2 que estava esperando
	 *  		 2 - P1 continua executando
	 *  	    
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test22() {//9
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 2, true);
		
		assertEquals("P1 - RUNNING\n"
				+ 	 "P2 - WAITING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
		
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 5, true);
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 7", esca.getStatusEscalonador());
		
	}
	/* Repetir T5 com Quantum não default (4)
	 * criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1 
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Tick = 4
	 * 			 Quantum = 4
	 * 			 Finaliza com tick 8
	 */
	@Test
	void test23() {//10
		int Quantum = 4;
		EscalonadorInterativo esca = new EscalonadorPrioridade(Quantum);
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 4, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\nQuantum: 4\nTick: 4", esca.getStatusEscalonador());
		
		
		rodaTickNVezes(esca, 4, true);
		
		assertEquals("P2 - RUNNING\n"
				+    "P1 - WAITING\nQuantum: 4\nTick: 8", esca.getStatusEscalonador());
	}
	/* criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1  
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 2 - P1 finaliza
	 *  		 3 - A cpu fica ociosa, até que no tick 4 o P2 é criado 
	 *  		 2 - P2 (EXECUTANDO) 
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test24() {//11
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 2, true);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
		esca.finalizarProcesso("P1");
		rodaTickNVezes(esca, 2, true);
		assertEquals("Nenhum Processo\nQuantum: 3\nTick: 4", esca.getStatusEscalonador());// era pra passar no tick 3 também
		addNProcessos(1, esca, 1, 1);
		
		rodaTickNVezes(esca, 3, true);
		assertEquals("P2 - RUNNING\nQuantum: 3\nTick: 7", esca.getStatusEscalonador());
		
	}
	/* criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1  
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 2 - P1 (BLOQUEADO)
	 *  		 Tick = 1
	 *  		 Quantum roda apenas entre P2 e P3  
	 * 			 Tick = 7
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test25() { //12
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		rodaTickNVezes(esca, 1, true);
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+    "P3 - WAITING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		assertEquals("P2 - RUNNING\n"
				+    "P3 - WAITING\n"
				+    "P1 - BLOCKED\n"
				+	 "Quantum: 3\nTick: 2", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 3, true);
		assertEquals("P3 - RUNNING\n"
				+    "P2 - WAITING\n"
				+    "P1 - BLOCKED\n"
				+	 "Quantum: 3\nTick: 5", esca.getStatusEscalonador());
		
	}
	/* criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1  
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 2 - P1 (BLOQUEADO)
	 *  		 Tick = 1
	 *  		 Quantum roda apenas entre P2 e P3
	 *  		 P1 é Retomado
	 *  		 Tick
	 *  		 4 - P1 (ESPERANDO)  
	 * 			 Tick
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test26() { //13
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		rodaTickNVezes(esca, 1, true);
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+    "P3 - WAITING\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		assertEquals("P2 - RUNNING\n"
				+    "P3 - WAITING\n"
				+    "P1 - BLOCKED\n"
				+	 "Quantum: 3\nTick: 2", esca.getStatusEscalonador());
		esca.retomarProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		assertEquals("P2 - RUNNING\n"
				+    "P3 - WAITING\n"
				+    "P1 - WAITING\n"
				+	 "Quantum: 3\nTick: 3", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 4, true);
		assertEquals("P3 - RUNNING\n"
				+    "P1 - WAITING\n"
				+    "P2 - WAITING\n"
				+	 "Quantum: 3\nTick: 7", esca.getStatusEscalonador());
		rodaTickNVezes(esca, 3, true);
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+    "P3 - WAITING\n"
				+	 "Quantum: 3\nTick: 10", esca.getStatusEscalonador());
		
	}
	/* criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1
	 * os 3 Processos Bloqueiam e retoma na ordem:
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P1 (ESPERANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test27() { //14
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		esca.bloquearProcesso("P2");
		esca.bloquearProcesso("P3");									
		rodaTickNVezes(esca, 1, true);
		assertEquals("P1 - BLOCKED\n"
				+    "P2 - BLOCKED\n"
				+    "P3 - BLOCKED\n"
				+	 "Quantum: 3\nTick: 1", esca.getStatusEscalonador());
		esca.retomarProcesso("P2");
		esca.retomarProcesso("P1");
		esca.retomarProcesso("P3");
		rodaTickNVezes(esca, 3, true);
		assertEquals("P2 - RUNNING\n"
				+    "P1 - WAITING\n"
				+    "P3 - WAITING\n"
				+	 "Quantum: 3\nTick: 4", esca.getStatusEscalonador());
	}
	/* criar escalonador com prioridade
	 * adiciona 1 processo com prioridade 1
	 * faz 3 ticks
	 * adiciona 1 processo com prioridade 2
	 * faz 10 ticks
	 * 
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test28() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		
		addNProcessos(1, esca, 1, 2);
		rodaTickNVezes(esca, 10, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+ 	 "Quantum: 3\nTick: 13", esca.getStatusEscalonador());
		
	}
	
	/* criar escalonador com prioridade
	 * adiciona 1 processo com prioridade 1
	 * faz 3 ticks
	 * adiciona 1 processo com prioridade 2
	 * faz 10 ticks
	 * Bloqueia P1
	 * 
	 * status -> 1 - P1 (Executando)
	 * 			 2 - P2 (Bloqueado)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test29() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		
		addNProcessos(1, esca, 1, 2);
		rodaTickNVezes(esca, 10, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+ 	 "Quantum: 3\nTick: 13", esca.getStatusEscalonador());
		
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		
		assertEquals("P2 - RUNNING\n"
				+    "P1 - BLOCKED\n"
				+ 	 "Quantum: 3\nTick: 14", esca.getStatusEscalonador());
		
		
	}
	/* criar escalonador com prioridade
	 * adiciona 1 processo com prioridade 1
	 * faz 3 ticks
	 * adiciona 1 processo com prioridade 2
	 * faz 10 ticks
	 * Bloqueia P1
	 * Tick
	 * Retorna P1
	 * status -> 1 - P1 (Executando)
	 * 			 2 - P2 (Bloqueado)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test30() { // não ta funcionando
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
		
		addNProcessos(1, esca, 1, 2);
		rodaTickNVezes(esca, 10, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+ 	 "Quantum: 3\nTick: 13", esca.getStatusEscalonador());
		
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		
		assertEquals("P2 - RUNNING\n"
				+    "P1 - BLOCKED\n"
				+ 	 "Quantum: 3\nTick: 14", esca.getStatusEscalonador());
		
		rodaTickNVezes(esca, 1, true);
		esca.retomarProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\n"
				+ 	 "Quantum: 3\nTick: 16", esca.getStatusEscalonador());
	}
		
	/* criar escalonador com prioridade
	 * adiciona 1 processo com prioridade 2
	 * tick
	 * status -> 1 - P1 (Executando)		 
	 * tick
	 * adiciona 1 processo com prioridade 1
	 * tick
	 * status -> 1 - P2 (Executando)
	 * 			 1 - P1 (Esperando)
	 * Finalizar P2
	 * tick
	 * status -> 1 - P1 (Executando)
	 * 			 Quantum = "default" = 3
	 */
		@Test
		void test31() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 2);
			rodaTickNVezes(esca, 3, true);
			
			assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());

			addNProcessos(1, esca, 1, 1);
			rodaTickNVezes(esca, 3, true);
			
			assertEquals("P2 - RUNNING\n"
					+    "P1 - WAITING\n"
					+ 	 "Quantum: 3\nTick: 6", esca.getStatusEscalonador());
			
			esca.finalizarProcesso("P2");
			rodaTickNVezes(esca, 3, true);
			assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 9", esca.getStatusEscalonador());
		}
		
		/*
		 *Para que esse teste fosse feito no nosso escalonador, 
		 *seria necessário mudar a tabela resultante 
		*/
		@Test
		void test32() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 3);
			assertEquals("P1 - WAITING\nQuantum: 3\nTick: 0", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 2, true);
			assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
			
			addNProcessos(1, esca, 1, 1);
			rodaTickNVezes(esca, 1, true);
			assertEquals("P2 - RUNNING\nP1 - WAITING\nQuantum: 3\nTick: 3", esca.getStatusEscalonador());
			
			esca.bloquearProcesso("P2");
			rodaTickNVezes(esca, 2, true);
			addNProcessos(1, esca, 2, 4);
			assertEquals("P1 - RUNNING\nP3 - WAITING\nP2 - BLOCKED\nQuantum: 3\nTick: 5", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			esca.bloquearProcesso("P1");
			assertEquals("P3 - RUNNING\nP2 - BLOCKED\nP1 - BLOCKED\nQuantum: 3\nTick: 6", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			
			esca.retomarProcesso("P2");
			assertEquals("P3 - RUNNING\nP2 - WAITING\nP1 - BLOCKED\nQuantum: 3\nTick: 7", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			addNProcessos(1, esca, 3, 1);
			esca.retomarProcesso("P1");
			
			rodaTickNVezes(esca, 2, true);
			
			addNProcessos(2, esca, 4, 2);
			
			rodaTickNVezes(esca, 10, true);
			esca.bloquearProcesso("P4");
			assertEquals("P2 - RUNNING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nP4 - BLOCKED\nQuantum: 3\nTick: 20", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 4, true);
			assertEquals("P2 - RUNNING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nP4 - BLOCKED\nQuantum: 3\nTick: 24", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			esca.retomarProcesso("P4");
			assertEquals("P2 - RUNNING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nP4 - WAITING\nQuantum: 3\nTick: 25", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 4, true);
			assertEquals("P4 - RUNNING\nP2 - WAITING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 29", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			esca.finalizarProcesso("P4");
			assertEquals("P2 - WAITING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 30", esca.getStatusEscalonador());
			//NOSSA DECISÃO É QUE QUANDO UM PROCESSO É DELETADO, TODOS FICAM EM ESPERA. 
			
			rodaTickNVezes(esca, 1, true);
			assertEquals("P2 - RUNNING\nP5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 31", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			esca.finalizarProcesso("P2");
			assertEquals("P5 - WAITING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 32", esca.getStatusEscalonador());
			//NOSSA DECISÃO É QUE QUANDO UM PROCESSO É DELETADO, TODOS FICAM EM ESPERA. 
			
			rodaTickNVezes(esca, 3, true);
			assertEquals("P5 - RUNNING\nP6 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 35", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 3, true);
			assertEquals("P6 - RUNNING\nP5 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 38", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 2, true);
			esca.bloquearProcesso("P5");
			assertEquals("P6 - RUNNING\nP1 - WAITING\nP3 - WAITING\nP5 - BLOCKED\nQuantum: 3\nTick: 40", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 2, true);
			assertEquals("P6 - RUNNING\nP1 - WAITING\nP3 - WAITING\nP5 - BLOCKED\nQuantum: 3\nTick: 42", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 3, true);
			esca.retomarProcesso("P5");
			assertEquals("P6 - RUNNING\nP1 - WAITING\nP3 - WAITING\nP5 - WAITING\nQuantum: 3\nTick: 45", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			esca.finalizarProcesso("P6");
			assertEquals("P5 - WAITING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 46", esca.getStatusEscalonador());
			
			rodaTickNVezes(esca, 1, true);
			assertEquals("P5 - RUNNING\nP1 - WAITING\nP3 - WAITING\nQuantum: 3\nTick: 47", esca.getStatusEscalonador());
			
		}
		/*criar escalonador com prioridade
		 * adicionar processos sem prioridade 
		 * verificar se exceção foi lançada
		 * status -> nenhum processo
		 * 			 Tick = 1
		 * 			 Quantum = "default" = 3
		 */
		@Test
		void test33() {
			EscalonadorInterativo esca = new RoundRobinInterativo();
			Assertions.assertThrows(ProcessoInvalidoException.class, () -> {
				addNProcessos(1, esca, 0, 1);
			});
		}
		
		

}
