package framework;

import java.util.ArrayList;

public abstract class Escalonador {
	
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

	public abstract TabelaResultante rodar();
	
}
