var metricService = {
	findByTemplateId : function(templateId, successCallback) {
		$.get(window.location.origin + "/api/v1/metrics?templateId=" + templateId, successCallback);
	},
	deleteById: function(id, callback){
		$.ajax({
			url: window.location.origin + "/api/v1/metrics/" + id,
			type: "DELETE",
			success: callback
		});
	}
}