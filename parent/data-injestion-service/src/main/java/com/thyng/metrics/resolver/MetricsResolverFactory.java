package com.thyng.metrics.resolver;

import com.thyng.model.enumeration.MetricsType;

public class MetricsResolverFactory {

	private final CsvMetricsResolver csvMetricsResolver = new CsvMetricsResolver();
	private final XmlMetricsResolver xmlMetricsResolver = new XmlMetricsResolver();
	private final JsonMetricsResolver jsonMetricsResolver = new JsonMetricsResolver();
	private final BinaryMetricsResolver binaryMetricsResolver = new BinaryMetricsResolver();
	private final DefaultMetricsResolver defaultMetricsResolver = new DefaultMetricsResolver();
	private final UrlQueryMetricsResolver urlQueryMetricsResolver = new UrlQueryMetricsResolver();
	private final UrlFormEncodedMetricsResolver urlFormEncodedMetricsResolver = new UrlFormEncodedMetricsResolver();
	
	public MetricsResolver getMetricsResolver(final MetricsType type){
		if(null == type) {
			return defaultMetricsResolver;
		}
		switch(type) {
		case XML: return xmlMetricsResolver;
		case CSV: return csvMetricsResolver;
		case JSON: return jsonMetricsResolver;
		case BINARY: return binaryMetricsResolver;
		case URL_QUERY_STRING: return urlQueryMetricsResolver;
		case URL_FORM_ENCODED: return urlFormEncodedMetricsResolver;
		default: throw new IllegalArgumentException(type+" is not supported");		
		}
	}

}
