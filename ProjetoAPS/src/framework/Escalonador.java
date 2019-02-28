package framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Escalonador {
	
	protected ArrayList<Processo> fila = new ArrayList<Processo>();

	public ArrayList<Processo> getFila() {
		return fila;
	}

	public void setFila(ArrayList<Processo> fila) {
		this.fila = fila;
	}

	public Processo addProcesso(String pid, int tempoInicio, int tempoExec) {
		Processo processo = criarProcesso(pid, tempoInicio, tempoExec);
		fila.add(processo);
		return processo;
	}

	public Processo criarProcesso(String pid, int tempoInicio, int tempoExec) {
		return new Processo(pid, tempoInicio, tempoExec);
	}

	public StatusProcesso checarStatus(String pid, int tempo) {
		// TODO Auto-generated method stub
		return null;
	}

	public TabelaResultante rodar() {
		//TODO ON SUBCLASS
		return null;
	}
	
	protected void odenarFila() {
		Collections.sort(this.fila, new Comparator<Processo>() {
			public int compare(Processo processo1, Processo processo2) {
				Processo p1 = processo1;
				Processo p2 = processo2;
				return p1.getTempoInicio() < p2.getTempoInicio() ? -1 : (p1.getTempoInicio() > p2.getTempoInicio() ? +1 : 0);
			}
		});
	}
	
}
