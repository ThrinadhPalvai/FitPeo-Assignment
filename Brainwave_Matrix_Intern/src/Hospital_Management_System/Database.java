package Hospital_Management_System;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

	Connection connection;
	Statement statement;
	
	public Database() {
	    try {
	        connection = DriverManager.getConnection(
	            "jdbc:mysql://localhost:3306/hospital_management_system?allowPublicKeyRetrieval=true&useSSL=false", 
	            "root", "root");
	        statement = connection.createStatement();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void executeUpdate(String query) {
		try 
		{ 
			statement.executeUpdate(query);
			}
		catch (Exception e) 
		{ 
			e.printStackTrace();
			}
	}


	}
