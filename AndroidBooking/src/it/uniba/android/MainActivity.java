package it.uniba.android;

import it.uniba.agenti.BookingCancelBehaviour;
import it.uniba.agenti.GUIUpdater;
import it.uniba.agenti.InformationRequestBehaviour;
import it.uniba.agenti.utils.JadeUtility;
import it.uniba.agenti.utils.Utility;
import it.uniba.views.adapters.DBAdapter;
import jade.android.ConnectionListener;
import jade.android.JadeGateway;
import jade.core.behaviours.Behaviour;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.net.ConnectException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements ConnectionListener  {
	
	
	private JadeGateway gateway;
	private GUIUpdater updater;
	
	private Behaviour behaviour;
	
	private final int PRENOTAZIONE = 0;
	//private final int CERCA_CENTRO = 1;
	private final int SETTINGS=2;
	private final int EXIT =3;
	
	private final int RIMUOVI_PRENOTAZIONE = 0;
	
	private final int WARNING_DIALOG = 1;
	private final int ERROR_DIALOG = 2;
	
	
	String dialogMessage ="";
	
	ListView listaPrenotazioni;
	
	SimpleCursorAdapter prenotazioniAdapter;
	
	Cursor cursor;

	DBAdapter dbAdapter;
		
	protected ProgressDialog pd_connessione;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lista_prenotazioni);
		
		
		dbAdapter=new DBAdapter(this);

		listaPrenotazioni = (ListView) findViewById(R.id.lvPrenotazioni);

		dbAdapter.open();
		cursor = dbAdapter.getAllEntries();			
		//dbAdapter.close();
		updateCursorAndView();
		String[] fromColumns = new String[] {DBAdapter.DOTTORE,DBAdapter.NUMERO, DBAdapter.DESCRIZIONE, DBAdapter.DATA, DBAdapter.KEY_ID};
		int[] toLayoutIDs = new int[] { R.id.item_prenotazione_tv_Dottore,R.id.item_prenotazione_tv_NumeroCentro,R.id.item_prenotazione_tv_Prestazione,R.id.item_prenotazione_tv_Data, R.id.item_prenotazione_tv_id_key};
		prenotazioniAdapter = new SimpleCursorAdapter(this,R.layout.item_prenotazione,cursor,fromColumns,toLayoutIDs);
		listaPrenotazioni.setAdapter(prenotazioniAdapter);
		
		registerForContextMenu(listaPrenotazioni);
		//INIZIALIZZA IL GUI UPDATER
		updater = new GUIUpdater(this);
	}
	
	public void updateCursorAndView(){
		cursor.requery();
		TextView tv_prenotazioni=(TextView)this.findViewById(R.id.Prenotazioni);
		if (cursor.getCount()==0) 
			tv_prenotazioni.setText(R.string.main_nessuna_prenotazione);
		else 
			tv_prenotazioni.setText(R.string.main_prenotazioni_effettuate);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		((TextView)this.findViewById(R.id.selezione_et_numeroPrenotazione)).setText("");
		updateCursorAndView();
	}



	public boolean onCreateOptionsMenu(Menu menu){
	    	super.onCreateOptionsMenu(menu);
	    	menu.add(PRENOTAZIONE, Menu.FIRST, Menu.FIRST , R.string.bt_book);
	    	//menu.add(CERCA_CENTRO, Menu.FIRST+1, Menu.FIRST+1, R.string.bt_find);
	    	menu.add(SETTINGS, Menu.FIRST+2, Menu.FIRST+2, R.string.bt_settings);
	    	menu.add(EXIT, Menu.FIRST+3, Menu.FIRST+3, R.string.bt_exit);	    	   	
	    	return true;
	    }
	    
	    
	    public boolean onOptionsItemSelected(MenuItem item){
	    	Intent intent;
	    	
	    	super.onOptionsItemSelected(item);
	    	
	    	switch (item.getGroupId()) {
	    	case PRENOTAZIONE:{
	    		EditText et_NumeroPrenotazione = (EditText)this.findViewById(R.id.selezione_et_numeroPrenotazione);
	    		if(!et_NumeroPrenotazione.getText().toString().equalsIgnoreCase("")){
	    			if(Utility.isPreferenceSet(this)){
	    				pd_connessione =  ProgressDialog.show(this, getResources().getText(R.string.connessione_jade_attendere), getResources().getText(R.string.connessione_jade_tv_caricamento), true,true);
	    				String numeroPrenotazione = et_NumeroPrenotazione.getText().toString();	
	    				String aidString = numeroPrenotazione+"@"+getResources().getString(R.string.jade_container);
	    				String platformAddress = getResources().getString(R.string.platform_ACC);	
	    				InformationRequestBehaviour spb = new InformationRequestBehaviour(aidString,platformAddress,pd_connessione);
	    				executeBehaviour(spb);
	    			}
	    			else {
	    				Utility.buildAlert(R.string.al_settings_add, this, R.string.warning_dialog).show();
	    			}
	    				
	    		}else{
	    			Utility.buildAlert(R.string.al_main_inserisci_numero, this, R.string.warning_dialog).show();
	    		}
				break;	
	    	}
			case SETTINGS:
			{
				intent = new Intent(this, SettingsActivity.class);
		        startActivity(intent);
		        break;
			}
			case EXIT:
			{
				finish();
		        break;
			}
			default:
				break;
			}
	    	return true;
	    }

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(0, RIMUOVI_PRENOTAZIONE, Menu.NONE, "Cancella");
			
		}

		@Override
		public boolean onContextItemSelected(MenuItem item) {
			super.onContextItemSelected(item);
			switch (item.getItemId()) {
			case RIMUOVI_PRENOTAZIONE:
				AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

				if(menuInfo != null){
					pd_connessione = ProgressDialog.show(this, getResources().getText(R.string.connessione_jade_attendere), getResources().getText(R.string.connessione_jade_tv_caricamento), true,true);
					View itemView = listaPrenotazioni.getChildAt(menuInfo.position);
					TextView tv_id =  (TextView)itemView.findViewById(R.id.item_prenotazione_tv_id_key);
					TextView tv_numeroPrenotazione = (TextView)itemView.findViewById(R.id.item_prenotazione_tv_NumeroCentro);
					String numero = tv_numeroPrenotazione.getText().toString();
					String aidString = numero+"@"+getResources().getString(R.string.jade_container);
					String platformAddress = getResources().getString(R.string.platform_ACC);
					String id = tv_id.getText().toString();
					Cursor prenotazione = dbAdapter.getEntry(id);
					prenotazione.moveToFirst();
					BookingCancelBehaviour bcb = new BookingCancelBehaviour(aidString,platformAddress,id,prenotazione.getString(0),prenotazione.getString(1), pd_connessione);
					executeBehaviour(bcb);				
				}
				break;

			default:
				break;
			}
			return true;
		}

		
	/*	private void removeBooking(long position) {
			dbAdapter.removeEntry(position);
			cursor.requery();	
			prenotazioniAdapter.notifyDataSetChanged();
			//adapter.notifyDataSetChanged();			
		}*/
		
		@Override
		protected Dialog onCreateDialog(int id) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			switch (id) {
			case WARNING_DIALOG:
				dialog.setTitle(R.string.warning_dialog);				
				break;
			case ERROR_DIALOG:
				dialog.setTitle(R.string.error_dialog);
				break;
			default:
				break;
			}	
			dialog.setMessage(dialogMessage);
			return dialog.create();
		}

		protected void onPrepareDialog(int id, Dialog dialog) {
			AlertDialog alertDialog = (AlertDialog) dialog;
			alertDialog.setMessage(dialogMessage);
				
		}
		

		public void onConnected(JadeGateway gateway) {
			this.gateway = gateway;				
			executeCommands();
		}
		
		private void executeCommands(){
			final Context cxt = this;		
				Thread threadGatewayConnection = new Thread(new  Runnable() {
				
				public void run() {					
					
						try {
							gateway.execute(updater,JadeUtility.JADE_TIMEOUT);
							if(behaviour!=null)
							gateway.execute(behaviour,JadeUtility.JADE_TIMEOUT);
							
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
							pd_connessione.dismiss();
							Utility.buildAlert(R.string.al_connessione_errore_generico, cxt, R.string.error_dialog).show();							
							e.printStackTrace();
						}
				}				
			});
			
			threadGatewayConnection.start(); 
		}
		
		
		private void executeBehaviour(Behaviour behaviour){
			this.behaviour = behaviour;
			if(gateway == null){
				JadeUtility.connectAgentWithGateway(this, this);
			}else{
				executeCommands();
			}
		}
		
		

		public void onDisconnected() {
			//Toast t  = Toast.makeText(this,"Disconnesso dal JadeGateway", 5000);
			//t.show();
			
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			dbAdapter.close();
			if(gateway!=null)
				gateway.disconnect(this);
		}
		

}
