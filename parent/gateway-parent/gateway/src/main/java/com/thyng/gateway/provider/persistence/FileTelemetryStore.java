package com.thyng.gateway.provider.persistence;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import lombok.Builder;
import lombok.SneakyThrows;

public class FileTelemetryStore implements TelemetryStore {
	
	private final Long sensorId;
	private final Path storePath;
	private final String baseStoragePath;
	
	@Builder
	public FileTelemetryStore(final Long sensorId, final String baseStoragePath) {
		this.sensorId = sensorId;
		this.baseStoragePath = baseStoragePath;
		this.storePath = buildStorePath();
	}
	
	@SneakyThrows
	private Path buildStorePath() {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve( "telemetry");
	}
	
	@SneakyThrows
	private Path getInFlightPath(Long transactionId) {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve(String.valueOf(transactionId));
	}
	
	@Override
	@SneakyThrows
	public synchronized byte[] read(Long transactionId) {
		final Path inFlightPath = getInFlightPath(transactionId);
		if(Files.exists(storePath, LinkOption.NOFOLLOW_LINKS)) {
			Files.move(storePath, inFlightPath, 
					StandardCopyOption.ATOMIC_MOVE, 
					StandardCopyOption.REPLACE_EXISTING);
			return Files.readAllBytes(inFlightPath);
		}
		return new byte[0];
	}

	@Override
	@SneakyThrows
	public synchronized TelemetryStore rollback(Long transactionId) {
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
	public synchronized TelemetryStore commit(Long transactionId) {
		Files.deleteIfExists(getInFlightPath(transactionId));
		return this;
	}

	@Override
	@SneakyThrows
	public synchronized TelemetryStore save(Long timestamp, Double value) {
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
