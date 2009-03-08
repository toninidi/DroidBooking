package it.booking.agent;

import it.booking.business.BookingHandler;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.Cliente;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneCompleta;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class BookingRequestServerBehaviour extends CyclicBehaviour {

	private ContentManager manager;

	@Override
	public void action() {
		manager = myAgent.getContentManager();
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchOntology(BookingOntology.CENTRO_PRENOTAZIONE));
		ACLMessage msg = myAgent.receive(mt);
		ContentElement content = null;
		if (msg != null) {
			System.out.println("ACCEPT_PROPOSAL Message received. Process it");	          
			try {
				content = manager.extractContent(msg);			
			} catch (UngroundedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CodecException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OntologyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(content != null){
				boolean esitoPositivo = true;
				ACLMessage reply = msg.createReply();
				if(content instanceof Prenotazione){					
					esitoPositivo = BookingHandler.gestisciPrenotazione((Prenotazione)content);					
				}else if(content instanceof PrenotazioneCompleta){
					esitoPositivo = BookingHandler.gestisciPrenotazioneCompleta((PrenotazioneCompleta)content);					
				}
				if(esitoPositivo){				      	   
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				}else{
					reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				}
				myAgent.send(reply);
			}
		}
		else {
			block();
		}

	}


}
