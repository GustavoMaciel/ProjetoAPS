package tests;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import exceptions.QuantumMenorQueUmException;
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
	
	/* Processo P2 inicia primeiro, ambos demoram para iniciar
	 * P1 (5, 03) -> NNNNNWWRRRFF
	 * P2 (4, 04) -> NNNNRRRWWWRF
	 */
	
	/* Quantum 3, processo p2 não chegou mesmo finalizando o quantum de p1
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
	@Test
	void roundRobinQuantum1Negativo() {
		EscalonadorRoundRobin esca = new EscalonadorRoundRobin(-1);
		assertTrue(esca.getQuantum()>=1);
	}
	
	/* Quantum 5
	 * P1 (1, 04) -> IWWWWRRRRFF
	 * P2 (0, 06) -> RRRRRWWWWRF
	 */
	@Test
	void roundRobinQuantum5() {
		EscalonadorRoundRobin esca = new EscalonadorRoundRobin(8);
		assertEquals(8, esca.getQuantum());
		Processo p1 = esca.addProcesso("P1", 1, 4);
		Processo p2 = esca.addProcesso("P2", 0, 6);
		TabelaResultante res = esca.rodar();
		assertEquals("P1 IWWWWRRRRFF\n"
				+ "P2 RRRRRWWWWRF\n", res.resultado());
	}
	
	
	/* Quantum 10 e processos com tempo necessário menor que o quantum
	 *  P1 (0, 02) -> RRFFFFFF
	 *  P2 (1, 01) -> IWWRFFFF
	 *  P3 (0, 01) -> WWRFFFFF
	 *  P4 (2, 03) -> IIWWRRRF
	 */
	@Test
	void roundRobinProcessosTempoMenorQueQuantum10() {
		EscalonadorRoundRobin esca = new EscalonadorRoundRobin(10);
		assertEquals(8, esca.getQuantum());
		Processo p1 = esca.addProcesso("P1", 0, 9);
		Processo p2 = esca.addProcesso("P2", 1, 6);
		Processo p3 = esca.addProcesso("P3", 0, 9);
		Processo p4 = esca.addProcesso("P4", 2, 6);
		TabelaResultante res = esca.rodar();
		assertEquals("P1 RRFFFFFF\n"
				+ "P2 IWWRFFFF\n"
				+ "P3 WWRFFFFF\n"
				+ "P4 IIWWRRRF\n", res.resultado());
	}
	
	/* Quantum 8, processos chegando depois de algum tempo
	 * P1 (4, 09) -> IIIIRRRRRRRRWWWWWWRF
	 * P2 (5, 06) -> IIIIIWWWWWWWRRRRRRFF
	 */
	
	@Test
	void roundRobinProcessosChegandoTardeQuantum8() {
		EscalonadorRoundRobin esca = new EscalonadorRoundRobin(8);
		assertEquals(8, esca.getQuantum());
		Processo p1 = esca.addProcesso("P1", 4, 9);
		Processo p2 = esca.addProcesso("P2", 5, 6);
		TabelaResultante res = esca.rodar();
		assertEquals("P1 IIIIRRRRRRRRWWWWWWRF\n"
				+ "P2 IIIIIWWWWWWWRRRRRRFF\n", res.resultado());
	}
	
	/* Processo finalizado no meio da execução, quantum 4
	 * P1 (0, 05) -> RRRXF
	 * P2 (0, 01) -> WWWRF
	 */
	
	
	/* Quantum 3 cenário comum
	 * P1 (0, 01) -> RFFFFFFFFFFF
	 * P2 (0, 02) -> WRRFFFFFFFFF
	 * P3 (0, 06) -> WWWRRRWWRRRF
	 * P4 (3, 02) -> IIIWWWRRFFFF
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
		assertEquals("P1 RFFFFFFFFFFF\n", tabela.linhaProcesso(p1));
		assertEquals("P1 RFFFFFFFFFFF\n" + 
				"P2 WRRFFFFFFFFF\n" + 
				"P3 WWWRRRWWRRRF\n" + 
				"P4 IIIWWWRRFFFF\n", tabela.resultado());
		
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
	