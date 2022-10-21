package tgm.olphonia.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import tgm.olphonia.App;

public class User {

	public static boolean checkName(String uname) {
		if(uname == null) return false;
		
		if(uname.contains(" ")) return false;
		if(uname.contains("'")) return false;
		if(uname.contains("\"")) return false;
		if(uname.contains(";")) return false;
		if(uname.contains("\\")) return false;
		
		//if(uname.length() > 20) return false;
		
		return true;
	}
	
	public static String getUUID(String uname) {
		ResultSet results = App.sqlTable.requestDatabase("SELECT id FROM users WHERE uname = '" + uname + "';");
		try {
			results.next();
			return results.getString(1);
		}
		catch (SQLException e) {}
		return null;
	}
	
	public static boolean exists(Account account) {
		ResultSet results = App.sqlTable.requestDatabase("SELECT * FROM users WHERE id = '" + account.uuid + "';");
		try {
			return results.next();
		}
		catch (SQLException e) {}
		return false;
	}
	
	public static boolean isOnline(Account account) {
		ResultSet results = App.sqlTable.requestDatabase("SELECT online FROM users WHERE id = '" + account.uuid + "';");
		
		try {
			if(!results.next()) return false;
			
			return results.getBoolean(1);
		}
		catch (SQLException e) {}
		
		return false;
	}
	
	public static boolean setOnline(Account account, boolean online) {
		if(!User.exists(account)) return false;
		App.sqlTable.sendStatement("UPDATE users SET online = " + online + " WHERE id = '" + account.uuid + "';");
		return true;
	}
}