package it.booking.business;

import java.io.Serializable;
import java.util.Date;

public class Richiesta implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1041077296781559026L;
	
	private int tipo;
	private Date giorno;
	private Date ora;
	private String prestazione;
	private String nomeUtente;
	private String cognomeUtente;
	private String telefonoUtente;
	
	
	public int getTipo() {
		return tipo;
	}

	public Date getGiorno() {
		return giorno;
	}

	public Date getOra() {
		return ora;
	}

	public String getPrestazione() {
		return prestazione;
	}

	public String getNomeUtente() {
		return nomeUtente;
	}

	public String getCognomeUtente() {
		return cognomeUtente;
	}

	public String getTelefonoUtente() {
		return telefonoUtente;
	}

	
	
	
	

}
