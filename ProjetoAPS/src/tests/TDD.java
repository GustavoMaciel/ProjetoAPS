package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import framework.EscalonadorRoundRobin;
import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;

class TDD {
	
//	@Before
//	public void inicializarEscalonadorRoudRobinComQuantum3(){
//		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin();
//		escalonador.setQuantum(3);
//	}
	

	// Walderlindo
	/* Quantum 3 com 4 processos
	 * P1 (0, 01) -> RFFFFFFFFFFF
	 * P2 (0, 02) -> WRRFFFFFFFFF
	 * P3 (0, 06) -> WWWRRRWWRRRF
	 * P4 (2, 02) -> NNWWWWRRFFFF
	 */
	
	/* Um processo apenas
	 * P1 (0,10) -> P01 RRRRRRRRRRF
	 */
	
	/* Dois processos, um seguido do outro, Quantum 3
	 * P1 (0,03) -> P01 RRRFFFF
	 * P2 (3,03) -> P02 NNNRRRF
	 */
	
	/* Dois processos ao mesmo tempo e tamanho, Quantum 3
	 * P1 (0,03) -> P01 RRRFFFF
	 * P2 (0,03) -> P02 WWWRRRF
	 */
	
	/* Quantum 3, dois procesos com intervalo sem processos rodando
	 * P1 (0, 03) -> RRRFFFFF
	 * P2 (5, 02) -> NNNNNRRF
	 */
	
	// Herculano
	/* Sem Processos
	 * Fila vazia
	 */
	@Test
	public void escalonadorSemProcessos() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin();
		assertEquals(0, escalonador.getFila().size());
	}
	
	/* Processo P2 inicia primeiro, ambos demoram para iniciar
	 * P1 (5, 03) -> NNNNNWWRRRFF
	 * P2 (4, 04) -> NNNNRRRWWWRF
	 */
	@Test
	public void processosDemoramMuitoPraChegar() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin();
		escalonador.addProcesso("P1",5, 03);
		escalonador.addProcesso("P2",4, 04);

		assertEquals(StatusProcesso.NaoExistente, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.NaoExistente, escalonador.checarStatus("P1", 4));

		assertEquals(StatusProcesso.NaoExistente, escalonador.checarStatus("P2", 0));
		assertEquals(StatusProcesso.NaoExistente, escalonador.checarStatus("P2", 3));
	}
	
	/* Quantum 3, processo p2 n�o chegou mesmo finalizando o quantum de p1
	 * P1 (0, 04) -> RRRRFFFF
	 * P2 (4, 03) -> IIIIRRRF
	 */
	@Test
	public void processoP2ChegaMuitoDepoisDoP1Terminar() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin();
		escalonador.setQuantum(3);
		
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P1", 0));
		assertEquals(StatusProcesso.NaoExistente, escalonador.checarStatus("P2", 0));
		assertEquals(StatusProcesso.Executando, escalonador.checarStatus("P2", 4));
		assertEquals(StatusProcesso.Finalizados, escalonador.checarStatus("P1", 4));
		
	}
	
	
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
		
		System.out.println();
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
	