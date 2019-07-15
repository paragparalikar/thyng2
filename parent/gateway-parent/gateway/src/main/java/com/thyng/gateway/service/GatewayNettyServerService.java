package com.thyng.gateway.service;

import com.thyng.gateway.AbstractGatewayNettyServer;
import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.CommitResponse;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.gateway.model.RollbackResponse;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.gateway.telemetry.TelemetryMessageHandler;

public class GatewayNettyServerService extends AbstractGatewayNettyServer implements Service {

	private final Context context;
	private final TelemetryMessageHandler telemetryMessageHandler;
	
	public GatewayNettyServerService(final Context context) {
		super(context.getProperties().getInteger("thyng.gateway.server.port", 8080));
		this.context = context;
		this.telemetryMessageHandler = new TelemetryMessageHandler(context);
	}
	
	@Override
	protected CommitResponse handle(CommitRequest request) {
		context.getSensorIds().forEach(sensorId -> {
			final TelemetryStore telemetryStroe = context.getPersistenceProvider().getTelemetryStore(sensorId);
			telemetryStroe.commit(request.getTransactionId());
		});
		return new CommitResponse();
	}

	@Override
	protected RollbackResponse handle(RollbackRequest request) {
		context.getSensorIds().forEach(sensorId -> {
			final TelemetryStore telemetryStroe = context.getPersistenceProvider().getTelemetryStore(sensorId);
			telemetryStroe.rollback(request.getTransactionId());
		});
		return new RollbackResponse();
	}

	/**
	 * Message should be in below format:
	 * 1. First 4 bytes size of the message
	 * 2. Next 4 bytes as int 10 -> stands for byte[] type
	 * 3. Actual byte[] data that will be resolved to long and double pairs
	 * Thus message containing one sensor reading would be -
	 * byte[] having a consecutive long and a double
	 * As the type is byte[], an int 10, thus now size is 16 + 4 = 20
	 * Prepend size int 20, thus message would look like,
	 * [4 bytes int = 20][4 bytes int = 10][8 bytes long][8 bytes double]
	 */
	@Override
	protected byte handle(byte[] data) {
		telemetryMessageHandler.handle(data);
		return 0;
	}

}
