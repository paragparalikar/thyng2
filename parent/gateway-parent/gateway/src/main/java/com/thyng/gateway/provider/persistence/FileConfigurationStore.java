package com.thyng.gateway.provider.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.gateway.model.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.Serializer;
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FileConfigurationStore implements ConfigurationStore {

	private final PropertyProvider properties;
	
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
