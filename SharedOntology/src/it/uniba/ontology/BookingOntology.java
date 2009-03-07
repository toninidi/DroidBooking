package it.uniba.ontology;


import java.util.Date;

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
	public static final String CLIENTE  = "CLIENTE";

	// Slots
	public static final String NOME   = "NOME";
	public static final String TELEFONO = "TELEFONO";
	public static final String VIA = "VIA";
	public static final String PRESTAZIONI   = "PRESTAZIONI";

	public static final String NOMEC   = "NOMEC";
	public static final String COGNOME   = "COGNOME";
	public static final String TELEFONOC   = "TELEFONOC";
	public static final String PRESTAZIONEC   = "PRESTAZIONEC";
	public static final String DATAPRENOTAZIONE   = "DATAPRENOTAZIONE";

	//
	private static BookingOntology theInstance = new BookingOntology(BasicOntology.getInstance());

	public static BookingOntology getInstance() {
		return theInstance;
	}

	public BookingOntology(Ontology base) {
		super(ONTOLOGY_NAME, base, new ReflectiveIntrospector());

		try {
			PrimitiveSchema stringSchema  = (PrimitiveSchema)getSchema(BasicOntology.STRING);
			PrimitiveSchema dateSchema = (PrimitiveSchema)getSchema(BasicOntology.DATE);
			//ContentElementListSchema listSchema = (ContentElementListSchema)getSchema(BasicOntology.CONTENT_ELEMENT_LIST);

			ConceptSchema centroSchema = new ConceptSchema(CENTRO_PRENOTAZIONE);
			centroSchema.add(NOME, stringSchema,  ObjectSchema.MANDATORY);
			centroSchema.add(TELEFONO, stringSchema, ObjectSchema.MANDATORY);
			centroSchema.add(VIA,   stringSchema);
			centroSchema.add(PRESTAZIONI, stringSchema, 1, ObjectSchema.UNLIMITED);

			add(centroSchema, CentroPrenotazione.class);

			ConceptSchema clienteSchema = new ConceptSchema(CLIENTE);
			clienteSchema.add(NOMEC, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(COGNOME, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(TELEFONOC, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(PRESTAZIONEC, stringSchema,  ObjectSchema.MANDATORY);
			clienteSchema.add(DATAPRENOTAZIONE, stringSchema,  ObjectSchema.OPTIONAL);
			add(clienteSchema, Cliente.class);

		} catch(OntologyException oe) { 
			oe.printStackTrace(); 
			}
	}
}
