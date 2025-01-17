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

	static DatabaseConnectionService dbService = null;

	// Chase RHIT Laptop
//	 static String csvFolder = "C:\\Users\\villencr\\Documents\\CS333-FSM\\FootballManager\\src\\CSVParser\\CSV Files\\";

	// Chase PC
	static String csvFolder = "A:\\280\\CS333-FSM\\FootballManager\\src\\CSVParser\\CSV Files\\";


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		connect();
		insertTeam();
		insertPlayer();
		insertOffensivePlayer();
		insertDefensivePlayer();
		insertSpecialTeamsPlayer();
		insertStaff();
		
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println(totalTime / 1000000000 + " seconds to populate databse.");
	}
	
	public static void connect() {
		String serverName = "";
		String databaseName = "";
		String username = "";
		String password = "";

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
		dbService = new DatabaseConnectionService(serverName, databaseName);
		/////

		///// Attempt to connect, if failed, abort
		try {
			dbService.connect(username, password);
			System.out.println("Successfully Connected To: " + databaseName);

		} catch (Exception e) {
			System.out.println("Failed Connection To: " + databaseName);
			System.exit(0);
		}
		/////
	}

	public static void insertOffensivePlayer() {
		String offPlayers = csvFolder + "OffensivePlayers.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(offPlayers))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createOffensivePlayer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
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
							if (headers.get(curHeader).equals("RushingTD")) {
								if (ready) {
									cs.setString(2, col);
								}
							}
							if (headers.get(curHeader).equals("Player")) {
								String[] name = col.split(" ");
								if (ready) {
									cs.setString(13, name[0]);
									String lname = "";
									for (int i = 1; i < name.length; i++) {
										lname += name[i];
										lname += " ";
									}
									lname = lname.substring(0, lname.length() - 1);
									cs.setString(12, lname);
								}
							}
							if (headers.get(curHeader).equals("PassingCompletions")) {
								if (ready) {
									cs.setString(3, col);
								}
							}
							if (headers.get(curHeader).equals("Int")) {
								if (ready) {
									cs.setString(4, col);
								}
							}
							if (headers.get(curHeader).equals("PassingAttempts")) {
								if (ready) {
									cs.setString(5, col);
								}
							}
							if (headers.get(curHeader).equals("ReceivingYards")) {
								if (ready) {
									cs.setString(6, col);
								}
							}
							if (headers.get(curHeader).equals("RushingAttempts")) {
								if (ready) {
									cs.setString(7, col);
								}
							}
							if (headers.get(curHeader).equals("PassingYards")) {
								if (ready) {
									cs.setString(8, col);
								}
							}
							if (headers.get(curHeader).equals("PassingTD")) {
								if (ready) {
									cs.setString(9, col);
								}
							}
							if (headers.get(curHeader).equals("RushingYards")) {
								if (ready) {
									cs.setString(10, col);
								}
							}
							if (headers.get(curHeader).equals("ReceivingTD")) {
								if (ready) {
									cs.setString(11, col);
								}
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

	public static void insertDefensivePlayer() {
		String defPlayers = csvFolder + "DefensivePlayers.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(defPlayers))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createDefensivePlayer(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
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
							if (headers.get(curHeader).equals("NumTackles")) {
								if (ready) {
									cs.setString(2, col);
								}
							}
							if (headers.get(curHeader).equals("Player")) {
								String[] name = col.split(" ");
								if (ready) {
									cs.setString(10, name[0]);
									String lname = "";
									for (int i = 1; i < name.length; i++) {
										lname += name[i];
										lname += " ";
									}
									lname = lname.substring(0, lname.length() - 1);
									cs.setString(9, lname);
								}
							}
							if (headers.get(curHeader).equals("PD")) {
								if (ready) {
									cs.setString(8, col);
								}
							}
							if (headers.get(curHeader).equals("Int")) {
								if (ready) {
									cs.setString(7, col);
								}
							}
							if (headers.get(curHeader).equals("Fumb")) {
								if (ready) {
									cs.setString(4, col);
								}
							}
							if (headers.get(curHeader).equals("FumbRec")) {
								if (ready) {
									cs.setString(5, col);
								}
							}
							if (headers.get(curHeader).equals("TD")) {
								if (ready) {
									cs.setString(6, col);
								}
							}
							if (headers.get(curHeader).equals("Sacks")) {
								if (ready) {
									cs.setString(3, col);
								}
							}
						} catch (Exception e) {
						}
						curHeader++;
					}
				}
				curHeader = 0;
				if (ready) {
//					System.out.println(cs.toString());
					cs.execute();
				}

				ready = true;
