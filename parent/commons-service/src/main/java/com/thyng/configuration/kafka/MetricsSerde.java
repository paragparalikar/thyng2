package com.thyng.configuration.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.thyng.domain.metrics.Metrics;

import lombok.SneakyThrows;

/**
 * Ignores sensorId and timestamp as they will be part of kafka messaging anyway
 * This will reduce payload size by half, will allow us to store double the data for same cost
 */
public class MetricsSerde implements Serializer<Metrics>, Deserializer<Metrics>, Serde<Metrics> {

	public static final MetricsSerde INSTANCE = new MetricsSerde();
	
	@Override
	@SneakyThrows
	public byte[] serialize(String topic, Metrics data) {
		if(null == data) {
			return null;
		}
		
		byte[] meta = new byte[0];
		if(null != data.getMeta()) {
			meta = data.getMeta().toString().getBytes("UTF-8");
		}
		
		long bits = Double.doubleToLongBits(data.getValue());
		byte[] value = new byte[] {
	            (byte) (bits >>> 56),
	            (byte) (bits >>> 48),
	            (byte) (bits >>> 40),
	            (byte) (bits >>> 32),
	            (byte) (bits >>> 24),
	            (byte) (bits >>> 16),
	            (byte) (bits >>> 8),
	            (byte) bits
	        };
		final byte[] result = new byte[value.length + meta.length];
		System.arraycopy(value, 0, result, 0, value.length);
		if(0 < meta.length) {
			System.arraycopy(meta, 0, result, value.length, meta.length);
		}
		return result;
	}
	
	@Override
	@SneakyThrows
	public Metrics deserialize(String topic, byte[] data) {
		if(null == data || 0 == data.length) {
			return null;
		}
		
		final Metrics metrics = new Metrics();
		long value = 0;
        for (int index = 0; index < 8; index++) {
            value <<= 8;
            value |= data[index] & 0xFF;
        }
        metrics.setValue(Double.longBitsToDouble(value));
        
        if(8 < data.length) {
        	final String meta = new String(data, 8, data.length - 8, "UTF-8");
        	metrics.setMeta(meta);
        }
        
        return metrics;
	}

	@Override
	public Serializer<Metrics> serializer() {
		return this;
	}

	@Override
	public Deserializer<Metrics> deserializer() {
		return this;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		
	}

	@Override
	public void close() {
		
	}

}
