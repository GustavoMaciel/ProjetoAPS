package framework;

import java.util.HashMap;

public class TabelaResultante {
private HashMap<String, Linha> linhas;
	
	public TabelaResultante() {
		linhas = new HashMap<>();
	}

	public String linhaProcesso(Processo processo) {
		return linhas.get(processo.getProcessoID()).toString();
	}
	
	public void addStatus(String processoID, StatusProcesso status, int tempoAtual) {
		this.linhas.get(processoID).addColuna(tempoAtual, status);
	}
	
	public HashMap<String, Linha> getLinhas() {
		return linhas;
	}

	public void setLinhas(HashMap<String, Linha> linhas) {
		this.linhas = linhas;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addLinha(String processoID, Linha linha) {
		this.linhas.put(processoID, linha);
		
	}
	
	public String resultado() {
		String resultado = "";
		for(Linha i: this.linhas.values()) {
			resultado += i.toString();
		}
		return resultado;
	}

	public StatusProcesso checarStatus(String processoID, int tempo) {
		StatusProcesso status = this.linhas.get(processoID).getStatusNoTempo(tempo);
		return status;
	}

}
