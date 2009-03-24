package it.uniba.views.adapters;


import it.uniba.agenti.utils.Utility;
import it.uniba.ontology.CentroPrenotazione;
import it.uniba.ontology.Conferma;
import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
/**
 * @author el-principal
 *
 */
/**
 * @author el-principal
 *
 */
public class DBAdapter {
	
	private static final String DATABASE_NAME = "bookings.db";
	private static final String DATABASE_TABLE = "booking";
	private static final int DATABASE_VERSION = 1;
	
	// The index (key) column name for use in where clauses.
	public static final String KEY_ID="_id";
	
	// The name and column index of each column in your database.
	public static final String DOTTORE="dottore";
	public static final int DOTTORE_COLUMN = 1;
	
	public static final String NUMERO = "numero";
	public static final int NUMERO_COLUMN = 2;
	
	public static final String DATA="data";
	public static final int NAME_COLUMN = 3;
	
	public static final String DESCRIZIONE="descrizione";
	public static final int DESCRIZIONE_COLUMN = 4;
	
	public static final String ID_CALENDAR="id_calendar";
	public static final int ID_CALENDAR_COLUMN = 5;
	
	public static final String ID_PRENOTAZIONE = "id_prenotazione";
	public static final int ID_PRENOTAZIONE_COLUMN = 6;
	

	// SQL Statement to create a new database.
	private static final String DATABASE_CREATE = "CREATE TABLE "+ DATABASE_TABLE + " ("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
												DOTTORE +" TEXT,"+ NUMERO + " TEXT,"+ DATA +" TEXT,"+ DESCRIZIONE +" TEXT," + ID_CALENDAR +
												 " TEXT,"+ID_PRENOTAZIONE+" INTEGER);";
	
		
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.

	private final Context context;
	// Database open/upgrade helper
	private myDbHelper dbHelper;
	public DBAdapter(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}
	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}
	
	public void close() {
		db.close();
	}
	// Insert a new task
	/*public long insertEntry(Prenotazione p) {
		// Create a new row of values to insert.
		ContentValues prenotazioneValues = new ContentValues();
		// Assign values for each row.
		prenotazioneValues.put(DOTTORE, p.getDottore());
		prenotazioneValues.put(DATA, p.getData());
		prenotazioneValues.put(ORA, p.getOra());
		prenotazioneValues.put(DESCRIZIONE, p.getDescrizione());
		//TODO QUI VA INSERITA LA CHIAVE RITORNATA DALL'INSERIMENTO NEL CALENDAR COSì SE DOBBIAMO CANCELLARE ABBIAMO LA CHIAVE,
		//ALTRIMENTI NON POSSIAMO CANCELLARE
		prenotazioneValues.put(ID_CALENDAR, p.getId_calendar());
		// Insert the row.
		return db.insert(DATABASE_TABLE, null, prenotazioneValues);
	}*/
	
	public long insertEntry(Conferma conferma, String id_calendar) {
		/**
		 * ricordati di inserire i tipi corretti
		 */
		CentroPrenotazione centro = (CentroPrenotazione) conferma.getCentro();
		
		// Create a new row of values to insert.
		ContentValues prenotazioneValues = new ContentValues();
		// Assign values for each row.
		prenotazioneValues.put(DOTTORE, centro.getNome());
		
		prenotazioneValues.put(NUMERO, centro.getTelefono());
		
		prenotazioneValues.put(DATA, Utility.dateToCompleteText(conferma.getInizioPrenotazione()));
		prenotazioneValues.put(DESCRIZIONE, conferma.getPrestazione());

		prenotazioneValues.put(ID_CALENDAR, id_calendar);
		prenotazioneValues.put(ID_PRENOTAZIONE, conferma.getId_prenotazione());
		// Insert the row.
		return db.insert(DATABASE_TABLE, null, prenotazioneValues);
	}
	
	public void removeDB(){
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	}
	
	/**
	 * @param _rowIndex chiave della tupla da eliminare
	 * @return true l'operazione è stata eseguita, false altrimenti
	 */
	public boolean removeEntry(String _rowIndex) {
		//SE NELLA LISTA METTIAMO UN CAMPO DEL TIPO CODICE PER OGNI PRENOTAZIONE,QUI COME WHERE ABBIAMO SOLO LA CONDIZIONE SUL CODICE,
		//MENTRE SE NON LO METTIAMO LA WHERE DEV'ESSERE MODIFICATA PER CONTENERE LA CONDIZIONE SU TUTTI I CAMPI.
		return db.delete(DATABASE_TABLE, KEY_ID +"=" + _rowIndex, null) > 0;
		//in realtà,il metodo delete restituisce il numero di tuple cancellate oppure -1 se ci sono stati degli errori
		//siccome non ci interessa il numero di righe cancellate, questo metodo restituisce solo true o false.
	}
	
	public Cursor getAllEntries () {
		String[] campiSelezionati=new String[] {KEY_ID, DOTTORE, NUMERO, DATA, DESCRIZIONE, ID_CALENDAR, ID_PRENOTAZIONE};
		return db.query(DATABASE_TABLE, campiSelezionati,
				null, null, null, null, null);
	}
	
	
	public Cursor getEntry(String id) {
		String[] campiSelezionati=new String[] {ID_CALENDAR, ID_PRENOTAZIONE};
		String where = KEY_ID + "=" + id;
		return db.query(DATABASE_TABLE, campiSelezionati,
				where, null, null, null, null);
	}

	
	
	/**
	 * Questo metodo restituisce il puntatore ad una precisa tupla del Database.
	 * Nel nostro caso restituisce solo l'id dell'evento memorizzato nel calendar.
	 * @param _rowIndex
	 * @return result,il cursore alla tupla
	 * @throws SQLException se non trova tuple
	 */
	//non serve più
	/*public Cursor setCursorToItem(long _rowIndex) throws SQLException {
		
		Cursor result = db.query(true, DATABASE_TABLE, new String[] {ID_CALENDAR}, KEY_ID + "=" + _rowIndex, null, null, null, null, null);
		if ((result.getCount() == 0) || !result.moveToFirst()) {
			throw new SQLException("Nessun elemento trovato alla riga: " + _rowIndex);
		}
		return result;
	}*/
	
	//questo metodo esegue l'aggiornamentgo di una singola riga
	//nel nostro caso non ci serve
	
	public int updateEntry(long _rowIndex, long _long) {
		String where = KEY_ID + "=" + _rowIndex;
		ContentValues contentValues = new ContentValues();
		// TODO fill in the ContentValue based on the new object
		return db.update(DATABASE_TABLE, contentValues, where, null);
	}

	
	private static class myDbHelper extends SQLiteOpenHelper {
		
		public myDbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		// Called when no database exists in
		// disk and the helper class needs
		// to create a new one.
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}
		
		// Called when there is a database version mismatch meaning that
		// the version of the database on disk needs to be upgraded to
		// the current version.
		@Override
		//questo metodo deve essere necessariamente implementato ma nel nostro caso non serve
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +
					_oldVersion + " to " +
					_newVersion +
			", which will destroy all old data");
			// Upgrade the existing database to conform to the new version.
			// Multiple previous versions can be handled by comparing
			// _oldVersion and _newVersion values.
			// The simplest case is to drop the old table and create a
			// new one.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(_db);
		}
		
	}
}
