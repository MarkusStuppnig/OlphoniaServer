package tgm.olphonia.connection.distributor;

import java.util.ArrayList;

import org.json.JSONObject;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;

import tgm.olphonia.connection.Data;
import tgm.olphonia.user.Account;
import tgm.olphonia.user.User;

public class OlphoniaSession extends AbstractStreamHandler {

    public static ArrayList<Account> onlineAccounts = new ArrayList<Account>();

    public Account account = null;

    @Override
    public void read(final Object msg) {
	String string = new String((byte[]) msg);

	System.out.println(string);

	JSONObject json = new JSONObject(string);
	String request = json.getString("request");

	switch (request) {

	case "register":

	    User.register(this, json.getString("uname"), json.getString("password").hashCode());
	    User.login(this, json.getString("uname"), json.getString("password").hashCode());

	    return;

	case "login":

	    User.login(this, json.getString("uname"), json.getString("password").hashCode());
	    this.send((new JSONObject()).put("Acknowledge", "logged-In").put("id", 200).toString());

	    return;
	}

	if (this.account == null) {
	    this.handleWrongRequest(Data.Status.BAD_REQUEST);
	    return;
	}

	switch (request) {

	case "send":

	    User.send(this, json.getString("receiver"), json.getString("message"));

	    break;

	case "receive":

	    User.receiveAllMessages(this);

	    break;
	}
    }

    @Override
    public void event(final SessionEvent event) {
	switch (event) {

	case CLOSED:
	    if (this.account.isConnected())
		this.account.setOnline(false);

	    OlphoniaSession.onlineAccounts.remove(this.account);

	    break;
	}
    }

    public void send(final String message) {
	this.getSession().write(message.getBytes());
    }

    public void handleWrongRequest(final Data.Status status) {
	this.send(status.getJSONObject());
	this.getSession().close();
    }
}