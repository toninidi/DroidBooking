package it.booking.agent;

import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.CentroPrenotazione;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SenderInformationBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -7828906716883291071L;

	private ContentManager manager;
	private Codec codec = new SLCodec();
	private Ontology bookingOntology = BookingOntology.getInstance();
	
	@Override
	public void action() {
		manager = myAgent.getContentManager();
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			System.out.println("REQUEST richiesta della lista di prestazioni");

			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setLanguage(codec.getName());
			reply.setOntology(bookingOntology.getName());
			CentroPrenotazione centro = BookingAgent.CENTRO;			

			//reply.setContentObject(prestazioni);
			try {
				manager.fillContent(reply, new Action(myAgent.getAID(),centro));
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(reply);
		}
		else {
			block();
		}

	}

}
