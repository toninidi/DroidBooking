package it.uniba.agenti.utils;

import it.uniba.agenti.JadeAndroidAgent;
import it.uniba.android.R;
import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.util.leap.Properties;
import android.content.Context;
import android.widget.Toast;

public class JadeUtility {
	
	public static final long JADE_TIMEOUT = 30000;
	
	private static DFAgentDescription[] result = null;
	
	public static void connectAgentWithGateway(Context cxt,ConnectionListener cl){
		//CREATE THE JADE PROPERTIES CLASS
		Properties props = new Properties();
		props.setProperty(Profile.MAIN_HOST, cxt.getResources().getString(R.string.host));
		props.setProperty(Profile.MAIN_PORT, cxt.getResources().getString(R.string.port));
		props.setProperty(JICPProtocol.MSISDN_KEY, cxt.getResources().getString(R.string.msisdn));
		//props.setProperty(Profile.MTPS, "http://XELA:7778/acc");
		
		//Toast.makeText(this, R.string.connessione_jade_tv_caricamento, 5000).show();
		try {
			JadeGateway.connect(JadeAndroidAgent.class.getName(), null, props, cxt, cl);				
		} catch (Exception e) {			
			Toast.makeText(cxt,"connect "+ e.getMessage(), 5000).show();
		}
	}
	
	public static boolean trovaAgente(String aidString, Agent myAgent) throws FIPAException {
		if(result == null || result.length == 0){
			DFAgentDescription template = new DFAgentDescription();
			//template.setOwnership("book");
	        ServiceDescription sd = new ServiceDescription();
	        //AID aid = template.getName();
	        sd.setType("booking-center");
	        template.addServices(sd);
	        //AID amsAIDName = new AID("ams@XELA:1099/JADE",true);
	       // amsAIDName.addAddresses("http://XELA:7778/acc");
	        //AMSAgentDescription[] result;
			result = DFService.search(myAgent, template);	 
	    	//System.out.println("Found the following seller agents:");
		}
		AID[] bookingAgents = new AID[result.length];
    	for (int i = 0; i < result.length; ++i) {
    		bookingAgents[i] = result[i].getName();
    		System.out.println(bookingAgents[i].getName());
    		if(bookingAgents[i].getName().equalsIgnoreCase(aidString)){
    			System.out.println(bookingAgents[i].getName()+" vs "+aidString);
    			return true;
    		}          	
    	}
    	//return false;
    	return true;
	}

}
