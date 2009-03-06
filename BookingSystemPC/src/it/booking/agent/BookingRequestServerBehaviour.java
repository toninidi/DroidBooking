package it.booking.agent;

import it.booking.business.Richiesta;
import it.booking.business.TipologiaRichiesta;
import jade.content.ContentManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class BookingRequestServerBehaviour extends CyclicBehaviour {

	private ContentManager manager;
	
	@Override
	public void action() {
		manager = myAgent.getContentManager();
		 MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
		ACLMessage msg = myAgent.receive(mt);
	    if (msg != null) {
	      System.out.println("ACCEPT_PROPOSAL Message received. Process it");
	      //String title = msg.getContent();
	      Richiesta richiesta;
		try {
			richiesta = ((Richiesta)msg.getContentObject());
			
			if(richiesta.getTipo()==TipologiaRichiesta.PRENOTAZIONE_LIBERA){
		    	// TODO aggiungere il behaviour per la prenotazione libera	    	 
		      }else if(richiesta.getTipo()==TipologiaRichiesta.PRENOTAZIONE_VINCOLATA){
		    	// TODO aggiungere il behaviour per la prenotazione vincolata  
		     }
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	      
	      ACLMessage reply = msg.createReply();	      	   
	      //reply.setPerformative(ACLMessage.INFORM);
	      
	      
	      myAgent.send(reply);
	    }
		  else {
		    block();
		  }

	}


}
