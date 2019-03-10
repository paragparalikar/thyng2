MetricService = function(){};
MetricService.prototype.findByTemplateIdUrl = function(templateId){
	return window.location.origin + "/templates/" + templateId + "/metrics"; 
};
MetricService.prototype.findByTemplateId = function(templateId, successCallback){
	$.get(this.findByTemplateIdUrl(templateId), successCallback);
};
metricService = new MetricService();