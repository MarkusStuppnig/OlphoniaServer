
package tgm.olphonia.user;

public class Account {
	
	public final String uname;
	public final String uuid;
	public boolean loggedIn;
	
	public Account(String uname, String uuid) {
		this.uname = uname;
		this.uuid = uuid;
		
		this.loggedIn = false;
	}
}