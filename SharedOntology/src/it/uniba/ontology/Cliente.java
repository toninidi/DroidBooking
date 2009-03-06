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
	private Date giornoPrenotazione;
	private Date oraPrenotazione;
	
	
	public Cliente(String nome, String cognome, String telefono,
			String prestazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.prestazione = prestazione;
	}
	
	

	public Cliente(String nome, String cognome, String telefono,
			String prestazione, Date giornoPrenotazione, Date oraPrenotazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.prestazione = prestazione;
		this.giornoPrenotazione = giornoPrenotazione;
		this.oraPrenotazione = oraPrenotazione;
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
		return giornoPrenotazione;
	}



	public Date getOraPrenotazione() {
		return oraPrenotazione;
	}
	
	

	
	

}
