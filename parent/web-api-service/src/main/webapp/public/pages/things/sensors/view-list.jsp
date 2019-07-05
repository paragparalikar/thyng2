<input type="hidden" id="thing-id">
<table id="sensor-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
	<thead>
		<tr>
			<th scope="col">Name</th>
			<th scope="col">Abbreviation</th>
			<th scope="col">Unit</th>
		</tr>
	</thead>
</table>
<script>
renderSensorDataTable = (function($){
	var sensorDataTable = $("#sensor-table").DataTable({
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
	               return user.hasAuthority("SENSOR_VIEW") ? '<a href="" onclick="$(\'#sensor-table\').trigger(\'view-sensor\', [this, event,'+row.id+'])">' + data + '</a>' : data;
	           }
	       }, 
	       {data : "abbreviation" }, 
	       {data : "unit"}
	    ]
	});
	
	$("#sensor-table").on("view-sensor", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = sensorDataTable.row("#" + id);
		$.publish("show-sensor-view-modal", [$("#thing-id").val(), row.data().id]);
	});
	
	return function(thingId, sensors){
		$("#thing-id").val(thingId);
		sensorDataTable.clear().rows.add(sensors).draw().columns.adjust();
	};
})(jQuery);
</script>