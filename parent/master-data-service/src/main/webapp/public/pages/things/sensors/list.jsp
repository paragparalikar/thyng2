<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
final UserDTO user = (UserDTO)session.getAttribute("user"); 
if(user.hasAuthority(Authority.SENSOR_LIST)){ 
	final boolean hasSensorWriteAccess = user.hasAuthority(Authority.SENSOR_CREATE) || user.hasAuthority(Authority.SENSOR_UPDATE) || user.hasAuthority(Authority.SENSOR_DELETE);
%>
<style>
	#sensor-card {
		width: 60em;
		margin-top: 2em;
		margin-left: auto;
		margin-right: auto;
	}
</style>
<div class="card" id="sensor-card">
	<div class="card-header">
		<label>Sensors</label>
	</div>
	<div class="card-body">
		<table id="sensor-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Abbreviation</th>
					<th scope="col">Unit</th>
					<th scope="col">Data Type</th>
					<th scope="col">Active</th>
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
	</div>
</div>
<script>
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
       {data : "dataType"}, 
       {
    	   data : "active",
    	   render: function(data, type, row, meta){
    		   return data ? "Yes" : "No";
    	   }
    	}
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
		$.publish("show-sensor-edit-modal", [sensorsListView.thingId, 0, function(data){
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
	$.publish("show-sensor-edit-modal", [sensorsListView.thingId, row.data().id, function(data){
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
        sensorService.deleteById(sensorsListView.thingId, id, function () {
        	toast('Sensor has been deleted successfully');
            row.remove().draw();
            $.modal.close();
        });
    }]);
});

</script>
<%} %>
<script>
	var sensorsListView = {
		thingId : 0,
		data : function(thingId, sensors){
			this.thingId = thingId;
			if(sensors && sensorDataTable){
				sensorDataTable.clear().rows.add(sensors).draw().columns.adjust();
			}
		},
		display : function(value){
			var card = $("#sensor-card");
			if(card){
				if(value){
					card.show();
				}else{
					card.hide();
				}
			}
		}
	};
</script>