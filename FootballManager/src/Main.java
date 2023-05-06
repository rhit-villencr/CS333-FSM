import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;
import fsm.frame.Frame;
import fsm.services.DatabaseConnectionService;

/**
 * @author villencr
 *
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		///// Declare variables
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
		DatabaseConnectionService connection = new DatabaseConnectionService(serverName, databaseName);
		/////

		///// Attempt to connect, if failed, abort
		try {
			connection.connect(username, password);
			System.out.println("Successfully Connected To: " + databaseName);

		} catch (Exception e) {
			System.out.println("Failed Connection To: " + databaseName);
			System.exit(0);
		}

		/////

		///// Create new frame and run the startup function
		Frame frame = new Frame();
		frame.launchLogin(connection);
		/////
	}
}
