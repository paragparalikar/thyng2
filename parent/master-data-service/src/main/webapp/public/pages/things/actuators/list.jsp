<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
final UserDTO user = (UserDTO)session.getAttribute("user"); 
if(user.hasAuthority(Authority.ACTUATOR_LIST)){ 
	final boolean hasActuatorWriteAccess = user.hasAuthority(Authority.ACTUATOR_CREATE) || user.hasAuthority(Authority.ACTUATOR_UPDATE) || user.hasAuthority(Authority.ACTUATOR_DELETE);
%>
<style>
	#actuator-card {
		width: 60em;
		margin-top: 2em;
		margin-left: auto;
		margin-right: auto;
	}
</style>
<div class="card" id="actuator-card">
	<div class="card-header">
		<label>Actuators</label>
	</div>
	<div class="card-body">
		<table id="actuator-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Abbreviation</th>
					<th scope="col">Unit</th>
					<th scope="col">Data Type</th>
					<th scope="col">Protocol</th>
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
	</div>
</div>
<script type="text/javascript">
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
	               return user.hasAuthority("ACTUATOR_VIEW") ? '<a href="#things/view/'+row.id+'">' + data + '</a>' : data;
	           }
	    	}, 
	       {data : "abbreviation" }, 
	       {data : "unit"}, 
	       {data : "dataType"}, 
	       {data : "protocol"}
	       <% if(hasActuatorWriteAccess){ %>
	       ,{
	            render: function (data, type, row, meta) {
	            	var copyHtml = user.hasAuthority("ACTUATOR_CREATE") ? 
	                		'<a class="btn btn-success btn-xs" href="#things/copy/'+row.id+'">' +
	                        	'<span class="fa fa-copy"></span> Copy' +
	                        '</a>' : "";
	            	var editHtml = user.hasAuthority("ACTUATOR_UPDATE") ? 
	            		'<a class="btn btn-warning btn-xs" href="#things/edit/'+row.id+'">' +
	                        '<span class="fa fa-edit"></span> Edit' +
	                    '</a>' : "";
	                var deleteHtml = user.hasAuthority("ACTUATOR_DELETE") ?
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
	var actuatorsListView = {
		data : function(actuators){
			if(actuators && actuatorDataTable){
				actuatorDataTable.clear().rows.add(actuators).draw().columns.adjust();
			}
		},
		display : function(value){
			var card = $("#actuator-card");
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