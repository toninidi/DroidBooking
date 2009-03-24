package it.uniba.agenti;

import it.uniba.agenti.utils.Utility;
import it.uniba.android.BookingActivity;
import it.uniba.android.R;
import it.uniba.ontology.BookingOntology;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneConData;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.Spinner;

public class BookingProposalBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 5708399740561107307L;
	
	private ACLMessage msg;
	private Activity activity;
	private ContentManager manager;
	private Codec codec = new SLCodec();
	private Ontology bookingOntology = BookingOntology.getInstance();
	
	public BookingProposalBehaviour(ACLMessage msg, Activity cxt) {
		this.msg=msg;
		this.activity=cxt;
	}
	
	public void action() {
		manager=myAgent.getContentManager();
		ACLMessage reply= msg.createReply();
		reply.setPerformative(ACLMessage.PROPOSE);
		reply.setLanguage(codec.getName());
		reply.setOntology(bookingOntology.getName());
		
		CheckBox cb_prenotaGiorno = (CheckBox) activity.findViewById(R.id.prenotazione_cb_preferenza);
		Spinner prestazioniSpinner = (Spinner) activity.findViewById(R.id.prenotazione_sp_prestazioni);
		if (cb_prenotaGiorno.isChecked()) {
			PrenotazioneConData pc = new PrenotazioneConData(Utility.getCliente(activity),prestazioniSpinner.getSelectedItem().toString(),BookingActivity.giorno.getTime());
			try {
				manager.fillContent(reply, new Action(myAgent.getAID(),pc));
				myAgent.send(reply);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			Prenotazione pre = new Prenotazione(Utility.getCliente(activity),prestazioniSpinner.getSelectedItem().toString());
			try {
				manager.fillContent(reply, new Action(myAgent.getAID(),pre));
				myAgent.send(reply);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myAgent.addBehaviour(new BookingResultBehaviour());
		
	}

}
