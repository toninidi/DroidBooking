package it.uniba.android;

import it.uniba.agenti.utils.Utility;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings);
        initialize_view();
        Button saveButton = (Button) this.findViewById(R.id.settings_bt_save);

        saveButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		save_settings();
        		
        		
        	}
        });
        }

		
	private void initialize_view() {
		EditText te_cognome = (EditText) this.findViewById(R.id.settings_et_cognome);
		EditText te_nome = (EditText) this.findViewById(R.id.settings_et_nome);
		EditText te_numero = (EditText) this.findViewById(R.id.settings_et_numero);
		EditText te_indirizzo = (EditText) this.findViewById(R.id.settings_et_indirizzo);
		SharedPreferences settings = getSharedPreferences(Utility.MYPREFS,Activity.MODE_PRIVATE);
		if (Utility.isPreferenceSet(this))
		{
			te_cognome.setText(settings.getString("cognome",""));
			te_nome.setText(settings.getString("nome",""));
			te_numero.setText(settings.getString("numero",""));
			te_indirizzo.setText(settings.getString("indirizzo",""));
		}
		
	}
	
	private void save_settings() {
		EditText te_cognome = (EditText) this.findViewById(R.id.settings_et_cognome);
		EditText te_nome = (EditText) this.findViewById(R.id.settings_et_nome);
		EditText te_numero = (EditText) this.findViewById(R.id.settings_et_numero);
		EditText te_indirizzo = (EditText) this.findViewById(R.id.settings_et_indirizzo);
		if (Utility.isTextEmpty(te_cognome) | Utility.isTextEmpty(te_nome) | Utility.isTextEmpty(te_numero) | Utility.isTextEmpty(te_indirizzo))
		Utility.buildAlert(R.string.al_settings_empty, this, R.string.warning_dialog).show();
		else 
		{
			SharedPreferences settings = getSharedPreferences(Utility.MYPREFS,Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			//memorizza i settings
			editor.putString("cognome", te_cognome.getText().toString());
			editor.putString("nome", te_nome.getText().toString());
			editor.putString("indirizzo", te_indirizzo.getText().toString());
			editor.putString("numero", te_numero.getText().toString());
			//effettua la memorizzazione			
			editor.commit();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			Toast t = Toast.makeText(this,R.string.al_settings_save, 3000);
			t.show();
		}
	}        	
}