package it.booking.business;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

 public class SQLManager {
	
	 public static final String TABLE_PRESTAZIONE = "prestazione";
	 public static final String TABLE_CLIENTE = "cliente";
	 public static String TABLE_PRENOTAZIONE = "prenotazione";
	 public static final String DB_NAME = "bookingdb";
	 
	
	private  static Connection connection = null;
	private  static  Statement statement;
	
	
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
	
	
	
	
	private static void makeDBConnection(Properties properties) {
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
			statement = connection.createStatement();
		}catch(Exception ex){
			System.out.println("Error: " + ex.toString());
			ex.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
	public static Vector<Object[]> executeQuery(String query){
		Vector<Object[]> result = new Vector<Object[]>();
		makeDBConnection(getProperties());
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

}
