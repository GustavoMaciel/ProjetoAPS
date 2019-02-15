package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import framework.EscalonadorRoundRobin;
import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;

class TDD {

	@Test
	void testRoundRobin() {
		
		/*
			P1 RF
			P2 WRRF
			P3 WWWRRRWWRRRF
			P4 NNWWWWRRF
		 */
		
		EscalonadorRoundRobin comp = new EscalonadorRoundRobin(3);
		comp.addProcesso("P1", 0, 1);
		comp.addProcesso("P2", 0, 2);
		comp.addProcesso("P3", 0, 6);
		comp.addProcesso("P4", 3, 2);
		
		TabelaResultante tabela = comp.rodar();
		assertEquals(StatusProcesso.Executando, comp.checarStatus("P1", 0));
		assertEquals("P1 RF\n", tabela.linhaProcesso("P1"));
		assertEquals("P1 RF\n" + 
				"P2 WRRF\n" + 
				"P3 WWWRRRWWRRRF\n" + 
				"P4 NNWWWWRRF\n", tabela.resultado());
		
	}
	
	@Test
	void testProcesso() {
		Processo pro = new Processo();
		pro.setPid("P1");
		pro.setTempoExec(3);
		pro.setTempoInicio(0);
		assertEquals("P1", pro.getPid());
		assertEquals(3, pro.getTempoExec());
		assertEquals(0, pro.getTempoInicio());
	}

}
	