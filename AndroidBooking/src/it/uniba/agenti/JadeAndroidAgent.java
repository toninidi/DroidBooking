package it.uniba.agenti;

import it.uniba.ontology.BookingOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.wrapper.gateway.GatewayAgent;


public class JadeAndroidAgent extends GatewayAgent {
	
	public ACLMessageListener updater;
	
    // This agent speaks the SL language
    private Codec codec = new SLCodec();
    
    private Ontology   ontology = BookingOntology.getInstance();
	
	private static final long serialVersionUID = 1L;	

	protected void setup() {
		/*AMSAgentDescription dfd = new AMSAgentDescription();
		dfd.setName(getAID());
		dfd.setState(AMSAgentDescription.INITIATED);
		//dfd.setOwnership("book");
		AID amsAID = new AID("ams@XELA:1099/JADE",true);
		amsAID.addAddresses("http://XELA:7778/acc");
		try {
			AMSService.register(this, amsAID,dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		super.setup();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
	}

	
	
	protected void processCommand(final Object command) {
		if (command instanceof Behaviour) {
			SequentialBehaviour sb = new SequentialBehaviour(this);
			sb.addSubBehaviour((Behaviour) command);
			sb.addSubBehaviour(new OneShotBehaviour(this) {
				private static final long serialVersionUID = 1L;

				public void action() {
					JadeAndroidAgent.this.releaseCommand(command);
				}
			});
			addBehaviour(sb);
		} else if (command instanceof ACLMessageListener) {
			//myLogger.log(Logger.INFO, "processCommand(): New GUI updater received and registered!");
			
			ACLMessageListener listener =(ACLMessageListener) command;
			
			updater = listener;
			releaseCommand(command);
		}
		
		else {
			//myLogger.log(Logger.WARNING, "processCommand().Unknown command "+command);
		}
	}

}
