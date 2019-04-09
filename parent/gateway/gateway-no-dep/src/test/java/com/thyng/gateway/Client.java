package com.thyng.gateway;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class Client {

	public static void main(String[] args) throws Exception {
		while(true){
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final DataOutputStream out = new DataOutputStream(baos);
			
			out.writeLong(197);
			out.writeByte(1);
			out.writeBoolean(Boolean.TRUE);
			final CoapResponse response = new CoapClient("coap","localhost",5683,"telemetry")
			.post(baos.toByteArray(), MediaTypeRegistry.APPLICATION_OCTET_STREAM);
			
			System.out.println("success : "+response.isSuccess());
			
			Thread.sleep(3000);
		}
	}

}
