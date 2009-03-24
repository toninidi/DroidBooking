package it.uniba.agenti;

import it.uniba.agenti.utils.JadeUtility;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import android.app.ProgressDialog;

public class InformationRequestBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	private ProgressDialog pd_connessione;
	private String aidString;
	private String platformAddress;
	
	public InformationRequestBehaviour(String aidString, String platformAddress, ProgressDialog pd_connessione)  {
		this.aidString = aidString;
		this.pd_connessione = pd_connessione;
		this.platformAddress = platformAddress;
	}
	
	
	@Override
	public void action(){		
        
        boolean trovato = false;
		try {
			trovato = JadeUtility.trovaAgente(aidString, myAgent);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(trovato){
    		AID aid = new AID(aidString,true);
    		aid.addAddresses(platformAddress);
    		
    		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    		msg.addReceiver(aid);
        	
        	myAgent.send(msg);
    		System.out.println("SEND: ho mandato la richiesta per le prestazioni");
    		myAgent.addBehaviour(new InformationReceiverBehaviour(pd_connessione));	
    	}else{
    		pd_connessione.dismiss();
    		((JadeAndroidAgent)myAgent).updater.updateAgenteNonTrovato();
    	}
	
	}




}