//				System.out.println("------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public static void insertSpecialTeamsPlayer() {
		String specPlayers = csvFolder + "SpecialTeamsPlayers.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(specPlayers))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createSpecialTeamsPlayer(?, ?, ?, ?, ?, ?, ?)}");
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
							if (headers.get(curHeader).equals("FGA")) {
								if (ready) {
									cs.setString(2, col);
								}
							}
							if (headers.get(curHeader).equals("Player")) {
								String[] name = col.split(" ");
								if (ready) {
									cs.setString(8, name[0]);
									String lname = "";
									for (int i = 1; i < name.length; i++) {
										lname += name[i];
										lname += " ";
									}
									lname = lname.substring(0, lname.length() - 1);
									cs.setString(7, lname);
								}
							}
							if (headers.get(curHeader).equals("FGM")) {
								if (ready) {
									cs.setString(3, col);
								}
							}
							if (headers.get(curHeader).equals("XPA")) {
								if (ready) {
									cs.setString(5, col);
								}
							}
							if (headers.get(curHeader).equals("XPM")) {
								if (ready) {
									cs.setString(4, col);
								}
							}
							if (headers.get(curHeader).equals("Y/P")) {
								if (ready) {
									cs.setString(6, col);
								}
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

	public static void insertStaff() {
		String staff = csvFolder + "Staff.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(staff))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createStaff(?, ?, ?, ?)}");
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
							if (headers.get(curHeader).equals("Role")) {
								if (ready)
									cs.setString(5, col);
							}
							if (headers.get(curHeader).equals("Staff Name")) {
								String[] name = col.split(" ");
								if (ready) {
									cs.setString(2, name[0]);
									String lname = "";
									for (int i = 1; i < name.length; i++) {
										lname += name[i];
										lname += " ";
									}
									lname = lname.substring(0, lname.length() - 1);
									cs.setString(3, lname);
								}
							}
							if (headers.get(curHeader).equals("Team Name")) {
								if (ready)
									cs.setString(4, col);
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

	public static void insertTeam() {

		// Name of the CSV file to read
		String teams = csvFolder + "Teams.csv";
		String line = "";
		String csvSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
		CallableStatement cs = null;
		boolean ready = false;
		try (BufferedReader br = new BufferedReader(new FileReader(teams))) {
			while ((line = br.readLine()) != null) {
				if (ready) {
					cs = con.prepareCall("{? = call createTeam(?, ?, ?, ?, ?, ?, ?)}");
					cs.registerOutParameter(1, Types.INTEGER);
				}
				if (isHeader) {
					String[] row = line.split(csvSplitBy);
					for (String col : row) {
						headers.add(col);
					}
					isHeader = false;
				} else {
					String[] row = line.split(csvSplitBy);
					for (String col : row) {
						try {
							if (headers.get(curHeader).equals("Name")) {
								if (ready)
									cs.setString(2, col);
							}
							if (headers.get(curHeader).equals("Location")) {
								if (ready)
									cs.setString(3, col);
							}
							if (headers.get(curHeader).equals("Division")) {
								if (ready)
									cs.setString(4, col);
							}
							if (headers.get(curHeader).equals("Conference")) {
								if (ready)
									cs.setString(5, col);
							}
							if (headers.get(curHeader).equals("Wins")) {
								if (ready)
									cs.setInt(6, Integer.parseInt(col));
							}
							if (headers.get(curHeader).equals("Losses")) {
								if (ready)
									cs.setInt(7, Integer.parseInt(col));
							}
							if (headers.get(curHeader).equals("SalaryCapSpace")) {
								if (ready)
									cs.setInt(8, Integer.parseInt(col));
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

	public static void insertPlayer() {
		String person = csvFolder + "Players.csv";
		String line = "";
		String cvsSplitBy = ",";
		boolean isHeader = true;
		int curHeader = 0;
		ArrayList<String> headers = new ArrayList<String>();
		Connection con = dbService.getConnection();
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
									cs.setString(2, name[0]);
									String lname = "";
									for (int i = 1; i < name.length; i++) {
										lname += name[i];
										lname += " ";
									}
									lname = lname.substring(0, lname.length() - 1);
									cs.setString(3, lname);
								}
							}
							if (headers.get(curHeader).equals("TeamName")) {
								if (ready)
									cs.setString(4, col);
							}
							if (headers.get(curHeader).equals("Total Contract Value")) {
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

}