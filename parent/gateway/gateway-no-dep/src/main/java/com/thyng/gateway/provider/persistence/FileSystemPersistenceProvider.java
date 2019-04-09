package com.thyng.gateway.provider.persistence;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.event.EventBus;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.dto.TelemetryDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileSystemPersistenceProvider implements PersistenceProvider {

	private final EventBus eventBus;
	private final PropertyProvider properties;
	
	@Override
	public Message save(Message message) throws Exception {
		final Map<Long, Object> values = message.getValues();
		for(final Long sensorId : values.keySet()){
			final ThingDetailsDTO thing = message.getThing(sensorId);
			try(final RandomAccessFile file = getStorageFile(thing.getId(), sensorId)){
				try(final FileLock lock = file.getChannel().lock()){
					byte dataType = 0 == file.length() ? 8 : file.readByte();
					final int unsentCount = 0 == file.length() ? 0 : file.readInt();
					final long unsentOffset = 0 == file.length() ? 0 : file.readLong();
					final Object value = values.get(sensorId);
					file.seek(Math.max(Byte.BYTES + Integer.BYTES + Long.BYTES, file.length()));
					file.writeLong(message.getTimestamp());
					dataType = TelemetryDTO.write(value, file);
					file.seek(0);
					file.writeByte(dataType);
					file.writeInt(unsentCount + 1);
					file.writeLong(unsentOffset);
					message.getUnsentCounts().put(sensorId, unsentCount + 1);
				}
			}
		}
		eventBus.publish(PERSISTED, message);
		return message;
	}
	
	
	@Override
	@SuppressWarnings("unused")
	public TelemetryDTO getUnsentTelemetry(Long thingId, Long sensorId) throws IOException{
		try(final RandomAccessFile file = getStorageFile(thingId, sensorId)){
			try(final FileLock lock = file.getChannel().lock()){
				byte dataType = 0 == file.length() ? 8 : file.readByte();
				final int unsentCount = 0 == file.length() ? 0 : file.readInt();
				final long unsentOffset = 0 == file.length() ? 0 : file.readLong();
				file.seek(unsentOffset);
				final byte[] data = new byte[(int)(file.length() - unsentOffset)];
				file.readFully(data);
				return new TelemetryDTO(sensorId, dataType, data);
			}
		}
	}
	
	private RandomAccessFile getStorageFile(Long thingId, Long sensorId) throws IOException{
		final Path path = buildPath(thingId, sensorId);
		final File file = path.toFile();
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return new RandomAccessFile(file, "rws");
	}

	private Path buildPath(Long thingId, Long sensorId){
		return Paths.get(properties.get(Constant.KEY_STORAGE, null), 
				"data", 
				thingId.toString(), 
				sensorId.toString()+".binary");
	}

}
