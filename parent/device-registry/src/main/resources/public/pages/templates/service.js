var templateService = {
	findAll : function(success, error, always) {
		$.get(window.location.origin + "/api/v1/templates")
		.done(success).fail(error ? error : errorCallback).always(always);
	},
	findOne : function(id, success, error, always) {
		$.get(window.location.origin + "/api/v1/templates/" + id)
		.done(success).fail(error ? error : errorCallback).always(always);
	},
	save: function(template, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/templates",
			type: "POST",
			contentType:"application/json; charset=utf-8",
			data: template,
		}).done(success).fail(error ? error : errorCallback).always(always);
	},
	deleteById : function(id, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/templates/" + id,
			type: "DELETE",
		}).done(success).fail(error ? error : errorCallback).always(always);
	}
}

$.router.add("#templates", function(){
    $("#template-container").loadTemplate("pages/templates/list.html", null, {
    	success: function(){
    		render();
    	},
    	error: errorCallback
    });
});

$.router.add("#templates/:id", function(params){
	var id = arguments[0];
	$("#template-container").loadTemplate("pages/templates/edit.html", null, {
		success : function(){
			render(id);
		},
    	error: errorCallback
	});
});

$.subscribe("show-template-view-modal", function(event, id){
	templateService.findOne(id, function(template, status){
		$("#modal-container").loadTemplate("pages/templates/view.html", template, {
			success : function(){
				$("#modal-container").modal();
				showMetricsDataTable(template.metrics);
			},
	    	error: errorCallback
		});
	});
});