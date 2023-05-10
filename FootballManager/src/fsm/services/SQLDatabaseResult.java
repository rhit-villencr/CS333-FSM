package fsm.services;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class SQLDatabaseResult {

	/**
	 * 
	 * @param connection
	 * @param useEmpty
	 * @return String[]
	 */
	/* Returns a 1d array of all nonempty table names in the given database */
	public static String[] getTables(DatabaseConnectionService connection, boolean useEmpty) {
		try {
			///// Initialize variables
			DatabaseMetaData metaData = connection.getConnection().getMetaData();
			String[] types = { "TABLE" };
			/////

			///// Retrieving the columns in the database and putting in resultString
			///// ArrayList
			ArrayList<String> resultString = new ArrayList<String>();
			ResultSet tables = metaData.getTables(null, null, "%", types);
			while (tables.next()) {
				if ((!tables.getString("TABLE_NAME").contains("trace_xe")
						&& !tables.getString("TABLE_NAME").contains("User"))
						&& (!(getResult(connection, tables.getString("TABLE_NAME")).length == 0) || useEmpty)) {
					resultString.add(tables.getString("TABLE_NAME"));
				}
			}
			/////

			///// Converts that array list into an array: str
			String[] str = new String[resultString.size()];
			for (int i = 0; i < resultString.size(); i++) {
				str[i] = resultString.get(i);
			}
			/////
			return str;
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
	public static Object[][] getResult(DatabaseConnectionService connection, String table) {
		try {
			///// Create a callable statement that calls the SPROC viewAll and set vars
			CallableStatement cs = connection.getConnection().prepareCall("{? = call viewAll(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, table);
			boolean results = cs.execute();
			/////

			///// Initialize 2d array list and parse through inputtin data
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (results) {
				ResultSet rs = cs.getResultSet();
				while (rs.next()) {
					ArrayList<String> temp = new ArrayList<String>();
					for (int i = 0; i < getHeaders(connection, table).length; i++) {
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
	public static String[] getHeaders(DatabaseConnectionService dcs, String tableName) {
		try {
			///// Creating a callable statement that calls a SPROC from the database
			CallableStatement cs = dcs.getConnection().prepareCall("{? = call getHeaders(?)}");
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, tableName);
			ArrayList<String> columnNames = new ArrayList<String>();
			boolean results = cs.execute();
			// Loop through the available result sets.
			while (results) {
				ResultSet rs = cs.getResultSet();
				// Retrieve data from the result set.
				while (rs.next()) {
					// using rs.getxxx() method to retrieve data
					columnNames.add(rs.getString("COLUMN_NAME"));
				}
				rs.close();
				// Check for next result set
				results = cs.getMoreResults();
			}
			cs.close();
			///// Convert said array list into a 1d string array
			String[] returnData = new String[columnNames.size()];
			for (int i = 0; i < columnNames.size(); i++) {
				returnData[i] = columnNames.get(i);
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

}
