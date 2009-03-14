package it.booking.agent;

import java.util.Calendar;

import it.booking.business.BookingHandler;
import it.booking.exception.AvailableDayNotFoundException;
import it.booking.exception.PrestazioneNotFoundException;
import it.booking.ui.MainUI;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.Conferma;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneConData;
import it.uniba.ontology.PropostaIntervalli;
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
	private MainUI gui;
	
	public BookingRequestServerBehaviour(MainUI gui) {
		this.gui=gui;
	}
	
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
			ACLMessage reply = msg.createReply();
			if(content != null){				
				System.out.println("ACCEPT_PROPOSAL Preparazione risposta");	  				
				if(content instanceof Prenotazione){
					System.out.println("E il caso di una PRENOTAZIONE SENZA DATA");
					reply= setReplyContent(reply, (Prenotazione)content);					

				}else if(content instanceof PrenotazioneConData){
					System.out.println("E il caso di una PRENOTAZIONE CON DATA");
					reply= setReplyContent(reply, (PrenotazioneConData)content);	
				}	
				//System.out.println("Sto mandando il messaggio con Performative "+ reply.getPerformative());
				myAgent.send(reply);
				System.out.println("Messaggio inviato "+ reply.getPerformative());
			}else{
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
		}
		else {
			block();
		}

	}

	//PRENOTAZIONE SENZA DATA
	private ACLMessage setReplyContent(ACLMessage reply, Prenotazione content) {
		Conferma conferma = null;
		boolean trovatoGiorno = false;
		try {
			conferma = BookingHandler.gestisciPrenotazione((Prenotazione)content);	
			if(conferma!=null){
				System.out.println("Inserita la prenotazione nel giorno "+conferma.getInizioPrenotazione());
				trovatoGiorno = true;
				gui.getBooking();
			}
		} catch (AvailableDayNotFoundException e) {
			// Nessuno giorno libero trovato				
			e.printStackTrace();
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
		if(trovatoGiorno){
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			reply.setOntology(BookingOntology.ONTOLOGY_NAME);
			reply.setLanguage(codec.getName());
			try {
				//System.out.println("Sto riempiendo il contenuto");
				manager.fillContent(reply, conferma);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
		}else{
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
		}
		return reply;
	}
	
	
	// PRENOTAZIONE CON DATA
	private ACLMessage setReplyContent(ACLMessage reply, PrenotazioneConData content) {
		Conferma conferma = null;
		boolean disponibilita = false;
		System.out.println("Data richiesta: "+content.getDataPrenotazione());
		
		// Verifica della disponibilita 
		try {
			conferma = BookingHandler.gestisciPrenotazioneConData(content);
			if(conferma!=null){
				disponibilita = true;
				gui.getBooking();
			}
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Trovata disponibilità 
		if(disponibilita){				      	   
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			try {
				//System.out.println("Sto riempiendo il contenuto");
				manager.fillContent(reply, conferma);
			} catch (CodecException e) {
				e.printStackTrace();
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			} catch (OntologyException e) {
				e.printStackTrace();
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
		}else{// No disponiblità
			PropostaIntervalli propostaIntervalli = null;
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			
			//Inserimento delle proposte degli intervalli liberi
			try {
				propostaIntervalli = BookingHandler.getIntervalliDisponibili(4, content.getPrestazione(), Calendar.getInstance());
				manager.fillContent(reply, propostaIntervalli);
			} catch (PrestazioneNotFoundException e) {				
				e.printStackTrace();
				reply.setContent(e.getMessage());
			} catch (CodecException e) {
				e.printStackTrace();
				reply.setContent(e.getMessage());
			} catch (OntologyException e) {
				e.printStackTrace();
				reply.setContent(e.getMessage());
			}					
			
		}
		return reply;
		
	}


}
