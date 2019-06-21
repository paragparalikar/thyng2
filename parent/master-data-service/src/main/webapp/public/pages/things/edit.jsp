<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
	final UserDTO user = (UserDTO)session.getAttribute("user");
	final boolean hasSensorWriteAccess = user.hasAuthority(Authority.SENSOR_CREATE) || user.hasAuthority(Authority.SENSOR_UPDATE) || user.hasAuthority(Authority.SENSOR_DELETE);
	final boolean hasActuatorWriteAccess = user.hasAuthority(Authority.ACTUATOR_CREATE) || user.hasAuthority(Authority.ACTUATOR_UPDATE) || user.hasAuthority(Authority.ACTUATOR_DELETE);
%>
<style>
#thing-details-card, 
#sensor-card,
#actuator-card {
	width: 60em;
	margin-left: auto;
	margin-right: auto;
}
#sensor-card,
#actuator-card {
	margin-top: 2em;
}

#thing-details-table {
	width: 100%;
}

#thing-details-table>tbody>tr>td:LAST-CHILD {
	width: 50%;
	padding: 0 0 0 1em;
}

#thing-details-table>tbody>tr>td:FIRST-CHILD {
	width: 50%;
	padding: 0 1em 0 0;
}
</style>
<form id="thing-form">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<input type="hidden" id="thing-id" name="id" data-value="id">
			<table id="thing-details-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-name">Name</label> 
								<input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255" type="text" name="name" class="form-control" id="thing-name" placeholder="Thing name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea data-rule-maxlength="255" maxlength="255" name="properties"class="form-control" id="thing-properties" rows="8" placeholder="Thing properties"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input data-rule-maxlength="255" maxlength="255" type="text" name="description" class="form-control" id="thing-description" placeholder="Thing description">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-gateway">Gateway</label>
								<select id="thing-gateway" name="gatewayId" class="form-control"></select> 
							</div>
						</td>
	
					</tr>
				</tbody>
			</table>
			
		</div>
		<div class="card-footer">
			<a href="#" id="cancelButton" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button type="submit" href="#" id="saveButton" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
	</div>
</form>
<%if(user.hasAuthority(Authority.SENSOR_LIST)){ %>
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
<%} %>
<%if(user.hasAuthority(Authority.ACTUATOR_LIST)){ %>
<div class="card" id="actuator-card">
	<div class="card-header">
		<h5>Actuators</h5>
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
<%} %>
<script>
	render = (function($) {
		var thing = null;
		var toModel = function(thing){
			thing.name = $("#thing-name").val();
			thing.description = $("#thing-description").val();
			thing.gatewayId = $("#thing-gateway").val();
			thing.properties = parseProperties($("#thing-properties").val());
		};
		var toView = function(thing){
			if(thing){
				$("#thing-id").val(thing.id);
				$("#thing-name").val(thing.name);
				$("#thing-description").val(thing.description);
				$("#thing-properties").val(formatProperties(thing.properties));
				if(thing.gatewayId){
					var selectableOption = $("#thing-gateway option[value='"+thing.gatewayId+"']");
					if(selectableOption){
						selectableOption.prop('selected', true).change();
					}
				}
				<%if(user.hasAuthority(Authority.SENSOR_LIST)){ %>
				$("#sensor-table").DataTable({
			        searching : false,
			        ordering : false,
			        paging : false,
			        info : false,
			        data : thing.sensors,
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
				<%}%>
				<%if(user.hasAuthority(Authority.ACTUATOR_LIST)){ %>
			    $("#actuator-table").DataTable({
			        searching : false,
			        ordering : false,
			        paging : false,
			        info : false,
			        data : thing.actuators,
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
			    <%}%>
			}
		};
		var bindHandlers = function(){
			$("#cancelButton").click(function(){
				window.history.back();
			});
		};
		$("#thing-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			rules:{
				name: {
					remote : {
		                url : "/api/v1/things",
		                data : {
		                    id : function() {
			                    return $("#thing-id").val();
		                    },
		                    name : function() {
			                    return $("#thing-name").val();
		                    }
		                }
		            }
				},
		        properties: {
		        	propertiesMap : true
		        }
			},			
			submitHandler: function(){
				toModel(thing);
				thingService.save(thing, function(data){
					thing = data;
					toView(thing);
					toast('Thing has been saved successfully');
				});
			}	
		});
		var hideElement = function(element, value){
			if(element && value){
				element.hide();
			}else{
				element.show();
			}
		}
	    return function(id) {
	    	hideElement($("#sensor-card"), !id || 0 >= id);
	    	hideElement($("#actuator-card"), !id || 0 >= id);
	    	bindHandlers();
		    gatewayService.findAllThin(function(gateways){
		    	$.each(gateways, function(index, gateway) {   
		    	     $('#thing-gateway').append("<option value='"+gateway.id+"'>"+gateway.name+"</option>"); 
		    	});
		    	if(id && 0 < id){
		    		$("#page-title").text("Edit Thing Details");
		    		thingService.findOne(id, function(data){
		    			thing = data;
		    			toView(thing);
		    		});
		    	}else{
		    		$("#page-title").text("Create New Thing");
		    		thing = {properties:{}};
		    		toView(thing);
		    	}
		    });
	    };
    })(jQuery);
</script>