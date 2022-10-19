package tgm.client;

import org.snf4j.core.handler.AbstractStreamHandler;

public class ClientHandler extends AbstractStreamHandler {

	@Override
	public void read(Object msg) {
		String s = new String((byte[])msg);
    	
    	System.out.println(s);
	}
}
