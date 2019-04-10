package interativo;

import java.util.Collections;
import java.util.Comparator;


import framework.StatusProcesso;

public class EscalonadorPrioridade extends RoundRobinInterativo{
	
	public EscalonadorPrioridade() {
		super();
	}
	
	public EscalonadorPrioridade(int quantum) {
		super(quantum);
	}
	//Adiciona aqui Neném