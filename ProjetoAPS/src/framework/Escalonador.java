package framework;

import java.util.Queue;

public abstract class Escalonador {
	
	private Queue<Processo> fila;

	public Escalonador() {
		// TODO Auto-generated constructor stub
	}
	

	public Queue<Processo> getFila() {
		return fila;
	}

	public void setFila(Queue<Processo> fila) {
		this.fila = fila;
	}

	public void addProcesso(String pid, int tempoInicio, int tempoExec) {
		fila.add(criarProcesso(pid, tempoInicio, tempoExec));
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
