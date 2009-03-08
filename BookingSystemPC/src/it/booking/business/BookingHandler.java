package it.booking.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneCompleta;

public class BookingHandler {
		
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	static ArrayList<long[]> intervalli = getIntervalliPrenotazione();
	
	
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
		fine1.set(Calendar.MINUTE, 0);
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
		try {
			instance.setTime(dateFormat.parse("00:00:00"));
		} catch (ParseException e) {			
			e.printStackTrace();
		}		
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
	
	public static boolean gestisciPrenotazione(Prenotazione prenotazione){
		long durataPrestazione = getDurataFromPrestazione(prenotazione.getPrestazione());
		//trovaPrimoIntervalloDisponibile(Calendar.getInstance().getTime(), durataPrestazione.getTimeInMillis());
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2009);
		date.set(Calendar.MONTH, 3);
		date.set(Calendar.DAY_OF_MONTH, 23);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		Calendar result = trovaPrimoIntervalloDisponibile(date, durataPrestazione);
		System.out.println("Orario trovato: "+printCalendar(result) +"-"+printCalendar(getCalendarFromMillis(result.getTimeInMillis()+durataPrestazione)));
		return true;
	}

	private static long getDurataFromPrestazione(String prestazione) {
		String query ="SELECT durata FROM bookingdb.prestazione WHERE nome ='" + prestazione +"'";		
		Vector<Object[]> data= SQLManager.executeQuery(query);
		if(data.size()==0){
			return 0;
		}else{
			try {		
				Date date = dateFormat.parse(data.get(0)[0].toString());
				Calendar dateCal = getInstance();
				dateCal.setTime(date);
				return (dateCal.get(Calendar.HOUR_OF_DAY)*3600+dateCal.get(Calendar.MINUTE)*60+dateCal.get(Calendar.SECOND))*1000;
				
			} catch (ParseException e) {				
				e.printStackTrace();
				return 0;
			}
		}
	}


	public static boolean gestisciPrenotazioneCompleta(PrenotazioneCompleta prenotazioneCompleta) {
		
		return false;
	}
	
	
	
	public static void main(String[] args) {
		
		
		Prenotazione prenot = new Prenotazione();
		prenot.setPrestazione("Ecografia");
		
		gestisciPrenotazione(prenot);
		
		
	}
	
	
	private static Calendar trovaPrimoIntervalloDisponibile(Calendar from, long durataPrestazioneInMillis){
		Calendar goalData = from;
		boolean trovato = false;
		boolean error = false;	
		
		long oneDay = 24 * 60 * 60 * 1000;
		
		Calendar inizio = getInstance();
		Calendar fine = getInstance();			
		Calendar inizioSuccessivo = getInstance();
		Calendar fineSuccessivo = getInstance(); 
		
		
		
		while(trovato == false && error == false){
			System.out.println("Giorno considerato "+printCalendar(from));
			String query ="SELECT OraInizio, OraFine FROM bookingdb.prenotazione WHERE GiornoInizio ='" +
			from.get(Calendar.YEAR)+"-"+from.get(Calendar.MONTH)+"-"+from.get(Calendar.DAY_OF_MONTH) +"' order BY OraInizio, OraFine";
			Vector<Object[]> data= SQLManager.executeQuery(query);
			
			if(data.size()!=0){
			int intervalIndex = 0;
			long currFrom = intervalli.get(intervalIndex)[0];
			long currTo = 0;
			
			//cicla sui dati del database
			for (int i = 0; i < data.size()-1; i++) {
				try {
					inizio.setTime(dateFormat.parse((data.get(i)[0].toString())));
					fine.setTime(dateFormat.parse((data.get(i)[1].toString())));				
					inizioSuccessivo.setTime(dateFormat.parse((data.get(i+1)[0].toString())));
					fineSuccessivo.setTime(dateFormat.parse((data.get(i+1)[1].toString())));
				} catch (ParseException e) {
					error = true;
					e.printStackTrace();
				}
				currTo = inizio.getTimeInMillis();
				
				System.out.println("Intervallo inizio: "+printCalendar(getCalendarFromMillis(intervalli.get(intervalIndex)[0])));
				System.out.println("Intervallo fine: "+printCalendar(getCalendarFromMillis(intervalli.get(intervalIndex)[1])));
				System.out.println("Data inizio: "+printCalendar(getCalendarFromMillis(currFrom)));
				System.out.println("Data fine: "+printCalendar(getCalendarFromMillis(currTo)));
				
				if(ciSta(durataPrestazioneInMillis,currFrom,currTo)){
					trovato = true;
					break;
				}else{
					currFrom = fine.getTimeInMillis();
					currTo = inizioSuccessivo.getTimeInMillis();
					/* se il limite destro supera la fine dell'intervallo disponibile e se ci sono altri intervalli allora il limite sinistro
					*deve essere l'inizio del prossimo intervallo
					*/
					if(currTo >= intervalli.get(intervalIndex)[1] && intervalIndex<intervalli.size()-1){
						intervalIndex++;
						currFrom = intervalli.get(intervalIndex)[0];
					}else{
						System.out.println("Nada");
					}
				}		
			}
			if(trovato){
				goalData = setTimeOfDataFromMillis(goalData,currFrom);
				break;
			}else{
				if(ciSta(durataPrestazioneInMillis,fineSuccessivo.getTimeInMillis(),intervalli.get(intervalIndex)[1])){
					trovato = true;					
					goalData = setTimeOfDataFromMillis(goalData,fineSuccessivo.getTimeInMillis());
				}else{
					if(intervalIndex<intervalli.size()-1){
						trovato = true;
						intervalIndex++;
						goalData = setTimeOfDataFromMillis(goalData,intervalli.get(intervalIndex)[0]);
					}else{
						//Giorno successivo				
						from.setTimeInMillis(from.getTimeInMillis() + oneDay);
					}
				}				
			}
		}else{
			//Trovato un giorno libero
			System.out.println("Trovato giorno libero "+printCalendar(from));
			goalData = from;
			//imposto l'orario all'inizio del primo intervallo
			goalData = setTimeOfDataFromMillis(goalData, intervalli.get(0)[0]);
			trovato = true;
			break;
		}
		}
		if(trovato){	
			System.out.println("Data trovata: "+printCalendar(goalData));
			return goalData;
		}else{
			return null;
		}
	}


	/*
	 * Questo metodo imposta solo il tempo della data corrispondente ai millisecondi sulla data target passata in input
	 */
	private static Calendar setTimeOfDataFromMillis(Calendar target, long dateInMillis) {
		Calendar result = target;
		Calendar date = getInstance();
		date.setTimeInMillis(dateInMillis);
		result.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
		result.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
		result.set(Calendar.SECOND, date.get(Calendar.SECOND));
		return result;
	}


	private static boolean ciSta(long target, long from, long to){
		return target+from<=to;
	}
	
	


}
