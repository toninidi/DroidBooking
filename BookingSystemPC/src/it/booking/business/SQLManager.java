package it.booking.business;

import it.booking.exception.PrestazioneNotFoundException;
import it.uniba.ontology.Cliente;
import it.uniba.ontology.Prenotazione;
import it.uniba.ontology.PrenotazioneConData;

import jade.util.leap.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

 public class SQLManager {
	 
	 private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	 public static final String TABLE_PRESTAZIONE = "prestazione";
	 public static final String TABLE_CLIENTE = "cliente";
	 public static String TABLE_PRENOTAZIONE = "prenotazione";
	 public static final String DB_NAME = "bookingdb";
	 
	
	private  static Connection connection = null;
	//private  static  Statement statement;
	
	
	
	public static int executeUdpateQuery(String query){
		int generatedKey = -1;
		Statement statement = makeDBConnection(getProperties());
		try {
			statement.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet res =  statement.getGeneratedKeys();
			while (res.next())
				generatedKey = res.getInt(1);
			System.out.println("Generated key: " + generatedKey);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedKey;
	}
	
	
	public static Vector<Object[]> executeQuery(String query){
		Vector<Object[]> result = new Vector<Object[]>();
		Statement statement = makeDBConnection(getProperties());
		try {
			 ResultSet rs = statement.executeQuery(query);		
			 java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int colNo = rsmd.getColumnCount();
				while(rs.next()){
					Object[] objects = new Object[colNo];
					// tanks to umit ozkan for the bug fix!
					for(int i=0;i<colNo;i++){
						objects[i]=rs.getObject(i+1);
					}
					result.add(objects);
				}
				rs.close();
		}catch(Exception ex){
			System.out.println("Error: " + ex.toString());			
			ex.printStackTrace();
			System.exit(1);
		}finally{
			try {			
				connection.close();
			} catch (SQLException e) {				
				e.printStackTrace();				
			}	
		}
		return result;			
	}
	
	public static List getPrestazioni() {
		List result = new jade.util.leap.ArrayList();
		String query = "SELECT NOME FROM "+ SQLManager.TABLE_PRESTAZIONE;
		Vector<Object[]> data = SQLManager.executeQuery(query);
		for (Iterator<Object[]> iterator = data.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			result.add(objects[0].toString());			
		}
		return result;
	}

	public static int insertPrenotazione(PrenotazioneConData prenotazione, long durataPrestazione){
		int generatedKey = -1;
		Calendar dataInizioPrenotazione = Calendar.getInstance();
		Calendar dataFinePrenotazione = Calendar.getInstance();		
		dataInizioPrenotazione.setTime(prenotazione.getGiornoPrenotazione());
		dataFinePrenotazione.setTimeInMillis(dataInizioPrenotazione.getTimeInMillis()+durataPrestazione);
		int idCliente = insertCliente(prenotazione.getCliente());  
		if(idCliente!=-1){
			generatedKey = insertPrenotazione(dataInizioPrenotazione, dataFinePrenotazione, prenotazione.getPrestazione(), idCliente);
		}
		return generatedKey;

	}
	
	public static int insertPrenotazione(Prenotazione prenotazione, Calendar dataInizioPrenotazione, long durataPrestazione){
		int generatedKey = -1;
		Calendar dataFinePrenotazione = Calendar.getInstance();
		dataFinePrenotazione.setTimeInMillis(dataInizioPrenotazione.getTimeInMillis()+durataPrestazione);
		int idCliente = insertCliente(prenotazione.getCliente());
		if(idCliente!=-1){
			generatedKey = insertPrenotazione(dataInizioPrenotazione, dataFinePrenotazione, prenotazione.getPrestazione(), idCliente);
		}
		return generatedKey;
	}
	
	public static boolean deletePrenotazione(int prenotazioneId){
		String query ="DELETE FROM "+TABLE_PRENOTAZIONE +" WHERE Id="+ prenotazioneId;
		if(executeUdpateQuery(query)!=-1)
			return true;
		else
			return false;
	}
	
	/*
	 * Inserisce una prenotazione nel db e restituisce il suo id (di tipo autoincrement)
	 */
	private static int insertPrenotazione(Calendar dataInizioPrenotazione, Calendar dataFinePrenotazione, String prestazione, int idCliente){
		int generatedKey = -1;
		String query = "INSERT INTO "+SQLManager.TABLE_PRENOTAZIONE+"(GiornoInizio, GiornoFine, OraInizio, OraFine, Descrizione, Id_cliente) VALUES('"+
		dataToSqlFormat(dataInizioPrenotazione) +"','"+dataToSqlFormat(dataFinePrenotazione)+"','"+timeToSqlFormat(dataInizioPrenotazione)+"','"+timeToSqlFormat(dataFinePrenotazione) 
		+"','"+prestazione +"',"+idCliente +")";
		generatedKey = executeUdpateQuery(query);
		return generatedKey;
	}

	public static long getDurataFromPrestazione(String prestazione) throws PrestazioneNotFoundException {
		long durataPrestazione = 0;
		String query ="SELECT durata FROM "+ SQLManager.TABLE_PRESTAZIONE+" WHERE nome ='" + prestazione +"'";		
		Vector<Object[]> data= SQLManager.executeQuery(query);
		if(data.size()!=0){
			try {		
				Date date = dateFormat.parse(data.get(0)[0].toString());
				Calendar dateCal = Calendar.getInstance();
				dateCal.setTime(date);
				durataPrestazione = (dateCal.get(Calendar.HOUR_OF_DAY)*3600+dateCal.get(Calendar.MINUTE)*60+dateCal.get(Calendar.SECOND))*1000;

			} catch (ParseException e) {				
				e.printStackTrace();
			}
			return durataPrestazione;
		}else{
			throw new PrestazioneNotFoundException("Prestazione " +prestazione+" non esistente nel database");			
		}

	}
	
	private static int getIdCliente(Cliente cliente){
		int idCliente = -1;
		if(cliente != null){
			String nomeCliente = cliente.getNomeCliente().trim();
			String cognomeCliente = cliente.getCognomeCliente().trim();
			String telefonoCliente = cliente.getTelefonoCliente().trim();

			String query = "SELECT Id FROM "+TABLE_CLIENTE+ " WHERE Nome= '"+nomeCliente+"' AND Cognome= '"+cognomeCliente+
			"'AND Telefono= '"+telefonoCliente+"'";
			Vector<Object[]> risultato =  executeQuery(query);
			if(risultato.size()!=0){	
				idCliente = Integer.parseInt(risultato.get(0)[0].toString());
			}
		}
		return idCliente;
	}
	
	
		public static boolean deleteCliente(Cliente cliente){
		int idCliente = getIdCliente(cliente);
		String query ="DELETE FROM "+TABLE_CLIENTE +" WHERE Id="+ idCliente;
		if(executeUdpateQuery(query)!=-1)
			return true;
		else
			return false;
	}

		
	/*
	 * Inserisce un cliente nel db e restituisce il suo id (di tipo autoincrement)
	 */
	private static int insertCliente(Cliente cliente){
		int idCliente = -1;
		if(cliente != null){			
			String nomeCliente = cliente.getNomeCliente().trim();
			String cognomeCliente = cliente.getCognomeCliente().trim();
			String telefonoCliente = cliente.getTelefonoCliente().trim();

			String query = "SELECT Id FROM "+TABLE_CLIENTE+ " WHERE Nome= '"+nomeCliente+"' AND Cognome= '"+cognomeCliente+
			"'AND Telefono= '"+telefonoCliente+"'";
			Vector<Object[]> risultato =  executeQuery(query);
			if(risultato.size()==0){		
				//System.out.println("INSERT INTO "+ TABLE_CLIENTE + "(Nome,Cognome,Telefono) VALUES('"+nomeCliente+"','"+cognomeCliente+"','"+telefonoCliente+"')");					
				String insertQuery = "INSERT INTO "+ TABLE_CLIENTE + "(Nome,Cognome,Telefono) VALUES('"+nomeCliente+"','"+cognomeCliente+"','"+telefonoCliente+"')";
				idCliente = executeUdpateQuery(insertQuery);
			}else{
				idCliente = getIdCliente(cliente);
			}
		}
		return idCliente;
	}

	private static String timeToSqlFormat(Calendar date){
		return date.get(Calendar.HOUR_OF_DAY)+":"+date.get(Calendar.MINUTE)+":"+date.get(Calendar.SECOND);
	}

	
	public static String dataToSqlFormat(Calendar date){
		return date.get(Calendar.YEAR)+"-"+date.get(Calendar.MONTH)+"-"+date.get(Calendar.DAY_OF_MONTH);
	}
	
	
	private static Properties getProperties(){
		// TODO definire il metodo che recupera le properties, per esempio in un file xml
		Properties connProp = new Properties();
		connProp.put("server", "localhost");
		connProp.put("port", "3306");
		connProp.put("db", "bookingdb");
		connProp.put("user", "root");
		connProp.put("password", "");
		return connProp;
	}
	
	
	
	
	private static Statement makeDBConnection(Properties properties) {
		String driver = "com.mysql.jdbc.Driver";		
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" + properties.getProperty("server") + ":" + properties.getProperty("port") + "/" + properties.getProperty("db"),properties);			
		} catch (Exception ex) {
			System.out.println("Error in DB connection: " + ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}		
		try {
			return connection.createStatement();
		}catch(Exception ex){
			System.out.println("Error: " + ex.toString());
			ex.printStackTrace();
			return null;
		}
		
	}
	
	
	
	


}
