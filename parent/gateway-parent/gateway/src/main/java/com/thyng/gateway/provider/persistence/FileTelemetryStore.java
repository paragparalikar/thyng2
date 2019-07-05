package com.thyng.gateway.provider.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.thyng.gateway.Constant;
import com.thyng.model.Telemetry;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

@Value
public class FileTelemetryStore implements TelemetryStore {
	private static final int MAX_INT_BYTES = String.valueOf(Integer.MAX_VALUE).getBytes().length;

	private final Long sensorId; 
	private final Path archivePath;
	private final Path storePath;
	private final Path countPath;
	private final String inFlightPath;
	private final String baseStoragePath;
	private final AtomicInteger count = new AtomicInteger();
	private final ReentrantReadWriteLock archiveLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock storeLock = new ReentrantReadWriteLock();
	private final ReentrantReadWriteLock countLock = new ReentrantReadWriteLock();
	
	@Builder
	@SneakyThrows
	public FileTelemetryStore(final Long sensorId, final String baseStoragePath) {
		this.sensorId = Objects.requireNonNull(sensorId, "sensorId can not be null");
		this.baseStoragePath = Objects.requireNonNull(baseStoragePath, "baseStoragePath can not be null");
		this.storePath = buildStorePath();
		this.countPath = buildCountPath();
		this.archivePath = buildArchivePath();
		this.inFlightPath = buildInFlightPath();
		readCount();
	}
	
	@Override
	public int getCount() {
		return count.get();
	}
	
	@Override
	public TelemetryStore save(final Long timestamp,final Double value) {
		return save(Collections.singletonMap(timestamp.toString(), value));
	}
	
	@Override
	@SneakyThrows
	public TelemetryStore save(final Map<String, Double> values) {
		final ByteBuffer byteBuffer = ByteBuffer.wrap(
				(values.keySet().stream()
				.map(key -> key+","+values.get(key))
				.collect(Collectors.joining(Constant.LINEFEED))
				+ Constant.LINEFEED).getBytes(Constant.CHARSET));
		storeLock.writeLock().lock();
		try (final FileChannel storeFileChannel = FileChannel.open(storePath, 
													StandardOpenOption.CREATE,
													StandardOpenOption.WRITE, 
													StandardOpenOption.APPEND);
				final FileLock storeFileLock = storeFileChannel.lock()){
			storeFileChannel.write(byteBuffer);
			count.addAndGet(values.size());
			writeCount();
		}finally {
			storeLock.writeLock().unlock();
		}
		return this;
	}
	
	@Override
	@SneakyThrows
	public Telemetry read() {
		final String uuid = UUID.randomUUID().toString();
		final Map<Long,Double> values = new HashMap<>();
		storeLock.writeLock().lock();
		try  (final FileChannel storeFileChannel = FileChannel.open(storePath, 
				StandardOpenOption.CREATE,
				StandardOpenOption.READ);
				final BufferedReader bufferedReader = 
						new BufferedReader(Channels.newReader(storeFileChannel, Constant.CHARSET))){
			bufferedReader.lines().forEach(line -> {
				if(null != line && 0 < line.trim().length()) {
					final String[] tokens = line.split(",");
					if(2 == tokens.length) {
						values.put(Long.parseLong(tokens[0]), Double.parseDouble(tokens[1]));
					}
				}
			});
			storeFileChannel.transferTo(0, Long.MAX_VALUE, 
					FileChannel.open(Paths.get(inFlightPath, uuid), 
					StandardOpenOption.CREATE,
					StandardOpenOption.WRITE,
					StandardOpenOption.APPEND));
			storeFileChannel.truncate(0);
		}finally {
			storeLock.writeLock().unlock();
		}
		return Telemetry.builder()
				.sensorId(sensorId)
				.values(values)
				.uuid(uuid)
				.build();
	}
	
	@Override
	@SneakyThrows
	public TelemetryStore rollback(final String uuid) {
		final Path inFlightFilePath = Paths.get(inFlightPath, uuid);
		storeLock.writeLock().lock();
		try  (final FileChannel storeFileChannel = FileChannel.open(storePath, 
													StandardOpenOption.CREATE,
													StandardOpenOption.WRITE,
													StandardOpenOption.APPEND);
				final BufferedReader bufferedReader = new BufferedReader(Channels.newReader(storeFileChannel, Constant.CHARSET));
				final FileLock storeFileLock = storeFileChannel.lock();
				final FileChannel inFlightFileChannel = FileChannel.open(inFlightFilePath, 
													StandardOpenOption.CREATE,
													StandardOpenOption.READ);
				final FileLock inFlightFileLock = inFlightFileChannel.lock()){
			storeFileChannel.transferFrom(inFlightFileChannel, 0, Long.MAX_VALUE);
		}finally {
			storeLock.writeLock().unlock();
		}
		Files.delete(inFlightFilePath);
		return this;
	}
	
	@Override
	@SneakyThrows
	public TelemetryStore commit(final String uuid) {
		archiveLock.writeLock().lock();
		final Path inFlightFilePath = Paths.get(inFlightPath, uuid);
		try (final FileChannel archiveFileChannel = FileChannel.open(archivePath, 
													StandardOpenOption.CREATE,
													StandardOpenOption.WRITE, 
													StandardOpenOption.APPEND);
				final FileLock storeFileLock = archiveFileChannel.lock();
				final FileChannel inFlightFileChannel = FileChannel.open(inFlightFilePath, 
													StandardOpenOption.CREATE,
													StandardOpenOption.READ);
				final FileLock inFlightFileLock = inFlightFileChannel.lock()){
			archiveFileChannel.transferFrom(inFlightFileChannel, 0, Long.MAX_VALUE);
		}finally {
			archiveLock.writeLock().unlock();
		}
		Files.delete(inFlightFilePath);
		return this;
	}
	
	
	private void readCount() throws NumberFormatException, IOException {
		countLock.readLock().lock();
		try (final FileChannel countFileChannel = FileChannel.open(countPath, 
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.APPEND);
				final FileLock storeFileLock = countFileChannel.lock()){
			final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_INT_BYTES);
			if(0 < countFileChannel.size() && 0 < countFileChannel.read(byteBuffer)) {
				count.set(Integer.parseInt(new String(byteBuffer.array(),Constant.CHARSET)));
			}else {
				count.set(0);
			}
		}finally {
			countLock.readLock().unlock();
		}
	}

	private void writeCount() throws IOException {
		countLock.writeLock().lock();
		try (final FileChannel countFileChannel = FileChannel.open(countPath, 
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
				final FileLock storeFileLock = countFileChannel.lock()){
			final ByteBuffer byteBuffer = ByteBuffer.wrap(String.valueOf(count.get()).getBytes(Constant.CHARSET));
			countFileChannel.write(byteBuffer);
		}finally {
			countLock.writeLock().unlock();
		}
	}
	
	private String buildInFlightPath() {
		return 	baseStoragePath + File.separator 
				+ "data" + File.separator
				+ sensorId.toString();
	}
	
	private Path buildArchivePath() throws IOException {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve("telemetry.archive.csv");
	}
	
	private Path buildCountPath() throws IOException {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve( "telemetry.count.csv");
	}

	private Path buildStorePath() throws IOException {
		return Files.createDirectories(Paths.get(baseStoragePath, "data", 
				sensorId.toString())).resolve( "telemetry.csv");
	}
	
}
