package it.uniba.android;

import it.uniba.agenti.BookingProposalBehaviour;
import it.uniba.agenti.GUIUpdater;
import it.uniba.agenti.utils.JadeUtility;
import it.uniba.agenti.utils.Utility;
import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.IntervalloPrenotazione;
import it.uniba.ontology.PropostaIntervalli;
import it.uniba.views.adapters.IntervalliPrenotazioneAdapter;
import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.List;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BookingActivity extends Activity implements ConnectionListener{

	private static final int PRENOTAZIONE = 0;
	public static Calendar giorno=Calendar.getInstance();
	SimpleDateFormat formatter = new SimpleDateFormat();
	public Button bt_intervalli;
	//private JadeGateway gateway;
	private GUIUpdater updater;
	private JadeGateway gateway;
	private Behaviour behaviour;

	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);	
		this.setContentView(R.layout.prenotazione);
		
		giorno=Calendar.getInstance();
		
		TextView tv_nomeCentro = (TextView) this.findViewById(R.id.prenotazione_tv_nomeCentro);
		TextView tv_viaCentro = (TextView) this.findViewById(R.id.prenotazione_tv_viaCentro);
		Spinner prestazioniSpinner = (Spinner) this.findViewById(R.id.prenotazione_sp_prestazioni);
		
		CentroPrenotazione centroPrenotazione = (CentroPrenotazione) this.getIntent().getSerializableExtra("centro");
		tv_nomeCentro.setText(centroPrenotazione.getNome());
		tv_viaCentro.setText(centroPrenotazione.getVia());	
		
		List prestazioni = centroPrenotazione.getPrestazioni();
		ArrayList<CharSequence> list = new ArrayList<CharSequence>();
		ArrayList<Object> objects = Utility.listToArrayList(prestazioni);
		for (Iterator<Object> iterator = objects.iterator(); iterator.hasNext();) {
			CharSequence object = iterator.next().toString();
			list.add(object);
		} 
		ArrayAdapter<CharSequence> prestazioniAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,list);
		prestazioniAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prestazioniSpinner.setAdapter(prestazioniAdapter);

	


		set_table_enabled(false);
		updater = new GUIUpdater(this);

		Button bt_setgiorno = (Button)this.findViewById(R.id.prenotazione_bt_setgiorno);
		Button bt_setora = (Button)this.findViewById(R.id.prenotazione_bt_setora);
		bt_intervalli = (Button)this.findViewById(R.id.prenotazione_bt_intervalli);
		CheckBox cb_prenotaGiorno = (CheckBox) this.findViewById(R.id.prenotazione_cb_preferenza);
		cb_prenotaGiorno.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton cb, boolean scelta) {
				set_table_enabled(scelta);				
			}
		});

		bt_setgiorno.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				showDateDialog();
			}
		});

		bt_setora.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				showTimeDialog();
			}
		});
		
		bt_intervalli.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				showIntervalliDialog();
			}
		});
	}

	public  void showIntervalliDialog() {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle(R.string.al_intervalli_disponibili);
    	alert.setPositiveButton("OK", null);
    	//PropostaIntervalli pi = (PropostaIntervalli) intent.getSerializableExtra("proposta_intervalli");
		ArrayList<IntervalloPrenotazione> intervalli = new ArrayList<IntervalloPrenotazione>();
		/*IntervalloPrenotazione ip1 = new IntervalloPrenotazione(new Date(),new Date());
		IntervalloPrenotazione ip2 = new IntervalloPrenotazione(new Date(),new Date());
		intervalli.add(ip1);
		intervalli.add(ip2);*/
		Intent intent=this.getIntent();
		PropostaIntervalli proposta = (PropostaIntervalli)intent.getSerializableExtra("proposta");
		ArrayList<Object> objects = Utility.listToArrayList(proposta.getIntervalli());
		for (Iterator<Object> iterator = objects.iterator(); iterator.hasNext();) {
			IntervalloPrenotazione object = (IntervalloPrenotazione) iterator.next();
			intervalli.add(object);
		}
		IntervalliPrenotazioneAdapter ipa = new IntervalliPrenotazioneAdapter(this,R.layout.item_intervallo_disponibile,intervalli);
		alert.setAdapter(ipa, null);
		
		alert.setCancelable(true);
    	alert.show();
	}

	private void showTimeDialog() {
		//Calendar today = Calendar.getInstance();
		TimePickerDialog tpd = new TimePickerDialog(this, new OnTimeSetListener(){
			public void onTimeSet(TimePicker view , int hour, int minute){
				giorno.set(Calendar.HOUR_OF_DAY,hour);
				giorno.set(Calendar.MINUTE,minute);   
				EditText et_ora = (EditText) BookingActivity.this.findViewById(R.id.prenotazione_et_ora);
				formatter.applyPattern("HH:mm");
				et_ora.setText(formatter.format(giorno.getTime()));
			}			       	
		},    giorno.get(Calendar.HOUR_OF_DAY), giorno.get(Calendar.MINUTE), true);
		tpd.show();
	}


	private void showDateDialog(){
		//Calendar today = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener(){
			public void onDateSet(DatePicker view , int year, int month, int day){
				giorno.set(year,month,day);
				EditText et_giorno = (EditText) BookingActivity.this.findViewById(R.id.prenotazione_et_giorno);
				formatter.applyPattern("dd MMMMM yyyy");
				et_giorno.setText(formatter.format(giorno.getTime()));
			}			       	
		},    giorno.get(Calendar.YEAR), giorno.get(Calendar.MONTH), giorno.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}

	private void set_table_enabled(boolean en) {
		TextView tv_giorno = (TextView) this.findViewById(R.id.prenotazione_tv_giorno);
		TextView tv_ora = (TextView) this.findViewById(R.id.prenotazione_tv_ora);
		EditText et_giorno = (EditText) this.findViewById(R.id.prenotazione_et_giorno);
		EditText et_ora = (EditText) this.findViewById(R.id.prenotazione_et_ora);
		Button bt_setgiorno = (Button)this.findViewById(R.id.prenotazione_bt_setgiorno);
		Button bt_setora = (Button)this.findViewById(R.id.prenotazione_bt_setora);
		Button bt_intervalli=(Button)this.findViewById(R.id.prenotazione_bt_intervalli);

		tv_giorno.setEnabled(en);
		tv_ora.setEnabled(en);
		et_giorno.setEnabled(en);
		et_ora.setEnabled(en);
		bt_setgiorno.setEnabled(en);
		bt_setora.setEnabled(en);
		if (!en)bt_intervalli.setVisibility(Button.INVISIBLE);
	}

	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(PRENOTAZIONE, Menu.FIRST, Menu.FIRST , R.string.bt_book);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);

		switch (item.getGroupId()) {
		case PRENOTAZIONE:{
			EditText et_ora = (EditText) BookingActivity.this.findViewById(R.id.prenotazione_et_ora);
			EditText et_giorno = (EditText) BookingActivity.this.findViewById(R.id.prenotazione_et_giorno);
			CheckBox cb_prenotaGiorno = (CheckBox) this.findViewById(R.id.prenotazione_cb_preferenza);
			
			Intent intent = BookingActivity.this.getIntent();
			ACLMessage msg = (ACLMessage)intent.getSerializableExtra("ACLMessage");
			BookingProposalBehaviour spb = new BookingProposalBehaviour(msg,this);
			
			
			if (cb_prenotaGiorno.isChecked()) {
				if (Utility.isTextEmpty(et_giorno) | Utility.isTextEmpty(et_ora)) Utility.buildAlert(R.string.al_settings_empty, this, R.string.warning_dialog).show();
				else {					
					//JadeUtility.connectAgentWithGateway(this,this);
					executeBehaviour(spb);
				}
			}
			else {
				//JadeUtility.connectAgentWithGateway(this,this);
				executeBehaviour(spb);
			}

		}
		break;	
		default:
			break;
		}
		return true;
	}
	
	private void executeBehaviour(Behaviour behaviour){
		this.behaviour = behaviour;
		if(gateway == null){
			JadeUtility.connectAgentWithGateway(this, this);
		}else{
			executeCommand(behaviour);
		}
	}

	public void onConnected(JadeGateway gateway) {
		this.gateway=gateway;		
		executeCommand(updater);
		executeCommand(behaviour);
	}
	
	private void executeCommand(final Object command){
		final ProgressDialog pd_connessione = ProgressDialog.show(this, getResources().getText(R.string.connessione_jade_attendere), getResources().getText(R.string.connessione_jade_tv_caricamento), true,true);
		final Context cxt = this;
		Thread threadGatewayConnection = new Thread(new  Runnable() {
			public void run() {
						
				
					try {
						gateway.execute(command,30000);
						//gateway.execute(spb,30000);
						pd_connessione.dismiss();
					} catch (ConnectException e) {
						pd_connessione.dismiss();
						Utility.buildAlert(R.string.al_connessione_errore_connessione,cxt, R.string.error_dialog).show();
						e.printStackTrace();
					} catch (StaleProxyException e) {
						pd_connessione.dismiss();
						Utility.buildAlert(R.string.al_connessione_errore_comando,cxt, R.string.error_dialog).show();
						e.printStackTrace();
					} catch (ControllerException e) {							
						pd_connessione.dismiss();
						Utility.buildAlert(R.string.al_connessione_errore_generico, cxt, R.string.error_dialog).show();
						e.printStackTrace();
					} catch (InterruptedException e) {
						pd_connessione.dismiss();
						Utility.buildAlert(R.string.al_connessione_timeout, cxt, R.string.error_dialog).show();
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						pd_connessione.dismiss();
						Utility.buildAlert(R.string.al_connessione_errore_generico, cxt, R.string.error_dialog).show();
											
					}
               // Dismiss the Dialog
               pd_connessione.dismiss();
			}				
		});
		
		threadGatewayConnection.start(); 
	}

	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {			
		super.onDestroy();	
		System.out.println("Destroy dell'activity BookingActivity");
		if(gateway!=null)
		gateway.disconnect(this);
	}
	
	
}
