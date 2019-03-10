var editTemplateDetails = function(id){
	loadTemplateDetails(id);
}

$("#cancelButton").click(function(){
	$.modal.close();
});

var loadTemplateDetails = function(id){
	templateService.findOne(id, function(template, status){
		populateTemplateDetails(template);
		var table = loadMetrics(template.id);
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
	return $("#metrics-table").DataTable({
		searching: false,
		ordering: false,
		paging: false,
		info: false,
		ajax: {
			url : "../../templates/"+id+"/metrics",
			dataSrc : ""
		},
		columns : [
			{data: "name"},
			{data: "abbreviation"},
			{data: "unit"},
			{data: "dataType"},
			{data: "description"},
			{
				mRender : function(data, type, row) {
					return '<div class="btn-group" role="group">'+
						'<button type="button" class="btn btn-warning btn-xs" onclick="showMatricEditorModal(this, event, '+row.id+');">'+
							'<span class="fa fa-edit"></span> Edit'+
						'</button>'+
						'<button type="button" class="btn btn-danger btn-xs">'+
							'<span class="fa fa-trash"></span> Delete'+
						'</button>'+
					'</div>';
				}
			}
		]
	});
}

var showMatricEditorModal = function(source, event, id){
	event.preventDefault();
	source.blur();
}
