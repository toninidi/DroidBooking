package it.booking.ui;

import it.booking.agent.BookingAgent;
import it.booking.business.SQLManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

public class MainUI extends JFrame {


	private static final long serialVersionUID = 1L;
	
	SimpleDateFormat formatter = (SimpleDateFormat)DateFormat.getDateTimeInstance(DateFormat.LONG,
			 DateFormat.LONG, Locale.ITALY);
	String dataString;
	
	private JTable table;
	private JLabel lbl_data;
	final JScrollPane scrollPane;
	public JCalendar calendar;


	/**
	 * Create the frame
	 */
	public MainUI(BookingAgent agent) {
		super();
		setTitle("JCalendar");
		initializeLookAndFeels();
		getContentPane().setLayout(new BorderLayout());
		setBounds(100, 100, 423, 512);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(300);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setSize(413, 299);
		panel.setMinimumSize(new Dimension(200, 200));
		splitPane.setLeftComponent(panel);
		
		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		splitPane.setRightComponent(panel_1);

		final JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.NORTH);
		
		final JLabel lbl_prenotazioni = new JLabel();
		lbl_prenotazioni.setName("lbl_prenotazioni");
		lbl_prenotazioni.setText("Prenotazioni del");
		panel_2.add(lbl_prenotazioni);
		
		lbl_data = new JLabel();
		lbl_data.setName("lbl_data");
		panel_2.add(lbl_data);

		scrollPane = new JScrollPane();
		scrollPane.setName("sp_tabella");
		panel_1.add(scrollPane, BorderLayout.CENTER);

		/*table = new JTable(){
		      public boolean isCellEditable(int rowIndex, int colIndex) {
		          return false;   //Disallow the editing of any cell
		        }
		      };
		table.setMaximumSize(new Dimension(150, 150));
		table.setEditingRow(0);
		table.setEditingColumn(0);
		table.setName("tbl_prenotazioni");*/
		//inizializza la tabella
		initialize_table();
		scrollPane.setViewportView(table);

		calendar = new JCalendar();
		calendar.setMaximumSize(new Dimension(500, 400));
		calendar.setName("calendar");
		panel.add(calendar);
		calendar.setDecorationBackgroundVisible(true);
		calendar.setDecorationBordersVisible(false);
		calendar.setBorder(new MatteBorder(0, 0, 0, 0, Color.black));
		calendar.setWeekOfYearVisible(false);
		calendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				//System.out.println(evt.getPropertyName());
				setSelectedDate(calendar.getDate());
				getBooking();
			}
		});	


		final JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);	
		//
	}
	
	
	protected void getBooking() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		String dataString = formatter.format(calendar.getDate());		
		
		String[] columnNames = {"Ora","Cognome","Nome","Prestazione","Tel."};
		String query = "SELECT OraInizio, Cognome, Nome,  Descrizione, Telefono FROM prenotazione, cliente WHERE Id_Cliente = cliente.id AND prenotazione.GiornoInizio  ='"+dataString+"' ORDER BY OraInizio;" ;
		try {
			Vector<Object[]> data = SQLManager.executeQuery(query);
			populateTable(columnNames, data);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setSelectedDate(Date date) {		
		formatter.applyPattern("dd MMMMM yyyy");
		dataString = formatter.format(date);
		lbl_data.setText(dataString);
		
	}
	
	public void initialize_table()
	{
		table = new JTable(){
			
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
		          return false;   //Disallow the editing of any cell
		        }
		      };
		table.setMaximumSize(new Dimension(150, 150));
		table.setEditingRow(0);
		table.setEditingColumn(0);
		table.setName("tbl_prenotazioni");
	}

	public void populateTable(String[] columns, Vector<Object[]> data) throws SQLException{
		//la tabella deve essere inizializzata ogni volta che viene popolata altrimenti aggiunge righe cliccando sui giorni
		//anzichè presentare solo le righe relative al giorno scelto
		initialize_table();
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("HH:mm");
		DefaultTableModel aModel = (DefaultTableModel) table.getModel();	
		if(data.size()>0){			
			aModel.setColumnIdentifiers(columns);		
				for (Object[] object : data) {
					object[0] = formatter.format(object[0]);
					aModel.addRow(object);
				}		
			table.setModel(aModel);			
		}else{
			Vector<String> noColumns = new Vector<String>();
			noColumns.add("Nessuna prenotazione per questa data");			
			aModel.setDataVector(data, noColumns);
		}
		scrollPane.setViewportView(table);
	}
	
	public void initializeLookAndFeels() {
		// if in classpath thry to load JGoodies Plastic Look & Feel
		try {		
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			System.out.println(UIManager.getLookAndFeel().getName());
		} catch (Throwable t) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				System.out.println("Non ho il nimbus. ClassName:com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
