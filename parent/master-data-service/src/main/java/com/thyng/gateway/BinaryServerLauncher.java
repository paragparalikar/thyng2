package com.thyng.gateway;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thyng.model.enumeration.GatewayOps;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BinaryServerLauncher {
	
	@Value("${server.gateway.port}")
	private int port;
	
	private final Thread thread = new Thread(this::startServer);
	private final BinaryServer server = new BinaryServer();
	private final RegistrationHandler registrationHandler;
	
	@PostConstruct 
	public void postConstruct() throws IOException{
		thread.setDaemon(true);
		thread.setName("BinaryServer");
		thread.start();
	}
	
	private void startServer(){
		try {
			server.register(GatewayOps.REGISTERATION, registrationHandler);
			server.start(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PreDestroy 
	public void preDestroy(){
		server.stop();
		thread.interrupt();
	}

}
