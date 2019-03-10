var viewTemplateDetails = function(id){
	$.get("../../templates/"+id, function(template, status){
		$("#templateName").val(template.name);
		$("#templateDescription").val(template.description);
		$("#inactivityPeriod").val(template.inactivityPeriod);
		$("#templateTags").val(template.tags);
		$("#metrics").DataTable({
			searching: false,
			ordering: false,
			paging: false,
			info: false,
			data: template.metrics,
			columns : [
				{data: "name"},
				{data: "abbreviation"},
				{data: "unit"},
				{data: "dataType"},
				{data: "description"},
				{data: "dataType"}
			]
		});
	});
}
