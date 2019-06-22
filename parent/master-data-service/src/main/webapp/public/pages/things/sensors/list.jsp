<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
final UserDTO user = (UserDTO)session.getAttribute("user"); 
if(user.hasAuthority(Authority.SENSOR_LIST)){ 
	final boolean hasSensorWriteAccess = user.hasAuthority(Authority.SENSOR_CREATE) || user.hasAuthority(Authority.SENSOR_UPDATE) || user.hasAuthority(Authority.SENSOR_DELETE);
%>
<div class="card" id="sensor-card">
	<div class="card-header">
		<h5>Sensors</h5>
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
                		'<a class="btn btn-success btn-xs" href="#things/copy/'+row.id+'">' +
                        	'<span class="fa fa-copy"></span> Copy' +
                        '</a>' : "";
            	var editHtml = user.hasAuthority("SENSOR_UPDATE") ? 
            		'<a class="btn btn-warning btn-xs" href="#things/edit/'+row.id+'">' +
                        '<span class="fa fa-edit"></span> Edit' +
                    '</a>' : "";
                var deleteHtml = user.hasAuthority("SENSOR_DELETE") ?
                	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#things\').trigger(\'delete-thing\', [this, event,'+row.id+'])">' +
                    	'<span class="fa fa-trash"></span> Delete' +
                    '</button>' : "";
                return '<div class="btn-group pull-right" role="group">' + copyHtml + editHtml + deleteHtml + '</div>';
            }
        }
       <%}%>
    ]
});
</script>
<%} %>
<script>
	var sensorsListView = {
		data : function(sensors){
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