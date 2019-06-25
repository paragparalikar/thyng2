<table id="sensor-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
	<thead>
		<tr>
			<th scope="col">Name</th>
			<th scope="col">Abbreviation</th>
			<th scope="col">Unit</th>
			<th scope="col">Data Type</th>
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
	               return user.hasAuthority("SENSOR_VIEW") ? '<a href="#things/view/'+row.id+'">' + data + '</a>' : data;
	           }
	       }, 
	       {data : "abbreviation" }, 
	       {data : "unit"}, 
	       {data : "dataType"}
	    ]
	});
	
	return function(sensors){
		sensorDataTable.clear().rows.add(sensors).draw().columns.adjust();
	};
})(jQuery);
</script>