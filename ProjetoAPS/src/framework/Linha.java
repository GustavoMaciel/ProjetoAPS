package framework;

import java.util.HashMap;

public class Linha {
	private String processoID;
	private HashMap<Integer, StatusProcesso> linhaProcesso;
	
	
	public Linha(Processo p) {
		processoID = p.getProcessoID();
		linhaProcesso = new HashMap<>();
	}
	
	public void addColuna(int tempo, StatusProcesso status) {
		this.linhaProcesso.put(tempo, status);
	}
	
	public StatusProcesso getStatusNoTempo(int tempo) {
		return this.linhaProcesso.get(tempo);
	}
	
	@Override
	public String toString() {
		return null;
	}
	
}
