<div class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title">Actuator Details</h4>
		</div>
		<div class="modal-body">
			<table class="form-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-name">Name</label> 
								<input 	type="text" 
										disabled="disabled"
										class="form-control" 
										id="actuator-name">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-protocol">Protocol</label>
								<select id="actuator-protocol"
										disabled="disabled"
										class="form-control" >
								</select> 
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-abbreviation">Abbreviation</label> 
								<input 	type="text" 
										disabled="disabled"
										class="form-control" 
										id="actuator-abbreviation">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-topic">Topic</label> 
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-topic">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-description">Description</label> 
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-description">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-host">Host</label>
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-host">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-unit">Unit</label> 
								<input  type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-unit">
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-port">Port</label> 
								<input  type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-port">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="actuator-data-type">Data Type</label> 
								<select id="actuator-data-type" disabled="disabled" class="form-control"></select>
							</div>
						</td>
						<td>
							<div class="form-group">
								<label for="actuator-path">Path</label> 
								<input  type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-path">
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script>
	renderViewActuatorModal = (function($) {
		
		return function(actuator){
			if(actuator){
				$("#actuator-name").val(actuator.name);
				$("#actuator-description").val(actuator.description);
				$("#actuator-abbreviation").val(actuator.abbreviation);
				$("#actuator-unit").val(actuator.unit);
				$('#actuator-data-type').empty().append("<option selected value='"+actuator.dataType+"'>"+actuator.dataType+"</option>");
				$("#actuator-topic").val(actuator.topic);
				$('#actuator-protocol').empty().append("<option selected value='"+actuator.protocol+"'>"+actuator.protocol+"</option>");
				$("#actuator-host").val(actuator.host);
				$("#actuator-port").val(actuator.port);
				$("#actuator-path").val(actuator.path);
			}else{
				console.log("Received null actuator, can not view");
			}
		};
	})(jQuery);
</script>