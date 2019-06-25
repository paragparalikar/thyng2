<div class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title">Sensor Details</h4>
		</div>
		<div class="modal-body">
			<table class="form-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-name">Name</label> 
								<input 	type="text" 
										disabled="disabled"
										class="form-control" 
										id="sensor-name">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="sensor-inactivity-period">Inactivity Period</label> 
								<input 	type="text" 
										disabled="disabled"
										class="form-control" 
										id="sensor-inactivity-period">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-abbreviation">Abbreviation</label> 
								<input 	type="text" 
										disabled="disabled"
										class="form-control" 
										id="sensor-abbreviation">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="sensor-batch-size">Batch Size</label> 
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="sensor-batch-size">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-description">Description</label> 
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="sensor-description">
							</div>
						</td>
						<td rowspan="3">
							<label for="sensor-normalizer">Normalizer</label> 
							<div class="form-group">
								<textarea 	rows="8"
											disabled="disabled" 
											class="form-control" 
											id="sensor-normalizer"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-unit">Unit</label> 
								<input  type="text"
										disabled="disabled" 
										class="form-control" 
										id="sensor-unit">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="sensor-data-type">Data Type</label> 
								<select id="sensor-data-type" disabled="disabled" class="form-control"></select>
							</div>
						</td>
						<td>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
	renderViewSensorModal = (function($) {
		
		return function(sensor){
			if(sensor){
				$("#sensor-name").val(sensor.name);
				$("#sensor-description").val(sensor.description);
				$("#sensor-abbreviation").val(sensor.abbreviation);
				$("#sensor-unit").val(sensor.unit);
				$("#sensor-inactivity-period").val(sensor.inactivityPeriod);
				$("#sensor-batch-size").val(sensor.batchSize);
				$("#sensor-normalizer").val(sensor.normalizer);	
				$('#sensor-data-type').empty().append("<option selected value='"+sensor.dataType+"'>"+sensor.dataType+"</option>");
			}else{
				console.log("Received null sensor, can not view");
			}
		};
	})(jQuery);
</script>