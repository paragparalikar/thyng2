package com.thyng.metrics;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.thyng.configuration.kafka.KafkaTopicProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SensorActivityProcessor {
	
	private final StreamsBuilder builder;
	private final HazelcastInstance hazelcastInstance;
	private final KafkaTopicProvider kafkaTopicProvider;
	
	@PostConstruct
	public void start() throws Exception{
		final IMap<Long, Long> map = hazelcastInstance.getMap("sensor-last-activity-timestamp");
		builder
		.stream(kafkaTopicProvider.getAllTenantsMetricsPattern(), Consumed.<Long, byte[]>with(Serdes.Long(), Serdes.ByteArray()))
		.transformValues(() -> new TimestampValueTransformer<byte[]>())
		.groupByKey()
		.reduce((aggregateValue, newValue) -> newValue)
		.toStream()
		.foreach(map::putAsync);
	}
	
	@PreDestroy
	public void stop() throws Exception{
		
	}
	
}

class TimestampValueTransformer<K> implements ValueTransformer<K,Long>{

	private ProcessorContext context;
	
	@Override
	public void init(ProcessorContext context) {
		this.context = context;
	}

	@Override
	public Long transform(K value) {
		return context.timestamp();
	}

	@Override
	public void close() {
		
	}
	
}