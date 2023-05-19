package fsm.services;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SQLDatabaseResult {

	public static String[] getPlayer(DatabaseConnectionService dbService, String fName, String lName) {
		try {
			///// Creating a callable statement that calls a SPROC from the database
			CallableStatement cs = dbService.getConnection().prepareCall("{? = call viewSelectPlayer(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, fName);
			cs.setString(3, lName);
			ArrayList<String> row = new ArrayList<String>();
			boolean results = cs.execute();
			while (results) {
				ResultSet rs = cs.getResultSet();
				while (rs.next()) {
					int i = 1;
					while (true) {
						try {
							row.add(rs.getString(i));
						} catch (SQLException e) {
							break;
						}
						i++;
					}
				}
				rs.close();
				results = cs.getMoreResults();
			}
			int returnValue = cs.getInt(1);
			if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "Player Does Not Exist");
			}
			cs.close();

			String[] returnData = new String[row.size()];
			for (int i = 0; i < row.size(); i++) {
				returnData[i] = row.get(i);
			}
			if (returnData.length == 0)
				return null;
			return returnData;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getTeams(DatabaseConnectionService dbService) {
		try {
			///// Creating a callable statement that calls a SPROC from the database
			CallableStatement cs = dbService.getConnection().prepareCall("{? = call getTeams()}");
			cs.registerOutParameter(1, Types.INTEGER);
			ArrayList<String> TeamNames = new ArrayList<String>();
			boolean results = cs.execute();
			// Loop through the available result sets.
			while (results) {
				ResultSet rs = cs.getResultSet();
				// Retrieve data from the result set.
				while (rs.next()) {
					// using rs.getxxx() method to retrieve data
					TeamNames.add(rs.getString("Name"));
				}
				rs.close();
				// Check for next result set
				results = cs.getMoreResults();
			}
			cs.close();
			///// Convert said array list into a 1d string array
			String[] returnData = new String[TeamNames.size()];
			for (int i = 0; i < TeamNames.size(); i++) {
				returnData[i] = TeamNames.get(i);
			}
			/////
			return returnData;
		}
		/* Checking for SQL errors */
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param dbService
	 * @param userName
	 * @param favTeam
	 * @param favPlayerFName
	 * @param favPlayerLName
	 */
	/* Update User Profile */
	public static void updateUser(DatabaseConnectionService dbService, String userName, String favTeam,
			String favPlayerFName, String favPlayerLName) {
		try {
			///// Create a callable statement to updateUser
			CallableStatement cs = dbService.getConnection().prepareCall("{? = call updateUser(?, ?, ?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, userName);
			cs.setString(3, favTeam);
			cs.setString(4, favPlayerFName);
			cs.setString(5, favPlayerLName);
			cs.execute();
			/////

			///// Check return values of the SPROC
			int returnValue = cs.getInt(1);
			if (returnValue == 0) {
				JOptionPane.showMessageDialog(null, "Update Successful");
			} else if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Improper Team Name");
			} else if (returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: Improper Name");
			}
			/////

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Update user not working.");
		}
	}

	/**
	 * 
	 * @param dbService
	 * @param table
	 * @return Object[][]
	 */
	/* Returns a 2d array of the contents of a given table in a given database */
	public static Object[][] getResult(DatabaseConnectionService dbService, String team, String type, String pos) {
		try {
			///// Create a callable statement that calls the SPROC viewAll and set vars
			CallableStatement cs = dbService.getConnection().prepareCall("{? = call viewTeam" + type + "(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, team);
			cs.setString(3, pos);
			boolean results = cs.execute();
			/////

			///// Initialize 2d array list and parse through inputtin data
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (results) {
				ResultSet rs = cs.getResultSet();
				while (rs.next()) {
					ArrayList<String> temp = new ArrayList<String>();

					for (int i = 0; i < getHeaders(dbService, type).length; i++) {
						temp.add(rs.getString(i + 1));
					}
					data.add(temp);
				}
				rs.close();
				results = cs.getMoreResults();
			}
			cs.close();
			/////

			///// Convert said array list into a 2d string array
			Object[][] returnData = new String[data.size()][];
			for (int i = 0; i < data.size(); i++) {
				ArrayList<String> rowList = data.get(i);
				String[] rowArray = new String[rowList.size()];
				for (int j = 0; j < rowList.size(); j++) {
					rowArray[j] = rowList.get(j);
				}
				returnData[i] = rowArray;
			}
			/////
			return returnData;

		}

		/* Checking for SQL errors */
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param dbService
	 * @param tableName
	 * @return String[]
	 */
	/* Returns a 1d array of the headers of a given table in a given database */
	public static String[] getHeaders(DatabaseConnectionService dbService, String type) {
		String[] headers = null;
		if (type.equals("Players")) {
			String[] pl = { "FirstName", "LastName", "Position", "Salary" };
			headers = pl;
		}
		if (type.equals("Staff")) {
			String[] pl = { "FirstName", "LastName", "Role" };
			headers = pl;
		}
		return headers;
	}

	public static void addPlayer(DatabaseConnectionService dbService, String fName, String lName, String age,
			String playerNumber, String salary, String teamName, String position) {
		try {
			///// Create a callable statement that calls the SPROC viewAll and set vars
			CallableStatement cs = dbService.getConnection()
					.prepareCall("{? = call createPlayer(?, ?, ?, ?, ?, ?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			if (fName.equals("")) {
				cs.setString(2, null);
			} else {
				cs.setString(2, fName);
			}

			if (lName.equals("")) {
				cs.setString(3, null);
			} else {
				cs.setString(3, lName);
			}

			if (teamName.equals("")) {
				cs.setString(4, null);
			} else {
				cs.setString(4, teamName);
			}

			if (salary.equals("")) {
				cs.setString(5, null);
			} else {
				cs.setString(5, salary);
			}

			if (position.equals("")) {
				cs.setString(6, null);
			} else {
				cs.setString(6, position);
			}

			if (playerNumber.equals("")) {
				cs.setString(7, null);
			} else {
				cs.setString(7, playerNumber);
			}

			if (age.equals("")) {
				cs.setString(8, null);
			} else {
				cs.setString(8, age);
			}

			cs.execute();

			int returnValue = cs.getInt(1);

			if (returnValue == 0) {
				JOptionPane.showMessageDialog(null, "Successfully Added Player");
			} else if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "First Name Cannot Be Null");
			} else if (returnValue == 2) {
				JOptionPane.showMessageDialog(null, "Last Name Cannot Be Null");
			} else if (returnValue == 3) {
				JOptionPane.showMessageDialog(null, "Already Exists");
			} else if (returnValue == 4) {
				JOptionPane.showMessageDialog(null, "Position Cannot Be Null");
			} else if (returnValue == 5) {
				JOptionPane.showMessageDialog(null, "Team Cannot Be Null");
			} else if (returnValue == 6) {
				JOptionPane.showMessageDialog(null, "Not a Team");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void deletePlayer(DatabaseConnectionService dbService, String fName, String lName) {
		try {
			///// Create a callable statement that calls the SPROC viewAll and set vars
			CallableStatement cs = dbService.getConnection().prepareCall("{? = call deletePlayer(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			if (fName.equals("")) {
				cs.setString(2, null);
			} else {
				cs.setString(2, fName);
			}

			if (lName.equals("")) {
				cs.setString(3, null);
			} else {
				cs.setString(3, lName);
			}

			cs.execute();

			int returnValue = cs.getInt(1);

			if (returnValue == 0) {
				JOptionPane.showMessageDialog(null, "Successfully Removed Player");
			} else if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "Player Doesn't Exist");
			} else if (returnValue == 2) {
				JOptionPane.showMessageDialog(null, "First Name Cannot Be Null");
			} else if (returnValue == 3) {
				JOptionPane.showMessageDialog(null, "Last Name Cannot Be Null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
