package it.uniba.ontology;

import jade.content.Predicate;

public class Prenotazione implements Predicate{
	
	private static final long serialVersionUID = 8804926995898819340L;
	
	private Cliente cliente = null;
	private String prestazione;
	
	public Prenotazione(){ }
	
	public Prenotazione(Cliente cliente, String prestazione) {
		super();
		this.cliente = cliente;
		this.prestazione = prestazione;
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
	
	
	
	

}
