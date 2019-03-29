package interativo;
import framework.StatusProcesso;

public class ProcessoInterativo {
	private String processoID;
	private StatusProcesso status;
	private int tempoChegada;
	
	public ProcessoInterativo(String processoID, StatusProcesso status, int tempoChegada) {
		this.processoID = processoID;
		this.status = status;
		this.tempoChegada = tempoChegada;
	}

	public int getTempoChegada() {
		return tempoChegada;
	}

	public void setTempoChegada(int tempoChegada) {
		this.tempoChegada = tempoChegada;
	}

	public String getProcessoID() {
		// TODO Auto-generated method stub
		return this.processoID;
	}
	public StatusProcesso getStatus() {
		return status;
	}

	public void setProcessoID(String processoID) {
		this.processoID = processoID;
	}

	public void setStatus(StatusProcesso status) {
		this.status = status;
	}
	
	
}
