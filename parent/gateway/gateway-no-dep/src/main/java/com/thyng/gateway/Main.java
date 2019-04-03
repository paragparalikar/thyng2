package com.thyng.gateway;

import java.io.IOException;

import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.model.enumeration.GatewayOps;

public class Main {

	public static void main(String[] args) throws IOException {
		final Thread thread = new Thread(() -> {
			try {
				final BinaryServer server = new BinaryServer();
				server.register(GatewayOps.CONFIGURATION, new PrintHandler());
				server.start(9090);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		thread.setDaemon(false);
		thread.start();
		
		
		final GatewayRegistrationDTO dto = new GatewayRegistrationDTO();
		dto.setGatewayId(164l);
		dto.setHost("0.0.0.0");
		dto.setPort(9090);
		
		BinaryClient.builder()
			.host("0.0.0.0")
			.port(8082)
			.gatewayOps(GatewayOps.REGISTERATION)
			.payload(dto)
			.build().send();
	}

}

class PrintHandler extends AbstractSocketChannelHandler<Object>{

	@Override
	protected void handle(Object input) {
		System.out.println(input);
	}
	
}