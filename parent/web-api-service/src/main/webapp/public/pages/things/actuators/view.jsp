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
							<div id="topic-form-group" class="form-group">
								<label for="actuator-topic">Topic</label> 
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-topic">
							</div>
							<div id="url-form-group" class="form-group">
								<label for="actuator-url">Url</label>
								<input 	type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-url">
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
								<label for="actuator-unit">Unit</label> 
								<input  type="text"
										disabled="disabled" 
										class="form-control" 
										id="actuator-unit">
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
				$("#actuator-topic").val(actuator.topic);
				$('#actuator-protocol').empty().append("<option selected value='"+actuator.protocol+"'>"+actuator.protocol+"</option>");
				$("#actuator-url").val(actuator.url);
				
				if('MQTT' == actuator.protocol){
					$("#topic-form-group").show();
					$("#url-form-group").hide();
				}else{
					$("#topic-form-group").hide();
					$("#url-form-group").show();
				}
			}else{
				console.log("Received null actuator, can not view");
			}
		};
	})(jQuery);
</script>