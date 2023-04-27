package fsm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionService {

	//DO NOT EDIT THIS STRING, YOU WILL RECEIVE NO CREDIT FOR THIS TASK IF THIS STRING IS EDITED
	private final String SampleURL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}}";

	private Connection connection = null;

	public String databaseName;
	private String serverName;
	private String username;
	private String password;
	
	public DatabaseConnectionService(String serverName, String databaseName) {
		//DO NOT CHANGE THIS METHOD
		this.serverName = serverName;
		this.databaseName = databaseName;
	}

	public boolean connect(String user, String pass) {
		//TODO: Task 1
		//BUILD YOUR CONNECTION STRING HERE USING THE SAMPLE URL ABOVE
		username = user;
		password = pass;
		String connectionUrl = SampleURL
		        .replace("${dbServer}", serverName)
		        .replace("${dbName}", databaseName)
		        .replace("${user}", user)
		        .replace("${pass}", pass);
//		System.out.println(connectionUrl);
		try {
			connection = DriverManager.getConnection(connectionUrl);
            return true;
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	

	public Connection getConnection() {
		return this.connection;
	}
	
	public void closeConnection() {
		//TODO: Task 1
		try {
		if(!connection.isClosed()) {
			connection.close();
			System.out.println("Disconnected");
		}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void refreshConnection() {
		closeConnection();
		connect(username, password);
	}

}
