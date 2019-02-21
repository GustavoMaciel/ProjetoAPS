package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import framework.EscalonadorRoundRobin;
import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;

class TDD {

	// Walderlindo
	/* Quantum 3 com 4 processos
	 * P1 (0, 01) -> RFFFFFFFFFFF
	 * P2 (0, 02) -> WRRFFFFFFFFF
	 * P3 (0, 06) -> WWWRRRWWRRRF
	 * P4 (2, 02) -> NNWWWWRRFFFF
	 */
	@Test
	void testRoundRobinQuantum3com4processos() {
				
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 1);
		Processo p2 = escalonador.addProcesso("P2", 0, 2);
		Processo p3 = escalonador.addProcesso("P3", 0, 6);
		Processo p4 = escalonador.addProcesso("P4", 3, 2);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P2", 2));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P3", 4));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P4", 7));
		assertEquals("P1 RFFFFFFFFFFF", tabela.linhaProcesso(p1));
		assertEquals("P1 WRRFFFFFFFFF\n" + 
				     "P2 WRRFFFFFFFFF\n" + 
				     "P3 WWWRRRWWRRRF\n" + 
				     "P4 NNWWWWRRFFFF\n", tabela.resultado());
		}
	
	/* Um processo apenas - Quantum de 4
	 * P1 (0,10) -> P01 RRRRRRRRRRF
	 */
	
	@Test
	void testRoundRobinComUmProcessoApenas() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(4);
		assertEquals(4, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 10);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 5));
		assertEquals("P1 RRRRRRRRRRF", tabela.linhaProcesso(p1));
		assertEquals("P1 RRRRRRRRRRF\n", tabela.resultado());
		
		}
	
	/* Dois processos, um seguido do outro, Quantum 3
	 * P1 (0,03) -> P01 RRRFFFF
	 * P2 (3,03) -> P02 NNNRRRF
	 */
	@Test
	void testRoundRobinComDoisProcessosSeguidos() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 3);
		Processo p2 = escalonador.addProcesso("P1", 3, 3);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 4));
		assertEquals("P1 RRRFFFF", tabela.linhaProcesso(p1));
		assertEquals("P1 RRRFFFF\n" + 
			         "P2 NNNRRRF\n", tabela.resultado());
		}
	
	/* Dois processos ao mesmo tempo e tamanho, Quantum 3
	 * P1 (0,03) -> P01 RRRFFFF
	 * P2 (0,03) -> P02 WWWRRRF
	 */
	@Test
	void testRoundRobinComDoisProcessosAoMesmoTempo() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 3);
		Processo p2 = escalonador.addProcesso("P1", 0, 3);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 4));
		assertEquals("P1 RRRFFFF", tabela.linhaProcesso(p1));
		assertEquals("P1 RRRFFFF\n" + 
			         "P2 WWWRRRF\n", tabela.resultado());
		}
	
	/* Quantum 3, dois procesos com intervalo sem processos rodando
	 * P1 (0, 03) -> RRRFFFFF
	 * P2 (5, 02) -> NNNNNRRF
	 */
	@Test
	void testRoundRobinComDoisProcessosComIntervaloSemProcessosRodando() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 3);
		Processo p2 = escalonador.addProcesso("P1", 5, 2);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 6));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 7)); 
		//ter certeza se o processo ta realmente rodando no tempo seguinte
		assertEquals("P1 RRRFFFFF", tabela.linhaProcesso(p1));
		assertEquals("P1 RRRFFFFF\n" + 
			         "P2 NNNNNRRF\n", tabela.resultado());
		}
	/* Processo que não existe na lista
	 * 
	 */
	//@Test
	//void testRoundRobinComDoisProcessosComIntervaloSemProcessosRodando() {
		//EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		//assertEquals(3, escalonador.getQuantum());
		
		//}
	// Herculano
	/* Sem Processos
	 * Fila vazia
	 */
	
	/* Processo P2 inicia primeiro, ambos demoram para iniciar
	 * P1 (5, 03) -> NNNNNWWRRRFF
	 * P2 (4, 04) -> NNNNRRRWWWRF
	 */
	
	/* Quantum 3, processo p2 n�o chegou mesmo finalizando o quantum de p1
	 * P1 (0, 04) -> RRRRFFFF
	 * P2 (4, 03) -> IIIIRRRF
	 */
	
	/* Quantum 3 com 3 processos
	 * P1 (3, 07) -> NNNWRRRRRRRF
	 * P2 (0, 01) -> RFFFFFFFFFFF
	 * P3 (1, 03) -> NRRRFFFFFFFF
	 */
	
	
	/*Quantum 0
	 * P1 (0, 02) -> Erro quantum menor que 1
	 * P2 (0, 03)
	 */
	
	// Gustavson
	/* Quantum menor que 0
	 * P1 (0, 02) -> Erro quantum menor que 1
	 */
	
	/* Quantum 5
	 * P1 (1, 04) -> IWWWWRRRRFF
	 * P2 (0, 06) -> RRRRRWWWWRF
	 */
	
	/* Quantum 10 e processos com tempo necess�rio menor que o quantum
	 *  P1 (0, 02) -> RRFFFFFF
	 *  P2 (1, 01) -> IWWRFFFF
	 *  P3 (0, 01) -> WWRFFFFF
	 *  P4 (2, 03) -> IIWWRRRF
	 */
	
	/* Processo finalizado no meio da execu��o, quantum 4
	 * P1 (0, 05) -> RRRXF
	 * P2 (0, 01) -> WWWRF
	 */
	
	@Test
	void testRoundRobin() {
		
		
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 1);
		Processo p2 = escalonador.addProcesso("P2", 0, 2);
		Processo p3 = escalonador.addProcesso("P3", 0, 6);
		Processo p4 = escalonador.addProcesso("P4", 3, 2);
		
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals("P1 RF\n", tabela.linhaProcesso(p1));
		assertEquals("P1 RF\n" + 
				"P2 WRRF\n" + 
				"P3 WWWRRRWWRRRF\n" + 
				"P4 NNWWWWRRF\n", tabela.resultado());
		
	}
	
	@Test
	void testProcesso() {
		Processo pro = new Processo();
		pro.setProcessoID("P1");
		pro.setTempoExec(3);
		pro.setTempoInicio(0);
		assertEquals("P1", pro.getProcessoID());
		assertEquals(3, pro.getTempoExec());
		assertEquals(0, pro.getTempoInicio());
	}

}
	