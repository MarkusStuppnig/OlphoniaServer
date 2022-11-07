package tgm.olphonia.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.snf4j.core.SelectorLoop;
import org.snf4j.core.factory.AbstractSessionFactory;
import org.snf4j.core.handler.IStreamHandler;

import tgm.olphonia.connection.distributor.OlphoniaSession;

public class ServerManager {

    public ServerManager() {
	this.launchDistributor(1800);
    }

    private OlphoniaSession launchDistributor(final int port) {
	try {
	    SelectorLoop loop = new SelectorLoop();
	    loop.start();

	    ServerSocketChannel channel = ServerSocketChannel.open();
	    channel.configureBlocking(false);
	    channel.socket().bind(new InetSocketAddress(port));

	    loop.register(channel, new AbstractSessionFactory() {
		@Override
		protected IStreamHandler createHandler(final SocketChannel channel) {
		    return new OlphoniaSession();
		}
	    });

	    loop.join();
	} catch (IOException | InterruptedException e) {
	}
	return null;
    }
}