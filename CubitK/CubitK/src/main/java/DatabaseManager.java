/******************************************************************
Student Name: Kyle Cubit
File Name: DatabaseManager.java  
Assignment Number: Project 1

Comments- This class manages a derby database. It uses reflection to get information from a class in order to create a table in the database.

*******************************************************************/
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
	String tableName;
	
	/**
	 * Creates a DatabaseManager
	 */
	public DatabaseManager() {
		initLog();
		tableName = null;
		dataValues = new ArrayList<String>();
		classFields = null;
		classLoader = null;
		conn = null;
		
	}
	
	/**
	 * Creates a DatabaseManager with an initial Connection
	 * @param conn The initial Connection
	 */
	public DatabaseManager(Connection conn) {
		initLog();
		tableName = null;
		dataValues = new ArrayList<String>();
		classFields = null;
		classLoader = null;
		this.conn = conn;
	}
	
	/**
	 * Sets the table name
	 * @param tableName the table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	/**
	 * Returns the table name
	 * @return tableName the table name
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * Gets the input data required to create randomized objects
	 * @param filename the file which contains the data used for classifying the objects
	 */
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
	
	/**
	 * Writes a file given a file name to write randomized object values to
	 * @param filename the file name that the user chooses
	 */
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
	
	/**
	 * Gets the instance fields of a class using reflection
	 * @param cl The class that instance fields will be taken from
	 */
	public void getInstanceFields(Class cl) {
		try {
			classLoader = Class.forName(cl.getName());
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found");
			e.printStackTrace();
		}
		classFields = classLoader.getDeclaredFields();
//		for (Field f: classFields)
//		{
//			System.out.println(f.getName());
//		}
		setTableName(classLoader.getName());
	}

	/**
	 * Creates a table in the database using the fields from the getInstanceFields() method
	 */
	public void createDatabase() {
		Statement createDB;
		try {
			System.out.println("Creating table "+ getTableName() + " in the database.");
			writeLog("Creating database.");
			createDB = conn.createStatement();
			createDB.execute("CREATE TABLE " + getTableName() +
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
	
	/**
	 * Deletes the table in the database
	 */
	public void deleteDatabase() {
		Statement deleteStatement;
		String deleteDB = "DROP TABLE " + getTableName();
		System.out.println("Dropping table " + getTableName() + ".");
		writeLog("Issuing command: " + deleteDB);
		try {
			deleteStatement = conn.createStatement();
			deleteStatement.execute(deleteDB);
			System.out.println("Table dropped.");
		}
		catch(Exception e){
			System.out.println("Table was not dropped.");
			writeLog("Command: " + deleteDB + " failed.");
		}
	}
	
	/**
	 * Selects all rows from the table
	 */
	public void selectAll() {
		System.out.println("Issuing query: SELECT * FROM " + getTableName() + ".");
		writeLog("Issuing query: SELECT * FROM " + getTableName() + ".");
		PreparedStatement selectQuery;
		String query = "SELECT * FROM " + getTableName();

		try {
			selectQuery = conn.prepareStatement(query);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);
		} catch (SQLException e) {
			System.out.println("Query: " + "SELECT * FROM " + getTableName() + " failed.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Selects rows from the table with an or condition
	 * @param columnName the column ehich will be queried
	 * @param conditions the conditions that will be used in the query
	 */
	public void selectColumnOr(String columnName, String[] conditions) {
		PreparedStatement selectQuery;
		System.out.println("Issuing query: SELECT * FROM " + getTableName() + " WHERE " + columnName + " = " + conditions[0] + " OR " + columnName + " = " + conditions[1]);
		writeLog("Issuing query: SELECT * FROM " + getTableName() + " WHERE " + columnName + " = " + conditions[0] + " OR " + columnName + " = " + conditions[1]);
		String query = "SELECT * FROM " + getTableName() + " WHERE " + columnName + " = ? OR " + columnName + " = ?";
		
		try {
			selectQuery = conn.prepareStatement(query);
			selectQuery.setString(1, conditions[0] );
			selectQuery.setString(2, conditions[1]);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);

		} catch (SQLException e) {
			System.out.println("Query: SELECT * FROM " + getTableName() + " WHERE " + columnName + " = " + conditions[0] + " OR " + columnName + " = " + conditions[1] + " failed.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Selects rows from the table with a where the value is greater than a given condition
	 * @param columnName The column that will be queried
	 * @param condition the condition that will be used
	 */
	public void selectGreater(String columnName, String condition) {
		PreparedStatement selectQuery;
		System.out.println("Issuing query: SELECT * FROM " + getTableName() + " WHERE " + columnName + " > " + condition);
		writeLog("Issuing query: SELECT * FROM " + getTableName() + " WHERE " + columnName + " > " + condition);
		String query = "SELECT * FROM " + getTableName() + " WHERE " + columnName + " > ?";
		try {
			selectQuery = conn.prepareStatement(query);
			selectQuery.setString(1, condition);
			ResultSet result = selectQuery.executeQuery();
			printQuery(result);
			
			
		} catch (SQLException e) {
			System.out.println("Query : SELECT * FROM " + getTableName() + " WHERE " + columnName + " > " + condition + " failed");
			writeLog("Query : SELECT * FROM " + getTableName() + " WHERE " + columnName + " > " + condition + " failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts the randomized object values into the table from a file
	 * @param filename the file containing the randomized object values
	 */
	public void insertValues(String filename) {
		PreparedStatement insert;
		String logString = "";
		FileInputStream randomizedValues;
		Scanner fileScanner = null;
		String insertQuery= "INSERT INTO " + getTableName() + " VALUES (?,?,?,?,?)";
		String values;
		String[] valuesAsArray;
		System.out.println("Inserting values into " + getTableName());
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
				logString = "Issuing command: INSERT INTO " + getTableName() + " Values(";
				for (String s: valuesAsArray) {
					logString += s + ",";
				}
				logString += ")";
				writeLog(logString);
				
				for (int i = 0; i < valuesAsArray.length; i++) {
					insert.setString(i + 1, valuesAsArray[i]);
				}
				insert.executeUpdate();
			}
			
		} catch (SQLException e) {
			System.out.println("Values were not inserted into " + getTableName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints the log file to the console
	 */
	public void printLog() {
		FileInputStream log = null;
		try {
			log = new FileInputStream("dbOperations.log");
		} catch (FileNotFoundException e) {
			System.out.println("Log file could not be opened.");
			e.printStackTrace();
		}
		Scanner logReader = new Scanner(log);
		while(logReader.hasNext()) {
			System.out.println(logReader.nextLine());
		}
		logReader.close();
		try {
			log.close();
		} catch (IOException e) {
			System.out.println("Log file could not be closed.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints a query to the console 
	 * @param queryResult a ResultSet that will be parsed for information
	 */
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
	/**
	 * Initializes the log file
	 */
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
	
	/**
	 * Writes a given statement to the log file
	 * @param message what will be written to the log file
	 */
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
	 
}
