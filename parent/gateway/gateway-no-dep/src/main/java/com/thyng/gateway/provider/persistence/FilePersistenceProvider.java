package com.thyng.gateway.provider.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.Serializer;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FilePersistenceProvider implements PersistenceProvider {
	
	private final PropertyProvider properties;
	private final Map<Long, FileStore> cache = new HashMap<>();
	
	@Override
	public Message save(Message message) throws Exception {
		final Map<Long, Object> values = message.getValues();
		for(final Long sensorId : values.keySet()){
			final Object value = values.get(sensorId);
			final ThingDetailsDTO thing = message.getThing(sensorId);
			final FileStore fileStore = getFileStore(thing.getId(), sensorId);
			final Integer unsentCount = fileStore.write(message.getTimestamp(), value);
			message.getUnsentCounts().put(sensorId, unsentCount);
		}
		return message;
	}

	private FileStore getFileStore(Long thingId, Long sensorId){
		FileStore fileStore = cache.get(sensorId);
		if(null == fileStore){
			fileStore = new FileStore(thingId, sensorId, properties);
			cache.put(sensorId, fileStore);
		}
		return fileStore;
	}
	
	@Override
	public PersistentTelemetry getUnsentTelemetry(Long thingId, Long sensorId) throws Exception {
		return getFileStore(thingId, sensorId).getUnsentTelemetry();
	}

	@Override
	public void markSent(PersistentTelemetry telemetry) throws Exception {
		getFileStore(telemetry.getThingId(), telemetry.getSensorId()).markSent(telemetry);
	}
	
	@Override
	public GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception{
		final File file = getConfigurationFile();
		log.info("Persisting gateway details to "+file.getAbsolutePath());
		try(final Output output = new Output(new FileOutputStream(file))){
			Serializer.kryo().writeObject(output, dto);
			return dto;
		}
	}
	
	@Override
	public GatewayConfigurationDTO load() throws Exception{
		final File file = getConfigurationFile();
		log.info("Loading gateway details from "+file.getAbsolutePath());
		try(final Input input = new Input(new FileInputStream(file))){
			return Serializer.kryo().readObject(input, GatewayConfigurationDTO.class);
		}
	}
	
	private File getConfigurationFile() throws IOException{
		final String path = properties.get(Constant.KEY_STORAGE, null)
				+ properties.get(Constant.KEY_GATEWAY_ID, null) + ".binary";
		final File file = new File(path);
		if(!file.exists()){
			log.info("Could not find gateway details file "
					+path+"\ncreating new file...");
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}
}
