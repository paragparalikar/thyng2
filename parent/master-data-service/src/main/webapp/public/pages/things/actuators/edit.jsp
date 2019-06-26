<%@ page import="com.thyng.model.enumeration.DataType" %>
<%@ page import="com.thyng.model.enumeration.Protocol" %>

<div id="actuator-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="actuator-form">
		<div class="modal-header">
			<h4 class="modal-title" id="actuator-edit-title" data-content="title">Edit Actuator</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" data-value="thingId" id="thing-id">
			<input type="hidden" data-value="id" id="actuator-id">
			<table class="form-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group required">
								<label for="actuator-name">Name</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="3" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="name" 
										data-value="name"
										class="form-control" 
										id="actuator-name" 
										placeholder="Name">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-protocol">Protocol</label>
								<select id="actuator-protocol" class="form-control" name="protocol" data-value="protocol" >
									<% for(Protocol protocol : Protocol.values()) out.print("<option value=\""+protocol.name()+"\">"+protocol.toString()+"</option>"); %>
								</select> 
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group required">
								<label for="actuator-abbreviation">Abbreviation</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="abbreviation" 
										data-value="abbreviation"
										class="form-control" 
										id="actuator-abbreviation" 
										placeholder="Abbreviation">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-topic">Topic</label> 
								<input 	data-rule-maxlength="255"
										maxlength="255" 
										type="text" 
										name="topic" 
										data-value="topic"
										class="form-control" 
										id="actuator-topic" 
										placeholder="Topic">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-description">Description</label> 
								<input 	data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="description" 
										data-value="description"
										class="form-control" 
										id="actuator-description" 
										placeholder="Description">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-host">Host</label> 
								<input 	data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="host" 
										data-value="host"
										class="form-control" 
										id="actuator-host" 
										placeholder="Host">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group required">
								<label for="actuator-unit">Unit</label> 
								<input  data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="unit" 
										data-value="unit"
										class="form-control" 
										id="actuator-unit" 
										placeholder="Unit">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-port">Port</label> 
								<input 	data-rule-maxlength="255" 
										data-rule-digits="true"
										maxlength="255" 
										type="text" 
										data-value="port"
										name="port" 
										class="form-control" 
										id="actuator-port" 
										placeholder="Port">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-data-type">Data Type</label> 
								<select id="actuator-data-type" name="data-type" data-value="dataType"  class="form-control">
								<% for(DataType dataType : DataType.values()) out.print("<option value=\""+dataType.name()+"\">"+dataType.toString()+"</option>"); %>
								</select>
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-path">Path</label> 
								<input 	data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="path" 
										data-value="path"
										class="form-control" 
										id="actuator-path" 
										placeholder="Path">
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<a id="actuator-edit-modal-cancel-button" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button id="actuator-edit-modal-action-button" type="submit" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
		</form>
	</div>
</div>
<script>
	renderEditActuatorView = (function($) {
		
		$("#actuator-edit-modal-cancel-button").click(function(){
			$("#actuator-form").trigger("actuator-edit-cancel");
		});
		
		var toModel = function(){
			return {
				id : $("#actuator-id").val(),
				name : $("#actuator-name").val(),
				description : $("#actuator-description").val(),
				abbreviation : $("#actuator-abbreviation").val(),
				unit : $("#actuator-unit").val(),
				dataType : $("#actuator-data-type").val(),
				protocol : $("#actuator-protocol").val(),
				topic : $("#actuator-topic").val(),
				host : $("#actuator-host").val(),
				port : $("#actuator-port").val(),
				path : $("#actuator-path").val(),
			};
		};
		var toView = function(thingId, actuator){
			$("#thing-id").val(thingId);
			if(actuator){
				$("#actuator-id").val(actuator.id);
				$("#actuator-name").val(actuator.name);
				$("#actuator-description").val(actuator.description);
				$("#actuator-abbreviation").val(actuator.abbreviation);
				$("#actuator-unit").val(actuator.unit);
				$("#actuator-data-type").val(actuator.dataType);
				$("#actuator-protocol").val(actuator.protocol);
				$("#actuator-topic").val(actuator.topic);
				$("#actuator-host").val(actuator.host);
				$("#actuator-port").val(actuator.port);
				$("#actuator-path").val(actuator.path);
			}
		};
		var save = function(){
			$("#actuator-form").trigger("save-actuator",[$("#thing-id").val(), toModel()]);
		};
		
		$("#actuator-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			rules:{
				name: {
					remote : {
		                url : "/api/v1/actuators",
		                data : {
		                    id : function() {
			                    return $("#actuator-id").val();
		                    },
		                    name : function() {
			                    return $("#actuator-name").val();
		                    },
		                    thingId : function(){
		                    	return $("#thing-id").val();
		                    }
		                }
		            }
				}
			},			
			submitHandler: save
		});
		return function(thingId, actuator){
			$("#actuator-edit-title").text(actuator && actuator.id && 0 < actuator.id ? "Edit Actuator Details" : "Create New Actuator");
			toView(thingId, actuator);
		}
	})(jQuery);
</script>