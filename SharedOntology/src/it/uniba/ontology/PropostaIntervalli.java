package it.uniba.ontology;

import jade.content.Predicate;
import jade.util.leap.List;

public class PropostaIntervalli implements Predicate {
	
	private static final long serialVersionUID = -7933945238245662424L;
	
	private List intervalli;
	private CentroPrenotazione centro;
	
	public PropostaIntervalli() {}

	public PropostaIntervalli(List intervalli, CentroPrenotazione centro) {
		super();
		this.intervalli = intervalli;
		this.centro = centro;
	}

	public List getIntervalli() {
		return intervalli;
	}

	public void setIntervalli(List intervalli) {
		this.intervalli = intervalli;
	}

	public CentroPrenotazione getCentro() {
		return centro;
	}

	public void setCentro(CentroPrenotazione centro) {
		this.centro = centro;
	}
}
