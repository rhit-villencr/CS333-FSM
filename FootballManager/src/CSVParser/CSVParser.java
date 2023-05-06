package CSVParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import fsm.services.DatabaseConnectionService;

public class CSVParser {

	static DatabaseConnectionService dbs = null;

	public static void connect() {
		String serverName = "";
		String databaseName = "";
		String username = "";
		String password = "";
		/////

		///// Load variables from properties file
		try {
			String filePath = new File("").getAbsolutePath();
			filePath += "\\src\\fsm.properties";
			InputStream input = new FileInputStream(filePath);
			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			serverName = (String) prop.getProperty("serverName");
			databaseName = (String) prop.getProperty("databaseName");
			username = (String) prop.getProperty("serverUsername");
			password = (String) prop.getProperty("serverPassword");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		/////

		///// Initialize DatabaseConnectionService class
		dbs = new DatabaseConnectionService(serverName, databaseName);
		/////

		///// Attempt to connect, if failed, abort
		try {
			dbs.connect(username, password);
			System.out.println("Successfully Connected To: " + databaseName);

		} catch (Exception e) {
			System.out.println("Failed Connection To: " + databaseName);
			System.exit(0);
		}
	}

	public static void main(String[] args) {

		connect();
		// Name of the CSV file to read
		String csvFile = "C:\\\\Users\\\\villencr\\\\Documents\\\\Players.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		String[] sqlHeaders = { "Player Name", "TeamName", "Pos" };
		String[] offPos = { "C", "FB", "LG", "QB", "RB", "RG", "TE", "WR", "LT", "RT" };
		String[] defPos = { "CB", "EDGE", "IDL", "LB", "S" };
		String[] specPos = { "K", "LS", "P" };

		Connection con = dbs.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			while ((line = br.readLine()) != null) {
				if(ready) {
					cs = con.prepareCall("{? = call createPerson(?, ?)}");
					cs.registerOutParameter(1, Types.INTEGER);
				}
				
				if (isHeader) {
					String[] row = line.split(cvsSplitBy);
					for (String col : row) {
						headers.add(col);
					}
					isHeader = false;
				} else {
					String[] row = line.split(cvsSplitBy);
					for (String col : row) {
						try {
							List<String> sqlHeadersList = Arrays.asList(sqlHeaders);
							if (!sqlHeadersList.contains(headers.get(curHeader))) {
								break;
							}
							if (headers.get(curHeader).equals("Pos")) {
								List<String> offensePos = Arrays.asList(offPos);
								List<String> defensePos = Arrays.asList(defPos);
								List<String> specialPos = Arrays.asList(specPos);
//								System.out.println("Position:" + col);
								if (offensePos.contains(col)) {
//									System.out.print("OffensivePlayer");
								} else if (defensePos.contains(col)) {
//									System.out.print("DefensivePlayer");
								} else {
//									System.out.print("SpecialteamsPlayer");

								}
							} else if (headers.get(curHeader).equals("Player Name")) {
								String[] name = col.split(" ");
								if(ready) cs.setString(3, name[1]);
//								System.out.println("FirstName:" + name[0]);

								if(ready) cs.setString(2, name[0]);
//								System.out.print("LastName:" + name[1]);
							} else {
//								System.out.print(headers.get(curHeader) + ":");
//								System.out.print(col);

							}
						} catch (Exception e) {

						}
						curHeader++;
//						System.out.println();
					}

				}
				curHeader = 0;
//				System.out.println("====================");
				// Next Player
				if(ready) cs.execute();
				
				if(ready) {
				int returnValue = cs.getInt(1);

				if (returnValue == 4) {
					JOptionPane.showMessageDialog(null, "Person Already Exists");
				} else if (returnValue == 5) {
					JOptionPane.showMessageDialog(null, "Failed To Create");
				} else if (returnValue == 2) {
					JOptionPane.showMessageDialog(null, "Last Name Cannot Be NULL");
				} else if (returnValue == 1) {
					JOptionPane.showMessageDialog(null, "First Name Cannot Be NULL");
				} 
//				else if (returnValue == 6) {
//					JOptionPane.showMessageDialog(null, "Success");
//				}
				}
				ready = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}