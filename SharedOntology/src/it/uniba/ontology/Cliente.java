package it.uniba.ontology;


import java.util.Date;

import jade.content.Concept;

public class Cliente implements Concept {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String cognome;
	private String telefono;
	private String prestazione;
	private Date dataPrenotazione;
	
	
	
	public Cliente() {
	}



	public Cliente(String nome, String cognome, String telefono,
			String prestazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.prestazione = prestazione;
	}
	
	

	public Cliente(String nome, String cognome, String telefono,
			String prestazione, Date dataPrenotazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.prestazione = prestazione;
		this.dataPrenotazione = dataPrenotazione;
	}



	public String getNome() {
		return nome;
	}



	public String getCognome() {
		return cognome;
	}



	public String getTelefono() {
		return telefono;
	}



	public String getPrestazione() {
		return prestazione;
	}



	public Date getGiornoPrenotazione() {
		return dataPrenotazione;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public void setPrestazione(String prestazione) {
		this.prestazione = prestazione;
	}



	public void setGiornoPrenotazione(Date giornoPrenotazione) {
		this.dataPrenotazione = giornoPrenotazione;
	}
}
