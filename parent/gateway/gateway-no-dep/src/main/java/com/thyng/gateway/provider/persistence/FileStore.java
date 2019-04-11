package com.thyng.gateway.provider.persistence;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Function;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.Lambda;
import com.thyng.model.dto.TelemetryDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileStore {
	private static final long HEAD = 0;
	private static final long BODY = 64;
	private static final String KEY_OFFSET_TARGET = "KEY_OFFSET_TARGET";
	private static final String KEY_UNSENT_COUNT = "KEY_UNSENT_COUNT";

	private byte dataType;
	private int unsentCount;
	private long unsentOffset = BODY;
	private final Long thingId;
	private final Long sensorId;
	private final PropertyProvider properties;
	
	public FileStore(Long thingId, Long sensorId, PropertyProvider properties) {
		Objects.requireNonNull(thingId);
		Objects.requireNonNull(sensorId);
		Objects.requireNonNull(properties);
		this.thingId = thingId;
		this.sensorId = sensorId;
		this.properties = properties;
	}
	
	public Integer write(Long timestamp, Object value) throws Exception{
		return execute(Lambda.function(file -> {
			file.seek(Math.max(BODY, file.length()));
			log.debug("Seeked to file.length() - "+file.getFilePointer());
			file.writeLong(timestamp);
			log.debug("Wrote "+Long.BYTES+" timestamp");
			log.debug("Writing data at "+file.getFilePointer());
			dataType = TelemetryDTO.write(value, file);
			log.debug("Now file length "+file.length());
			return ++unsentCount;
		}));
	}
	
	public PersistentTelemetry getUnsentTelemetry() throws Exception{
		return execute(Lambda.function(file -> {
			file.seek(unsentOffset);
			log.debug("Seeked to "+file.getFilePointer());
			final byte[] data = new byte[(int)(file.length() - unsentOffset)];
			log.debug("File length : "+file.length()+", unsentOffset : "+unsentOffset);
			log.debug("Reading "+data.length);
			file.readFully(data);
			final PersistentTelemetry telemetry = new PersistentTelemetry(thingId, sensorId, dataType, data);
			telemetry.getContext().put(KEY_OFFSET_TARGET, file.length());
			telemetry.getContext().put(KEY_UNSENT_COUNT, unsentCount);
			return telemetry;
		}));
	}
	
	public void markSent(PersistentTelemetry telemetry) throws Exception{
		execute(Lambda.function(file -> {
			final int previousUnsentCount = telemetry.getContext().getInteger(KEY_UNSENT_COUNT);
			final long targetUnsentOffset = telemetry.getContext().getLong(KEY_OFFSET_TARGET);
			unsentCount -= previousUnsentCount;
			unsentOffset = targetUnsentOffset;
			return null;
		}));
	}
	
	
	private <T> T execute(Function<RandomAccessFile, T> action) throws IOException{
		final Path path = buildPath();
		final File file = path.toFile();
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		try(final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws")){
			try(final FileLock lock = randomAccessFile.getChannel().lock()){
				final T result = action.apply(headers(randomAccessFile));
				writeHeaders(randomAccessFile);
				return result;
			}
		}
	}
	
	private RandomAccessFile readHeaders(RandomAccessFile file) throws IOException{
		file.seek(HEAD);
		log.debug("Reading headers from "+file.getFilePointer());
		dataType = file.readByte();
		unsentCount = file.readInt();
		unsentOffset = file.readLong();
		file.seek(BODY);
		log.debug("Read headers, now seeked to "+file.getFilePointer());
		return file;
	}
	
	private RandomAccessFile writeHeaders(RandomAccessFile file) throws IOException{
		file.seek(HEAD);
		log.debug("Writing headers at "+file.getFilePointer());
		file.writeByte(dataType);
		file.writeInt(unsentCount);
		file.writeLong(unsentOffset);
		file.seek(BODY);
		log.debug("Wrote headers, now seeked to "+file.getFilePointer());
		return file;
	} 
	
	private RandomAccessFile headers(RandomAccessFile file) throws IOException{
		return 0 == file.length() ? writeHeaders(file) : readHeaders(file);
	}
	
	private Path buildPath(){
		return Paths.get(properties.get(Constant.KEY_STORAGE, null), 
				"data", 
				thingId.toString(), 
				sensorId.toString()+".binary");
	}
	

}
