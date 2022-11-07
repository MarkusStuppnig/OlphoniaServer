package tgm.olphonia.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import tgm.olphonia.App;
import tgm.olphonia.connection.distributor.OlphoniaSession;

public class User {

    public static boolean checkName(final String uname) {
	if ((uname == null) || uname.contains(" ") || uname.contains("'") || uname.contains("\""))
	    return false;
	if (uname.contains(";") || uname.contains("\\"))
	    return false;

	// if(uname.length() > 20) return false;

	return true;
    }

    public static String getUUID(final String uname) {
	ResultSet results = App.sqlTable.requestDatabase("SELECT id FROM users WHERE uname = '" + uname + "';");
	try {
	    results.next();
	    return results.getString(1);
	} catch (SQLException e) {
	}
	return null;
    }

    public static void register(final OlphoniaSession session, final String uname, final int password) {
	if (session.account != null || !User.checkName(uname)
		|| (session.account = new Account(uname, User.getUUID(uname))).exists()) {
	    session.handleWrongRequest();
	    return;
	}

	session.account.uuid = UUID.randomUUID().toString();
	App.sqlHandler.registerUser(session.account, password);
    }

    public static void login(final OlphoniaSession session, final String uname, final int password) {
	if (!User.checkName(uname) || !(session.account = new Account(uname, User.getUUID(uname))).exists()
		|| !App.sqlHandler.checkPassword(session.account, password) || session.account.isConnected()) {
	    session.handleWrongRequest();
	    return;
	}

	session.account.setOnline(true);
	OlphoniaSession.onlineAccounts.add(session.account);
    }

    public static void send(final OlphoniaSession session, final String receiverStr, final String messageStr) {
	Account receiver;
	if ((receiver = new Account(receiverStr, User.getUUID(receiverStr))).exists()) {
	    session.handleWrongRequest();
	    return;
	}

	Message message = App.sqlHandler.sendMessage(session.account, receiver, messageStr);

	for (Account acc : OlphoniaSession.onlineAccounts) {
	    if (!session.account.equals(acc))
		continue;
	    acc.session.write(message.toString());
	}
    }

    public static void receiveAllMessages(final OlphoniaSession session) {
	ArrayList<Message> messages = App.sqlHandler.receiveAllMessages(session.account);

	JSONObject messagesJSON = new JSONObject();
	JSONArray messagesArray = new JSONArray();

	for (Message message : messages)
	    messagesArray.put(message.getJSON());

	messagesJSON.put("messages", messagesArray);

	session.send(messagesJSON.toString());
    }
}