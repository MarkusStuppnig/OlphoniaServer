package tgm.olphonia.user;

import org.json.JSONObject;

public class Message {

	private final String uuidSender;
	private final String uuidReceiver;
	private final String message;
	private final String time;
	
	public Message(String uuidSender, String uuidReceiver, String message, String time) {
		this.uuidSender = uuidSender;
		this.uuidReceiver = uuidReceiver;
		this.message = message;
		this.time = time;
	}

	public String getUuidSender() {
		return this.uuidSender;
	}

	public String getUuidReceiver() {
		return this.uuidReceiver;
	}

	public String getMessage() {
		return this.message;
	}

	public String getTime() {
		return this.time;
	}
	
	public JSONObject getJSON() {
		JSONObject message = new JSONObject();
		
		message.put("sender", this.getUuidSender());
		message.put("receiver", this.getUuidReceiver());
		message.put("message", this.getMessage());
		message.put("time", this.getTime());
		
		return message;
	}
}