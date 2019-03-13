var templateService = {
	findAll : function(successCallback) {
		$.get(window.location.origin + "/templates", successCallback);
	},
	findOne : function(id, successCallback) {
		$.get(window.location.origin + "/templates/" + id, successCallback);
	},
	deleteById : function(id, callback){
		$.ajax({
			url: window.location.origin + "/templates/" + id,
			type: "DELETE",
			success: callback
		});
	}
}

$.subscribe("show-template-list-view", function(){
	$(".content-wrapper").loadTemplate("pages/templates/list.html");
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
	templateService.findOne(id, function(template, status){
		$("#modal-container").loadTemplate("pages/templates/edit.html", template, {
			success : function(){
				$("#modal-container").modal();
				showMetricsDataTable(template.metrics);
			}
		});
	});
});

