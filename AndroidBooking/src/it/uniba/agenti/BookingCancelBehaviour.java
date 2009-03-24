package it.uniba.agenti;

import it.uniba.agenti.utils.JadeUtility;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import android.app.ProgressDialog;

public class BookingCancelBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 9213804061154005215L;
	
	private String aidString;
	private String platform;
	private String id;
	private String id_calendar;
	private String id_prenotazione;
	private ProgressDialog pd_connessione;
	

	
	
	public BookingCancelBehaviour(String aidString, String platform, String id, String id_calendar, String id_prenotazione, ProgressDialog pd_connessione) {
		this.aidString = aidString;
		this.platform = platform;
		this.id = id;
		this.pd_connessione = pd_connessione;
		this.id_calendar = id_calendar;
		this.id_prenotazione = id_prenotazione;
	}



	public void action() {
		boolean trovato = false;
		try {
			trovato = JadeUtility.trovaAgente(aidString, myAgent);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd_connessione.dismiss();
		}
		if(trovato){
			AID aid = new AID(aidString,true);
			aid.addAddresses(platform);

			ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
			msg.addReceiver(aid);
			msg.setContent(id_prenotazione);
			myAgent.send(msg);
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
			ACLMessage confirm = myAgent.blockingReceive(mt);
			if (confirm!=null)	((JadeAndroidAgent)myAgent).updater.udpateCancellazione(id,id_calendar);
			
		}else{
			((JadeAndroidAgent)myAgent).updater.updateAgenteNonTrovato();
		}
		pd_connessione.dismiss();
	}

}
