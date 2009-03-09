package it.uniba.ontology;


import jade.content.onto.BasicOntology;
import jade.content.onto.Introspector;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.ReflectiveIntrospector;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class BookingOntology extends Ontology {

	private static final long serialVersionUID = -3052063184483117688L;

	public BookingOntology(String name, Introspector introspector) {
		super(name, introspector);
	}

	public static final String ONTOLOGY_NAME = "BOOKING_ONTOLOGY";

	// Concepts
	public static final String CENTRO_PRENOTAZIONE  = "CENTRO";
	public static final String CLIENTE  = "CLIENTE";
	public static final String INTERVALLOPRENOTAZIONE = "INTERVALLOPRENOTAZIONE";

	// Slots Centro
	public static final String NOME   = "NOME";
	public static final String TELEFONO = "TELEFONO";
	public static final String VIA = "VIA";
	public static final String PRESTAZIONI   = "PRESTAZIONI";
	
	//Slots Cliente
	public static final String NOMEC   = "NOMECLIENTE";
	public static final String COGNOME   = "COGNOMECLIENTE";
	public static final String TELEFONOC   = "TELEFONOCLIENTE";	
	
	// Predicates
	public static final String PRENOTAZIONE = "PRENOTAZIONE";
	public static final String PRENOTAZIONE_CON_DATA = "PRENOTAZIONECONDATA";
	public static final String CONFERMA = "CONFERMA";
	public static final String PROPOSTAINTERVALLI = "PROPOSTAINTERVALLI";
	
	//Slots Prenotazione
	public static final String PRESTAZIONE   = "PRESTAZIONE";	
	//Slots Prenotazione Completa
	public static final String DATAPRENOTAZIONE   = "DATAPRENOTAZIONE";
	//Slots Conferma
	public static final String INIZIOPRENOTAZIONE = "INIZIOPRENOTAZIONE";
	public static final String FINEPRENOTAZIONE = "FINEPRENOTAZIONE";
	public static final String ID_PRENOTAZIONE = "ID_PRENOTAZIONE";
	//Slots PropostaIntervalli
	public static final String INTERVALLI="INTERVALLI";

	private static BookingOntology theInstance = new BookingOntology(BasicOntology.getInstance());

	public static BookingOntology getInstance() {
		return theInstance;
	}

	public BookingOntology(Ontology base) {
		super(ONTOLOGY_NAME, base, new ReflectiveIntrospector());

		try {
			PrimitiveSchema stringSchema  = (PrimitiveSchema)getSchema(BasicOntology.STRING);
			PrimitiveSchema dateSchema = (PrimitiveSchema)getSchema(BasicOntology.DATE);
			PrimitiveSchema	intSchema = (PrimitiveSchema)getSchema(BasicOntology.INTEGER);
			//ContentElementListSchema listSchema = (ContentElementListSchema)getSchema(BasicOntology.CONTENT_ELEMENT_LIST);
			
			
			//Definizione schema Centro Prenotazione
			ConceptSchema centroSchema = new ConceptSchema(CENTRO_PRENOTAZIONE);
			centroSchema.add(NOME, stringSchema,  ObjectSchema.MANDATORY);
			centroSchema.add(TELEFONO, stringSchema, ObjectSchema.MANDATORY);
			centroSchema.add(VIA,   stringSchema);
			centroSchema.add(PRESTAZIONI, stringSchema, 1, ObjectSchema.UNLIMITED);

			add(centroSchema, CentroPrenotazione.class);

			//Definizione schema Cliente
			ConceptSchema clienteSchema = new ConceptSchema(CLIENTE);
			clienteSchema.add(NOMEC, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(COGNOME, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(TELEFONOC, stringSchema,  ObjectSchema.MANDATORY);
			
			add(clienteSchema, Cliente.class);
			
			ConceptSchema intervalliSchema = new ConceptSchema(INTERVALLOPRENOTAZIONE);
			intervalliSchema.add(INIZIOPRENOTAZIONE, dateSchema,ObjectSchema.MANDATORY);
			intervalliSchema.add(FINEPRENOTAZIONE, dateSchema,ObjectSchema.MANDATORY);
			
			add(intervalliSchema, IntervalloPrenotazione.class);
			
			//Definizione predicati
			PredicateSchema prenotazioneSchema = new PredicateSchema(PRENOTAZIONE);
			prenotazioneSchema.add(CLIENTE, clienteSchema,ObjectSchema.MANDATORY);
			prenotazioneSchema.add(PRESTAZIONE, stringSchema,  ObjectSchema.MANDATORY);
			
			add(prenotazioneSchema, Prenotazione.class);
			
			PredicateSchema prenotazioneConDataSchema = new PredicateSchema(PRENOTAZIONE_CON_DATA);
			prenotazioneConDataSchema.add(CLIENTE, clienteSchema,ObjectSchema.MANDATORY);
			prenotazioneConDataSchema.add(PRESTAZIONE, stringSchema,  ObjectSchema.MANDATORY);
			prenotazioneConDataSchema.add(DATAPRENOTAZIONE, dateSchema,  ObjectSchema.OPTIONAL);
			
			add(prenotazioneConDataSchema, PrenotazioneConData.class);
			
			PredicateSchema confermaSchema = new PredicateSchema(CONFERMA);
			confermaSchema.add(CENTRO_PRENOTAZIONE, centroSchema, ObjectSchema.MANDATORY);
			confermaSchema.add(INIZIOPRENOTAZIONE, dateSchema, ObjectSchema.MANDATORY);
			confermaSchema.add(FINEPRENOTAZIONE, dateSchema, ObjectSchema.MANDATORY);
			confermaSchema.add(PRESTAZIONE, stringSchema,ObjectSchema.MANDATORY);
			confermaSchema.add(ID_PRENOTAZIONE, intSchema);
			
			add(confermaSchema, Conferma.class);
			
			PredicateSchema propostaSchema = new PredicateSchema(PROPOSTAINTERVALLI);
			propostaSchema.add(INTERVALLI, intervalliSchema,1,ObjectSchema.UNLIMITED);
			propostaSchema.add(CENTRO_PRENOTAZIONE, centroSchema,ObjectSchema.MANDATORY);
			
			add(propostaSchema,PropostaIntervalli.class);
			

		} catch(OntologyException oe) { 
			oe.printStackTrace(); 
			}
	}
}
