package com.thyng.gateway.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.function.Consumer;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.persistence.PersistentTelemetry;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class DispatchService implements Service, Consumer<Message> {
	public static final String KEY_TELEMETRY_URL = "thyng.server.url.telemetry";

	private final Context context;
	
	@Override
	public void start() throws Exception {
		context.getEventBus().register(Message.PERSISTED, this);
	}

	@Override
	public void stop() throws Exception {
		context.getEventBus().unregister(Message.PERSISTED, this);
	}

	@Override
	@SneakyThrows
	public void accept(Message message) {
		for(Long sensorId : message.getValues().keySet()){
			final SensorDTO sensor = message.getSensor(sensorId);
			final Integer unsentCount = message.getUnsentCounts().get(sensorId);
			if(unsentCount >= sensor.getBatchSize()){
				final ThingDetailsDTO thing = message.getThing(sensorId);
				final PersistentTelemetry telemetry = context.getPersistenceProvider().getUnsentTelemetry(thing.getId(), sensorId);
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final DataOutput out = new DataOutputStream(baos);
				final byte dataType = telemetry.write(out);
				final PropertyProvider properties = context.getProperties();
				final CoapClient client = new CoapClient("coap", 
						properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
						properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
						properties.get(KEY_TELEMETRY_URL, "telemetries"));
				final Request request = Request.newPost();
				request.setType(Type.CON);
				request.setPayload(baos.toByteArray());
				request.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_OCTET_STREAM);
				request.getOptions().setUriQuery("thingId="+thing.getId()+"&sensorId="+sensorId+"&dataType="+dataType);
				final CoapResponse response = client.advanced(request);
				if(null != response && response.isSuccess()){
					context.getPersistenceProvider().markSent(telemetry);
				}
			}
		}
	}

}
