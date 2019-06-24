<div id="gateway-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="gateway-form">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title" id="edit-gateway-title">Gateway Details</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" id="gateway-id" data-value="id">
			<table id="gateway-view-table" class="form-table">
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-name">Name</label> 
							<input 	data-rule-required="true" 
									data-rule-minlength="3" 
									data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="name" 
									class="form-control" 
									id="gateway-name" 
									name="name" 
									disabled="disabled"
									placeholder="Gateway Name">
						</div>
					</td>
					<td rowspan="3" id="gateway-properties-cell">
						<div class="form-group">
							<label for="gateway-properties">Properties</label> 
							<textarea 	data-rule-maxlength="255" 
										maxlength="255" 
										data-value="properties" 
										data-format="MapFormatter" 
										data-format-target="value" 
										name="properties"
										class="form-control" 
										id="gateway-properties" 
										placeholder="Gateway Properties" 
										disabled="disabled"
										rows="9"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-description">Description</label> 
							<input 	data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="description" 
									class="form-control" 
									id="gateway-description" 
									name="description" 
									disabled="disabled"
									placeholder="Gateway Description">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-inactivityPeriod">Inactivity Period</label> 
							<input 	data-rule-required="true" 
									data-rule-digits="true" 
									data-rule-min="60"
									type="text" 
									data-value="inactivityPeriod" 
									class="form-control" 
									id="gateway-inactivityPeriod" 
									name="inactivityPeriod" 
									disabled="disabled"
									placeholder="Inactivity Period">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	</div>
</div>
<script>
renderModal = (function($){
	
	return function(gateway){
		if(gateway){
			$("#gateway-id").val(gateway.id);
			$("#gateway-name").val(gateway.name);
			$("#gateway-description").val(gateway.description);
			$("#gateway-inactivityPeriod").val(gateway.inactivityPeriod);
			$("#gateway-properties").val(formatProperties(gateway.properties));
		}
	};
	
})(jQuery);
</script>