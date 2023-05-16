package fsm.services;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SQLDatabaseResult {
	
	public static String[] getPlayer(DatabaseConnectionService connection, String fName, String lName) {
		try {
			///// Creating a callable statement that calls a SPROC from the database
			CallableStatement cs = connection.getConnection().prepareCall("{? = call viewSelectPlayer(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, fName);
			cs.setString(3, lName);
			ArrayList<String> row = new ArrayList<String>();
			boolean results = cs.execute();
			while (results) {
				ResultSet rs = cs.getResultSet();
				// Retrieve data from the result set.
				while (rs.next()) {
					// using rs.getxxx() method to retrieve data
					int i = 1;
					while(true) {
						try {
							row.add(rs.getString(i));
						}catch(SQLException e) {
							break;
						}
						i++;
					}
					
				}
				rs.close();
				// Check for next result set
				results = cs.getMoreResults();
			}
			cs.close();
			
			String[] returnData = new String[row.size()];
			for (int i = 0; i < row.size(); i++) {
				returnData[i] = row.get(i);
			}
			if(returnData.length == 0) return null;
			/////
			return returnData;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[] getTeams(DatabaseConnectionService connection) {
		try {
			///// Creating a callable statement that calls a SPROC from the database
			CallableStatement cs = connection.getConnection().prepareCall("{? = call getTeams()}");
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
	 * @param connection
	 * @param userName
	 * @param favTeam
	 * @param favPlayerFName
	 * @param favPlayerLName
	 */
	/* Update User Profile */
	public static void updateUser(DatabaseConnectionService connection, String userName, String favTeam,
			String favPlayerFName, String favPlayerLName) {
		try {
			///// Create a callable statement to updateUser
			CallableStatement cs = connection.getConnection().prepareCall("{? = call updateUser(?, ?, ?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);

			cs.setString(2, userName);
			cs.setString(3, favTeam);
			cs.setString(4, favPlayerFName);
			cs.setString(5, favPlayerLName);
//			System.out.println(userName + "\n" + favTeam + "\n" + favPlayerFName + "\n" + favPlayerLName);

			cs.execute();
			/////

			///// Check return values of the SPROC
			int returnValue = cs.getInt(1);

			if (returnValue == 0) {
				JOptionPane.showMessageDialog(null, "Update Successful");
			} else if (returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Improper Inputs");
			}
			/////

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Update user not working.");
		}
	}

	/**
	 * 
	 * @param connection
	 * @param table
	 * @return Object[][]
	 */
	/* Returns a 2d array of the contents of a given table in a given database */
	public static Object[][] getResult(DatabaseConnectionService connection, String team, String type, String pos) {
		try {
			///// Create a callable statement that calls the SPROC viewAll and set vars
			CallableStatement cs = connection.getConnection().prepareCall("{? = call viewTeam" + type + "(?, ?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, team);
			cs.setString(3, pos);
			boolean results = cs.execute();
			/////

			///// Initialize 2d array list and parse through inputtin data
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (results) {
				ResultSet rs = cs.getResultSet();
//				System.out.println(getHeaders(connection, team)[0]);
				while (rs.next()) {
					ArrayList<String> temp = new ArrayList<String>();
					
					for (int i = 0; i < getHeaders(connection, type).length; i++) {
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
	 * @param dcs
	 * @param tableName
	 * @return String[]
	 */
	/* Returns a 1d array of the headers of a given table in a given database */
	public static String[] getHeaders(DatabaseConnectionService dcs, String type) {
		String[] headers = null;
		
		if(type.equals("Players")) {
			String[] pl = {"FirstName", "LastName", "Position", "Salary"};
			headers = pl;
		}
		if(type.equals("Staff")) {
			String[] pl = {"FirstName", "LastName", "Role"};
			headers = pl;
		}
		
		return headers;
	}

}
