package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import framework.EscalonadorRoundRobin;
import framework.Processo;
import framework.StatusProcesso;
import framework.TabelaResultante;
import gui.EscalonadorGUI;

class EscalonadorGUITest {

	@Test
	void executarInterfaceGrafica() {
		EscalonadorRoundRobin escalonador = new EscalonadorRoundRobin(3);
		assertEquals(3, escalonador.getQuantum());
		Processo p1 = escalonador.addProcesso("P1", 0, 1);
		Processo p2 = escalonador.addProcesso("P2", 0, 2);
		Processo p3 = escalonador.addProcesso("P3", 0, 6);
		Processo p4 = escalonador.addProcesso("P4", 3, 2);

		
		ArrayList<Processo> fila = new ArrayList<Processo>();
		for(Processo p : escalonador.getFila()) {
			//(String processoID, int tempoInicio, int tempoExec)
			fila.add(new Processo(p.getProcessoID(), p.getTempoInicio(), p.getTempoExec()));
		}
		
		System.out.println("fila" + fila.size());
		TabelaResultante tabela = escalonador.rodar();
		assertEquals(StatusProcesso.RUNNING, tabela.checarStatus("P1", 0));
		assertEquals(StatusProcesso.RUNNING, tabela.checarStatus("P2", 2));
		assertEquals(StatusProcesso.RUNNING, tabela.checarStatus("P3", 4));
		assertEquals(StatusProcesso.RUNNING, tabela.checarStatus("P4", 7));
		System.out.println("fila" + fila.size());
		assertEquals("P1 RF\n", tabela.linhaProcesso(p1));

		assertEquals("P1 RF\n" 
				+ "P2 WRRF\n" 
				+ "P3 WWWRRRWWRRRF\n" 
				+ "P4 NNNWWWRRF\n", tabela.resultado());
		
		EscalonadorGUI gui = new EscalonadorGUI();
		
		
		gui.addResultado(tabela);
		gui.setFilaProcessos(fila);
		gui.run(null);
		
		
		
	}

}
