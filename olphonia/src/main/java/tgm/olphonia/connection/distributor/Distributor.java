package tgm.olphonia.connection.distributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;

import tgm.olphonia.App;
import tgm.olphonia.user.Account;
import tgm.olphonia.user.Message;
import tgm.olphonia.user.User;

public class Distributor extends AbstractStreamHandler {

	private HashMap<String, IStreamSession> sessions = new HashMap<String, IStreamSession>();
	public Account account = null;
	
	@Override
	public void read(Object msg) {
		String string = new String((byte[])msg);

		JSONObject json = new JSONObject(string);
		String request = json.getString("request");
		
		switch(request) {
		
			case "register":
				
				if(this.account != null || !User.checkName(json.getString("uname"))) {
					this.handleWrongRequest();
					return;
				}
				
				this.register(json.getString("uname"), json.getString("password").hashCode());
				this.login(json.getString("uname"), json.getString("password").hashCode());
				
				return;
				
			case "login":
				
				if(this.account != null || !User.checkName(json.getString("uname"))) {
					this.handleWrongRequest();
					return;
				}

				this.login(json.getString("uname"), json.getString("password").hashCode());
				
				return;
		}
		
		if(this.account == null) {
			this.handleWrongRequest();
			return;
		}
		
		switch(request) {
		
			case "send":
				
				String receiverStr = json.getString("receiver");
				Account receiver = new Account(receiverStr, User.getUUID(receiverStr));
				
				if(!receiver.exists()) {
					this.handleWrongRequest();
					return;
				}
				
				Message message = App.sqlHandler.sendMessage(this.account, receiver, json.getString("message"));
				
				if(receiver.isOnline()) {
					this.sessions.get(receiver.uuid).write(message.toString());
				}

				break;
			
			case "receive":
				
				this.receiveAllMessages();
				
				break;
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void event(SessionEvent event) {
		switch (event) {
			case CLOSED:
				this.account.setOnline(false);
				sessions.remove(this.account.uuid);
				break;
		}
	}
	
	private void register(String uname, int password) {
		this.setAccount(new Account(uname, User.getUUID(uname)));
		if(this.account.exists()) {
			this.handleWrongRequest();
			return;
		}
		this.account.uuid = UUID.randomUUID().toString();
		App.sqlHandler.registerUser(this.account, password);
	}

	private void login(String uname, int password) {
		this.setAccount(new Account(uname, User.getUUID(uname)));
		if(!account.exists() || !App.sqlHandler.checkPassword(this.account, password)) {
			this.handleWrongRequest();
			return;
		}
		this.account.setOnline(true);
		this.sessions.put(this.account.uuid, getSession());
		
		this.receiveAllMessages();
	}
	
	private void setAccount(Account account) {
		this.account = account;
	}
	
	private void receiveAllMessages() {
		ArrayList<Message> messages = App.sqlHandler.receiveAllMessages(this.account); 
		
		JSONObject messagesJSON = new JSONObject();
		JSONArray messagesArray = new JSONArray();
		
		for(Message message : messages) {
			messagesArray.put(message.getJSON());
		}
		
		messagesJSON.put("messages", messagesArray);
		
		this.send(messagesJSON.toString());
	}
	
	private void send(String message) {
		this.getSession().write(message.getBytes());
	}
	
	private void handleWrongRequest() {
		this.send("Wrong request!");
		this.getSession().close();
	}
}