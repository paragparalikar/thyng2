<div id="sensor-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="sensor-form">
		<div class="modal-header">
			<h4 class="modal-title" id="sensor-edit-title" data-content="title">Edit Sensor</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" data-value="thingId" id="thing-id">
			<input type="hidden" data-value="id" id="sensor-id">
			<table class="form-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group required">
								<label for="sensor-name">Name</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="3" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="name" 
										data-value="name"
										class="form-control" 
										id="sensor-name" 
										placeholder="Name">
							</div>
						</td>
						<td>
							<div class="form-group required">
								<label for="sensor-inactivity-period">Inactivity Period</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255" 
										data-rule-min="10"
										data-rule-digits="true"
										maxlength="255" 
										type="text" 
										value="60"
										data-value="inactivityPeriod"
										name="inactivity-period" 
										class="form-control" 
										id="sensor-inactivity-period" 
										placeholder="Inactivity Period(Seconds)">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group required">
								<label for="sensor-abbreviation">Abbreviation</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="abbreviation" 
										data-value="abbreviation"
										class="form-control" 
										id="sensor-abbreviation" 
										placeholder="Abbreviation">
							</div>
						</td>
						<td>
							<div class="form-group required">
								<label for="sensor-batch-size">Batch Size</label> 
								<input 	data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255"
										data-rule-min="1" 
										data-rule-digits="true"
										maxlength="255" 
										type="text" 
										name="batch-size" 
										data-value="batchSize"
										class="form-control" 
										id="sensor-batch-size" 
										placeholder="Batch Size" 
										value="10">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-description">Description</label> 
								<input 	data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="description" 
										data-value="description"
										class="form-control" 
										id="sensor-description" 
										placeholder="Description">
							</div>
						</td>
						<td rowspan="2">
							<label for="sensor-normalizer">Normalizer</label> 
							<div class="form-group">
								<textarea 	rows="5" 
											data-rule-maxlength="255" 
											maxlength="255" 
											class="form-control" 
											name="normalizer"
											data-value="normalizer" 
											id="sensor-normalizer"
											placeholder="Normalizer">
								</textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group required">
								<label for="sensor-unit">Unit</label> 
								<input  data-rule-required="true" 
										data-rule-minlength="1" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										name="unit" 
										data-value="unit"
										class="form-control" 
										id="sensor-unit" 
										placeholder="Unit">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							
						</td>
						<td>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<a id="sensor-edit-modal-cancel-button" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button id="sensor-edit-modal-action-button" type="submit" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
		</form>
	</div>
</div>
<script>
	renderEditSensorView = (function($) {
		
		$("#sensor-edit-modal-cancel-button").click(function(){
			$("#sensor-form").trigger("sensor-edit-cancel");
		});
		
		var toModel = function(){
			return {
				id : $("#sensor-id").val(),
				name : $("#sensor-name").val(),
				description : $("#sensor-description").val(),
				abbreviation : $("#sensor-abbreviation").val(),
				unit : $("#sensor-unit").val(),
				inactivityPeriod : $("#sensor-inactivity-period").val(),
				batchSize : $("#sensor-batch-size").val(),
				normalizer : $("#sensor-normalizer").val()
			};
		};
		var toView = function(thingId, sensor){
			//window.alert(JSON.stringify(sensor));
			$("#thing-id").val(thingId);
			if(sensor){
				$("#sensor-id").val(sensor.id);
				$("#sensor-name").val(sensor.name);
				$("#sensor-description").val(sensor.description);
				$("#sensor-abbreviation").val(sensor.abbreviation);
				$("#sensor-unit").val(sensor.unit);
				$("#sensor-inactivity-period").val(sensor.inactivityPeriod);
				$("#sensor-batch-size").val(sensor.batchSize);
				$("#sensor-normalizer").val(sensor.normalizer);	
			}
		};
		var save = function(){
			$("#sensor-form").trigger("save-sensor",[$("#thing-id").val(), toModel()]);
		};
		
		$("#sensor-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			rules:{
				name: {
					remote : {
		                url : "/api/v1/sensors",
		                data : {
		                    id : function() {
			                    return $("#sensor-id").val();
		                    },
		                    name : function() {
			                    return $("#sensor-name").val();
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
		return function(thingId, sensor){
			$("#sensor-edit-title").text(sensor && sensor.id && 0 < sensor.id ? "Edit Sensor Details" : "Create New Sensor");
			toView(thingId, sensor);
		}
	})(jQuery);
</script>