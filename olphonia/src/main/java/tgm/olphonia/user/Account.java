
package tgm.olphonia.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.snf4j.core.session.IStreamSession;

import tgm.olphonia.App;

public class Account {
	
	public String uname;
	public String uuid;
	public IStreamSession session;
	
	public Account(String uname, String uuid) {
		this.uname = uname;
		this.uuid = uuid;
	}
	
	public boolean loggedIn() {
		return this.session.isOpen();
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
		System.out.println("Set Online " + online);
		App.sqlTable.sendStatement("UPDATE users SET online = " + online + " WHERE id = '" + this.uuid + "';");
		return true;
	}
}