
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DatabaseManager {

	private List<String> dataValues;
	private Class classLoader;
	private Field[] classFields;
	private FileWriter log;
	private Connection conn;
	String dbName;
	
	public DatabaseManager() {
		initLog();
		dbName = null;
		dataValues = new ArrayList<String>();
		classFields = null;
		classLoader = null;
		conn = null;
		
	}
	
	public DatabaseManager(Connection conn) {
		initLog();
		dbName = null;
		dataValues = new ArrayList<String>();
		classFields = null;
		classLoader = null;
		this.conn = conn;
	}
	
	public void setDBName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getDBName() {
		return dbName;
	}
	
	public void getInputData(String filename) {
		FileInputStream dataValueReader = null;
		try {
			   dataValueReader = new FileInputStream(filename);
		   }
		   catch(IOException e) {
			   System.out.println(filename + "could not be opened.");
			   e.printStackTrace();
		   }
		System.out.println("Getting values from " + filename);
		Scanner fileScanner = new Scanner(dataValueReader);
		String line = fileScanner.nextLine();
		Collections.addAll(dataValues, line.split(","));
		dataValues.add(";");
		line = fileScanner.nextLine();
		Collections.addAll(dataValues, line.split(","));
		fileScanner.close();
	}
	

	public void writeDataFile(String filename) {
		PrintWriter dataWriter = null;
		try {
			   dataWriter = new PrintWriter(filename);	   
		   }
		   catch(IOException e)
		   {
			   System.out.println(filename + "could not be created.\n");
			   e.printStackTrace();
		   }
		   
		   System.out.println("Creating Vehicles.dat");
		   Random rand = new Random();
			int dataSeparator = 0;
			for (int i = 0; i < dataValues.size(); i++) {
				   if (dataValues.get(i) == ";")
					   dataSeparator = i;
			   }
		   List<String> dataSet1 = (List<String>) dataValues.subList(0, dataSeparator);
		   List<String> dataSet2 = (List<String>) dataValues.subList(dataSeparator + 1, dataValues.size());
		   for (int i = 0; i < 10; i++) {
			   int temp = 0;
			   Double temp2 = 0.0;
			   temp = rand.nextInt(dataSet1.size());
			   dataWriter.print(dataSet1.get(temp) + ",");
			   temp2 = rand.nextDouble(1500, 4001);
			   if(temp2 >= 1500 && temp2<= 2000) {
				   dataWriter.print(dataSet2.get(0) + ",");
				   dataWriter.printf("%.2f", temp2);
				   dataWriter.print(",");		
			   }
			   if(temp2 > 2000 && temp2 <=2500) {
				   dataWriter.print(dataSet2.get(1) + ",");
				   dataWriter.printf("%.2f", temp2);
				   dataWriter.print(",");
			   }
			   if (temp2 > 2500 && temp2 <= 4000){
				   dataWriter.print(dataSet2.get(2) + ",");
				   dataWriter.printf("%.2f", temp2);
				   dataWriter.print(",");
			   }
			   temp2 = rand.nextDouble(1.7, 6.0);
			   dataWriter.printf("%.2f", temp2);
			   dataWriter.print(",");
			   temp = rand.nextInt(0 ,2);
			   if(temp > 0)
				   dataWriter.println("true");
			   else
				   dataWriter.println("false");
		   }
		   dataWriter.close();
	}
	
	public void getInstanceFields(Class cl) {
		try {
			classLoader = Class.forName(cl.getName());
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
			e.printStackTrace();
		}
		classFields = classLoader.getDeclaredFields();
		for (Field f: classFields)
		{
			System.out.println(f.getName());
		}
		setDBName(classLoader.getName());
	}

	public void createDatabase() {
		Statement createDB;
		try {
			System.out.println("Creating database.");
			writeLog("Creating database.");
			createDB = conn.createStatement();
			createDB.execute("CREATE TABLE " + getDBName() +
			"(" + classFields[0].getName() + " CHAR (20)," +
			classFields[1].getName() + " CHAR(20), " +
			classFields[2].getName() + " DOUBLE, " + 
			classFields[3].getName() + " DOUBLE, " +
			classFields[4].getName() + " BOOLEAN) ");
			System.out.println("Table created");
			writeLog("Table Created.");
		}
		catch(SQLException e) {
			System.out.println("Table was not created");
			e.printStackTrace();
		}
	}
	
	public void deleteDatabase() {
		Statement deleteStatement;
		String deleteDB = "DROP TABLE " + getDBName();
		System.out.println("Dropping table " + getDBName() + ".");
		writeLog("Dropping table " + getDBName() + ".");
		try {
			deleteStatement = conn.createStatement();
			deleteStatement.execute(deleteDB);
			System.out.println("Table dropped.");
			writeLog("Table dropped.");
		}
		catch(Exception e){
			System.out.println("Table was not dropped.");
			writeLog("Table was not dropped.");
		}
	}
	
	public void selectAll() {
		System.out.println("Issuing query: SELECT * FROM " + getDBName() + ".");
		writeLog("Issuing query: SELECT * FROM " + getDBName() + ".");
		PreparedStatement selectQuery;
		String query = "SELECT * FROM " + getDBName();

		try {
			selectQuery = conn.prepareStatement(query);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);
		} catch (SQLException e) {
			System.out.println("Query: " + "SELECT * FROM " + getDBName() + " failed.");
			e.printStackTrace();
		}
	}

	public void selectColumnOr(String columnName, String[] condition) {
		PreparedStatement selectQuery;
		System.out.println("Issuing query: SELECT * FROM " + getDBName() + " WHERE " + columnName + " = " + condition[0] + " OR " + columnName + " = " + condition[1]);
		writeLog("Issuing query: SELECT * FROM " + getDBName() + " WHERE " + columnName + " = " + condition[0] + " OR " + columnName + " = " + condition[1]);
		String query = "SELECT * FROM " + getDBName() + " WHERE " + columnName + " = ? OR " + columnName + " = ?";
		
		try {
			selectQuery = conn.prepareStatement(query);
			selectQuery.setString(1, condition[0] );
			selectQuery.setString(2, condition[1]);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);

		} catch (SQLException e) {
			System.out.println("Query: SELECT * FROM " + getDBName() + " WHERE " + columnName + " = " + condition[0] + " OR " + columnName + " = " + condition[1] + " failed.");
			e.printStackTrace();
		}
	}
	
	public void selectColumnWhere(String columnName, String condition) {
		PreparedStatement selectQuery;
		System.out.println("Issuing query: SELECT * FROM " + getDBName() + " WHERE " + columnName + " > " + condition);
		writeLog("Issuing query: SELECT * FROM " + getDBName() + " WHERE " + columnName + " > " + condition);
		String query = "SELECT * FROM " + getDBName() + " WHERE " + columnName + " > ?";
		try {
			selectQuery = conn.prepareStatement(query);
			selectQuery.setString(1, condition);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);
			
			
		} catch (SQLException e) {
			System.out.println("Query : SELECT * FROM " + getDBName() + " WHERE " + columnName + " > " + condition + " failed");
			writeLog("Query : SELECT * FROM " + getDBName() + " WHERE " + columnName + " > " + condition + " failed");
			e.printStackTrace();
		}
	}
	
	public void insertValues(String filename) {
		PreparedStatement insert;
		FileInputStream randomizedValues;
		Scanner fileScanner = null;
		String insertQuery= "INSERT INTO " + getDBName() + " VALUES (?,?,?,?,?)";
		String values;
		String[] valuesAsArray;
		System.out.println("Inserting values into " + getDBName());
		try {
			insert = conn.prepareStatement(insertQuery);
			try {
				randomizedValues = new FileInputStream(filename);
				fileScanner = new Scanner(randomizedValues);
			} catch (FileNotFoundException e) {
				System.out.println("File was not opened");
			}
			while(fileScanner.hasNext()) {

				values = fileScanner.nextLine();
				valuesAsArray = values.split(",");
				for (int i = 0; i < valuesAsArray.length; i++) {
					insert.setString(i + 1, valuesAsArray[i]);
				}
				insert.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.out.println("Values were not inserted into " + getDBName());
			e.printStackTrace();
		}
	}
	private void printQuery(ResultSet queryResult) throws SQLException {
		ResultSetMetaData rsm = queryResult.getMetaData();
		int cols = rsm.getColumnCount();

		for(int i = 1; i <= cols; i++) {
		         System.out.print(rsm.getColumnName(i) + "                 ");
		}
		System.out.println("");
		  while(queryResult.next())
		  {
		    for(int i = 1; i <= cols; i++) {
		    	String r = queryResult.getString(i);
		    	if(i == 4) {
		    	System.out.print(String.format("%17d", 0));
		    	}
		    	if(i == 5) {
		    		System.out.print("                     ");
		    	}
		    	System.out.print(queryResult.getString(i) + " ");
		    }
	    	System.out.println("");   
		  }
	}
	private void initLog() {
		try {
			log = new FileWriter("dbOperations.log");
			PrintWriter initLogWriter = new PrintWriter(log);
			initLogWriter.println("Database Log");
			initLogWriter.println("#############");
			log.close();
		} catch (IOException e) {
			System.out.println("Log was not erased.");
		}
	}
	
	private void writeLog(String message) {
		try {
			log = new FileWriter("dbOperations.log", true);
			PrintWriter logWriter = new PrintWriter(log);
			logWriter.println(message);
			logWriter.close();
		}
		catch(IOException e) {
			System.out.println("Log could not be written.");
		}
	}
	
	/*
	 * refactor rename or delete this  
	 */
	public void executeStatement(String statement) throws SQLException {
		Statement s = conn.createStatement();
		ResultSet r = s.executeQuery(statement);
		printQuery(r);
		
	}
}
