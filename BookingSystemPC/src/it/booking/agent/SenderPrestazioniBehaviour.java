package it.booking.agent;

import it.booking.business.SQLManager;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.CentroPrenotazione;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.List;

import java.util.Iterator;
import java.util.Vector;

public class SenderPrestazioniBehaviour extends CyclicBehaviour {

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

			AID senderAID = msg.getSender();			
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			reply.setLanguage(codec.getName());
			reply.setOntology(bookingOntology.getName());
			CentroPrenotazione centro = new CentroPrenotazione("Brizzi","345677","Via dei cicci per fiaschi", getPrestazioni());
			Action action = new Action();
			action.setActor(myAgent.getAID());
			action.setAction(centro);			

			//reply.setContentObject(prestazioni);
			try {
				manager.fillContent(reply, action);
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
	
	private List getPrestazioni() {
		List result = new jade.util.leap.ArrayList();
		String query = "SELECT NOME FROM "+ SQLManager.TABLE_PRESTAZIONE;
		Vector<Object[]> data = SQLManager.executeQuery(query);
		for (Iterator<Object[]> iterator = data.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			result.add(objects[0].toString());			
		}
		return result;
	}
	
	/*private String[] getPrestazioni() {
		String[] result;
		String query = "SELECT NOME FROM "+ SQLManager.TABLE_PRESTAZIONI;
		Vector<Object[]> data = SQLManager.executeQuery(query);
		result = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			Object[] objects = (Object[]) data.get(i);
			result[i]=(objects[0].toString());	
		}
		return result;
	}*/
	

	/*private ArrayList<String> getPrestazioni() {
		ArrayList<String> result = new ArrayList<String>();
		String query = "SELECT NOME FROM "+ SQLManager.TABLE_PRESTAZIONI;
		Vector<Object[]> data = SQLManager.executeQuery(query);
		for (Iterator<Object[]> iterator = data.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			result.add(objects[0].toString());			
		}
		return result;
	}*/

}
