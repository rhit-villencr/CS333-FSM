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

/**
 * @author villencr
 *
 */
public class CSVParser {

	static DatabaseConnectionService dbs = null;
	static String csvFolder = "C:\\Users\\villencr\\Documents\\CS333-FSM\\FootballManager\\src\\CSVParser\\CSV Files\\";

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		connect();

		insertTeam();

		insertPerson();
		insertPlayer();

	}

	public static void insertTeam() {

	}

	public static void insertPlayer() {
		String person = csvFolder + "Players.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbs.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(person))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createPlayer(?, ?, ?, ?, ?)}");
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
							if (headers.get(curHeader).equals("Pos")) {
								if (ready)
									cs.setString(6, col);
							}
							if (headers.get(curHeader).equals("Player Name")) {
								String[] name = col.split(" ");
								if (ready) {
									cs.setString(3, name[1]);
									cs.setString(2, name[0]);
								}

							}
							if (headers.get(curHeader).equals("TeamName")) {
								if (ready)
									cs.setString(4, col);
							}
							if (headers.get(curHeader).equals("Salary") && ready) {
								if (ready)
									cs.setString(5, col);
							}
						} catch (Exception e) {
						}
						curHeader++;
					}
				}
				curHeader = 0;
				if (ready)
					cs.execute();
				ready = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public static void insertPerson() {
		// Name of the CSV file to read
		String person = csvFolder + "Players.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbs.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(person))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
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
							if (headers.get(curHeader).equals("Player Name")) {
								String[] name = col.split(" ");
								if (ready)
									cs.setString(3, name[1]);
								if (ready)
									cs.setString(2, name[0]);
							}
						} catch (Exception e) {
						}
						curHeader++;
					}
				}
				curHeader = 0;
				if (ready)
					cs.execute();
				ready = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

}