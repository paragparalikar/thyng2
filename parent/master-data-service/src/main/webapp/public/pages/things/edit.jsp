<div class="modal" style="display: block;">
	<div class="modal-content" style="width: 60em;">
		<form id="thing-form">
			<div class="modal-header">
				<h4 class="modal-title" id="thing-edit-page-title">Thing Details</h4>
			</div>
			<div class="modal-body">
				
				<ul class="nav nav-tabs" id="thing-edit-tab-pane">
					<li class="active" target="#thing-details-form-container">
						<a>Details</a>
					</li>
					<li id="sensor-tab" target="#sensor-data-table-container">
						<a>Sensors</a>
					</li>
					<li id="actuator-tab" target="#actuator-data-table-container">
						<a>Actuators</a>
					</li>
				</ul>
			
				<div class="tab-content">
					<div id="thing-details-form-container" class="tab-pane active">
						<table class="form-table">
							<tbody>
								<tr>
									<td>
										<div class="form-group">
											<label for="thing-name">Name</label> 
											<input type="hidden" id="thing-id">
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
						<div class="text-right">
							<a id="thing-edit-modal-cancel-button" class="btn btn-secondary"> 
								<i class="fa fa-trash"></i> Cancel
							</a>
							<button type="submit" href="#" id="saveButton" class="btn btn-primary pull-right">
								<i class="fa fa-save"></i> Save
							</button>
						</div>
					</div>
					<div id="sensor-data-table-container" class="tab-pane"></div>
					<div id="actuator-data-table-container" class="tab-pane"></div>
				</div>
			</div>
		</form>
	</div>
</div>
<script>
	renderModal = (function($) {
		
		tabify($("#thing-edit-tab-pane"));
		
		$("#thing-edit-modal-cancel-button").click(function(){
			$("#thing-form").trigger("thing-edit-cancel");
		});
		
		var toModel = function(thing){
			return {
				id : $("#thing-id").val(),
				name : $("#thing-name").val(),
				description : $("#thing-description").val(),
				gatewayId : $("#thing-gateway").val(),
				properties : parseProperties($("#thing-properties").val())
			};
		};
		
		var toView = function(thing){
			$("#thing-form").trigger("load-sensor-data-table", thing);
			$("#thing-form").trigger("load-actuator-data-table", thing);
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
			}
		};
		
		var save = function(){
			$("#thing-form").trigger("save-thing", [toModel()]);
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
			submitHandler: save
		});
		
	    return function(thing, gateways) {
	    	$("#thing-edit-page-title").text(thing && thing.id && 0 < thing.id ? "Edit Thing Details" : "Create New Thing");
	    	$('#thing-gateway').empty();
	    	$.each(gateways, function(index, gateway) {   
	    	     $('#thing-gateway').append("<option value='"+gateway.id+"'>"+gateway.name+"</option>"); 
	    	});
	    	toView(thing);
	    	$("#thing-name").focus();
	    };
    })(jQuery);
</script>