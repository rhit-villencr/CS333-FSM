package fsm.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import fsm.services.DatabaseConnectionService;
import fsm.services.SQLDatabaseResult;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Frame {
	
	/////Initialize a JFrame
	JFrame frame = new JFrame();
	/////
	
	/* Method that will take a 2 dimensional string and print it */
	public void printTable(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();

	}

	// Starts the code
	public void launch(DatabaseConnectionService con) {
		
		/////Checks if any nonempty tables exist
		String[] NET = SQLDatabaseResult.getNonEmptyTables(con.getConnection());
		if(NET.length != 0) {
			viewTable(NET[0], con);
		}else {
			System.exit(1);
		}
		/////
	}

	/* Will refresh the JFrame with whatever table in inserted */
	public void viewTable(String tableName, DatabaseConnectionService con) {

		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		///// Initializing out connection to the given one
		DatabaseConnectionService dcs = con;
		/////

		///// To create the dropdown menu
		String[] choices = SQLDatabaseResult.getNonEmptyTables(con.getConnection());
		JComboBox<String> cb = new JComboBox<String>(choices);
		cb.setSelectedItem((Object) tableName);

		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cb.getSelectedItem().equals("tableName")) {
					System.out.println("Changed view to " + cb.getSelectedItem() + " table.");
					viewTable((String) cb.getSelectedItem(), dcs);
				}
			}
		});
		/////

		///// Creating the button to refresh the JFrame
		JButton refresh = new JButton("Refresh Results");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Refreshed " + tableName + " table.");
				viewTable(tableName, dcs);
			}
		});
		/////

		///// Initializing the table and sizing properly
		DefaultTableModel tableModel = new DefaultTableModel();
		JTable table = new JTable(tableModel) {
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
			}
		};
		/////

		///// Getting the headers and adding them to the table
		String[] headers = SQLDatabaseResult.getHeaders(dcs, tableName);
		for (int i = 0; i < headers.length; i++) {
			tableModel.addColumn(headers[i]);
		}
		/////

		///// Inputting collected data into the table
		Object[][] result = SQLDatabaseResult.getResult(dcs.getConnection(), tableName);
		Vector<Object> newRow;
		for (int i = 0; i < result.length; i++) {
			newRow = new Vector<Object>();
			for (int j = 0; j < result[i].length; j++) {
				newRow.add(result[i][j]);
			}
			tableModel.addRow(newRow);
		}
		/////

		///// Standardizing minimum column width
		int var, width;
		TableColumn l_Col;/* from w w w .j av a 2s . c om */
		for (var = 0; var < table.getColumnCount(); var++) {
			l_Col = table.getColumn(table.getColumnName(var));
			width = columnHeaderWidth(table, l_Col) + 6;
			l_Col.setMinWidth(100);
			l_Col.setMaxWidth(width);
		}
		/////

		///// Properly formatting layouts
		frame.setLayout(new BorderLayout());
		JPanel topPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel refreshBtnPnl = new JPanel();
		JPanel dropdownPnl = new JPanel();
		/////

		///// Adding components to all the panels
		refreshBtnPnl.add(refresh);
		dropdownPnl.add(cb);
		topPnl.add(refreshBtnPnl, BorderLayout.WEST);
		topPnl.add(dropdownPnl, BorderLayout.EAST);
		/////

		///// Making it so you cannot edit the values in the table or change the order
		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultEditor(Object.class, null);
		/////

		///// Adding the table and the panel to the JFrame
		frame.add(table.getTableHeader(), BorderLayout.CENTER);
		frame.add(table, BorderLayout.SOUTH);
		frame.add(topPnl, BorderLayout.NORTH);
		/////

		///// Adding a bunch of JFrame setting to make it look good
		frame.setTitle(tableName);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				dcs.closeConnection();
				frame.dispose();
				System.exit(0);
			}
		});
		;
		frame.pack();
		frame.setVisible(true);
		/////

	}

	/* Function to get the width of a given header of a table */
	static private int columnHeaderWidth(JTable l_Table, TableColumn col) {
		TableCellRenderer renderer = l_Table.getTableHeader().getDefaultRenderer();
		Component comp = renderer.getTableCellRendererComponent(l_Table, col.getHeaderValue(), false, false, 0, 0);
		return comp.getPreferredSize().width;
	}

}