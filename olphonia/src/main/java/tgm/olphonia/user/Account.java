
package tgm.olphonia.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.snf4j.core.session.IStreamSession;

import tgm.olphonia.App;
import tgm.olphonia.connection.distributor.OlphoniaSession;

public class Account {

    public String uname;
    public String uuid;
    public IStreamSession session;

    public Account(final String uname, final String uuid) {
	this.uname = uname;
	this.uuid = uuid;
    }

    @Override
    public boolean equals(final Object obj) {
	if (!(obj instanceof Account acc))
	    return false;

	return acc.uname.equals(this.uname) && acc.uuid.equals(this.uuid);
    }

    public boolean loggedIn() {
	return this.session.isOpen();
    }

    public boolean exists() {
	ResultSet results = App.sqlTable.requestDatabase("SELECT * FROM users WHERE id = '" + this.uuid + "';");
	try {
	    return results.next();
	} catch (SQLException e) {
	}
	return false;
    }

    public boolean isOnline() {
	ResultSet results = App.sqlTable.requestDatabase("SELECT online FROM users WHERE id = '" + this.uuid + "';");

	try {
	    if (!results.next())
		return false;

	    return results.getBoolean(1);
	} catch (SQLException e) {
	}

	return false;
    }

    public boolean setOnline(final boolean online) {
	if (!this.exists())
	    return false;
	App.sqlTable.sendStatement("UPDATE users SET online = " + online + " WHERE id = '" + this.uuid + "';");
	return true;
    }

    public boolean isConnected() {
	for (Account acc : OlphoniaSession.onlineAccounts) {
	    if (!this.equals(acc))
		continue;
	    return true;
	}
	return false;
    }
}