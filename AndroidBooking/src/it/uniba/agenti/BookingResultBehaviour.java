package it.uniba.agenti;

import it.uniba.ontology.Conferma;
import it.uniba.ontology.PropostaIntervalli;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookingResultBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 8187874539069407293L;
	
	public BookingResultBehaviour() {}
	
	
	
	public void action() {
		ContentManager manager = myAgent.getContentManager();
		MessageTemplate mt = MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL));
		ACLMessage msg = myAgent.blockingReceive(mt);
		if (msg != null) {
			try {
				if (msg.getPerformative()==ACLMessage.ACCEPT_PROPOSAL){
					
					ContentElement content = manager.extractContent(msg);
					Concept action = ((Action)content).getAction();
					
					Conferma conferma=(Conferma)action;
					((JadeAndroidAgent)myAgent).updater.udpatePrenotazioneAccettata(conferma);
				}
				else if(msg.getPerformative()==ACLMessage.REJECT_PROPOSAL){
					
					ContentElement content = manager.extractContent(msg);
					Concept action = ((Action)content).getAction();
					
					PropostaIntervalli proposta = (PropostaIntervalli)action;
					
					((JadeAndroidAgent)myAgent).updater.udpateIntervalliDisponibili(proposta);
				}
				else {
					block();
				}
			} catch (UngroundedException e1) {
				e1.printStackTrace();
			} catch (CodecException e1) {
				e1.printStackTrace();
			} catch (OntologyException e1) {
				e1.printStackTrace();
			}
			

		}
	}

}
