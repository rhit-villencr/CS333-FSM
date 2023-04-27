import java.util.Scanner;
import fsm.frame.Frame;
import fsm.services.DatabaseConnectionService;

public class Main {

	///// Developer Bool
	static boolean useLogin = false;
	/////

	///// Username and password variables
	static String username;
	static String password;
	/////

	/* Method attempts to log in, if failed, increases fail counter */
	public static int attemptLogin(int attempts, String databaseName) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the username for " + databaseName + ": ");
		username = sc.nextLine();
		System.out.println("Enter the password for " + databaseName + ": ");
		password = sc.nextLine();
		attempts++;
		return attempts;
	}

	public static void main(String[] args) {
		///// Temporarily initialize database settings
		String serverName = "titan.csse.rose-hulman.edu";
		String databaseName = "FootBall_Statistics_Manager";
		username = "FSMOwner";
		password = "Password123";
		/////

		///// First login attempt
		int numAtt = 0;
		if (useLogin) {
			numAtt = attemptLogin(0, databaseName);
		}
		/////

		///// Initialize DatabaseConnectionService class
		DatabaseConnectionService connection = new DatabaseConnectionService(serverName, databaseName);
		/////

		///// Attempt to connect, if failed, abort
		while (true) {
			try {
				connection.connect(username, password);
				System.out.println("Successfully Connected To: " + databaseName);
				break;

			} catch (Exception e) {
				System.out.println("Failed Connection To: " + databaseName);
				numAtt = attemptLogin(numAtt, databaseName);
				if (numAtt >= 3) {
					System.out.println("Too many failed login attempts...\nShutting Down...");
					System.exit(0);
				}

			}
		}
		/////

		///// Create new frame and run the startup function
		Frame frame = new Frame();
		frame.launch(connection);
		/////
	}

}
