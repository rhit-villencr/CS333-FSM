import fsm.frame.Frame;
import fsm.services.DatabaseConnectionService;
import fsm.services.SQLDatabaseResult;

public class Main {

	public static void main(String[] args) {
		String serverName = "titan.csse.rose-hulman.edu";
		String databaseName = "FootBall_Statistics_Manager";
		String username = "FSMOwner";
		String password = "Password123";
		
		DatabaseConnectionService connection = new DatabaseConnectionService(serverName, databaseName);
		
		if(!connection.connect(username, password)) {
			System.out.println("failed Connection To: " + databaseName + "\n");
			return;
		}
		System.out.println("Successfully Connected To: " + databaseName + "\n");
//		System.out.println(connection.getConnection());
		
		Frame frame = new Frame();
		
		frame.viewTable("Person", connection);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		frame.viewTable("Player");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		frame.viewTable("Team");
	}

}

