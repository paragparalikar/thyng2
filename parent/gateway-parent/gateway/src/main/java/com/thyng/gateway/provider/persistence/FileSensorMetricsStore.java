package com.thyng.gateway.provider.persistence;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.SneakyThrows;

public class FileSensorMetricsStore implements SensorMetricsStore {
	
	private final Long sensorId;
	private final Path storePath;
	private final String baseStoragePath;
	
	@Builder
	public FileSensorMetricsStore(final Long sensorId, final String baseStoragePath) {
		this.sensorId = sensorId;
		this.baseStoragePath = baseStoragePath;
		this.storePath = buildStorePath();
	}
	
	@SneakyThrows
	private Path buildStorePath() {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve( "metrics");
	}
	
	@SneakyThrows
	private Path getInFlightPath(Long transactionId) {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve(String.valueOf(transactionId));
	}
	
	@Override
	@SneakyThrows
	public synchronized Map<Long, Double> read(Long transactionId) {
		final Path inFlightPath = getInFlightPath(transactionId);
		if(Files.exists(storePath, LinkOption.NOFOLLOW_LINKS)) {
			Files.move(storePath, inFlightPath, 
					StandardCopyOption.ATOMIC_MOVE, 
					StandardCopyOption.REPLACE_EXISTING);
			final Map<Long, Double> map = new HashMap<>();
			final ByteBuffer byteBuffer = ByteBuffer.wrap(Files.readAllBytes(inFlightPath));
			while(byteBuffer.hasRemaining()) {
				map.put(byteBuffer.getLong(), byteBuffer.getDouble());
			}
			return map;
		}
		return Collections.emptyMap();
	}

	@Override
	@SneakyThrows
	public synchronized SensorMetricsStore rollback(Long transactionId) {
		final Path inFlightPath = getInFlightPath(transactionId); 
		if(Files.exists(inFlightPath, LinkOption.NOFOLLOW_LINKS)) {
			Files.copy(inFlightPath, Files.newOutputStream(storePath, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.WRITE, 
					StandardOpenOption.APPEND));
			Files.delete(inFlightPath);
		}
		return this;
	}

	@Override
	@SneakyThrows
	public synchronized SensorMetricsStore commit(Long transactionId) {
		Files.deleteIfExists(getInFlightPath(transactionId));
		return this;
	}

	@Override
	@SneakyThrows
	public synchronized SensorMetricsStore save(Long timestamp, Double value) {
		final ByteBuffer byteBuffer = ByteBuffer.allocate(16);
		byteBuffer.putLong(timestamp);
		byteBuffer.putDouble(value);
		Files.write(storePath, byteBuffer.array(), 
				StandardOpenOption.CREATE, 
				StandardOpenOption.WRITE, 
				StandardOpenOption.APPEND);
		return this;
	}
	
	
}
