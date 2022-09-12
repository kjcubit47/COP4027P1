//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.Statement;
//import java.util.Random;
//import java.util.Scanner;
//import java.lang.reflect.*;
//
///**
//   Tests a database installation by creating and querying
//   a sample table.
//*/
//public class TestDB 
//{
//   public static void main(String[] args) throws Exception
//   { DatabaseManager t = new DatabaseManager(); 
//   t.getInputData("VehicleValues.dat");
//   t.writeDataFile("Vehicles.dat");
//   
//	   Random rand = new Random();
//	   PrintWriter vehicleWriter = null;
//	  // FileInputStream vehicleValueReader = null;
//	   Scanner fileScanner = null;
//	   String line;
//	   Class<Vehicle> vehicle = Vehicle.class;
//	   Class v = Class.forName("Vehicle");
//	   try {
//		   vehicleValueReader = new FileInputStream("VehicleValues.dat");
//	   }
//	   catch(IOException e) {
//		   System.out.println("VehicleValues.dat could not be opened.");
//		   e.printStackTrace();
//	   }
//	  
//	   fileScanner = new Scanner(vehicleValueReader);
//	   line = fileScanner.nextLine();
//	   String[] makeValues =  line.split(",");
//	   line = fileScanner.nextLine();
//	   String[] sizeValues = line.split(",");
//
//	   
//	   try {
//		   vehicleWriter = new PrintWriter("Vehicles.dat");	   
//	   }
//	   catch(IOException e)
//	   {
//		   System.out.println("Vehicles.dat could not be created.\n");
//		   e.printStackTrace();
//	   }
//	   
//	   System.out.println("Creating Vehicles.dat");
//
//	   for (int i = 0; i < 10; i++) {
//		   int temp = 0;
//		   temp = rand.nextInt(5);
//		   vehicleWriter.print(makeValues[temp] + ",");
//		   temp = rand.nextInt(1500, 4001);
//		   if(temp >= 1500 && temp<= 2000) {
//			   vehicleWriter.print(sizeValues[0] + ",");
//			   vehicleWriter.println(temp);
//		   }
//		   
//		   if(temp > 2000 && temp <=2500) {
//			   vehicleWriter.print(sizeValues[1] + ",");
//			   vehicleWriter.println(temp);
//		   }
//		   if (temp > 2500 && temp <= 4000){
//			   vehicleWriter.print(sizeValues[2] + ",");
//			   vehicleWriter.println(temp);
//		   }
//		   
//		   
//	   }
//	   
//	   fileScanner.close();
//	   vehicleWriter.close();
//
//   }
//}
//      SimpleDataSource.init("database.properties");
//      
//      Connection conn = SimpleDataSource.getConnection();
//      Statement stat = conn.createStatement();     
// 	   try {  
//		  stat.execute("DROP TABLE Test2"); 
//      }
//	   catch (Exception e)
//		{ System.out.println("drop failed"); }      
//
//      try
//      {
//   
//         stat.execute("CREATE TABLE Test2 (Name CHAR(20),Age INTEGER, Active BOOLEAN)");
//         stat.execute("INSERT INTO Test2 VALUES ('Romeo',27, true)");
//         stat.execute("INSERT INTO Test2 VALUES ('Juliet',25, true)");
//         stat.execute("INSERT INTO Test2 VALUES ('Tom',64, true)");
//         stat.execute("INSERT INTO Test2 VALUES ('Dick',55, false)");
//         stat.execute("INSERT INTO Test2 VALUES ('Harry',33, true)");
//         ResultSet result = stat.executeQuery("SELECT * FROM Test2");
//			  
//			System.out.println("after inserts");
//			ResultSetMetaData rsm = result.getMetaData();
//			int cols = rsm.getColumnCount();
//			  while(result.next())
//			  {
//			    for(int i = 1; i <= cols; i++)
//               System.out.print(result.getString(i)+" ");
//             System.out.println("");      
//			  }
//			try {  
//		     stat.execute("DROP TABLE Test2"); 
//         }
//			catch (Exception e)
//			{ System.out.println("drop failed"); }    
//		}
//      finally
//      {
//         conn.close();
//			System.out.println("dropped Table Test2, closed connection and ending program");  
//      }
//   }
//}
