package it.uniba.agenti;

import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.Conferma;
import it.uniba.ontology.PropostaIntervalli;
import jade.lang.acl.ACLMessage;

public interface ACLMessageListener {
	
	public void udpateCentroPrenotazioneData(CentroPrenotazione centroPrenotazione, ACLMessage msg);

	public void udpateIntervalliDisponibili(PropostaIntervalli proposta);

	public void udpatePrenotazioneAccettata(Conferma conferma);

	public void udpateCancellazione(String id,String id_calendar);
	
	public void updateAgenteNonTrovato();
	
}
