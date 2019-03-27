<style>
#tenant-view-table{
	width: 100%;
}
#tenant-view-table tr td:LAST-CHILD {
	padding: 0 0 0 1em;
}
#tenant-view-table tr td:FIRST-CHILD {
	padding: 0 1em 0 0;
}
#tenant-locked-label{
	display: inline;
}
</style>

<div class="modal-content" style="width: 60em;">
	<div class="modal-header">
		<h4 class="modal-title">Tenant</h4>
	</div>
	<div class="modal-body">
		<input type="text" style="display: none;" id="tenant-id" data-value="id">
		<table id="tenant-view-table">
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-name">Name</label> 
						<input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255" disabled="disabled" readonly="readonly"
						type="text" data-value="name" class="form-control" id="tenant-name" name="name" placeholder="Tenant Name">
					</div>
				</td>
				<td>
					<div class="form-group">
						<input type="checkbox" data-value="locked" name="locked" id="tenant-locked" class="pull-right" disabled="disabled" readonly="readonly">
						<label for="tenant-locked" id="tenant-locked-label">Locked</label>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-description">Description</label> 
						<input data-rule-maxlength="255" maxlength="255" type="text" data-value="description" name="description" disabled="disabled" readonly="readonly" 
						class="form-control" id="tenant-description" placeholder="Description for the Tenant">
					</div>
				</td>
				<td>
					<div class="form-group">
						<label for="tenant-tags">Tags</label> 
						<input data-rule-maxlength="255" maxlength="255" type="text" data-value="tags" name="tags"  disabled="disabled" readonly="readonly"
						class="form-control" id="tenant-tags" placeholder="Tenant Tags">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-start">Start</label> 
						<input data-rule-maxlength="255" maxlength="255" type="datetime" data-value="start" name="start"  disabled="disabled" readonly="readonly"
						class="form-control" id="tenant-start" placeholder="Tenant Start Date">
					</div>
				</td>
				<td rowspan="2">
					<div class="form-group">
						<label for="tenant-properties">Properties</label> 
						<textarea data-rule-maxlength="255" maxlength="255" data-value="properties" data-format="MapFormatter" data-format-target="value" name="properties"  disabled="disabled" readonly="readonly"
						class="form-control" id="tenant-properties" placeholder="Tenant Properties" rows="5"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-expiry">Expiry</label> 
						<input data-rule-maxlength="255" maxlength="255" type="datetime" data-value="expiry" name="expiry"  disabled="disabled" readonly="readonly"
						class="form-control" id="tenant-expiry" placeholder="Tenant Expiry Date">
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
<script>
	renderModal = (function($){
		var toView = function(tenant){
			$("#tenant-start").val(new Date(tenant.start).toLocaleDateString());
			$("#tenant-expiry").val(new Date(tenant.expiry).toLocaleDateString());
			$("#tenant-locked").prop("checked", tenant.locked);
		};
		return function(tenant){
			toView(tenant);
		};		
	})(jQuery);
</script>