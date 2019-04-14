package com.thyng.gateway.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.SensorStatus;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThingMonitoringService extends CompositeService implements Consumer<SensorStatus>{

	private Boolean active;
	private final Context context;
	private final ThingDetailsDTO thing;
	private final Map<Long, Boolean> sensorStatuses = new HashMap<>();
	
	public ThingMonitoringService(Context context, ThingDetailsDTO thing) { 
		this.thing = thing;
		this.context = context;
		for(SensorDTO sensor : thing.getSensors()){
			add(new SensorMonitoringService(context, sensor));
		}
	}
	
	@Override
	public void start() throws Exception {
		for(SensorDTO sensor : thing.getSensors()){
			context.getEventBus().register(SensorMonitoringService.getTopicStatusChanged(sensor.getId()), this);
		}
		super.start();
	}

	@Override
	public void accept(SensorStatus sensorStatus) {
		try{
			sensorStatuses.put(sensorStatus.getSensor().getId(), sensorStatus.getActive());
			if(!active && sensorStatus.getActive()){
				active = true;
				context.getClient().sendThingStatus(thing.getId(), active);
			}else if(active && !sensorStatus.getActive()){
				Boolean active = false;
				for(SensorDTO sensor : thing.getSensors()){
					final Boolean sensorActive = sensorStatuses.get(sensor.getId());
					if(active = null != sensorActive && sensorActive){
						break;
					}
				}
				if(!active){
					this.active = false;
					context.getClient().sendThingStatus(thing.getId(), this.active);
				}
			}
		}catch(Exception e){
			log.error("Exception while monitoring thing", e);
		}
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		for(SensorDTO sensor : thing.getSensors()){
			context.getEventBus().unregister(SensorMonitoringService.getTopicStatusChanged(sensor.getId()), this);
		}
	}
	
}
