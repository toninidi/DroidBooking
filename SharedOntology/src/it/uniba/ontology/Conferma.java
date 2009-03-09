package it.uniba.ontology;

import java.util.Date;

import jade.content.Predicate;

public class Conferma implements Predicate {
	
	private static final long serialVersionUID = 8290247289511129107L;
	
	private CentroPrenotazione centro = null;
	private String prestazione;
	private Date inizioPrenotazione;
	private Date finePrenotazione;
	private int id_prenotazione;
	
	public Conferma() {}

	public Conferma(CentroPrenotazione centro, String prestazione,
			Date inizioPrenotazione, Date finePrenotazione, int id_prenotazione) {
		super();
		this.centro = centro;
		this.prestazione = prestazione;
		this.inizioPrenotazione = inizioPrenotazione;
		this.finePrenotazione = finePrenotazione;
		this.id_prenotazione = id_prenotazione;
	}

	public CentroPrenotazione getCentro() {
		return centro;
	}

	public void setCentro(CentroPrenotazione centro) {
		this.centro = centro;
	}
	
	public String getPrestazione() {
		return prestazione;
	}

	public void setPrestazione(String prestazione) {
		this.prestazione = prestazione;
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

	public long getId_prenotazione() {
		return id_prenotazione;
	}

	public void setId_prenotazione(int id_prenotazione) {
		this.id_prenotazione = id_prenotazione;
	}
}
