package interativo;
import framework.StatusProcesso;

public class ProcessoInterativo {
	private String processoID;
	private StatusProcesso status;
	private int tempoChegada;
	private int prioridade;
	
	public ProcessoInterativo() throws Exception{
		throw new Exception("Usa a API direito jovem desenvolvedor(a)");
	}
	
	public ProcessoInterativo(String processoID, StatusProcesso status, int tempoChegada) {
		this.processoID = processoID;
		this.status = status;
		this.tempoChegada = tempoChegada;
	}
	public ProcessoInterativo(String processoID, StatusProcesso status, int tempoChegada, int prioridade) {
		this(processoID, status, tempoChegada);
		this.prioridade = prioridade;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
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
	
	@Override
	public boolean equals(Object received) {
		ProcessoInterativo obj = (ProcessoInterativo) received;
		if(this.getProcessoID().equals(obj.getProcessoID())) {
			if(this.getStatus().equals(obj.getStatus())) {
				if(this.getTempoChegada() == obj.getTempoChegada()) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}