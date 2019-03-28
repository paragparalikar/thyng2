<style>
#thing-details-card {
	width: 60em;
}

#thing-details-table {
	width: 100%;
}
#thing-details-table>tbody>tr>td:LAST-CHILD {
	width: 50%;
	padding: 0 0 0 1em;
}
#thing-details-table>tbody>tr>td:FIRST-CHILD {
	width: 50%;
	padding: 0 1em 0 0;
}

</style>
<div class="parent-center">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<input type="hidden" id="thing-id" name="id" data-value="id">
			<table id="thing-details-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-name">Name</label> 
								<input type="text" data-value="name" class="form-control" id="thing-name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea data-value="properties" data-format="MapFormatter" data-format-target="value" class="form-control" id="thing-properties" rows="8"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input type="text" data-value="description" class="form-control" id="thing-description">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-tags">Tags</label> 
								<input type="text" data-value="tags" class="form-control" id="thing-tags">
							</div>
						</td>
						
					</tr>
				</tbody>
			</table>
			<table id="metrics-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
				<thead>
					<tr>
						<th scope="col">Name</th>
						<th scope="col">Abbreviation</th>
						<th scope="col">Unit</th>
						<th scope="col">Data Type</th>
						<th scope="col">Description</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</div>
<script>
	render = (function($) {
	    var toView = function(thing) {
	    	if(thing){
	    		$("#thing-id").val(thing.id);
	    		$("#thing-name").val(thing.name);
	    		$("#thing-description").val(thing.description);
	    		addTags($("#thing-tags"), thing.tags);
	    		$("#thing-properties").val(formatProperties(thing.properties));
	    	}
	    };
	    return function(thing) {
		    toView(thing);
		    $("#page-title").text(thing && thing.id ? "Edit Thing Details" : "Create New Thing");
	    };

    })(jQuery);
</script>