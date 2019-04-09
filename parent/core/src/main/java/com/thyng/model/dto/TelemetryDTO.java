package com.thyng.model.dto;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TelemetryDTO {

	private final Long sensorId;
	private final HashMap<Long, Object> values = new HashMap<>();
	
	public TelemetryDTO(Long sensorId, byte dataType, byte[] data) throws IOException {
		this.sensorId = sensorId;
		read(dataType, data);
	}
	
	public TelemetryDTO(Long sensorId) {
		this.sensorId = sensorId;
	}
	
	public Long getSensorId() {
		return sensorId;
	}
	
	public Map<Long, Object> getValues() {
		return Collections.unmodifiableMap(values);
	}
	
	public TelemetryDTO read(byte dataType, byte[] data) throws IOException{
		final DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		while(0 < in.available()){
			final Long timestamp = in.readLong();
			switch(dataType){
			case 0: values.put(timestamp, in.readUTF()); break;
			case 1: values.put(timestamp, in.readBoolean()); break;
			case 2: values.put(timestamp, in.readByte()); break;
			case 3: values.put(timestamp, in.readChar()); break;
			case 4: values.put(timestamp, in.readShort()); break;
			case 5: values.put(timestamp, in.readInt()); break;
			case 6: values.put(timestamp, in.readFloat()); break;
			case 7: values.put(timestamp, in.readLong()); break;
			case 8: values.put(timestamp, in.readDouble()); break;
			}
		}
		return this;
	}
	
	public static byte write(Object value, DataOutput out) throws IOException{
		byte dataType = 8;
		if(value instanceof Double){
			dataType = 8;
			out.writeDouble((Double)value);
		}else if(value instanceof Long){
			dataType = 7;
			out.writeLong((Long)value);
		}else if(value instanceof Float){
			dataType = 6;
			out.writeFloat((Float)value);
		}else if(value instanceof Integer){
			dataType = 5;
			out.writeInt((Integer)value);
		}else if(value instanceof Short){
			dataType = 4;
			out.writeShort((Short)value);
		}else if(value instanceof Character){
			dataType = 3;
			out.writeChar((Character)value);
		}else if(value instanceof Byte){
			dataType = 2;
			out.writeByte((Byte)value);
		}else if(value instanceof Boolean){
			dataType = 1;
			out.writeBoolean((Boolean)value);
		}else if(value instanceof String){
			dataType = 0;
			out.writeUTF(value.toString());
		}
		return dataType;
	}
	
	public byte write(DataOutput file) throws IOException{
		byte dataType = 8;
		for(Long timestamp : values.keySet()){
			final Object value = values.get(timestamp);
			dataType = TelemetryDTO.write(value, file);
		}
		return dataType;
	}
		
}
