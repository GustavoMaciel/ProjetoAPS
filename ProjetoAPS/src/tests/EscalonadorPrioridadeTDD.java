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
	 * 			 2 - P1 (EXECUTANDO)
	 *  	     3 - P1 (EXECUTANDO)
	 *  		 Tick = 3
	 *  		 4 - Nenhum processo
	 * 			 Tick = 4
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
	 * adicionar 2 processos com prioridade 1 em Tick 1
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 *  		 Tick = 1
	 *  		 Chamar Tick até estourar o Quantum 
	 * 			 Tick = 7
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
	/* O mesmo teste que T6
	 * criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1 em Tick 1
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 * 			 2 - P3 (ESPERANDO)
	 *  		 Tick = 1
	 *  		 Chamar Tick até estourar o Quantum 
	 * 			 Tick = 7
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
	/* O mesmo teste que T5
	 * criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1 em Tick 1
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Chamar Tick até 3, onde será criado P2 (A concorrência começa em Tick 3)
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Tick = 7
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
	/* criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Matar P1 no em Execução
	 *  		 Chamar Tick até 2, onde será criado P2 (A concorrência começa em Tick 2)
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Tick = 5
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test21() {//8
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 2, true);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());
		esca.finalizarProcesso("P1");
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
		assertEquals("P2 - RUNNING\nQuantum: 3\nTick: 5", esca.getStatusEscalonador());
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
	}
	/* criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1 e 2 
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Porcesso continua executando mesmo depois de estourar o Quantum
	 *  		 Matar P1 no em Execução
	 *  		 Chamar Tick até 5, onde será criado P2 (A concorrência começa em Tick 5)
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Tick = 9
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
	 * 			 Finaliza com quantum 9
	 */
	@Test
	void test23() {//10
		int Quantum = 4;
		EscalonadorInterativo esca = new EscalonadorPrioridade(Quantum);
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P1 - RUNNING\n"
				+    "P2 - WAITING\nQuantum: 4\nTick: 3", esca.getStatusEscalonador());
		
		
		rodaTickNVezes(esca, 3, true);
		
		assertEquals("P2 - RUNNING\n"
				+    "P1 - WAITING\nQuantum: 4\nTick: 6", esca.getStatusEscalonador());
	}
	/* criar escalonador com prioridade
	 * adicionar 2 processos com prioridade 1  
	 * status -> 1 - P1 (EXECUTANDO)
	 *  		 Tick = 1
	 *  		 Prorcesso Executa até estourar o quantum 
	 *  		 Intevalo de 2 ticks (depois de parar P1), até começar o P2
	 *  		 Chamar Tick no tempo 5, onde será criado P2 (A concorrência começa em Tick 5)
	 *  	     2 - P2 (EXECUTANDO) 
	 * 			 Tick = 9
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
	 *  		 Tick = 6
	 *  		 4 - P1 (ESPERANDO)  
	 * 			 Tick = 9
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test27() { //13
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
	 * os 3 Processos Bloqueiam  
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P1 (ESPERANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test28() { //14
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
		
	}
	@Test
	void test30() {
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
	// referencia a T28 do quadro
	/* criar escalonador com prioridade
	 * adicionar 1 processos com prioridade 1  
	 * 			 Roda 3 ticks
	 * adicionar 1 processos com prioridade 2
	 * status -> 1 - P1 (EXECUTANDO)
	 * 			 2 - P2 (ESPERANDO)
	 *  		 Tick = 13....
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test31() { // não ta funcionando
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
		@Test
		void test32() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 2);
			rodaTickNVezes(esca, 3, true);
			
			assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 2", esca.getStatusEscalonador());

			addNProcessos(1, esca, 1, 1);
			rodaTickNVezes(esca, 3, true);
			
		}
		
		@Test
		void test33() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 1);
			rodaTickNVezes(esca, 3, true);
			esca.bloquearProcesso("P1");
			addNProcessos(1, esca, 1, 2);
			rodaTickNVezes(esca, 1, true);
			esca.retomarProcesso("P1");
			rodaTickNVezes(esca, 9, true);
		}
		
		/* criar escalonador com prioridade
		 * adicionar 1 processos com prioridade 2  
		 * 			 Roda 1 tick
		 * adicionar 1 processos com prioridade 1
		 * status -> 1 - P2 (EXECUTANDO)
		 * 			 2 - P1 (ESPERANDO)
		 * 			 Finalizar P2
		 * 			 Quantum = "default" = 3
		 */
		@Test
		void test34() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 2);
			rodaTickNVezes(esca, 1, true);
			addNProcessos(1, esca, 1, 1);
			rodaTickNVezes(esca, 2, true);
			esca.finalizarProcesso("P2");
			rodaTickNVezes(esca, 1, true);
			
		}
		@Test
		void test35() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 3);
			
			rodaTickNVezes(esca, 2, true);
			
			addNProcessos(1, esca, 1, 1);
			
			rodaTickNVezes(esca, 1, true);
			esca.bloquearProcesso("P2");
			rodaTickNVezes(esca, 2, true);
			
			addNProcessos(1, esca, 2, 4);
			
			rodaTickNVezes(esca, 2, true);
			esca.bloquearProcesso("P1");
			rodaTickNVezes(esca, 1, true);
			
			esca.retomarProcesso("P2");
			addNProcessos(1, esca, 3, 1);
			
			rodaTickNVezes(esca, 1, true);
			esca.retomarProcesso("P1");
			rodaTickNVezes(esca, 1, true);
			
			addNProcessos(1, esca, 4, 2);
			addNProcessos(1, esca, 5, 2);
			rodaTickNVezes(esca, 11, true);
			esca.bloquearProcesso("P4");
			
			rodaTickNVezes(esca, 5, true);
			esca.retomarProcesso("P4");
			rodaTickNVezes(esca, 5, true);
			esca.finalizarProcesso("P4");
			rodaTickNVezes(esca, 2, true);
			esca.finalizarProcesso("P2");
			rodaTickNVezes(esca, 8, true);
			
			esca.bloquearProcesso("P5");
			
			rodaTickNVezes(esca, 5, true);
			esca.finalizarProcesso("P6");
			
			
			
		}
		/*criar escalonador com prioridade
		 * adicionar processos sem prioridade 
		 * verificar se exceção foi lançada
		 * status -> nenhum processo
		 * 			 Tick = 1
		 * 			 Quantum = "default" = 3
		 */
		@Test
		void test36() {
			EscalonadorInterativo esca = new RoundRobinInterativo();
			Assertions.assertThrows(ProcessoInvalidoException.class, () -> {
				addNProcessos(1, esca, 0, 1);
			});
		}
		
		

}
