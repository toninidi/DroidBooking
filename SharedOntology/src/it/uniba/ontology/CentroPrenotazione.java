package it.uniba.ontology;


import jade.content.Concept;
import jade.util.leap.List;

public class CentroPrenotazione implements Concept {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2321393353586000579L;
	
	private String nome;
	private String telefono;
	private String via;
	private List prestazioni;
	
	
	
	public CentroPrenotazione(String nome, String telefono, String via) {
		super();
		this.nome = nome;
		this.telefono = telefono;
		this.via = via;
	}
	
	public CentroPrenotazione(String nome, String telefono, String via,
			List prestazioni) {
		super();
		this.nome = nome;
		this.telefono = telefono;
		this.via = via;
		this.prestazioni = prestazioni;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public String getVia() {
		return via;
	}
	
	public List getPrestazioni() {
		return prestazioni;
	}
}
