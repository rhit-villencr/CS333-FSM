package fsm.services;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLDatabaseResult {

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

	/* Returns a 2d array of the contents of a given table in a given database */
	public static Object[][] getResult(DatabaseConnectionService connection, String table) {
		try {
			///// Creating a query and querying the database
			// TODO:Prevent SQL Injection Attacks
			Statement statement = connection.getConnection().createStatement();
			String selectSql = "SELECT * from " + table;
			ResultSet rs = statement.executeQuery(selectSql);
			/////

			///// Convert data into array list
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (rs.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < getHeaders(connection, table).length; i++) {
					temp.add(rs.getString(i + 1));
				}
				data.add(temp);
			}
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

	/* Returns a 1d array of the headers of a given table in a given database */
	public static String[] getHeaders(DatabaseConnectionService dcs, String tableName) {
		try {
			///// Creating a query and querying the database
			// TODO:Prevent SQL Injection Attacks
			Statement statement = dcs.getConnection().createStatement();
			String selectSql = "SELECT * from " + tableName;
			ResultSet rs = statement.executeQuery(selectSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			/////

			///// Convert meta data into array list of column headers
			ArrayList<String> columnNames = new ArrayList<String>();
			for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
				columnNames.add(rsmd.getColumnName(i));
			}
			/////

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
