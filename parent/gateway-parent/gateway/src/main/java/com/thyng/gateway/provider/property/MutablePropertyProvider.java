package com.thyng.gateway.provider.property;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.thyng.gateway.Constant;
import com.thyng.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MutablePropertyProvider implements PropertyProvider{

	private final Properties properties = new Properties();
	
	public MutablePropertyProvider load() throws IOException{
		loadDefaultProperties();
		loadCustomProperties();
		loadSystemProperties();
		resolveSystemProperties();
		return this;
	}
	
	public MutablePropertyProvider load(Map<String, String> properties){
		log.info("Loading properties - provided");
		this.properties.putAll(properties);
		return this;
	}
	
	private void loadDefaultProperties() throws IOException{
		log.info("Loading properties - default");
		properties.put(Constant.KEY_STORAGE, System.getProperty("user.home") 
				+ File.separator + "thyng" + File.separator);
		final String location = "/Thyng.properties";
		try(final InputStream in = MutablePropertyProvider.class.getResourceAsStream(location)){
			properties.load(in);
		}
	}
	
	private void loadCustomProperties() throws IOException{
		log.info("Loading properties - custom");
		final String location = System.getProperty("thyng.gateway.configuration.path");
		if(StringUtil.hasText(location)){
			try(final InputStream in = MutablePropertyProvider.class.getResourceAsStream(location)){
				properties.load(in);
			}
		}
	}
	
	private void loadSystemProperties(){
		log.info("Loading properties - system");
		properties.putAll(System.getProperties());
	}
	
	private void resolveSystemProperties() {
		final String prefix = "system:";
		log.info("Resolving properties with prefix \""+prefix+"\"");
		properties.stringPropertyNames().forEach(key -> {
			final String value = properties.getProperty(key);
			if(StringUtil.hasText(value) && value.startsWith(prefix)) {
				final String envKey = value.substring(prefix.length());
				final String envValue = System.getProperty(envKey, System.getenv(envKey));
				log.info("Resolving " + key + " using " + envKey + " as " + envValue);
				properties.put(key, envValue);
			}
		});
	}

	@Override
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		final String value = get(key, null);
		return StringUtil.hasText(value) ? Long.parseLong(value) : defaultValue;
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		final String value = get(key, null);
		return StringUtil.hasText(value) ? Integer.parseInt(value) : defaultValue;
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		final String value = get(key, null);
		return StringUtil.hasText(value) ? Boolean.parseBoolean(value) : defaultValue;
	}
	
}
