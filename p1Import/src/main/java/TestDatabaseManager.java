import java.sql.Connection;
import java.sql.Statement;



public class TestDatabaseManager {
	public static void main(String[] args) throws Exception
	   {  
		   SimpleDataSource.init("database.properties");
	       Connection conn = SimpleDataSource.getConnection();
		   DatabaseManager m = new DatabaseManager(conn);
		   m.getInputData("VehicleValues.dat");
		   m.writeDataFile("Vehicles.dat");
		   m.getInstanceFields(Vehicle.class);

	      m.deleteDatabase();
	      m.createDatabase();
	      m.insertValues("Vehicles.dat");
	      m.selectAll();
	      m.selectColumnOr("make", new String[]{"Toyota", "Chevy"});
	      m.selectColumnWhere("weight", "2500");
	      
	      
	      m.deleteDatabase();

	}
}

