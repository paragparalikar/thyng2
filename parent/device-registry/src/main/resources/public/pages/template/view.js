var viewTemplateDetails = function(id){
	$.get("../../templates/"+id, function(template, status){
		$("#templateName").val(template.name);
		$("#templateDescription").val(template.description);
		$("#inactivityPeriod").val(template.inactivityPeriod);
		$("#templateTags").val(template.tags);
	});
}
