package it.uniba.ontology;


import jade.content.onto.*;
import jade.content.schema.*;

public class BookingOntology extends Ontology {

	private static final long serialVersionUID = -3052063184483117688L;

	public BookingOntology(String name, Introspector introspector) {
		super(name, introspector);
		// TODO Auto-generated constructor stub
	}
	
	public static final String ONTOLOGY_NAME = "BOOKING_ONTOLOGY";

	// Concepts
	public static final String CENTRO_PRENOTAZIONE  = "CENTRO_PRENOTAZIONE";

	// Slots
	public static final String NOME   = "NOME";
	public static final String TELEFONO = "TELEFONO";
	public static final String VIA = "VIA";
	public static final String PRESTAZIONI   = "PRESTAZIONI";
  
	private static BookingOntology theInstance = new BookingOntology(BasicOntology.getInstance());
	
	public static BookingOntology getInstance() {
		return theInstance;
	}
	
	public BookingOntology(Ontology base) {
		super(ONTOLOGY_NAME, base, new ReflectiveIntrospector());

		try {
			PrimitiveSchema stringSchema  = (PrimitiveSchema)getSchema(BasicOntology.STRING);
			//ContentElementListSchema listSchema = (ContentElementListSchema)getSchema(BasicOntology.CONTENT_ELEMENT_LIST);

			ConceptSchema centroSchema = new ConceptSchema(CENTRO_PRENOTAZIONE);
			centroSchema.add(NOME, stringSchema,  ObjectSchema.MANDATORY);
			centroSchema.add(TELEFONO, stringSchema, ObjectSchema.MANDATORY);
			centroSchema.add(VIA,   stringSchema);
			centroSchema.add(PRESTAZIONI, stringSchema, 1, ObjectSchema.UNLIMITED);

			add(centroSchema, CentroPrenotazione.class);

	} catch(OntologyException oe) { oe.printStackTrace(); }
	}
}
