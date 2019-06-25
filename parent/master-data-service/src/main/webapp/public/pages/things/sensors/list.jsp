<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
final UserDTO user = (UserDTO)session.getAttribute("user"); 
final boolean hasSensorWriteAccess = user.hasAuthority(Authority.SENSOR_CREATE) || user.hasAuthority(Authority.SENSOR_UPDATE) || user.hasAuthority(Authority.SENSOR_DELETE);
%>
<input type="hidden" id="thing-id">
<table id="sensor-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
	<thead>
		<tr>
			<th scope="col">Name</th>
			<th scope="col">Abbreviation</th>
			<th scope="col">Unit</th>
			<th scope="col">Data Type</th>
			<% if(hasSensorWriteAccess){ %>
			<th scope="col" width="100">
				<%if(user.hasAuthority(Authority.SENSOR_CREATE)){ %>
				<button type="button" class="btn btn-primary btn-sm pull-right" formnovalidate="formnovalidate" id="newSensorBtn">
					<span class="fa fa-plus"></span> New Sensor
				</button>
				<%} %>
			</th>
			<%} %>
		</tr>
	</thead>
</table>
<div id="sensor-modal-container"></div>
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
	       <% if(hasSensorWriteAccess){ %>
	       ,{
	            render: function (data, type, row, meta) {
	            	var copyHtml = user.hasAuthority("SENSOR_CREATE") ? 
	        			'<button type="button" class="btn btn-success btn-xs" onclick="$(\'#sensor-table\').trigger(\'copy-sensor\', [this, event,'+row.id+'])">' +
	                    	'<span class="fa fa-copy"></span> Copy' +
	                    '</button>' : "";
	            	var editHtml = user.hasAuthority("SENSOR_UPDATE") ? 
	           			'<button type="button" class="btn btn-warning btn-xs" onclick="$(\'#sensor-table\').trigger(\'edit-sensor\', [this, event,'+row.id+'])">' +
	                       	'<span class="fa fa-edit"></span> Edit' +
	                    '</button>' : "";
	                var deleteHtml = user.hasAuthority("SENSOR_DELETE") ?
	                	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#sensor-table\').trigger(\'delete-sensor\', [this, event,'+row.id+'])">' +
	                    	'<span class="fa fa-trash"></span> Delete' +
	                    '</button>' : "";
	                return '<div class="btn-group pull-right" role="group">' + copyHtml + editHtml + deleteHtml + '</div>';
	            }
	        }
	       <%}%>
	    ]
	});
	
	if($("#newSensorBtn")){
		$("#newSensorBtn").click(function(){
			$.publish("show-sensor-edit-modal", [$("#thing-id").val(), 0, function(data){
				sensorDataTable.row.add(data).draw();
			}]);
		});	
	}
	
	$("#sensor-table").on("copy-sensor", function(){
		
	});

	$("#sensor-table").on("edit-sensor", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = sensorDataTable.row("#" + id);
		$.publish("show-sensor-edit-modal", [$("#thing-id").val(), row.data().id, function(data){
			row.data(data).draw();
		}]);
	});

	$("#sensor-table").on("delete-sensor", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = sensorDataTable.row("#" + id);
	    $.publish("show-confirmation-modal", [{
	        message: "Are you sure you want to delete sensor " + row.data().name + " ?"
	    }, function () {
	        sensorService.deleteById($("#thing-id").val(), id, function () {
	        	toast('Sensor has been deleted successfully');
	            row.remove().draw();
	            $.modal.close();
	        });
	    }]);
	});
	
	return function(thingId, sensors){
		$("#thing-id").val(thingId);
		sensorDataTable.clear().rows.add(sensors).draw().columns.adjust();
	}
	
})(jQuery);
</script>