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
		
		if(uname.length() > 20) return false;
		
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
}