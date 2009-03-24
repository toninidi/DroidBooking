package it.uniba.views.adapters;

import java.util.Date;
import java.util.List;

import it.uniba.agenti.utils.Utility;
import it.uniba.android.R;
import it.uniba.ontology.IntervalloPrenotazione;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntervalliPrenotazioneAdapter extends ArrayAdapter<IntervalloPrenotazione> {

	private int resource;
	
	public IntervalliPrenotazioneAdapter(Context context,
			int textViewResourceId, List<IntervalloPrenotazione> objects) {
		super(context, textViewResourceId, objects);
		this.resource=textViewResourceId;

	}
	
	

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout intervalliView;
		IntervalloPrenotazione item = getItem(position);
		Date dataInizio = item.getInizioPrenotazione();
		Date dataFine = item.getFinePrenotazione();

		String dataInizioString = Utility.dateToShortFormat(dataInizio);
		String oraInizioString = Utility.dateToTime(dataInizio);
		String oraFineString = Utility.dateToTime(dataFine);
		intervalliView = (LinearLayout) convertView;
		 if (convertView == null) {
			 intervalliView = new LinearLayout(getContext());
		      String inflater = Context.LAYOUT_INFLATER_SERVICE;
		      LayoutInflater vi;
		      vi = (LayoutInflater)getContext().getSystemService(inflater);
		      vi.inflate(resource, intervalliView, true);
		    } else {
		    	intervalliView = (LinearLayout) convertView;
		    }
		TextView dataView = (TextView)intervalliView.findViewById(R.id.item_intervallo_tv_data);
		TextView oraView = (TextView)intervalliView.findViewById(R.id.item_intervallo_tv_Ora);

		dataView.setText(dataInizioString);
		oraView.setText(oraInizioString+"-"+oraFineString);
		return intervalliView;
	}
}

