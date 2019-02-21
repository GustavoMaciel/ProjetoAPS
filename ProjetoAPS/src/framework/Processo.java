package framework;

public class Processo {
	private String processoID;
	private int tempoInicio;
	private int tempoExec;

	public Processo() {
		// TODO Auto-generated constructor stub
	}
	
	public Processo(String processoID, int tempoInicio, int tempoExec) {
		this.processoID = processoID;
		this.tempoInicio = tempoInicio;
		this.tempoExec = tempoExec;
	}

	public String getProcessoID() {
		return processoID;
	}

	public void setProcessoID(String processoID) {
		this.processoID = processoID;
	}

	public int getTempoInicio() {
		return tempoInicio;
	}

	public void setTempoInicio(int tempoInicio) {
		this.tempoInicio = tempoInicio;
	}

	public int getTempoExec() {
		return tempoExec;
	}

	public void setTempoExec(int tempoExec) {
		this.tempoExec = tempoExec;
	}
	

}
