var templateService = {
	findAll : function(successCallback) {
		$.get(window.location.origin + "/templates", successCallback);
	},
	findOne : function(id, successCallback) {
		$.get(window.location.origin + "/templates/" + id, successCallback);
	}
}

$.subscribe("show-template-list-view", function(){
	$(".content-wrapper").loadTemplate("pages/templates/list.html");
});
$.subscribe("show-template-view-modal", function(event, id){
	templateService.findOne(id, function(template, status){
		$(".modal").loadTemplate("pages/templates/view.html", template, {
			success : function(){
				$(".modal").modal();
				showMetricsDataTable(template.metrics);
			}
		});
	});
});