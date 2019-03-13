var metricService = {
	findByTemplateId : function(templateId, successCallback) {
		$.get(window.location.origin + "/templates/" + templateId + "/metrics", successCallback);
	}
}