package tgm.olphonia.user;

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
}