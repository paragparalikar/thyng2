var metricService = {
	findByTemplateId : function(templateId, successCallback) {
		$.get(window.location.origin + "/api/v1/metrics?templateId=" + templateId, successCallback);
	}
}