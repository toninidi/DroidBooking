package it.booking.business;


import it.booking.agent.BookingAgent;
import it.booking.exception.AvailableDayNotFoundException;
import it.booking.exception.PrestazioneNotFoundException;
import it.uniba.ontology.Conferma;
import it.uniba.ontology.IntervalloPrenotazione;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneConData;
import it.uniba.ontology.PropostaIntervalli;
import jade.util.leap.List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

public class BookingHandler {
		
	// il numero di giorni da considerare per trovare un intervallo libero
	private static final int TENTATIVI_MASSIMI = 100;
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static final long ONEDAY = 24*60*60*1000;
	
	static ArrayList<long[]> intervalli = getIntervalliPrenotazione();
	

	
	public static void main(String[] args) {
		
		Calendar date = getInstance();
		System.out.println("PRIMA" + printCalendar(date));
		//date.setTimeZone(TimeZone.)
//		System.out.println("PRIMA" + printCalendar(date));
//		date.set(Calendar.HOUR_OF_DAY, 1);
//		System.out.println("DOPO " + printCalendar(date));
//		System.out.println("MILLIS CALCOLATI " +(date.get(Calendar.HOUR_OF_DAY)*3600+date.get(Calendar.MINUTE)*60+date.get(Calendar.SECOND))*1000);
		
		/*Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2009);
		date.set(Calendar.MONTH, 2);
		date.set(Calendar.DAY_OF_MONTH, 23);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		
		Prenotazione prenot = new Prenotazione();
		//PrenotazioneConData prenot = new PrenotazioneConData();
		prenot.setPrestazione("Visita Med");
		//prenot.setGiornoPrenotazione(date.getTime());
		prenot.setCliente(new Cliente("ciccio","cappuccio","0803567889"));
		//gestisciPrenotazioneConData(prenot);		
		try {
			gestisciPrenotazione(prenot);
		} catch (AvailableDayNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			getIntervalliDisponibili(4, "Ecografia", Calendar.getInstance());
		} catch (PrestazioneNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*ArrayList<long[]> a = getIntervalliDisponibili(date);
		for (Iterator iterator = a.iterator(); iterator.hasNext();) {
			long[] ls = (long[]) iterator.next();
			//System.out.println("Intervallo: "+printCalendar(getCalendarFromMillis(ls[0]))+"-"+printCalendar(getCalendarFromMillis(ls[1])));
			
		}*/
		
	}
	
	// TODO correggere gli intervalli restituiti per evitare che ritorni l'epoca ma la data corretta
	public static PropostaIntervalli getIntervalliDisponibili(int quantita, String prestazione, Calendar date) throws PrestazioneNotFoundException{
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND,0);			
		PropostaIntervalli pi = new PropostaIntervalli();
		List intervalliTrovati = new jade.util.leap.ArrayList();
		Calendar currDataInizio = Calendar.getInstance();
		Calendar currDataFine = Calendar.getInstance();
	// TODO rimuovere 
//		currDataInizio.setTimeZone(TimeZone.getTimeZone("GMT"));
//		currDataFine.setTimeZone(TimeZone.getTimeZone("GMT"));		
		boolean raggiuntaQuantita = false;
		ArrayList<long[]> intervalli;
		
