package tgm.olphonia;

import java.io.IOException;
import java.sql.SQLException;

import tgm.olphonia.connection.ServerManager;
import tgm.olphonia.sqlHandler.SQLHandler;
import tgm.olphonia.sqlHandler.SQLTable;

public class App {

    public static SQLTable sqlTable;
    public static SQLHandler sqlHandler;

    public static ServerManager manager;

    public static int port = 80;

    public static void main(final String[] args) throws SQLException, IOException {
	sqlTable = new SQLTable("markus", "password", "olphonia");
	sqlHandler = new SQLHandler(sqlTable);

	manager = new ServerManager();
    }
}