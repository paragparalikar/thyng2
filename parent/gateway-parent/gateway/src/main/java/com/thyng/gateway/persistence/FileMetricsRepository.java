package com.thyng.gateway.persistence;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.Constant;
import com.thyng.gateway.model.ThingMetrics;
import com.thyng.util.Lambda;

import lombok.SneakyThrows;

public class FileMetricsRepository implements MetricsRepository {
	private static final long MAX_TRANSACTION_SIZE = 1048576;
	private static final String TRANSACTION_FILE_PREFIX = "metrics-";
	
	private final Path path;

	public FileMetricsRepository() {
		this.path = getStoragePath();
	}

	@SneakyThrows
	private Path getBasePath() {
		return Files.createDirectories(
				Paths.get(System.getProperty(Constant.KEY_STORAGE)));
	}
	
	private Path getStoragePath() {
		return getBasePath().resolve("metrics");
	}
	
	private Path getInFlightPath(Long transactionId) {
		return getBasePath().resolve(TRANSACTION_FILE_PREFIX+transactionId.toString());
	}
	
	@Override
	@SneakyThrows
	public synchronized void save(ThingMetrics thingMetrics) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream output = new DataOutputStream(baos);
		for(Long sensorId : thingMetrics.getValues().keySet()) {
			output.writeLong(sensorId);
			output.writeLong(thingMetrics.getTimestamp());
			output.writeDouble(thingMetrics.getValues().get(sensorId));
		}
		output.flush();
		Files.write(path, baos.toByteArray(), 
				StandardOpenOption.CREATE, 
				StandardOpenOption.WRITE, 
				StandardOpenOption.APPEND);
	}

	@Override
	@SneakyThrows
	public synchronized Map<Long, Map<Long, Double>> read(Long transactionId) {
		final  Map<Long, Map<Long, Double>> data = new HashMap<>();
		try(final InputStream inputStream = Files.newInputStream(path, 
				StandardOpenOption.CREATE, StandardOpenOption.READ);
				final DataInputStream input = new DataInputStream(inputStream)){
			long transactionSize = 0;
			while(transactionSize <= MAX_TRANSACTION_SIZE) {
				final Long sensorId = input.readLong();
				final Map<Long, Double> metrics = data.computeIfAbsent(sensorId, id -> new HashMap<>());
				metrics.put(input.readLong(), input.readDouble());
				transactionSize += 24;
			}
		}catch(EOFException eofeIgnored) {
			// Reached end of file, so stop reading
		}
		return data;
	}

	@Override
	@SneakyThrows
	public synchronized void rollback(Long transactionId) {
		final Path inFlightPath = getInFlightPath(transactionId); 
		if(Files.exists(inFlightPath, LinkOption.NOFOLLOW_LINKS)) {
			Files.copy(inFlightPath, Files.newOutputStream(path, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.WRITE, 
					StandardOpenOption.APPEND));
			Files.delete(inFlightPath);
		}
	}

	@Override
	@SneakyThrows
	public synchronized void commit(Long transactionId) {
		Files.deleteIfExists(getInFlightPath(transactionId));
	}

	@Override
	@SneakyThrows
	public void rollbackAll() {
		try(final OutputStream outputStream = Files.newOutputStream(path, 
				StandardOpenOption.CREATE, 
				StandardOpenOption.WRITE, 
				StandardOpenOption.APPEND)){
					Files.list(path)
					.filter(file -> file.startsWith(TRANSACTION_FILE_PREFIX))
					.forEach(Lambda.consumer(file -> {
						Files.copy(file, outputStream); 
						Files.delete(path);
					}));
		}
	}

	@Override
	@SneakyThrows
	public int getPendingTransactionCount() {
		return (int)Files.list(path)
				.filter(file -> file.startsWith(TRANSACTION_FILE_PREFIX))
				.count();
	}

}
