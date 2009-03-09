package it.booking.agent;

import it.booking.business.BookingHandler;
import it.booking.exception.AvailableDayNotFoundException;
import it.booking.exception.PrestazioneNotFoundException;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.Conferma;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneConData;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookingRequestServerBehaviour extends CyclicBehaviour {


	private static final long serialVersionUID = -1288582303605122245L;
	
	private ContentManager manager;
	private Codec codec = new SLCodec();
	@Override
	public void action() {
		manager = myAgent.getContentManager();
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchOntology(BookingOntology.ONTOLOGY_NAME));
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
				System.out.println("ACCEPT_PROPOSAL Preparazione risposta");	  
				ACLMessage reply = msg.createReply();
				if(content instanceof Prenotazione){
					System.out.println("E il caso di una PRENOTAZIONE SENZA DATA");
					reply= setReplyContent(reply, (Prenotazione)content);					

				}else if(content instanceof PrenotazioneConData){
					System.out.println("E il caso di una PRENOTAZIONE CON DATA");
					reply= setReplyContent(reply, (PrenotazioneConData)content);	
				}	
				System.out.println("Sto mandando il messaggio con Performative "+ reply.getPerformative());
				myAgent.send(reply);
			}
		}
		else {
			block();
		}

	}

	private ACLMessage setReplyContent(ACLMessage reply, Prenotazione content) {
		Conferma conferma = null;
		boolean trovatoGiorno = false;
		try {
			conferma = BookingHandler.gestisciPrenotazione((Prenotazione)content);	
			System.out.println("Inserita la prenotazione nel giorno "+conferma.getInizioPrenotazione());
			trovatoGiorno = true;
		} catch (AvailableDayNotFoundException e) {
			// Nessuno giorno libero trovato	
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			e.printStackTrace();
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			e.printStackTrace();
		}
		if(trovatoGiorno){
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			reply.setOntology(BookingOntology.ONTOLOGY_NAME);
			reply.setLanguage(codec.getName());
			
			try {
				System.out.println("Sto riempiendo il contenuto");
				manager.fillContent(reply, conferma);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reply;
	}
	
	
	
	private ACLMessage setReplyContent(ACLMessage reply, PrenotazioneConData content) {
		boolean esitoPositivo = false;
		try {
			esitoPositivo = BookingHandler.gestisciPrenotazioneConData((PrenotazioneConData)content);
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(esitoPositivo){				      	   
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		}else{
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
		}
		return reply;
		
	}


}
