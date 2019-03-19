var templateService = {
	findAll : function(successCallback) {
		$.get(window.location.origin + "/api/v1/templates", successCallback);
	},
	findOne : function(id, successCallback) {
		$.get(window.location.origin + "/api/v1/templates/" + id, successCallback);
	},
	save: function(template, callback){
		$.ajax({
			url: window.location.origin + "/api/v1/templates",
			type: "POST",
			contentType:"application/json; charset=utf-8",
			data: template,
			success: callback
		});
	},
	deleteById : function(id, callback){
		$.ajax({
			url: window.location.origin + "/api/v1/templates/" + id,
			type: "DELETE",
			success: callback
		});
	}
}

$.router.add("#templates", function(){
    $("#template-container").loadTemplate("pages/templates/list.html", null, {
    	success: function(){
    		render();
    	}
    });
});

$.router.add("#templates/:id", function(){
	var id = arguments[0];
	$("#template-container").loadTemplate("pages/templates/edit.html", null, {
		success : function(){
			render(id);
		}
	});
});

$.subscribe("show-template-view-modal", function(event, id){
	templateService.findOne(id, function(template, status){
		$("#modal-container").loadTemplate("pages/templates/view.html", template, {
			success : function(){
				$("#modal-container").modal();
				showMetricsDataTable(template.metrics);
			}
		});
	});
});