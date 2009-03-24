package it.booking.agent;

import it.booking.business.SQLManager;
import it.booking.ui.MainUI;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookingCancelBehaviour extends CyclicBehaviour {
	
	private static final long serialVersionUID = -6427146532108307585L;

	private MainUI gui;
	
	public BookingCancelBehaviour(MainUI gui){
		this.gui = gui;
	}
	
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
		ACLMessage msg = myAgent.receive(mt);		
		if (msg != null) {
			
			
			int key =(Integer.parseInt(msg.getContent()));
			SQLManager.deletePrenotazione(key);
			System.out.println("Cancel message:rimossa tupla "+key);
			gui.getBooking();
			
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.CONFIRM);
			myAgent.send(reply);
		}
		else {
			block();
		}
	}

}
