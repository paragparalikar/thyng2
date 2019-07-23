package com.thyng.configuration.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;

import com.thyng.domain.metrics.Metrics;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class KafkaMetricsProducer {

	private final String clientId;
	private final String bootstrapServers;
	private KafkaProducer<Long, Metrics> producer;
	
	@Builder
	public KafkaMetricsProducer(String clientId, String bootstrapServers) {
		super();
		this.clientId = clientId;
		this.bootstrapServers = bootstrapServers;
	}
	
	public void start() {
		stop();
		producer = buildProducer(clientId, bootstrapServers);
	}
	
	public void stop() {
		if(null != producer) {
			producer.flush();
			producer.close();
		}
	}
	
	protected KafkaProducer<Long, Metrics> buildProducer(String clientId, String bootstrapServers){
		final Properties properties = new Properties();
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MetricsSerde.class.getName());
		return new KafkaProducer<>(properties);
	}
		

	public void send(String topic, Metrics metrics){
		final ProducerRecord<Long, Metrics> dataRecord = new ProducerRecord<Long, Metrics>(topic, null, 
				metrics.getTimestamp(), metrics.getSensorId(), metrics);
		producer.send(dataRecord, (metadata, exception) -> onSent(metrics, metadata, exception));
		producer.flush();
	}
	
	protected void onSent(Metrics metrics, RecordMetadata metadata, Exception exception) {
	}
}
