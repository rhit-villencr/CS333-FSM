package fsm.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import fsm.services.DatabaseConnectionService;
import fsm.services.SQLDatabaseResult;
import fsm.services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author villencr
 *
 */
public class OfflineFrameTesting {

	///// Initialize globals
	JFrame frame = new JFrame();
	boolean useEmpty = false;
	String tableName;
	String userName;
	/////

	/**
	 * 
	 * @param data
	 */
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

	/**
	 * 
	 * @param con
	 */
	/* Starts the code */
	public void launchLogin() {
		loginFrame();
	}

	/**
	 * 
	 * @param con
	 */
	/* will start the table view frame */
	public void launchView() {

		///// Checks if any nonempty tables exist
		String[] NET = new String[2];
		NET[0] = "Test";
		NET[1] = "Tables";
		if (NET.length != 0) {
			tableName = NET[0];
			viewTable(tableName);
		} else {
			System.exit(1);
		}
		/////
	}

	/**
	 * 
	 * @param a
	 * @return String
	 */
	/* method to convert a char array to a string */
	public static String charToString(char[] a) {
		// Creating object of String class
		String string = new String(a);

		return string;
	}

	/**
	 * 
	 * @param con
	 */
	/* starts the login JFrame */
	public void profileFrame() {
		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		///// Create objects
//		JButton submit = new JButton("Update Favorite Things");
		
		/////////////////////////////////////////////
		JButton submit = new JButton("Update Profile");
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		JButton back = new JButton("Back To Tables");
		/////////////////////////////////////////////


		JLabel teamName = new JLabel("Enter Favorite Team");
		JTextField favTeam = new JTextField(8);

		JLabel playerFName = new JLabel("Enter Favorite Player FName");
		JTextField favPlayerF = new JTextField(8);

		JLabel playerLName = new JLabel("Enter Favorite Player LName");
		JTextField favPlayerL = new JTextField(8);

		Box teamBox = Box.createVerticalBox();
		Box playerBoxF = Box.createVerticalBox();
		Box playerBoxL = Box.createVerticalBox();

//		JPanel submitPanel = new JPanel();
		
		///////////////////////////////////////////////
		Box submitPanel = Box.createVerticalBox();
		///////////////////////////////////////////////
		
		JPanel input = new JPanel();
		/////

		///// Action listener for button
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String team = favTeam.getText();
				String fname = favPlayerF.getText();
				String lname = favPlayerL.getText();
				if (team.equals(""))
					team = null;
				if (fname.equals(""))
					fname = null;
				if (lname.equals(""))
					lname = null;
			}
		});
		
		/////////////////////////////////////////////
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchView();
			}
		});
		/////////////////////////////////////////////

		
		/////

		///// Add to panels
		teamBox.add(teamName);
		teamBox.add(favTeam);

		playerBoxF.add(playerFName);
		playerBoxF.add(favPlayerF);

		playerBoxL.add(playerLName);
		playerBoxL.add(favPlayerL);
		
		submitPanel.add(submit);
		
		/////////////////////////////////////////////
		submitPanel.add(back);
		/////////////////////////////////////////////

		input.add(teamBox);
		input.add(playerBoxF);
		input.add(playerBoxL);
		input.add(submitPanel);
		/////

		///// Add panels to frame
		frame.add(input);
		/////

		formatFrame();
	}

	/**
	 * @param con
	 */
	public void loginFrame() {
		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		///// Initializing out connection to the given one
		/////

		///// Initializing componenets and setting the echochar to "*"
		JTextField user = new JTextField(8);
		JPasswordField pass = new JPasswordField(8);
		JButton login = new JButton("Login");
		JButton register = new JButton("Register");
		JCheckBox showPass = new JCheckBox("Show Password", false);
		JLabel userText = new JLabel("Enter Username: ");
		JLabel passText = new JLabel("Enter Password: ");
		Box textField = Box.createVerticalBox();
		JPanel buttons = new JPanel();
		pass.setEchoChar('*');
		/////

		///// A bunch of action listeners
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					System.out.println("Login Successful");
					userName = user.getText();
					launchView();
				
				user.setText("");
				pass.setText("");
			}
		});



		showPass.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (showPass.isSelected()) {
					pass.setEchoChar((char) 0);
				} else {
					pass.setEchoChar('*');
				}
			}
		});
		/////

		///// Adding components to the JPanels
		buttons.add(login);
		buttons.add(register);
		textField.add(userText);
		textField.add(user);
		textField.add(passText);
		textField.add(pass);
		textField.add(showPass);
		/////

		///// Adding JPanels to the JFrame
		frame.add(textField, BorderLayout.NORTH);
		frame.add(buttons, BorderLayout.SOUTH);
		/////

		formatFrame();

	}

	/**
	 * 
	 * @param tableName
	 * @param con
	 */
	/* Will refresh the JFrame with whatever table in inserted */
	public void viewTable(String tableName) {

		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		///// Initializing out connection to the given one
		/////

		///// To create the dropdown menu
		String[] choices = new String[2];
		choices[0] = "Test";
		choices[1] = "Tables";
		JComboBox<String> cb = new JComboBox<String>(choices);
		cb.setSelectedItem((Object) tableName);

		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cb.getSelectedItem().equals("tableName")) {
					System.out.println("Changed view to " + cb.getSelectedItem() + " table.");
					viewTable((String) cb.getSelectedItem());
				}
			}
		});
		/////

		///// Button for user profile
		JButton userButton = new JButton("Profile");
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Swapped to profile frame");
				profileFrame();
			}
		});
		/////

		///// Creating the button to refresh the JFrame
		JButton refresh = new JButton("Refresh Results");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Refreshed " + tableName + " table.");
				viewTable(tableName);
			}
		});
		/////

		///// Create check box to say if want empties
		JCheckBox jcb = new JCheckBox("Show Empty tables?", useEmpty);
		jcb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				useEmpty = (e.getStateChange() == ItemEvent.SELECTED);
				viewTable(tableName);
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
		String[] headers = getHeaders(tableName);
		for (int i = 0; i < headers.length; i++) {
			tableModel.addColumn(headers[i]);
		}
		/////

		///// Inputting collected data into the table
		Object[][] result = getResult(tableName);
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
		TableColumn l_Col;/* from www.java2s.com */
		for (var = 0; var < table.getColumnCount(); var++) {
			l_Col = table.getColumn(table.getColumnName(var));
			width = columnHeaderWidth(table, l_Col) + 6;
			l_Col.setMinWidth(150);
			l_Col.setMaxWidth(width);
		}
		/////

		///// Properly formatting layouts
		frame.setLayout(new BorderLayout());
		JPanel topPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel refreshBtnPnl = new JPanel();
		JPanel checkboxPnl = new JPanel();
		JPanel dropdownPnl = new JPanel();
		/////

		///// Adding components to all the panels
		refreshBtnPnl.add(userButton);
		refreshBtnPnl.add(refresh);
		dropdownPnl.add(cb);
		checkboxPnl.add(jcb);
		topPnl.add(refreshBtnPnl, BorderLayout.WEST);
		topPnl.add(dropdownPnl, BorderLayout.CENTER);
		topPnl.add(checkboxPnl, BorderLayout.EAST);
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

		formatFrame();

	}

	/**
	 * @param con
	 */
	public void formatFrame() {
		///// Adding a bunch of JFrame setting to make it look better
		frame.setTitle(userName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setAlwaysOnTop(false);
		/////
	}

	public String[] getHeaders(String tableName) {
		String[] choices = null;

		if(tableName.equals("Test")) {
			choices = new String[2];
			choices[0] = "Test1";
			choices[1] = "Tables1";
		}else {
			choices = new String[2];
			choices[0] = "Test2";
			choices[1] = "Tables2";
		}
		return choices;
	}
	
	public String[][] getResult(String tableName) {
		String[][] choices = null;

		if(tableName.equals("Test")) {
			choices = new String[2][3];
			choices[0][0] = "0,0:test";
			choices[0][1] = "0,1:test";
			choices[0][2] = "0,2:test";

			choices[1][0] = "1,1:test";
			choices[1][1] = "1,2:test";
			choices[1][2] = "1,3:test";
		}else {
			choices = new String[2][3];
			choices[0][0] = "0,0:tables";
			choices[0][1] = "0,1:tables";
			choices[0][2] = "0,2:tables";

			choices[1][0] = "1,1:tables";
			choices[1][1] = "1,2:tables";
			choices[1][2] = "1,3:tables";
		}
		return choices;
	}
	
	/**
	 * 
	 * @param l_Table
	 * @param col
	 * @return int
	 */
	/* Function to get the width of a given header of a table */
	static private int columnHeaderWidth(JTable l_Table, TableColumn col) {
		TableCellRenderer renderer = l_Table.getTableHeader().getDefaultRenderer();
		Component comp = renderer.getTableCellRendererComponent(l_Table, col.getHeaderValue(), false, false, 0, 0);
		return comp.getPreferredSize().width;
	}

}