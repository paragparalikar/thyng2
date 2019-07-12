package com.thyng.gateway.provider.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FileConfigurationStore implements ConfigurationStore {

	private final PropertyProvider properties;
	
	@Override
	public synchronized GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception{
		final Path path = getConfigurationFilePath();
		try (final FileChannel channel = FileChannel.open(path, 
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
				final FileLock fileLock = channel.lock()){
			log.info("Persisting gateway details to "+path);
			try(final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ObjectOutputStream oos = new ObjectOutputStream(baos)){
				oos.writeObject(dto);
				oos.flush();
				channel.write(ByteBuffer.wrap(baos.toByteArray()));
				log.info("Successfully persisted gateway details to "+path);
				return dto;
			}
		}
	}
	
	@Override
	public synchronized Optional<GatewayConfigurationDTO> load() throws Exception{
		final Path path = getConfigurationFilePath();
		if(Files.exists(path)) {
			log.info("Reading gateway details from "+path);
			final ByteArrayInputStream bais = new ByteArrayInputStream(Files.readAllBytes(path));
			final ObjectInputStream ois = new ObjectInputStream(bais);
			final GatewayConfigurationDTO dto = (GatewayConfigurationDTO) ois.readObject();
			log.info("Successfully read gateway details from "+path);
			return Optional.of(dto);
		}
		return Optional.empty();
	}
	
	private Path getConfigurationFilePath() throws IOException {
		return Files.createDirectories(Paths.get(properties.get(Constant.KEY_STORAGE, null)))
				.resolve(properties.get(Constant.KEY_GATEWAY_ID, null) + ".ser");
	}
	
}
