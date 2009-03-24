package it.uniba.views.adapters;

import java.util.Calendar;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import it.uniba.android.R;
import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.Conferma;

public class CalendarHandler {
	
	private static String CALENDAR_URI = "content://calendar/events";
	
	public static String insertAppointment(Conferma conferma, Context cxt)
	{
		Date giorno=null;
		Calendar c_giorno=Calendar.getInstance();
		
		
		CentroPrenotazione centro=(CentroPrenotazione)conferma.getCentro();
		
		Uri uri = Uri.parse(CALENDAR_URI);  
		ContentResolver cr = cxt.getContentResolver();  
		
		ContentValues values = new ContentValues();  
		values.put("eventTimezone", "EST");  
		// TODO ma questo è l'id che torna? se sì modificalo
		values.put("calendar_id", 1); // query content://calendar/calendars for more  
		values.put("title", cxt.getResources().getString(R.string.calendar_prenotazione)+conferma.getPrestazione());  
		values.put("allDay", 0);  
		
		giorno= (Date) conferma.getInizioPrenotazione();
		c_giorno.setTime(giorno);
		values.put("dtstart", c_giorno.getTimeInMillis()); // long (start date in ms)  
		
		giorno= (Date) conferma.getFinePrenotazione();
		c_giorno.setTime(giorno);
		values.put("dtend", c_giorno.getTimeInMillis());     // long (end date in ms) 
		
		values.put("description", centro.getNome()+"\n"+centro.getTelefono());  
		values.put("eventLocation", centro.getVia());  
		values.put("transparency", 0);  
		values.put("visibility", 0);  
		values.put("hasAlarm", 0);  

		Uri key=cr.insert(uri, values); 
		//ritorna la chiave dell'inserimento effettuato
		//bisogna cambiare tipo nel db per l'id_calendar
		//forse va bene string
		return key.toString();
	}

	public static void deleteAppointment(String key, Context cxt)
	{
		Uri uri_key = Uri.parse(key);
		ContentResolver cr = cxt.getContentResolver();
		cr.delete(uri_key, null, null);
	}

}
