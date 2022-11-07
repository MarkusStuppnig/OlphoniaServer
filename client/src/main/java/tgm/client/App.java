package tgm.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.snf4j.core.SelectorLoop;
import org.snf4j.core.session.IStreamSession;

public class App {

    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {

	SelectorLoop loop = new SelectorLoop();
	loop.start();

	SocketChannel channel = SocketChannel.open();
	channel.configureBlocking(false);
	channel.connect(new InetSocketAddress(InetAddress.getByName("0"), 1800));

	IStreamSession session = (IStreamSession) loop.register(channel, new ClientHandler()).sync().getSession();
	session.getReadyFuture().sync();

	Scanner scan = new Scanner(System.in);
	String uname = scan.nextLine();
	String password = scan.nextLine();
	String request = scan.nextLine();
	scan.close();

	JSONObject json = new JSONObject();
	json.put("uname", uname);
	json.put("password", password);
	json.put("request", request);

	session.write(json.toString().getBytes());

	loop.join();
    }
}