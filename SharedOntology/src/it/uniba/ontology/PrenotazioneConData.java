package it.uniba.ontology;

import jade.content.Predicate;

import java.util.Date;

public class PrenotazioneConData implements Predicate {
	
	private static final long serialVersionUID = -7135660586282873456L;
	
	private Cliente cliente = null;
	private String prestazione;
	private Date dataPrenotazione;
	
	public PrenotazioneConData(){ }
	
	public PrenotazioneConData(Cliente cliente, String prestazione, Date dataPrenotazione) {
		super();
		this.cliente = cliente;
		this.prestazione = prestazione;
		this.dataPrenotazione = dataPrenotazione;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public String getPrestazione() {
		return prestazione;
	}
	
	public void setPrestazione(String prestazione) {
		this.prestazione = prestazione;
	}
	
	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public void setDataPrenotazione(Date giornoPrenotazione) {
		this.dataPrenotazione = giornoPrenotazione;
	}
	
	
	
	
	

}
