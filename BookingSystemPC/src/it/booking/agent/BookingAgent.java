package it.booking.agent;

import it.booking.business.SQLManager;
import it.booking.ui.MainUI;
import it.uniba.ontology.*;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookingAgent extends Agent {
	
	
	private static final long serialVersionUID = 1L;
	
	private ContentManager manager = (ContentManager)getContentManager();
	private Codec codec = new SLCodec();
	private Ontology bookingOntology = BookingOntology.getInstance();
	
	private static String NOME_CENTRO = "Brizzi Antonio";
	
	private static String KEY_TELEFONO = "telefono";
	private static String VALUE_TELEFONO =  "0803512345";
	private static String KEY_INDIRIZZO = "indirizzo";
	private static String VALUE_INDIRIZZO = "Via De Gasperi (palazzo di vetro)";
	
	public static CentroPrenotazione CENTRO = new CentroPrenotazione(NOME_CENTRO,VALUE_TELEFONO,VALUE_INDIRIZZO, SQLManager.getPrestazioni());
	
	private static MainUI gui;

	@Override
	protected void setup() {	
		manager.registerLanguage(codec);
		manager.registerOntology(bookingOntology);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("booking-center");
		sd.setName(NOME_CENTRO);
		sd.addProperties(new Property(KEY_TELEFONO,VALUE_TELEFONO));
		sd.addProperties(new Property(KEY_INDIRIZZO,VALUE_INDIRIZZO));
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		gui = new MainUI(this);
		gui.setVisible(true);
		
		addBehaviour(new SenderPrestazioniBehaviour());
		addBehaviour(new BookingRequestServerBehaviour());
	}

	@Override
	public void doDelete() {
		// TODO Auto-generated method stub
		super.doDelete();
	}

	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}
	
	

	
}
