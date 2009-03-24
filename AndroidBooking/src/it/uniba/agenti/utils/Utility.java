package it.uniba.agenti.utils;

import it.uniba.ontology.Cliente;
import jade.util.leap.List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public class Utility {
	
	public static final String MYPREFS = "settings";
	
	 public static Builder buildAlert(int stringId, Context cxt, int titleId){
		 return buildAlert(cxt.getResources().getString(stringId),cxt,titleId);	
	} 
	 
	 public static Builder buildAlert(String text, Context cxt, int titleId){
	    	AlertDialog.Builder alert = new AlertDialog.Builder(cxt);
	    	alert.setTitle(cxt.getResources().getString(titleId));
	    	alert.setMessage(text);
	    	alert.setCancelable(true);
	    	return alert;	
	}
	 
	 public static boolean isTextEmpty(EditText text)
	 {
		 if (text.getText().toString().equals(""))
			 return true;
		 else return false;
	 }
	 
	 public static String dateToCompleteText(Date giorno)
	 {
		 SimpleDateFormat formatter = new SimpleDateFormat("dd MMMMM yyyy, HH:mm");
		 return formatter.format(giorno);	
	 }
	 
	 public static String dateToShortFormat(Date giorno){
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		 return formatter.format(giorno);	
	 }
	 
	 public static String dateToTime(Date giorno){
		 SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		 return formatter.format(giorno);	
	 }
	 
	 public static boolean isPreferenceSet(Context cxt)
		{
			SharedPreferences settings = cxt.getSharedPreferences(MYPREFS,Activity.MODE_PRIVATE);
			if (settings.contains("cognome")) 
				return true;
			else{ 
				return false;
			}
		}
	 
		public static ArrayList<Object> listToArrayList(List prestazioni) {
			ArrayList<Object> result = new ArrayList<Object>();
			for (jade.util.leap.Iterator iterator = prestazioni.iterator(); iterator.hasNext();) {
				result.add(iterator.next());			
			}
			return result;
		}
		
		public static Cliente getCliente(Context cxt)
		{
			SharedPreferences settings = cxt.getSharedPreferences(Utility.MYPREFS,Activity.MODE_PRIVATE);
			Cliente cliente = new Cliente(settings.getString("nome",""),settings.getString("cognome",""),settings.getString("numero",""));
			return cliente;
			
		}
}
