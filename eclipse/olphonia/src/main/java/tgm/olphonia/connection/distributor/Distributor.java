package tgm.olphonia.connection.distributor;

import java.util.HashMap;

import org.json.JSONObject;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;

import tgm.olphonia.App;
import tgm.olphonia.user.User;

public class Distributor extends AbstractStreamHandler {

	private HashMap<String, IStreamSession> sessions = new HashMap<String, IStreamSession>();
	
	@Override
	public void read(Object msg) {
		String string = new String((byte[])msg);
		
		System.out.println(string);

		JSONObject json = new JSONObject(string);
		String uname = json.getString("uname");
		int password = json.getString("password").hashCode();
		String request = json.getString("request");
		
		switch(request) {
		
			case "register":
				if(!User.checkName(uname)) return;
				if(!User.checkPassword(password)) return;
				
				if(User.exists(uname)) return;
				
				App.sqlHandler.registerUser(uname, password);
				User.setOnline(uname, true);
				
				this.getSession().getAttributes().put(0, uname);
				this.sessions.put(uname, getSession());
					
				this.send("Registered Successfully");
				
				break;
				
			case "login":
				if(!User.checkName(uname)) return;
				if(!User.checkPassword(password)) return;
				
				if(!App.sqlHandler.loginUser(uname, password)) {
					this.handleWrongPassword();
					return;
				}
				if(User.isOnline(uname)) return;

				User.setOnline(uname, true);
					
				this.getSession().getAttributes().put(0, uname);
				this.sessions.put(uname, getSession());
					
				this.send("Logged in Successfully");
				
				break;
			
			case "send":
				String receiver = json.getString("receiver");
				String message = json.getString("message");
				
				//App.sqlHandler.sendMessage(String.valueOf(this.getSession().getAttributes().get(0)), receiver, message);
				if(!App.sqlHandler.sendMessage(String.valueOf(this.getSession().getAttributes().get(0)), receiver, message)) return;
				
				this.send("Sent successfully");
				break;
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void event(SessionEvent event) {
		switch (event) {
			case OPENED:
				break;
				
			case CLOSED:
				User.setOnline(String.valueOf(getSession().getAttributes().get(0)), false);
				sessions.remove(getSession().getAttributes().get(0));
				break;
		}
	}
	
	private void send(String message) {
		this.getSession().write(message.getBytes());
	}
	
	private void handleWrongPassword() {
		this.send("Wrong password");
		this.getSession().close();
	}
}