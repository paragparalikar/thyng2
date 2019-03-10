$("#templates")
		.DataTable({
					columns : [{data : "name"},
							{data : "inactivityPeriod"},
							{data : "description"},
							{data : "tags"},
							{
								orderable : false,
								mRender : function(data, type, row) {
									return '<a href="#" onclick="showViewModal(event, '+row.id+');">View</a>'
											+ ' | <a href="#">Edit</a>'
											+ ' | <a href="#">Delete</a>';
								}
							} ],
					ajax : {
						url : "../../templates",
						dataSrc : ""
					}
				});

var showViewModal = function(event, id) {
	event.preventDefault();
	$(".modal").load("pages/template/view.html", function() {
		$(".modal").modal();
		viewTemplateDetails(id);
	});
};


