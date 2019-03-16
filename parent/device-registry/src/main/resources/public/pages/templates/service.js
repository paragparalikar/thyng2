var templateService = {
	findAll : function(successCallback) {
		$.get(window.location.origin + "/api/v1/templates", successCallback);
	},
	findOne : function(id, successCallback) {
		$.get(window.location.origin + "/api/v1/templates/" + id, successCallback);
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
    $("#template-container").loadTemplate("pages/templates/list.html");
});
$.router.add("#templates/:id", function(){
    if(arguments[0]){
        templateService.findOne(arguments[0], function(template, status){
			doShowTemplateEditView(template);
		});
    }else{
        doShowTemplateEditView({metrics: []});
    }
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

var doShowTemplateEditView = function(template){
	$("#template-container").loadTemplate("pages/templates/edit.html", template, {
		success : function(){
			showMetricsDataTable(template.metrics);
		}
	});
}

