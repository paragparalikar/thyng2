package com.thyng.sensor;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.pipeline.JournalInitialPosition;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sources;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SensorActivityTrackingPipeline {

	private Job job;
	private final JetInstance jetInstance;
	
	public void start() {
		final Pipeline pipeline = Pipeline.create();
		pipeline
			.drawFrom(Sources.mapJournal("", JournalInitialPosition.START_FROM_CURRENT))
			.withoutTimestamps();
			
		
	}
	
	public void stop() {

	}

}
