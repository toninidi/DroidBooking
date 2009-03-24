package it.uniba.agenti;

import android.app.ProgressDialog;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.CentroPrenotazione;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class InformationReceiverBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private ContentManager manager;
	private Ontology bookingOntology = BookingOntology.getInstance();
	private ProgressDialog pd_connessione;
	
	public InformationReceiverBehaviour(ProgressDialog pd_connessione) {
		this.pd_connessione = pd_connessione;
	}

	public void action() {
		manager = (ContentManager)myAgent.getContentManager();
		CentroPrenotazione centroPrenotazione = null;
		ContentElement content = null;
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology(bookingOntology.getName()));
		ACLMessage msg = myAgent.blockingReceive(mt);	
		
		if (msg != null) {
			try {				
				content = manager.extractContent(msg);
				Action action = (Action)content;
				centroPrenotazione = (CentroPrenotazione)action.getAction();		
			} catch (UngroundedException e) {
				e.printStackTrace();
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
			if(centroPrenotazione != null){
				((JadeAndroidAgent)myAgent).updater.udpateCentroPrenotazioneData(centroPrenotazione,msg);				
			}			
		}
		pd_connessione.dismiss();
		//myAgent.send(reply);


	}


}
