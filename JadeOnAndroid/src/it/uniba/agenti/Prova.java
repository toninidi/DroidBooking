package it.uniba.agenti;

import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

public class Prova extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = Uri.parse("content://calendar/events");  
        ContentResolver cr = this.getContentResolver();  
               
        Date date = new Date();
        Date date1 = new Date();
        date.setMinutes(date.getMinutes()+6);
        date1.setDate(10);
        date1.setMinutes(date.getMinutes()+6);
        
        long dtstart = date.getTime();
        long dtend = date1.getTime();
        
        ContentValues values = new ContentValues();  
        values.put("eventTimezone", "EST");  
        values.put("calendar_id", 1); // query content://calendar/calendars for more  
        values.put("title", "Party over thurr");  
        values.put("allDay", 0);  
        values.put("dtstart", dtstart); // long (start date in ms)  
        values.put("dtend", dtend);     // long (end date in ms)  
        values.put("description", "Bring computers and alcohol");  
        values.put("eventLocation", "ZA WARULDO");  
         values.put("transparency", 0);  
         values.put("visibility", 0);  
         values.put("hasAlarm", 0);  
           
         cr.insert(uri, values); 
        
    }
}