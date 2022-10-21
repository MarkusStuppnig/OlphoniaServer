package tgm.olphonia.sqlHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import tgm.olphonia.user.Account;
import tgm.olphonia.user.User;

public class SQLHandler {

	private SQLTable sqlTable;
	
	public SQLHandler(SQLTable sqlTable) {
		this.sqlTable = sqlTable;
	}
	
	public void registerUser(Account account, int password) {
		this.sqlTable.sendStatement("INSERT INTO users VALUES ('" + UUID.randomUUID() + "', false, '" + account.uname + "', " + password + ");");
	}
	
	public boolean checkPassword(Account account, int password) {
		ResultSet results = this.sqlTable.requestDatabase("SELECT password FROM users WHERE uname = '" + account.uname + "';");
		
		try {
			if(!results.next()) return false;
			
			if(password == results.getInt(1)) {
				return true;
			}
		}
		catch (SQLException e) {}
		
		return false;
	}
	
	public boolean sendMessage(Account sender, Account receiver, String message) {
		if(!User.exists(sender)) return false;
		if(!User.exists(receiver)) return false;
		
		this.sqlTable.sendStatement("INSERT INTO messages VALUES ('" + sender.uuid + "', '" + receiver.uuid + "', '" + message + "', " + String.valueOf((new Date()).getTime()) + ");");
		return true;
	}
}