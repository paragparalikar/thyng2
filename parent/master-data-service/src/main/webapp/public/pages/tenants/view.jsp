<style>
#tenant-details-card-wrapper {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100%;
}

#tenant-details-card {
	
}

#tenant-view-table {
	width: 100%;
}

#tenant-view-table tr td:LAST-CHILD {
	padding: 0 0 0 1em;
}

#tenant-view-table tr td:FIRST-CHILD {
	padding: 0 1em 0 0;
}

#tenant-locked-label {
	display: inline;
}
#tenant-properties-cell{
	vertical-align: top;
}
</style>

<div id="tenant-details-card-wrapper">
	<div class="card" style="width: 60em;" id="tenant-details-card">
		<div class="card-body">
			<input type="text" style="display: none;" id="tenant-id" data-value="id">
			<table id="tenant-view-table">
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-name">Name</label> <input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255" disabled="disabled"
								readonly="readonly" type="text" data-value="name" class="form-control" id="tenant-name" name="name" placeholder="Tenant Name"
							>
						</div>
					</td>
					<td>
						<div class="form-group">
							<input type="checkbox" data-value="locked" name="locked" id="tenant-locked" class="pull-right" disabled="disabled" readonly="readonly"> <label for="tenant-locked"
								id="tenant-locked-label"
							>Locked</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-description">Description</label> <input data-rule-maxlength="255" maxlength="255" type="text" data-value="description" name="description"
								disabled="disabled" readonly="readonly" class="form-control" id="tenant-description" placeholder="Description for the Tenant"
							>
						</div>
					</td>
					<td rowspan="3" id="tenant-properties-cell">
						<div class="form-group">
							<label for="tenant-properties">Properties</label>
							<textarea data-rule-maxlength="255" maxlength="255" data-value="properties" data-format="MapFormatter" data-format-target="value" name="properties" disabled="disabled"
								readonly="readonly" class="form-control" id="tenant-properties" placeholder="Tenant Properties" rows="9"
							></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-start">Start</label> <input data-rule-maxlength="255" maxlength="255" type="datetime" data-value="start" data-format="DateFormatter"
								data-format-target="value" name="start" disabled="disabled" readonly="readonly" class="form-control" id="tenant-start" placeholder="Tenant Start Date"
							>
						</div>
					</td>
					
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-expiry">Expiry</label> <input data-rule-maxlength="255" maxlength="255" type="datetime" data-value="expiry" data-format="DateFormatter"
								data-format-target="value" name="expiry" disabled="disabled" readonly="readonly" class="form-control" id="tenant-expiry" placeholder="Tenant Expiry Date"
							>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>

<script>
	render = (function($) {
	    var toView = function(tenant) {
	    	if(tenant){
	    		$("#tenant-id").val(tenant.id);
	    		$("#tenant-name").val(tenant.name);
	    		$("#tenant-tags").val(tenant.tags);
	    		$("#tenant-description").val(tenant.description);
			    $("#tenant-locked").prop("checked", tenant.locked);
			    
			    $("#tenant-properties").val(formatProperties(tenant.properties));
			    $("#tenant-start").val(tenant.start ? new Date(tenant.start).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-expiry").val(tenant.expiry ? new Date(tenant.expiry).toLocaleDateString() : new Date().toLocaleDateString());
	    	}
	    };
	    return function(tenant) {
		    $("#page-title").text("Tenant Details");
		    toView(tenant);
	    };
    })(jQuery);
</script>