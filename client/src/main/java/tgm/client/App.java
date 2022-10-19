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

public class App 
{
    public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException
    {
    	SelectorLoop loop = new SelectorLoop();
		loop.start();
		
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		channel.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 1800));
		
		IStreamSession session = (IStreamSession)  loop.register(channel, new ClientHandler()).sync().getSession();
		session.getReadyFuture().sync();
		
		Scanner scan = new Scanner(System.in);
		
		JSONObject json = new JSONObject();
		json.put("uname", scan.nextLine());
		json.put("password", scan.nextLine());
		json.put("request", scan.nextLine());
		
		session.write(json.toString().getBytes());
		
		JSONObject json2 = new JSONObject();
		json2.put("receiver", "User2");
		json2.put("message", "Hallo User2");
		json2.put("request", "send");
		
		session.write(json2.toString().getBytes());
		
		loop.join();
		
		scan.close();
    }
}
