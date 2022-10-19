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
	
	public static boolean checkPassword(int password) {
		if(password == -1) return false;
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
	
	public static boolean exists(String uname) {
		ResultSet results = App.sqlTable.requestDatabase("SELECT * FROM users WHERE uname = '" + uname + "';");
		try {
			return results.next();
		}
		catch (SQLException e) {}
		return false;
	}
	
	public static boolean isOnline(String uname) {
		ResultSet results = App.sqlTable.requestDatabase("SELECT online FROM users WHERE uname = '" + uname + "';");
		
		try {
			if(!results.next()) return false;
			
			return results.getBoolean(1);
		}
		catch (SQLException e) {}
		
		return false;
	}
	
	public static boolean setOnline(String uname, boolean online) {
		if(!User.exists(uname)) return false;
		App.sqlTable.sendStatement("UPDATE users SET online = " + online + " WHERE uname = '" + uname + "';");
		return true;
	}
}