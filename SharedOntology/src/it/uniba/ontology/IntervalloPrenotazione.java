package it.uniba.ontology;

import java.util.Date;

import jade.content.Concept;


public class IntervalloPrenotazione implements Concept {
	
	private static final long serialVersionUID = -3900227226439567963L;
	
	private Date inizioPrenotazione;
	private Date finePrenotazione;
	
	public IntervalloPrenotazione()  {}
	
	public IntervalloPrenotazione(Date inizioPrenotazione, Date finePrenotazione) {
		super();
		this.inizioPrenotazione = inizioPrenotazione;
		this.finePrenotazione = finePrenotazione;
	}

	public Date getInizioPrenotazione() {
		return inizioPrenotazione;
	}

	public void setInizioPrenotazione(Date inizioPrenotazione) {
		this.inizioPrenotazione = inizioPrenotazione;
	}

	public Date getFinePrenotazione() {
		return finePrenotazione;
	}

	public void setFinePrenotazione(Date finePrenotazione) {
		this.finePrenotazione = finePrenotazione;
	}
}
