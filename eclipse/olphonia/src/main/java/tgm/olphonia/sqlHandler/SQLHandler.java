package tgm.olphonia.sqlHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import tgm.olphonia.user.User;

public class SQLHandler {

	private SQLTable sqlTable;
	
	public SQLHandler(SQLTable sqlTable) {
		this.sqlTable = sqlTable;
	}
	
	public void registerUser(String uname, int password) {
		this.sqlTable.sendStatement("INSERT INTO users VALUES ('" + UUID.randomUUID() + "', false, '" + uname + "', " + password + ");");
		this.sqlTable.sendStatement("CREATE TABLE " + uname + " (sender varchar(20), message varchar(255), time varchar(63));");
	}
	
	public boolean loginUser(String uname, int password) {
		ResultSet results = this.sqlTable.requestDatabase("SELECT password FROM users WHERE uname = '" + uname + "';");
		
		try {
			if(!results.next()) return false;
			
			if(password == results.getInt(1)) {
				return true;
			}
		}
		catch (SQLException e) {}
		
		return false;
	}
	
	public boolean sendMessage(String sender, String receiver, String message) {
		if(!User.exists(sender)) return false;
		if(!User.exists(receiver)) return false;
		
		System.out.println("INSERT INTO " + sender + " VALUES ('" + sender + "', '" + message + "', '" + (new Date()).getTime() + "');");
		this.sqlTable.sendStatement("INSERT INTO " + receiver + " VALUES ('" + sender + "', '" + message + "', '" + (new Date()).getTime() + "');");
		return true;
	}
}
