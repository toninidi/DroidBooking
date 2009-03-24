package it.uniba.agenti;

import it.uniba.agenti.utils.Utility;
import it.uniba.android.BookingActivity;
import it.uniba.android.MainActivity;
import it.uniba.android.R;
import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.Conferma;
import it.uniba.ontology.PropostaIntervalli;
import it.uniba.views.adapters.CalendarHandler;
import it.uniba.views.adapters.DBAdapter;
import jade.lang.acl.ACLMessage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.widget.Button;

public class GUIUpdater implements ACLMessageListener {

	private Handler handl;
	private Activity activity;		
	
	public GUIUpdater(Activity baseActivity) {
		
		handl = new Handler();
		activity = baseActivity;
		
	}
	
	public void udpatePrenotazioneAccettata(Conferma conferma) {
		PrenotazioneAccettataUpdater up = new PrenotazioneAccettataUpdater(activity,conferma);
		handl.post(up);
	}
	
	
	public void udpateCentroPrenotazioneData(CentroPrenotazione centroPrenotazione, ACLMessage msg){
		System.out.println("updateCentroPrenotazione(): GuiUpdater has received a command");
		CentroPrenotazioniUpdater up = new CentroPrenotazioniUpdater(activity,centroPrenotazione,msg);
		handl.post(up);
	}
	
	
	public void udpateIntervalliDisponibili(PropostaIntervalli proposta) {
		IntervalliPrenotazioniUpdater up = new IntervalliPrenotazioniUpdater(activity,proposta);
		handl.post(up);
	}
	
	public void updateAgenteNonTrovato() {
		AgenteNonTrovatoUpdater up = new AgenteNonTrovatoUpdater(activity);
		handl.post(up);
	}
	
	public void udpateCancellazione(String id, String id_calendar) {
		CancellazioneUpdater up = new CancellazioneUpdater(activity,id, id_calendar);
		handl.post(up);
	}
	
	private class CancellazioneUpdater implements Runnable {

		private Activity sendMsgAct;
		String id;
		String id_calendar;
		
		
		public CancellazioneUpdater(Activity sm, String id, String id_calendar) { 
			sendMsgAct = sm;
			this.id = id;
			this.id_calendar = id_calendar;
			
		}

		public void run() {
			
			DBAdapter dbAdapter= new DBAdapter(sendMsgAct);
			dbAdapter.open();					
			CalendarHandler.deleteAppointment(id_calendar, sendMsgAct);			
			dbAdapter.removeEntry(id);
			dbAdapter.close();	
			AlertDialog.Builder alert = Utility.buildAlert(R.string.al_main_cancellata, sendMsgAct, R.string.warning_dialog);
			alert.setPositiveButton("OK", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {														
					((MainActivity)sendMsgAct).updateCursorAndView();
				}
			});
			alert.show();
			
		}
	}
	
	private class PrenotazioneAccettataUpdater implements Runnable {

		private Activity sendMsgAct;
		private Conferma conferma;


		public PrenotazioneAccettataUpdater(Activity sm, Conferma conferma) { 
			sendMsgAct = sm;
			this.conferma = conferma;

		}

		public void run() {

			DBAdapter dba = new DBAdapter(sendMsgAct);
			dba.open();
							
			String id_calendar = CalendarHandler.insertAppointment(conferma, (Context)sendMsgAct);
			dba.insertEntry(conferma, id_calendar);
			
			dba.close();
			AlertDialog.Builder alert = Utility.buildAlert("La prenotazione è stata fissata il "+Utility.dateToCompleteText(conferma.getInizioPrenotazione()),sendMsgAct,R.string.al_booking_prenotazione_confermata);
			
			alert.setPositiveButton("OK", new OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(sendMsgAct,MainActivity.class);					
					sendMsgAct.startActivity(intent);	
					//sendMsgAct.finish();
				}
			});
			alert.show();
			
			
		}
	}
	
	private class IntervalliPrenotazioniUpdater implements Runnable {

		private Activity sendMsgAct;
		private PropostaIntervalli proposta;


		public IntervalliPrenotazioniUpdater(Activity sm, PropostaIntervalli proposta) { 
			sendMsgAct = sm;
			this.proposta = proposta;

		}

		public void run() {

			Button bt_intervalli = (Button)sendMsgAct.findViewById(R.id.prenotazione_bt_intervalli);
			bt_intervalli.setVisibility(Button.VISIBLE);
			Intent intent = sendMsgAct.getIntent();
			intent.putExtra("proposta", proposta);
			Utility.buildAlert(R.string.al_data_occupata,(Context) sendMsgAct, R.string.warning_dialog).show();
		}
	}

	
	private class CentroPrenotazioniUpdater implements Runnable {

		private Activity sendMsgAct;
		private CentroPrenotazione centroPrenotazione;
		private ACLMessage msg;


		public CentroPrenotazioniUpdater(Activity sm, CentroPrenotazione centroPrenotazione,ACLMessage msg) { 
			sendMsgAct = sm;
			this.centroPrenotazione = centroPrenotazione;
			this.msg=msg;

		}

		public void run() {

			Intent intent = new Intent(sendMsgAct, BookingActivity.class);
			intent.putExtra("centro", centroPrenotazione);
			intent.putExtra("ACLMessage", msg);
			sendMsgAct.startActivity(intent);

		}
	}
	
	private class AgenteNonTrovatoUpdater implements Runnable {

		private Activity sendMsgAct;


		public AgenteNonTrovatoUpdater(Activity sm) { 
			sendMsgAct = sm;

		}

		public void run() {
			
			Utility.buildAlert(R.string.al_agente_non_trovato, sendMsgAct,R.string.error_dialog ).show();

		}
	}
}
	

