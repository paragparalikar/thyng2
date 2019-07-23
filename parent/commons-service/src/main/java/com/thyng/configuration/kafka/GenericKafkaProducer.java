package com.thyng.configuration.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;

import lombok.Builder;

public class GenericKafkaProducer<T> implements AutoCloseable{

	private final KafkaProducer<Long, T> producer;

	@Builder
	public GenericKafkaProducer(String clientId, String bootstrapServers, String valueSerializerClassName) {
		producer = buildProducer(clientId, bootstrapServers, valueSerializerClassName);
	}
		
	@Override
	public void close() throws Exception {
		producer.flush();
		producer.close();
	}
	
	protected KafkaProducer<Long, T> buildProducer(String clientId, String bootstrapServers, String valueSerializerClassName){
		final Properties properties = new Properties();
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClassName);
		return new KafkaProducer<>(properties);
	}
	
	public void send(String topic, Long key, T value){
		send(topic, null, System.currentTimeMillis(), key, value);
	}
	
	public void send(String topic, Long timestamp, Long key, T value){
		send(topic, null, timestamp, key, value);
	}
	
	public void send(String topic, Integer partition, Long timestamp, Long key, T value){
		final ProducerRecord<Long, T> dataRecord = new ProducerRecord<Long, T>(topic, partition, 
				timestamp, key, value);
		producer.send(dataRecord, (metadata, exception) -> onSent(value, metadata, exception));
		producer.flush();
	}
	
	protected void onSent(T value, RecordMetadata metadata, Exception exception) {
		
	}

}
