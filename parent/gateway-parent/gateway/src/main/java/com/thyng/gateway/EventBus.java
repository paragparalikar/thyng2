package com.thyng.gateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class EventBus {

	private final ExecutorService executorService;
	private final Map<String, Set<Consumer>> registrations = new HashMap<>();

	public void register(String topic, Consumer<? extends Object> handler){
		Set<Consumer> handlers = registrations.get(topic);
		if(null == handlers){
			handlers = Collections.newSetFromMap(new WeakHashMap<>());
			registrations.put(topic, handlers);
		}
		handlers.add(handler);
	}
	
	public void unregister(String topic, Consumer handler){
		final Set<Consumer> handlers = registrations.get(topic);
		if(null != handlers){
			handlers.remove(handler);
		}
	}
	
	public void publishAsync(String topic, Object payload) {
		executorService.execute(() -> publish(topic, payload));
	}
	
	@SuppressWarnings("unchecked")
	public void publish(String topic, Object payload){
		final Set<Consumer> handlers = registrations.get(topic);
		if(null != handlers){
			for(Consumer handler : handlers){
				if(null != handler){
					try{
						handler.accept(payload);
					}catch(Throwable t){
						log.error("Event handler threw exception : "
								+handler.getClass().getCanonicalName(), t);
					}
				}
			}
		}
	}
	
	
}
