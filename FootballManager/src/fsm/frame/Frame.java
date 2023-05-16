package fsm.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
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
public class Frame {

	///// Initialize globals
	JFrame frame = new JFrame();
	boolean useEmpty = false;
	String tableName;
	String userName;
	String position = "QB";
	String curView = "Players";

	String[] playerSearch = null;
	JTable table = null;
	DefaultTableModel tableModel = null;
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
	 * @param dbService
	 */
	/* Starts the code */
	public void launchLogin(DatabaseConnectionService dbService) {
		loginFrame(dbService);
	}

	/**
	 * 
	 * @param dbService
	 */
	/* will start the table view frame */
	public void launchView(DatabaseConnectionService dbService) {

		///// Checks if any nonempty tables exist
		String[] NET = SQLDatabaseResult.getTeams(dbService);
		if (NET.length != 0) {
			tableName = NET[0];
			viewTable(tableName, dbService);
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

	public void searchFrame(DatabaseConnectionService dbService) {
		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		JLabel fname = new JLabel("First Name:");
		JTextField PFname = new JTextField(8);
		JLabel lname = new JLabel("Last Name:");
		JTextField PLname = new JTextField(8);

		JButton search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//				System.out.println("FirstName: " + PFname.getText() + "\nLastName: " + PLname.getText());
				playerSearch = SQLDatabaseResult.getPlayer(dbService, PFname.getText(), PLname.getText());
				PFname.setText("");
				PLname.setText("");
				searchFrame(dbService);
			}
		});

		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerSearch = null;
				launchView(dbService);
			}
		});

		if (playerSearch != null) {
			tableModel = new DefaultTableModel();
			table = new JTable(tableModel) {
				@Override
				public Dimension getPreferredScrollableViewportSize() {
					return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
				}
			};
			/////

			///// Getting the headers and adding them to the table
			String[] headers = null;
//			System.out.println(playerSearch.length);
			if (playerSearch.length == 15) {// Offense
				headers = new String[] { "FirstName", "LastName", "Salary", "TeamName", "Position", "RushTD",
						"PassComp", "Interceptions", "PassAttempts", "RecievingYards", "RushAttempts", "PassYards",
						"PassingTD", "RushingYards", "ReceivingTD" };
			} else if (playerSearch.length == 13) {// Defense
				headers = new String[] { "FirstName", "LastName", "Salary", "TeamName", "Position", "Tackles", "Sacks",
						"ForcedFumbles", "FubleRecoveries", "Touchdowns", "Interceptions", "PassDeflections" };
			} else if (playerSearch.length == 10) {// Special
				headers = new String[] { "FirstName", "LastName", "Salary", "TeamName", "Position", "Tackles", "Sacks",
						"ForcedFumbles", "FumbleRecoveries", "Touchdowns", "Interceptions", "PassDeflections" };
			} else {// Player
				headers = new String[] { "FirstName", "LastName", "Salary", "TeamName", "Position" };

			}

//			System.out.println(curView);
			for (int i = 0; i < headers.length; i++) {
//				System.out.println("Added Header: " + headers[i]);
				tableModel.addColumn(headers[i]);
			}
			/////

			///// Inputting collected data into the table
			Vector<Object> newRow = new Vector<Object>();
			for (int i = 0; i < playerSearch.length; i++) {
				newRow.add(playerSearch[i]);
			}
			tableModel.addRow(newRow);

			/////

			///// Standardizing minimum column width
			int var, width;
			TableColumn l_Col;/* from www.java2s.com */
			for (var = 0; var < table.getColumnCount(); var++) {
				l_Col = table.getColumn(table.getColumnName(var));
				width = columnHeaderWidth(table, l_Col) + 6;
				l_Col.setMinWidth(100);
				l_Col.setMaxWidth(width);
			}
		}

		JPanel top = new JPanel();
		frame.setLayout(new BorderLayout());
		top.add(back);
		top.add(fname);
		top.add(PFname);
		top.add(lname);
		top.add(PLname);
		top.add(search);

		if (playerSearch != null) {
			table.getTableHeader().setReorderingAllowed(false);
			table.setDefaultEditor(Object.class, null);
			/////

			///// Adding the table and the panel to the JFrame
			frame.add(table.getTableHeader(), BorderLayout.CENTER);
//			System.out.println(table.getTableHeader());
			frame.add(table, BorderLayout.SOUTH);
		}

		frame.add(top, BorderLayout.NORTH);

		formatFrame(dbService);
	}

	/**
	 * 
	 * @param dbService
	 */
	/* starts the login JFrame */
	public void profileFrame(DatabaseConnectionService dbService) {
		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		UserService serv = new UserService(dbService);
		/////

		///// Create objects
		JButton submit = new JButton("Update Profile");
		JButton back = new JButton("Back To Tables");
		JButton deleteProfile = new JButton("Delete Profile");
		JButton logout = new JButton("Logout");


		JLabel teamName = new JLabel("Enter Favorite Team");
		JTextField favTeam = new JTextField(8);

		JLabel playerFName = new JLabel("Enter Favorite Player FName");
		JTextField favPlayerF = new JTextField(8);

		JLabel playerLName = new JLabel("Enter Favorite Player LName");
		JTextField favPlayerL = new JTextField(8);

		Box teamBox = Box.createVerticalBox();
		Box playerBoxF = Box.createVerticalBox();
		Box playerBoxL = Box.createVerticalBox();

		Box submitPanel = Box.createVerticalBox();
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
				SQLDatabaseResult.updateUser(dbService, userName, team, fname, lname);
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchView(dbService);
			}
		});
		
		deleteProfile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					serv.removeAccount(userName);
					launchLogin(dbService);
				}
			});
			
		logout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					launchLogin(dbService);
				}
			});
		/////
		

		///// Add to panels
		teamBox.add(teamName);
		teamBox.add(favTeam);

		playerBoxF.add(playerFName);
		playerBoxF.add(favPlayerF);

		playerBoxL.add(playerLName);
		playerBoxL.add(favPlayerL);

		submitPanel.add(submit);
		submitPanel.add(back);
		submitPanel.add(deleteProfile);
		submitPanel.add(logout);

		input.add(teamBox);
		input.add(playerBoxF);
		input.add(playerBoxL);
		input.add(submitPanel);
		/////

		///// Add panels to frame
		frame.add(input);
		/////

		formatFrame(dbService);
	}

	/**
	 * @param dbService
	 */
	public void loginFrame(DatabaseConnectionService dbService) {
		UserService serv = new UserService(dbService);

		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
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
				if (serv.login(user.getText(), charToString(pass.getPassword()))) {
					System.out.println("Login Successful");
					userName = user.getText();
					launchView(dbService);
				}
				user.setText("");
				pass.setText("");
			}
		});

		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serv.register(user.getText(), charToString(pass.getPassword()))) {
					System.out.println("Register Successful");
					userName = user.getText();
					launchView(dbService);
				}
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

		formatFrame(dbService);

	}

	/**
	 * 
	 * @param tableName
	 * @param dbService
	 */
	/* Will refresh the JFrame with whatever table in inserted */
	public void viewTable(String tableName, DatabaseConnectionService dbService) {

		///// Removing possible old values
		frame.dispose();
		frame = new JFrame();
		/////

		///// Initializing out connection to the given one
		DatabaseConnectionService dcs = dbService;
		/////

		///// To create the dropdown menu
		String[] choices = SQLDatabaseResult.getTeams(dbService);
		JComboBox<String> cb = new JComboBox<String>(choices);
		cb.setSelectedItem((Object) tableName);

		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!cb.getSelectedItem().equals(tableName)) {
					System.out.println("Changed view to " + cb.getSelectedItem() + " table.");
					viewTable((String) cb.getSelectedItem(), dcs);
				}
			}
		});
		/////
		String[] pos = null;
		if (curView.equals("Players")) {
			String[] posi = { "C", "CB", "EDGE", "FB", "IDL", "K", "LB", "LG", "LS", "LT", "P", "QB", "RB", "RG", "RT",
					"S", "TE", "WR" };
			pos = posi;

		} else if (curView.equals("Staff")) {
			String[] posi = { "HC", "DC", "OC", "ST", "GM" };
			pos = posi;
		}
		JComboBox<String> posCB = new JComboBox<String>(pos);
		posCB.setSelectedItem((Object) position);

		posCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				position = (String) posCB.getSelectedItem();
				viewTable((String) cb.getSelectedItem(), dcs);
			}
		});

		String[] view = { "Staff", "Players" };
		JComboBox<String> viewCB = new JComboBox<String>(view);
		viewCB.setSelectedItem((Object) curView);

		viewCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curView = (String) viewCB.getSelectedItem();
				if (curView.equals("Players")) {
					position = "QB";
				} else if (curView.equals("Staff")) {
					position = "HC";
				}
				viewTable((String) cb.getSelectedItem(), dcs);
			}
		});

		///// Button for user profile
		JButton userButton = new JButton("Profile");
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Swapped to profile frame");
				profileFrame(dbService);
			}
		});
		/////

		///// Button for search
		JButton searchButton = new JButton("Player Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Swapped to search frame");
				searchFrame(dbService);
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
		String[] headers = SQLDatabaseResult.getHeaders(dcs, curView);
//		System.out.println(curView);
		for (int i = 0; i < headers.length; i++) {
			tableModel.addColumn(headers[i]);
		}
		/////

		///// Inputting collected data into the table
//		System.out.println("TableName:" + tableName + "\nCurView: " + curView + "\nPosition: " + position);
		Object[][] result = SQLDatabaseResult.getResult(dcs, tableName, curView, position);
//		printTable((String[][])result);
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
			if(curView.equals("Players")) {
				l_Col.setMinWidth(175);
			}else {
				l_Col.setMinWidth(225);
			}
			
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
		refreshBtnPnl.add(searchButton);
		refreshBtnPnl.add(refresh);
		dropdownPnl.add(cb);
		dropdownPnl.add(viewCB);
		dropdownPnl.add(posCB);
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

		formatFrame(dcs);

	}

	/**
	 * @param dbService
	 */
	public void formatFrame(DatabaseConnectionService dbService) {
		///// Adding a bunch of JFrame setting to make it look better
		frame.setTitle("User: " + userName);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				dbService.closeConnection();
				frame.dispose();
				System.exit(0);
			}
		});
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setAlwaysOnTop(false);
		/////
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