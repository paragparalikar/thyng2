package com.thyng.gateway.provider.persistence;

import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception{
		lock.writeLock().lock();
		final Path path = getConfigurationFilePath();
		try (final FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING);
				final FileLock fileLock = channel.lock()){
			log.info("Persisting gateway details to "+path);
			final String json = serializationProvider.serialize(dto);
			channel.write(ByteBuffer.wrap(json.getBytes(Constant.CHARSET)));
			log.info("Successfully persisted gateway details to "+path);
			return dto;
		}finally {
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public Optional<GatewayConfigurationDTO> load() throws Exception{
		lock.readLock().lock();
		final Path path = getConfigurationFilePath();
		if(Files.exists(path)) {
			try(final FileChannel channel = FileChannel.open(path, 
					StandardOpenOption.READ);
					final FileLock fileLock = channel.lock()){
				log.info("Reading gateway details from "+path);
				final ByteBuffer buffer = ByteBuffer.allocate(48);
				final StringWriter writer = new StringWriter();
				while(0 < channel.read(buffer)) {
					buffer.flip();
					while(buffer.hasRemaining()) {
						writer.append(buffer.getChar());
					}
					buffer.clear();
				}
				writer.flush();
				final String json = writer.toString();
				final GatewayConfigurationDTO dto = serializationProvider.deserialize(json, 
						GatewayConfigurationDTO.class);
				log.info("Successfully read gateway details from "+path);
				return Optional.of(dto);
			}finally {
				lock.readLock().unlock();
			}
		}
		return Optional.empty();
	}
	
	private Path getConfigurationFilePath() {
		return Paths.get(properties.get(Constant.KEY_STORAGE, null),  
				properties.get(Constant.KEY_GATEWAY_ID, null) + ".json");
	}
	
}
