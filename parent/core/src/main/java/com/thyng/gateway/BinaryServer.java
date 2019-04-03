package com.thyng.gateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.model.Serializer;
import com.thyng.model.enumeration.GatewayOps;

public class BinaryServer {
	private static final Logger log = Logger.getLogger(BinaryServer.class.getName());

	private volatile boolean stopRequested;
	private final ExecutorService executor = Executors.newWorkStealingPool();
	private final Map<GatewayOps, SocketChannelHandler> mappings = new HashMap<>();
	
	public void register(GatewayOps operation, SocketChannelHandler handler){
		mappings.put(operation, handler);
	}
	
	public void start(int port) throws IOException{
		log.info("Binary server starting on port " + port);
		try(final ServerSocket serverSocket = new ServerSocket(port)){
			log.info("Binary server Started @host: "+serverSocket.getInetAddress().getHostAddress()+", @port: "+port);
			while(!stopRequested){
				final Socket socket = serverSocket.accept();
				
				final Input input = new Input(socket.getInputStream());
				final Output output = new Output(socket.getOutputStream());
				final int opCode = Serializer.kryo().readObject(input, Integer.class);
				
				if(0 > opCode || GatewayOps.values().length <= opCode){
					socket.close();
					continue;
				}
				final SocketChannelHandler handler = mappings.get(GatewayOps.values()[opCode]);
				if(null != handler){
					executor.submit(() -> {
						try {
							handler.handle(input, output);
						} catch (Exception e1) {
							e1.printStackTrace();
						} finally{
							try {
								socket.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}
	
	public void stop(){
		stopRequested = true;
	}

}
