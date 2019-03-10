templateService.findAll(function(templates){
	$("#templates").DataTable({
		columns : [{data : "name"},
				{data : "inactivityPeriod"},
				{data : "description"},
				{data : "tags"},
				{
					mRender : function(data, type, row) {
						return '<div class="btn-group" role="group">'+
							'<button type="button" class="btn btn-warning btn-xs" onclick="showViewTemplateModal(this, event, '+row.id+');">'+
								'<span class="fa fa-edit"></span> Edit'+
							'</button>'+
							'<button type="button" class="btn btn-danger btn-xs">'+
								'<span class="fa fa-trash"></span> Delete'+
							'</button>'+
						'</div>';
					}
				} ],
		columnDefs: [{
			targets: -1,
			className: "text-center"
		}],
		data: templates
	});
});

var showViewTemplateModal = function(source, event, id) {
	event.preventDefault();
	source.blur();
	$(".modal").load("pages/template/view.html", function() {
		$(".modal").modal();
		viewTemplateDetails(id);
	});
};


