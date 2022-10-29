
package tgm.olphonia.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import tgm.olphonia.App;

public class Account {
	
	public final String uname;
	public String uuid;
	
	public Account(String uname, String uuid) {
		this.uname = uname;
		this.uuid = uuid;
	}
	
	public boolean exists() {
		ResultSet results = App.sqlTable.requestDatabase("SELECT * FROM users WHERE id = '" + this.uuid + "';");
		try {
			return results.next();
		}
		catch (SQLException e) {}
		return false;
	}
	
	public boolean isOnline() {
		ResultSet results = App.sqlTable.requestDatabase("SELECT online FROM users WHERE id = '" + this.uuid + "';");
		
		try {
			if(!results.next()) return false;
			
			return results.getBoolean(1);
		}
		catch (SQLException e) {}
		
		return false;
	}
	
	public boolean setOnline(boolean online) {
		if(!this.exists()) return false;
		App.sqlTable.sendStatement("UPDATE users SET online = " + online + " WHERE id = '" + this.uuid + "';");
		return true;
	}
}