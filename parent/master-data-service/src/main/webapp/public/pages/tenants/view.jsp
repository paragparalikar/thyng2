<div id="tenant-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="tenant-form">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title" id="edit-tenant-title">Tenant Details</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" id="tenant-id" data-value="id">
			<table id="tenant-edit-table" class="form-table">
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-name">Name</label> 
							<input 	data-rule-required="true" 
									data-rule-minlength="3" 
									data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="name" 
									class="form-control" 
									id="tenant-name" 
									name="name" 
									disabled="disabled"
									placeholder="Tenant Name">
						</div>
					</td>
					<td>
						<div class="form-group">
							<input 	type="checkbox" 
									disabled="disabled"
									data-value="locked" 
									name="locked" 
									id="tenant-locked" 
									class="pull-right">
							<label for="tenant-locked" id="tenant-locked-label">Locked</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-description">Description</label> 
							<input 	data-rule-maxlength="255" 
									maxlength="255" 
									type="text" 
									disabled="disabled"
									data-value="description" 
									name="description"
									class="form-control" 
									id="tenant-description" 
									placeholder="Description for the Tenant">
						</div>
					</td>
					<td rowspan="3" id="tenant-properties-cell">
						<div class="form-group">
							<label for="tenant-properties">Properties</label> 
							<textarea 	data-rule-maxlength="255" 
										maxlength="255" 
										disabled="disabled"
										data-value="properties" 
										data-format="MapFormatter" 
										data-format-target="value" 
										name="properties"
										class="form-control" 
										id="tenant-properties" 
										placeholder="Tenant Properties" 
										rows="9">
							</textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-start">Start</label> 
							<div class="input-group date" id="tenant-start-datepicker">
								<input 	data-rule-required="true" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										disabled="disabled"
										data-value="start" 
										name="start" 
										class="form-control" 
										id="tenant-start" 
										placeholder="Tenant Start Date">
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-expiry">Expiry</label> 
							<div class="input-group date" id="tenant-expiry-datepicker">
								<input 	data-rule-required="true" 
										data-rule-maxlength="255" 
										maxlength="255" 
										disabled="disabled"
										type="text" 
										data-value="expiry" 
										name="expiry" 
										class="form-control" 
										id="tenant-expiry" 
										placeholder="Tenant Expiry Date">
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
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
		
		return function(tenant){
			if(tenant){
				$("#tenant-id").val(tenant.id);
				$("#tenant-name").val(tenant.name);
				$("#tenant-description").val(tenant.description);
				$("#tenant-start").val(tenant.start ? new Date(tenant.start).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-expiry").val(tenant.expiry ? new Date(tenant.expiry).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-locked").prop("checked", tenant.locked);
				$("#tenant-properties").val(formatProperties(tenant.properties));
			}
		};
		
	})(jQuery);
</script>