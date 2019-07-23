package com.thyng.metrics.normalizer;

import com.thyng.domain.sensor.Language;

public class MetricsNormalizerFactory {

	private final JavascriptMetricsNormalizer javascriptMetricsNormalizer = new JavascriptMetricsNormalizer();

	public MetricsNormalizer getMetricsNormalizer(Language language) {
		switch (language) {
		case JAVASCRIPT:
			return javascriptMetricsNormalizer;
		default:
			throw new IllegalArgumentException("Language not supported - "+language);
		}
	}

}
