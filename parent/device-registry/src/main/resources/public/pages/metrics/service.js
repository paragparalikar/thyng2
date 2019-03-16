var metricService = {
	findByTemplateId : function(templateId, successCallback) {
		$.get(window.location.origin + "/api/v1/templates/" + templateId + "/metrics", successCallback);
	}
}