<input type="hidden" id="thing-id">
<table id="actuator-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
	<thead>
		<tr>
			<th scope="col">Name</th>
			<th scope="col">Abbreviation</th>
			<th scope="col">Unit</th>
		</tr>
	</thead>
</table>
<script>
renderActuatorDataTable = (function($){
	var actuatorDataTable = $("#actuator-table").DataTable({
	    searching : false,
	    ordering : false,
	    paging : false,
	    info : false,
	    rowId: "id",
	    processing: true,
	    columns : [ 
	       {
	    	   data : "name",
	           render: function (data, type, row) {
	               return user.hasAuthority("ACTUATOR_VIEW") ? '<a href="" onclick="$(\'#actuator-table\').trigger(\'view-actuator\', [this, event,'+row.id+'])">' + data + '</a>' : data;
	           }
	       }, 
	       {data : "abbreviation" }, 
	       {data : "unit"}
	    ]
	});
	
	$("#actuator-table").on("view-actuator", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = actuatorDataTable.row("#" + id);
		$.publish("show-actuator-view-modal", [$("#thing-id").val(), row.data().id]);
	});
	
	return function(thingId, actuators){
		$("#thing-id").val(thingId);
		actuatorDataTable.clear().rows.add(actuators).draw().columns.adjust();
	};
})(jQuery);
</script>