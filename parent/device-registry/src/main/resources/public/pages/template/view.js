var viewTemplateDetails = function(id){
	loadTemplateDetails(id);
}

$("#cancelButton").click(function(){
	$.modal.close();
});

var loadTemplateDetails = function(id){
	templateService.findOne(id, function(template, status){
		populateTemplateDetails(template);
		loadMetrics(template.id);
	});
}

var populateTemplateDetails = function(template){
	$("#templateId").val(template.id);
	$("#templateName").val(template.name);
	$("#templateDescription").val(template.description);
	$("#inactivityPeriod").val(template.inactivityPeriod);
	$("#templateTags").val(template.tags);
	$("#templateProperties").val($.map(template.properties, function(value,key){
		return key + "=" + value;
	}).join("\n"));
}


var loadMetrics = function(id){
	metricService.findByTemplateId(id, function(metrics){
		$("#metrics-table").DataTable({
			searching: false,
			ordering: false,
			paging: false,
			info: false,
			data: metrics,
			columns : [
				{data: "name"},
				{data: "abbreviation"},
				{data: "unit"},
				{data: "dataType"},
				{data: "description"},
			]
		});
	});
}
