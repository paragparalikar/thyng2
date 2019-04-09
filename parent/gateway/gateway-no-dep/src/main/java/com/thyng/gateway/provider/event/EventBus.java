package com.thyng.gateway.provider.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public class EventBus {

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
	
	@SuppressWarnings("unchecked")
	public void publish(String topic, Object payload){
		final Set<Consumer> handlers = registrations.get(topic);
		if(null != handlers){
			for(Consumer handler : handlers){
				if(null != handler){
					try{
						handler.accept(payload);
					}catch(Throwable t){
						t.printStackTrace();
					}
				}
			}
		}
	}
	
	
}
