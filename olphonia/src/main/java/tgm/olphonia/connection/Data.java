package tgm.olphonia.connection;

import org.json.JSONObject;

public class Data {

    public enum Status {
	OK("Request OK", 200), CREATED("Created Resource, Request OK", 201), NO_CONTENT("Request OK", 204),

	BAD_REQUEST(
		"Request Error (malformed request syntax, size too large, invalid request message framing, or deceptive request routing)",
		400),
	UNAUTHORIZED("Client unauthorized", 401), FORBIDDEN("forbidden, authorized but no rights (maybe a woman)", 403),
	NOT_FOUND("Resource not found", 404);

	private final String description;
	private final Integer code;

	Status(final String description, final Integer code) {
	    this.description = description;
	    this.code = code;
	}

	public String getJSONObject() {
	    JSONObject obj = new JSONObject();
	    obj.put("description", this.description);
	    obj.put("code", this.code);
	    return obj.toString();
	}
    }
}