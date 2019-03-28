<style>
#thing-details-card {
	width: 60em;
}

#thing-details-table {
	width: 100%;
}
#thing-details-table > tbody > tr > td:FIRST-CHILD {
	width: 50%;
	padding: 0 1em 0 0;
}
#thing-details-table > tbody > tr > td:LAST-CHILD {
	width: 50%;
	padding: 0 0 0 1em;
}
</style>
<div class="parent-center">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<table id="thing-details-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thingName">Name</label> <input type="text" data-value="name" class="form-control" id="thingName">
							</div>
						</td>
						<td>
							<span> 
								<label>BiDirectional</label> 
								<span class="pull-right"> <input id="biDirectional" type="checkbox" data-value="biDirectional"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thingDescription">Description</label> <input type="text" data-value="description" class="form-control" id="thingDescription">
							</div>
						</td>
						<td rowspan="2">
							<div class="form-group">
								<label for="properties">Properties</label>
								<textarea data-value="properties" data-format="MapFormatter" data-format-target="value" class="form-control" id="properties" rows="5"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="tags">Tags</label> <input type="text" data-value="tags" class="form-control" id="tags">
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
		    $("#alive").prop("checked", thing.alive);
		    $("#biDirectional").prop("checked", thing.biDirectional);
	    };
	    return function(thing) {
		    toView(thing);
		    $("#page-title").text(thing && thing.id ? "Edit Thing Details" : "Create New Thing");
	    };

    })(jQuery);
</script>