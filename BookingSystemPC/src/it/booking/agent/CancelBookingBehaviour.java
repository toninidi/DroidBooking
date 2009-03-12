package it.booking.agent;

import it.booking.business.SQLManager;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CancelBookingBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -6427146532108307585L;

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			int key =(Integer.parseInt(msg.getContent()));
			SQLManager.deletePrenotazione(key);
			System.out.println("Cancel message:rimossa tupla "+key);	          
		}
		else {
			block();
		}
	}

}
