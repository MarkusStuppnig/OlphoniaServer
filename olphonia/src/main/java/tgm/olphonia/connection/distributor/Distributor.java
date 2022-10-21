package tgm.olphonia.connection.distributor;

import java.util.HashMap;

import org.json.JSONObject;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;

import tgm.olphonia.App;
import tgm.olphonia.user.Account;
import tgm.olphonia.user.User;

public class Distributor extends AbstractStreamHandler {

	private HashMap<String, IStreamSession> sessions = new HashMap<String, IStreamSession>();
	private Account account = null;
	
	@Override
	public void read(Object msg) {
		String string = new String((byte[])msg);
		
		System.out.println(string);

		JSONObject json = new JSONObject(string);
		String request = json.getString("request");
		
		switch(request) {
		
			case "register":
				
				if(this.account != null) {
					this.handleWrongRequest();
					return;
				}
				
				this.account = new Account(json.getString("uname"), User.getUUID(json.getString("uname")));
				if(!User.checkName(this.account.uname)) return;
				if(User.exists(this.account)) return;
				
				App.sqlHandler.registerUser(this.account, json.getString("password").hashCode());
				
				this.login(json.getString("password").hashCode());
				
				break;
				
			case "login":
				
				if(this.account != null) {
					this.handleWrongRequest();
					return;
				}
				
				this.account = new Account(json.getString("uname"), User.getUUID(json.getString("uname")));
				if(!User.exists(account)) return;

				this.login(json.getString("password").hashCode());
				
				break;
			
			case "send":
				
				if(!isValidSession()) {
					this.handleWrongRequest();
					return;
				}
				
				String receiver = json.getString("receiver");
				String message = json.getString("message");
				
				if(!App.sqlHandler.sendMessage(account, new Account(receiver, User.getUUID(receiver)), message)) return;
				
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
				User.setOnline(this.account, false);
				sessions.remove(this.account.uuid);
				break;
		}
	}
	
	private void login(int password) {
		if(!App.sqlHandler.checkPassword(this.account, password)) {
			this.handleWrongRequest();
			return;
		}
		User.setOnline(this.account, true);
		this.account.loggedIn = true;
		
		this.sessions.put(this.account.uuid, getSession());
			
		this.send("Logged in Successfully");
	}
	
	private boolean isValidSession() {
		return this.account.loggedIn;
	}
	
	private void send(String message) {
		this.getSession().write(message.getBytes());
	}
	
	private void handleWrongRequest() {
		this.send("Wrong request");
		this.getSession().close();
	}
}