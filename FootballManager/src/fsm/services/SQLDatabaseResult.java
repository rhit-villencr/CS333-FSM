package fsm.services;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLDatabaseResult {
	public static Object[][] getResult(Connection connection, String table) {
		try {
			Statement statement = connection.createStatement();
			String selectSql = "SELECT * from " + table;
			ResultSet rs = statement.executeQuery(selectSql);
			ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
			while (rs.next()) {
				data.add(new ArrayList<>(Arrays.asList(rs.getString(1), rs.getString(3), rs.getString(2), rs.getString(4))));
			}
			Object[][] returnData = new String[data.size()][];
	        for (int i = 0; i < data.size(); i++) {
	            ArrayList<String> rowList = data.get(i);
	            String[] rowArray = new String[rowList.size()];
	            for (int j = 0; j < rowList.size(); j++) {
	                rowArray[j] = rowList.get(j);
	            }
	            returnData[i] = rowArray;
	        }
			return returnData;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String[] getHeaders(DatabaseConnectionService dcs, String tableName) {
		try {
			Statement statement = dcs.getConnection().createStatement();
			String selectSql = "SELECT * from " + tableName;
			ResultSet rs = statement.executeQuery(selectSql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList<String> columnNames = new ArrayList<String>();
			for(int i = 1; i < rsmd.getColumnCount()+1; i++) {
				columnNames.add(rsmd.getColumnName(i));
			}
			String[] returnData = new String[columnNames.size()];
	        for (int i = 0; i < columnNames.size(); i++) {
	            returnData[i] = columnNames.get(i);
	        }
	        return returnData;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
