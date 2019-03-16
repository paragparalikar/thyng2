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
    $("#page-container").loadTemplate("pages/templates/list.html");
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
$.subscribe("show-template-edit-modal", function(event, id){
	if(id){
		templateService.findOne(id, function(template, status){
			doShowTemplateEditModal(template);
		});
	}else{
		doShowTemplateEditModal({metrics: []});
	}
});

var doShowTemplateEditModal = function(template){
	$("#modal-container").loadTemplate("pages/templates/edit.html", template, {
		success : function(){
			$("#modal-container").modal();
			showMetricsDataTable(template.metrics);
		}
	});
}

