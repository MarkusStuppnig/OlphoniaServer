package tgm.olphonia.user;

import org.json.JSONObject;

public class Message {

	public final String uuidSender;
	public final String uuidReceiver;
	public final String message;
	public final String time;
	
	public Message(String uuidSender, String uuidReceiver, String message, String time) {
		this.uuidSender = uuidSender;
		this.uuidReceiver = uuidReceiver;
		this.message = message;
		this.time = time;
	}
	
	public JSONObject getJSON() {
		JSONObject message = new JSONObject();
		
		message.put("sender", this.uuidSender);
		message.put("receiver", this.uuidReceiver);
		message.put("message", this.message);
		message.put("time", this.time);
		
		return message;
	}
}