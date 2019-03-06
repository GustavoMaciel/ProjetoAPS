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
		escalonador.addProcesso("P5", 3, 2);
		escalonador.addProcesso("P6", 3, 2);
		escalonador.addProcesso("P7", 3, 2);
		escalonador.addProcesso("P8", 3, 2);
		escalonador.addProcesso("P9", 3, 2);
		escalonador.addProcesso("P10", 3, 2);
		escalonador.addProcesso("P11", 3, 2);
		escalonador.addProcesso("P12", 3, 2);
		escalonador.addProcesso("P13", 0, 2);

		
		ArrayList<Processo> fila = new ArrayList<Processo>();
		for(Processo p : escalonador.getFila()) {
			//(String processoID, int tempoInicio, int tempoExec)
			fila.add(new Processo(p.getProcessoID(), p.getTempoInicio(), p.getTempoExec()));
		}
		
		System.out.println("fila" + fila.size());
		TabelaResultante tabela = escalonador.rodar();
		System.out.println("fila" + fila.size());
		
		EscalonadorGUI gui = new EscalonadorGUI();
		
		
		gui.addResultado(tabela);
		gui.setFilaProcessos(fila);
		gui.run(null);
		
		
		
	}

}
