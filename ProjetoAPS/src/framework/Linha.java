package framework;

import java.util.HashMap;

public class Linha {
	private String processoID;
	private HashMap<Integer, StatusProcesso> colunasProcesso;
	
	
	public Linha(Processo p) {
		processoID = p.getProcessoID();
		colunasProcesso = new HashMap<>();
	}
	
	public void addColuna(int tempo, StatusProcesso status) {
		this.colunasProcesso.put(tempo, status);
	}
	
	public StatusProcesso getStatusNoTempo(int tempo) {
		return this.colunasProcesso.get(tempo);
	}
	
	@Override
	public String toString() {
		String linha = this.processoID + " ";
		for(StatusProcesso i: this.colunasProcesso.values()) {
			linha += i.toString().charAt(0);
		}
		linha += "\n";
		return linha;
	}
	
}
