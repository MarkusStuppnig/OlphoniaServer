package tgm.olphonia.sqlHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLTable {

	private Connection connection;
	
	public SQLTable(String username, String password, String database) {
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost/" + database + "?" + "user=" + username + "&password=" + password);
		}
		catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet requestDatabase(String command) {
		try
		{
			return this.connection.prepareStatement(command, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery();
		}
		catch (SQLException e) {}
		return null;
	}
	
	public ResultSet requestDatabaseOutput(String command) {
		try
		{
			ResultSet results = requestDatabase(command);
			
			ResultSetMetaData metadata = results.getMetaData();
		    
			while(results.next())
			{
				ArrayList<String> line = new ArrayList<String>();
				for (int i = 1; i <= metadata.getColumnCount(); i++)
				{
					line.add(results.getString(i));          
				}
				System.out.println(String.join(", ", line));
			}
			
			results.beforeFirst();
			return results;
			
		}
		catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendStatement(String command) {
		try
		{
			this.connection.createStatement().executeUpdate(command);
		}
		catch (SQLException e) {}
	}
}