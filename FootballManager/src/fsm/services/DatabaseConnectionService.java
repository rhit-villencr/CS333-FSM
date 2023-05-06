package fsm.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author villencr
 *
 */
public class DatabaseConnectionService {

	///// Globals
	private final String SampleURL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}}";
	private Connection connection = null;
	public String databaseName;
	private String serverName;
	private String username;
	private String password;
	/////

	/* Initialization */
	/**
	 * @param serverName
	 * @param databaseName
	 */
	public DatabaseConnectionService(String serverName, String databaseName) {
		this.serverName = serverName;
		this.databaseName = databaseName;
	}

	/* Connect to database */
	/**
	 * @param user
	 * @param pass
	 * @return boolean
	 * @throws Exception
	 */
	public boolean connect(String user, String pass) throws Exception {

		///// Set vars
		username = user;
		password = pass;
		String connectionUrl = SampleURL.replace("${dbServer}", serverName).replace("${dbName}", databaseName)
				.replace("${user}", user).replace("${pass}", pass);
		/////

		///// Attempt connection
		try {
			connection = DriverManager.getConnection(connectionUrl);
			return true;
		}
		/////

		/* if fails */
		catch (SQLException e) {
			throw new Exception();
		}
	}

	/**
	 * 
	 * @return Connection
	 */
	/* getter for connection */
	public Connection getConnection() {
		return this.connection;
	}

	/* Disconnect from database */
	public void closeConnection() {
		// TODO: Task 1
		try {
			if (!connection.isClosed()) {
				connection.close();
				System.out.println("Disconnected");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	/* Unused: quickly disconnect and then reconnect */
	public void refreshConnection() throws Exception {
		closeConnection();
		connect(username, password);
	}

}
