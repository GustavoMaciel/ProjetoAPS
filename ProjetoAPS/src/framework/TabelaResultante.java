package framework;

import java.util.HashMap;

public class TabelaResultante {
	private HashMap<Processo, Linha> linhas;
	
	public TabelaResultante() {
		linhas = new HashMap<>();
	}

	public String linhaProcesso(Processo processo) {
		return linhas.get(processo).toString();
	}

	public String resultado() {
		return this.toString();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
