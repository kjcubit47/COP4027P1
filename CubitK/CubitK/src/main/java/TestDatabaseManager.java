/******************************************************************
Student Name: Kyle Cubit
File Name: TestDatabaseManager.java  
Assignment Number: Project 1

Comments- The main method used to show the functionality of the DatabaseManager Class.

*******************************************************************/
import java.sql.Connection;

public class TestDatabaseManager {
	public static void main(String[] args) throws Exception {
		
		// Getting a connection
		SimpleDataSource.init("database.properties");
		Connection conn = SimpleDataSource.getConnection();
		
		// Creating the DatabaseManager
		DatabaseManager vehicleDatabase = new DatabaseManager(conn);
		
		// Reading the values for creation of Vehicle objects
		vehicleDatabase.getInputData("VehicleValues.dat");
		
		// Writing randomized Vehicle data
		vehicleDatabase.writeDataFile("Vehicles.dat");
		
		// Getting instance fields from class Vehicle
		vehicleDatabase.getInstanceFields(Vehicle.class);

		// Deleting Vehicle table if it exists
		vehicleDatabase.deleteDatabase();
		
		// Creating Vehicle table
	    vehicleDatabase.createDatabase();
	    
	    // Inserting the randomized values into the table
	    vehicleDatabase.insertValues("Vehicles.dat");
	    
	    // Selecting all rows from the table
	    vehicleDatabase.selectAll();
	    
	    // Selecting rows where the make column matches Toyota or Chevy
	    vehicleDatabase.selectColumnOr("make", new String[]{"Toyota", "Chevy"});
	    
	    // Selecting rows where the column weight > 2500
	    vehicleDatabase.selectGreater("weight", "2500");
	    
	    // Print the database log
	    vehicleDatabase.printLog();
	    
	    // Deleting the table
	    vehicleDatabase.deleteDatabase();

	}
}

