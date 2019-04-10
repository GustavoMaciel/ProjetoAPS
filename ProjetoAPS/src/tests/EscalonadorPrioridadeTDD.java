package tests;
import interativo.*;
import framework.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		cenarioNProcessos(1);
		rodaTickNVezes(esca, 1, true);
		assertEquals("Nenhum Processo\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
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
		rodaTickNVezes(esca, 1, true);
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
		rodaTickNVezes(esca, 3, true);
		esca.finalizarProcesso("P1");
		rodaTickNVezes(esca, 1, true);
		assertEquals("P1 - RUNNING\nQuantum: 3\nTick: 1"
			    	+"P1 - RUNNING\nQuantum: 3\nTick: 2"
			    	+"P1 - RUNNING\nQuantum: 3\nTick: 3"
			    	+"Nenhum Processo\nQuantum: 3\nTick: 4", esca.getStatusEscalonador());
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
	void test18() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 6, true);
		esca.finalizarProcesso("P1");
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
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
	void test19() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		rodaTickNVezes(esca, 9, true);
		esca.finalizarProcesso("P1");
		esca.finalizarProcesso("P2");
		esca.finalizarProcesso("P3");
		rodaTickNVezes(esca, 1, true);
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
	void test20() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
		esca.finalizarProcesso("P1");
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
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
	void test21() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 2, true);
		esca.finalizarProcesso("P1");
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
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
	void test22() {
		EscalonadorInterativo esca = new EscalonadorPrioridade(); //aqui da erro
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 5, true);
		esca.finalizarProcesso("P1");
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
		
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
	void test23() {
		int Quantum = 4;
		EscalonadorInterativo esca = new EscalonadorPrioridade(Quantum);
		addNProcessos(2, esca, 0, 1);
		rodaTickNVezes(esca, 8, true);
		esca.finalizarProcesso("P1");
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
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
	void test24() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 6, true);
		esca.finalizarProcesso("P1");// não ta funcionando
		rodaTickNVezes(esca, 2, true);
		addNProcessos(1, esca, 1, 1);
		rodaTickNVezes(esca, 3, true);
		esca.finalizarProcesso("P2");
		rodaTickNVezes(esca, 1, true);
		
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
	void test25() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 6, true);
		
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
	void test27() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		rodaTickNVezes(esca, 6, true);
		esca.retomarProcesso("P1");
		rodaTickNVezes(esca, 3, true);
		
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
	void test28() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		esca.bloquearProcesso("P2");
		esca.bloquearProcesso("P3");									
		rodaTickNVezes(esca, 1, true);
		esca.retomarProcesso("P2");
		esca.retomarProcesso("P1");
		esca.retomarProcesso("P3");
		rodaTickNVezes(esca, 9, true);
		
	}
	/* criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1
	 * Os três começam bloqueados  
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P3 (ESPERANDO)
	 * 			 2 - P1 (ESPERANDO)
	 *  		 Tick = 1
	 *  		 Quantum roda apenas entre P2 e P3
	 *  		 P1 é Retomado
	 *  		 Tick = 3
	 *  		 4 - P1 (ESPERANDO)  
	 * 			 Tick = 9
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test29() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		esca.bloquearProcesso("P2");
		esca.bloquearProcesso("P3");
		rodaTickNVezes(esca, 1, true);
		esca.retomarProcesso("P2");
		esca.retomarProcesso("P3");
		esca.retomarProcesso("P1");
		rodaTickNVezes(esca, 6, true);
		
	}
	/* criar escalonador com prioridade
	 * adicionar 3 processos com prioridade 1  
	 * os 3 começam bloqueados
	 * status -> 1 - P2 (EXECUTANDO)
	 * 			 2 - P1 (ESPERANDO)
	 * 			 2 - P3 (ESPERANDO)
	 *  		 Tick = 1
	 *  		 Quantum roda apenas entre P2 e P3
	 *  		 P1 é Retomado
	 *  		 Tick = 3
	 *  		 4 - P1 (ESPERANDO)  
	 * 			 Tick = 9
	 * 			 Quantum = "default" = 3
	 */
	@Test
	void test30() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(3, esca, 0, 1);
		esca.bloquearProcesso("P1");
		esca.bloquearProcesso("P2");
		esca.bloquearProcesso("P3");
		rodaTickNVezes(esca, 1, true);
		esca.retomarProcesso("P2");
		esca.retomarProcesso("P1");	
		esca.retomarProcesso("P3");
		rodaTickNVezes(esca, 6, true);
		
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
	void test31() {
		EscalonadorInterativo esca = new EscalonadorPrioridade();
		addNProcessos(1, esca, 0, 1);
		rodaTickNVezes(esca, 3, true);
		addNProcessos(1, esca, 1, 2);
		rodaTickNVezes(esca, 10, true);
	}
	// referencia a T28 do quadro
		/* criar escalonador com prioridade
		 * adicionar 1 processos com prioridade 1  
		 * 			 Roda 3 ticks
		 * 			 Bloquear P1
		 * adicionar 1 processos com prioridade 2
		 * status -> 1 - P1 (EXECUTANDO)
		 * 			 2 - P2 (ESPERANDO)
		 *  		 Tick = 13....
		 * 			 Quantum = "default" = 3
		 */
		@Test
		void test32() {
			EscalonadorInterativo esca = new EscalonadorPrioridade();
			addNProcessos(1, esca, 0, 1);
			rodaTickNVezes(esca, 3, true);
			esca.bloquearProcesso("P1");
			addNProcessos(1, esca, 1, 2);
			rodaTickNVezes(esca, 10, true);
		}
		
		/* criar escalonador com prioridade
		 * adicionar 1 processos com prioridade 1  
		 * 			 Roda 3 ticks
		 * 			 Bloqueia P1
		 * adicionar 1 processos com prioridade 2
		 * status -> 1 - P1 (EXECUTANDO)
		 * 			 2 - P2 (ESPERANDO)
		 * 			 Retomar P1
		 *  		 Tick = 13....
		 * 			 Quantum = "default" = 3
		 */
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
			addNProcessos(1, esca, 0, 1);
			rodaTickNVezes(esca, 1, true);
			assertEquals("Nenhum Processo\nQuantum: 3\nTick: 1", esca.getStatusEscalonador());
		}
		
		

}
