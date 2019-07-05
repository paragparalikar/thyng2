package com.thyng.gateway.provider.persistence;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import com.thyng.gateway.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.provider.serialization.SerializationProvider;
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FileConfigurationStore implements ConfigurationStore {

	private final PropertyProvider properties;
	private final SerializationProvider<String> serializationProvider;
	
	@Override
	public synchronized GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception{
		final Path path = getConfigurationFilePath();
		try (final FileChannel channel = FileChannel.open(path, 
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
				final FileLock fileLock = channel.lock()){
			log.info("Persisting gateway details to "+path);
			final String json = serializationProvider.serialize(dto);
			channel.write(ByteBuffer.wrap(json.getBytes(Constant.CHARSET)));
			log.info("Successfully persisted gateway details to "+path);
			return dto;
		}
	}
	
	@Override
	public synchronized Optional<GatewayConfigurationDTO> load() throws Exception{
		final Path path = getConfigurationFilePath();
		if(Files.exists(path)) {
			log.info("Reading gateway details from "+path);
			final String json = new String(Files.readAllBytes(path), Constant.CHARSET);
			final GatewayConfigurationDTO dto = serializationProvider.deserialize(json, 
					GatewayConfigurationDTO.class);
			log.info("Successfully read gateway details from "+path);
			return Optional.of(dto);
		}
		return Optional.empty();
	}
	
	private Path getConfigurationFilePath() throws IOException {
		return Files.createDirectories(Paths.get(properties.get(Constant.KEY_STORAGE, null)))
				.resolve(properties.get(Constant.KEY_GATEWAY_ID, null) + ".json");
	}
	
}
