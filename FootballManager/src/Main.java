import fsm.frame.Frame;
import fsm.services.DatabaseConnectionService;
import fsm.services.SQLDatabaseResult;

public class Main {

	public static void main(String[] args) {
		///// Temporarily initialize database settings
		String serverName = "titan.csse.rose-hulman.edu";
		String databaseName = "FootBall_Statistics_Manager";
		String username = "FSMOwner";
		String password = "Password123";
		/////

		///// Initialize DatabaseConnectionService class
		DatabaseConnectionService connection = new DatabaseConnectionService(serverName, databaseName);
		/////

		///// Attempt to connect, if failed, abort
		if (!connection.connect(username, password)) {
			System.out.println("failed Connection To: " + databaseName);
			System.exit(0);
		}
		System.out.println("Successfully Connected To: " + databaseName);
		/////

		///// Create new frame and run the startup function
		Frame frame = new Frame();
		frame.launch(connection);
		/////
	}

}
