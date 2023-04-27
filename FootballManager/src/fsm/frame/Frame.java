package fsm.frame;

import java.awt.BorderLayout;
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

public class Frame {
	JFrame frame = new JFrame();
	public void printTable(String[][] data) {
		for (int i = 0; i < data.length; i++) { // this equals to the row in our matrix.
			for (int j = 0; j < data[i].length; j++) { // this equals to the column in each row.
				System.out.print(data[i][j] + " ");
			}
			System.out.println(); // change line on console as row comes to end in the matrix.
		}
		System.out.println(); // change line on console as row comes to end in the matrix.

	}

	public void viewTable(String tableName, DatabaseConnectionService con) {
		frame.dispose();
		frame = new JFrame();
		DatabaseConnectionService dcs = con;
		DefaultTableModel tableModel = new DefaultTableModel();
		JButton refresh = new JButton("Refresh Results");
		refresh.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        viewTable(tableName, dcs);
		    }
		});
		JTable table = new JTable(tableModel) {
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
			}
		};
		
		String[] headers = SQLDatabaseResult.getHeaders(dcs, tableName);
		for(int i = 0; i < headers.length; i++) {
			tableModel.addColumn(headers[i]);
		}
		
		Object[][] result = SQLDatabaseResult.getResult(dcs.getConnection(), tableName);
		Vector<Object> newRow;
		for (int i = 0; i < result.length; i++) {
			newRow = new Vector<Object>();
			for (int j = 0; j < result[i].length; j++) {
				newRow.add(result[i][j]);
			}
			tableModel.addRow(newRow);
		}

		frame.setLayout(new BorderLayout());

		JPanel btnPnl = new JPanel(new BorderLayout());
		JPanel bottombtnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

		bottombtnPnl.add(refresh);

		btnPnl.add(bottombtnPnl, BorderLayout.CENTER);

		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultEditor(Object.class, null);		
		
		frame.add(table.getTableHeader(), BorderLayout.CENTER);
		frame.add(table, BorderLayout.SOUTH);
		frame.add(btnPnl, BorderLayout.NORTH);

		frame.setTitle(tableName);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		    	dcs.closeConnection();
		    	frame.dispose();
		        System.exit(0);
		    }
		});;
		frame.pack();
		frame.setVisible(true);
	}

}