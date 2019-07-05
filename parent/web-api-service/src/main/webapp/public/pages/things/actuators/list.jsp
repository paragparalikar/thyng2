<%@ page import="com.thyng.model.dto.UserDTO" %>
<%@ page import="com.thyng.model.enumeration.Authority" %>
<% 
final UserDTO user = (UserDTO)session.getAttribute("user"); 
final boolean hasActuatorWriteAccess = user.hasAuthority(Authority.ACTUATOR_CREATE) || user.hasAuthority(Authority.ACTUATOR_UPDATE) || user.hasAuthority(Authority.ACTUATOR_DELETE);
%>
<input type="hidden" id="thing-id">
<table id="actuator-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
	<thead>
		<tr>
			<th scope="col">Name</th>
			<th scope="col">Abbreviation</th>
			<th scope="col">Unit</th>
			<% if(hasActuatorWriteAccess){ %>
			<th scope="col" width="100">
				<%if(user.hasAuthority(Authority.ACTUATOR_CREATE)){ %>
				<button type="button" class="btn btn-primary btn-sm pull-right" formnovalidate="formnovalidate" id="newActuatorBtn">
					<span class="fa fa-plus"></span> New Actuator
				</button>
				<%} %>
			</th>
			<%} %>
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
	       <% if(hasActuatorWriteAccess){ %>
	       ,{
	            render: function (data, type, row, meta) {
	            	var copyHtml = user.hasAuthority("ACTUATOR_CREATE") ? 
	        			'<button type="button" class="btn btn-success btn-xs" onclick="$(\'#actuator-table\').trigger(\'copy-actuator\', [this, event,'+row.id+'])">' +
	                    	'<span class="fa fa-copy"></span> Copy' +
	                    '</button>' : "";
	            	var editHtml = user.hasAuthority("ACTUATOR_UPDATE") ? 
	           			'<button type="button" class="btn btn-warning btn-xs" onclick="$(\'#actuator-table\').trigger(\'edit-actuator\', [this, event,'+row.id+'])">' +
	                       	'<span class="fa fa-edit"></span> Edit' +
	                    '</button>' : "";
	                var deleteHtml = user.hasAuthority("ACTUATOR_DELETE") ?
	                	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#actuator-table\').trigger(\'delete-actuator\', [this, event,'+row.id+'])">' +
	                    	'<span class="fa fa-trash"></span> Delete' +
	                    '</button>' : "";
	                return '<div class="btn-group pull-right" role="group">' + copyHtml + editHtml + deleteHtml + '</div>';
	            }
	        }
	       <%}%>
	    ]
	});
	
	if($("#newActuatorBtn")){
		$("#newActuatorBtn").click(function(){
			$.publish("show-actuator-edit-modal", [$("#thing-id").val(), 0, function(data){
				actuatorDataTable.row.add(data).draw();
			}]);
		});	
	}
	
	$("#actuator-table").on("view-actuator", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = actuatorDataTable.row("#" + id);
		$.publish("show-actuator-view-modal", [$("#thing-id").val(), row.data().id]);
	});
	
	$("#actuator-table").on("copy-actuator", function(){
		
	});

	$("#actuator-table").on("edit-actuator", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = actuatorDataTable.row("#" + id);
		$.publish("show-actuator-edit-modal", [$("#thing-id").val(), row.data().id, function(data){
			row.data(data).draw();
		}]);
	});

	$("#actuator-table").on("delete-actuator", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = actuatorDataTable.row("#" + id);
	    $.publish("show-actuator-delete-modal", [$("#thing-id").val(), row.data(), function(){
	    	row.remove().draw();
	    }]);
	});
	
	return function(thingId, actuators){
		$("#thing-id").val(thingId);
		actuatorDataTable.clear().rows.add(actuators).draw().columns.adjust();
	}
	
})(jQuery);
</script>