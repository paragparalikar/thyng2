<div class="modal" style="display: block;">
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">Thing Details</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" id="thing-id">
			<table id="thing-details-form-table" class="form-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-name">Name</label> 
								<input disabled="disabled" type="text" class="form-control" id="thing-name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea disabled="disabled" class="form-control" id="thing-properties" rows="8"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input disabled="disabled" type="text" class="form-control" id="thing-description">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-gateway">Gateway</label>
								<select disabled="disabled" id="thing-gateway" class="form-control"></select> 
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="sensonr-table-container"></div>
			<div id="actuator-table-container"></div>
		</div>
	</div>
</div>
<script>
	renderModal = (function($){
		
		return function(thing){
			if(thing){
				$("#thing-id").val(thing.id);
				$("#thing-name").val(thing.name);
				$("#thing-description").val(thing.description);
				$("#thing-properties").val(formatProperties(thing.properties));
				$('#thing-gateway').empty().append("<option selected value='"+thing.gatewayId+"'>"+thing.gatewayName+"</option>");
			}
		};
	})(jQuery);
</script>