		long durataPrestazione = SQLManager.getDurataFromPrestazione(prestazione);	
		System.out.println("Durata prestazione: "+durataPrestazione);
		while(!raggiuntaQuantita){
			intervalli = getIntervalliDisponibili(date);
			long[] currIntervallo;
			for(int i=0;i<intervalli.size() && intervalliTrovati.size() != quantita; i++){
				currIntervallo= intervalli.get(i);
				
				if(currIntervallo[1]>currIntervallo[0] && currIntervallo[1]-currIntervallo[0]>=durataPrestazione){
					currDataInizio.setTimeInMillis(currIntervallo[0]+date.getTimeInMillis());
					currDataFine.setTimeInMillis(currIntervallo[1]+date.getTimeInMillis());
					intervalliTrovati.add(new IntervalloPrenotazione(currDataInizio.getTime(),currDataFine.getTime()));
					System.out.println("Intervallo corrente aggiunto: "+printCalendar(currDataInizio)+"-"+printCalendar(currDataFine));
				}
			}
			if(intervalliTrovati.size()!=quantita){
				date.setTimeInMillis(date.getTimeInMillis() + ONEDAY);
			}else{
				pi.setCentro(BookingAgent.CENTRO);
				pi.setIntervalli(intervalliTrovati);
				raggiuntaQuantita = true;
			}
			
		}
		return pi;
	}

	
	public static Conferma gestisciPrenotazione(Prenotazione prenotazione) throws AvailableDayNotFoundException, PrestazioneNotFoundException{		
		long durataPrestazione = SQLManager.getDurataFromPrestazione(prenotazione.getPrestazione());
		
		Calendar date = Calendar.getInstance();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		ArrayList<long[]> intervalliDisponibili; 
		long[] currInterval = null;
		boolean trovato = false;
		int tentativi = 0;
		while(!trovato && tentativi <= TENTATIVI_MASSIMI){	
			intervalliDisponibili =  getIntervalliDisponibili(date);
			for (int i = 0; i < intervalliDisponibili.size() && !trovato; i++) {
				currInterval = intervalliDisponibili.get(i);
				//System.out.println("Intervallo corrente: "+printCalendar(getCalendarFromMillis(currInterval[0]))+"-"+printCalendar(getCalendarFromMillis(currInterval[1])));
				if(currInterval[1] -currInterval[0] >= durataPrestazione){
					trovato = true;
					date.set(Calendar.HOUR_OF_DAY, getCalendarFromMillis(currInterval[0]).get(Calendar.HOUR_OF_DAY));
					date.set(Calendar.MINUTE, getCalendarFromMillis(currInterval[0]).get(Calendar.MINUTE));
					date.set(Calendar.SECOND, getCalendarFromMillis(currInterval[0]).get(Calendar.SECOND));
				}
			}
			if(!trovato){
				//giorno successivo
				date.setTimeInMillis(date.getTimeInMillis() + ONEDAY);
			}
			tentativi++;
		}
		if(!trovato){
			throw new AvailableDayNotFoundException("Non è stato trovato un giorno libero entro "+TENTATIVI_MASSIMI+ " giorni");
		}else{
			System.out.println("Orario trovato: "+printCalendar(date) +"-"+printCalendar(getCalendarFromMillis(date.getTimeInMillis()+durataPrestazione)));
			//INSERIMENTO PRENOTAZIONE SUL DB
			int idPrenotazione = SQLManager.insertPrenotazione(prenotazione, date, durataPrestazione);		
			if(idPrenotazione!=-1){
				Conferma conferma = new Conferma(BookingAgent.CENTRO,prenotazione.getPrestazione(),date.getTime(),getCalendarFromMillis(date.getTimeInMillis()+durataPrestazione).getTime(),idPrenotazione);
				return conferma;
			}else{
				return null;
			}
		}
	}
	
	public static Conferma gestisciPrenotazioneConData(PrenotazioneConData prenotazione) throws PrestazioneNotFoundException{
		long durataPrestazione = SQLManager.getDurataFromPrestazione(prenotazione.getPrestazione());
		//System.out.println(printCalendar(getCalendarFromMillis(durataPrestazione)));
		
		Calendar date = getInstance();
		date.setTime(prenotazione.getDataPrenotazione());
		if(controllaDisponibilita(date,durataPrestazione)){
			int idPrenotazione = SQLManager.insertPrenotazione(prenotazione, durataPrestazione);
			if(idPrenotazione!=-1){
				Conferma conferma = new Conferma(BookingAgent.CENTRO,prenotazione.getPrestazione(),date.getTime(),getCalendarFromMillis(date.getTimeInMillis()+durataPrestazione).getTime(),idPrenotazione);
				return conferma;
			}else{
				return null;
			}			
		}else{
			return null;
		}
		
	}

	
	
	private static boolean controllaDisponibilita(Calendar date, long durataPrestazione) {
		System.out.println("Data richiesta: "+printCalendar(date));
		ArrayList<long[]> intervalliDisponibili = getIntervalliDisponibili(date);
		long[] currInterval = null;
		Calendar dateNormalized = getInstance();
		dateNormalized.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
		dateNormalized.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
		dateNormalized.set(Calendar.SECOND, date.get(Calendar.SECOND));
		for (int i = 0; i < intervalliDisponibili.size(); i++) {
			currInterval = intervalliDisponibili.get(i);
			//System.out.println(printCalendar(getCalendarFromMillis(currInterval[0]))+ "<"+
					//printCalendar(dateNormalized));
			//System.out.println(printCalendar(getCalendarFromMillis(currInterval[1]))+ ">"+
					//printCalendar(getCalendarFromMillis(dateNormalized.getTimeInMillis()+durataPrestazione)));
			if(currInterval[0]<=dateNormalized.getTimeInMillis() && currInterval[1]>= (dateNormalized.getTimeInMillis()+durataPrestazione)){
				System.out.println("Confermata disponibilità nell'intervallo "+printCalendar(getCalendarFromMillis(currInterval[0]))+"-"+
						printCalendar(getCalendarFromMillis(currInterval[1])));
				return true;
			}
		}
		System.out.println("No disponibilita");
		return false;
		
	}

	private static ArrayList<long[]> getIntervalliPrenotazione(){
		ArrayList<long[]> result = new ArrayList<long[]>();
		Calendar inizio = getInstance();
		inizio.set(Calendar.HOUR_OF_DAY, 9);
		inizio.set(Calendar.MINUTE, 0);
		Calendar fine = getInstance();
		fine.set(Calendar.HOUR_OF_DAY, 13);
		fine.set(Calendar.MINUTE, 0);
		result.add(new long[] {inizio.getTimeInMillis(), fine.getTimeInMillis()});
		Calendar inizio1 = getInstance();
		inizio1.set(Calendar.HOUR_OF_DAY, 14);
		inizio1.set(Calendar.MINUTE, 30);
		Calendar fine1 = getInstance();
		fine1.set(Calendar.HOUR_OF_DAY, 18);
		fine1.set(Calendar.MINUTE, 30);
		result.add(new long[] {inizio1.getTimeInMillis(), fine1.getTimeInMillis()});
		return result;		
	}
	
	
	/*
	 * Questo metodo inizializza la variabile globale che rappresenta l'istanza del calendar
	 * per tutti i metodi che la richiedono. 
	 * Il metodo fa si che venga presa la mezzanotte di oggi
	 */
	private static Calendar getInstance() {
		Calendar instance = Calendar.getInstance();	
		//instance.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = null;
		try {
			date = dateFormat.parse("00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		instance.setTime(date);
		//instance.setTimeInMillis(0);
		return instance;
	}


	
	private static Calendar getCalendarFromMillis(long dateInMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(dateInMillis);
		return c;
	}


	private static String printCalendar(Calendar date){
		return "  "+date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.MONTH)+"/"+date.get(Calendar.YEAR)
		+"  "+date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+"  "+
		"("+date.getTimeInMillis() +")";
	}
	

	private static void addIntervalloDisponibile(ArrayList<long[]> intervalliDisponibili, long from, long to){
		if(from-to!=0){
			intervalliDisponibili.add(new long[] {from,to});
			//System.out.println("Intervallo aggiunto: "+printCalendar(getCalendarFromMillis(from))+"-"+printCalendar(getCalendarFromMillis(to)));
		}
	}
	

	/*
	 * Questo metodo restituisce un arraylist di intervalli disponibili per il giorno date.
	 * Gli intervalli sono espressi in millisecondi.
	 * Tutti i millisecondi sono calcolati a partire dall'epoca
	 */
	private static ArrayList<long[]> getIntervalliDisponibili(Calendar date){
		ArrayList<long[]> intervalliDisponibili = new ArrayList<long[]>();

		//System.out.println("Data richiesta: "+printCalendar(date));
		
		Calendar inizio = getInstance();
		Calendar fine = getInstance();			
		Calendar inizioSuccessivo = getInstance();
		Calendar fineUltimaPrenotazione = getInstance(); 

		String query ="SELECT OraInizio, OraFine FROM "+SQLManager.TABLE_PRENOTAZIONE+" WHERE GiornoInizio ='" + SQLManager.dataToSqlFormat(date)
		+"' order BY OraInizio, OraFine";
		Vector<Object[]> data= SQLManager.executeQuery(query);

		if(data.size()!=0){
			int intervalIndex = 0;
			long currFrom = intervalli.get(intervalIndex)[0];
			long currTo = 0;
			//cicla sui dati del database
			for (int i = 0; i < data.size() ; i++) {
				try {
					inizio.setTime(dateFormat.parse((data.get(i)[0].toString())));
					fine.setTime(dateFormat.parse((data.get(i)[1].toString())));
					if(i<data.size()-1)
						inizioSuccessivo.setTime(dateFormat.parse((data.get(i+1)[0].toString())));
					else
						inizioSuccessivo.setTimeInMillis(intervalli.get(intervalIndex)[0]);
				} catch (ParseException e) {
					e.printStackTrace();
					break;
				}
				currTo = inizio.getTimeInMillis();
				if(currTo >= intervalli.get(intervalIndex)[1])
					currTo = intervalli.get(intervalIndex)[1];
					
				
				
				addIntervalloDisponibile(intervalliDisponibili, currFrom,currTo);								
				
				currFrom = fine.getTimeInMillis();
				currTo = inizioSuccessivo.getTimeInMillis();
				/* se il limite destro supera la fine dell'intervallo disponibile e se ci sono altri intervalli allora il limite sinistro
				 *deve essere l'inizio del prossimo intervallo
				 */
				if(currTo >= intervalli.get(intervalIndex)[1] && intervalIndex<intervalli.size()-1){
					intervalIndex++;
					currFrom = intervalli.get(intervalIndex)[0];
				}
			}
			try {
				fineUltimaPrenotazione.setTime(dateFormat.parse(data.get(data.size()-1)[1].toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addIntervalloDisponibile(intervalliDisponibili, fineUltimaPrenotazione.getTimeInMillis(),intervalli.get(intervalIndex)[1]);			
			for (int i = intervalIndex+1; i < intervalli.size(); i++) {			
				addIntervalloDisponibile(intervalliDisponibili,intervalli.get(i)[0],intervalli.get(i)[1]);				
			}
		}else{
			for (int i = 0; i < intervalli.size(); i++) {
				addIntervalloDisponibile(intervalliDisponibili,intervalli.get(i)[0],intervalli.get(i)[1]);				
			}
		}
		return intervalliDisponibili;
	}
	
	


}
