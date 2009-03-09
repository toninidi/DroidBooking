package it.uniba.ontology;


import jade.content.Concept;

public class Cliente implements Concept {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String cognome;
	private String telefono;	
	
	
	
	public Cliente() {
	}


	public Cliente(String nome, String cognome, String telefono) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
	}



	public String getNomeCliente() {
		return nome;
	}



	public String getCognomeCliente() {
		return cognome;
	}



	public String getTelefonoCliente() {
		return telefono;
	}



	

	public void setNomeCliente(String nome) {
		this.nome = nome;
	}



	public void setCognomeCliente(String cognome) {
		this.cognome = cognome;
	}



	public void setTelefonoCliente(String telefono) {
		this.telefono = telefono;
	}


}
