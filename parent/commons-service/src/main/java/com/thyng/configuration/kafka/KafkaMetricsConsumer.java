package com.thyng.configuration.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;

import com.thyng.domain.metrics.Metrics;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class KafkaMetricsConsumer {

	private final String topic;
	private final String consumerGroupId;
	private final String bootstrapServers;
	private final Consumer<Long, Metrics> consumer;
	
	@Builder
	public KafkaMetricsConsumer(String consumerGroupId, String bootstrapServers, String topic) {
		super();
		this.topic = topic;
		this.consumerGroupId = consumerGroupId;
		this.bootstrapServers = bootstrapServers;
		this.consumer = buildKafkaConsumer();
		consumer.subscribe(Collections.singleton(topic));
	}
	
	private KafkaConsumer<Long, Metrics> buildKafkaConsumer(){
		final Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MetricsSerde.class.getName());
		return new KafkaConsumer<>(properties);
	}

}
