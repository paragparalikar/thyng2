<div class="modal-content" style="width: 60em; height: 30em;">
	<div class="modal-header">
		<h4 class="modal-title">Thing Details</h4>
	</div>
	<div class="modal-body">
		<ul class="nav nav-tabs" role="tablist" data-tabs="tabs">
			<li class="nav-item active">
				<a class="nav-link active" id="thing-details-tab" data-toggle="tab" href="#thing-details-tab-content" role="tab" aria-controls="thing-details" aria-selected="true">Details</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="metrics-tab" data-toggle="tab" href="#metrics-tab-content" role="tab" aria-controls="metrics" aria-selected="false">Metrics</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="location-tab" data-toggle="tab" href="#location-tab-content" role="tab" aria-controls="location" aria-selected="false">Location</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" id="activity-tab" data-toggle="tab" href="#activity-tab-content" role="tab" aria-controls="activity" aria-selected="false">Activity</a>
			</li>
		</ul>
		
		<div class="tab-content">
			<div class="tab-pane active" id="thing-details-tab-content" role="tabpanel" aria-labelledby="thing-details-tab">
				<table style="width:100%">
					<tr>
						<td width="50%" style="padding: 0 1em 0 0;">
							<div class="form-group">
								<label for="thingKey">Key</label> 
								<input type="text" readonly="readonly" disabled="disabled" data-value="key" class="form-control" id="thingKey">
							</div>
						</td>
						<td width="50%"  style="padding: 0 0 0 1em;">
							<span>
								<label>Alive</label>
								<span style="float:right;">
									<input id="alive" type="checkbox" data-value="alive" disabled="disabled" readonly="readonly">
								</span>
							</span>
						</td>
					</tr>
					<tr>
						<td width="50%" style="padding: 0 1em 0 0;">
							<div class="form-group">
								<label for="thingName">Name</label> 
								<input type="text" readonly="readonly" disabled="disabled" data-value="name" class="form-control" id="thingName">
							</div>
						</td>
						<td width="50%"  style="padding: 0 0 0 1em;">
							<span>
								<label>BiDirectional</label>
								<span style="float:right;">
									<input id="biDirectional" type="checkbox" data-value="biDirectional" disabled="disabled" readonly="readonly">
								</span>
							</span>
						</td>
					</tr>
					<tr>
						<td width="50%" style="padding: 0 1em 0 0;">
							<div class="form-group">
								<label for="thingDescription">Description</label> 
								<input type="text" readonly="readonly" disabled="disabled" data-value="description" class="form-control" id="thingDescription">
							</div>
						</td>
						<td width="50%"  style="padding: 0 0 0 1em;" rowspan="2">
							<div class="form-group">
								<label for="properties">Properties</label> 
								<textarea readonly="readonly" disabled="disabled" data-value="properties" data-format="MapFormatter" data-format-target="value" class="form-control" id="properties" rows="5"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td width="50%"  style="padding: 0 1em 0 0;">
							<div class="form-group">
								<label for="tags">Tags</label> 
								<input type="text" readonly="readonly" disabled="disabled" data-value="tags" class="form-control" id="tags">
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="tab-pane" id="metrics-tab-content" role="tabpanel" aria-labelledby="metrics-tab">
				<table id="metrics-table" class="table table-striped table-bordered table-sm" style="width:100%;">
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
			<div class="tab-pane" id="location-tab-content" role="tabpanel" aria-labelledby="location-tab">
				<div class="form-group">
					<label for="altitude">Altitude</label> 
					<input type="text" readonly="readonly" disabled="disabled" data-value="altitude" class="form-control" id="altitude">
				</div>
				<div class="form-group">
					<label for="latitude">Latitude</label> 
					<input type="text" readonly="readonly" disabled="disabled" data-value="latitude" class="form-control" id="latitude">
				</div>
				<div class="form-group">
					<label for="longitude">Longitude</label> 
					<input type="text" readonly="readonly" disabled="disabled" data-value="longitude" class="form-control" id="longitude">
				</div>
			</div>
			<div class="tab-pane" id="activity-tab-content" role="tabpanel" aria-labelledby="activity-tab">
				<div class="form-group">
					<label for="lastEvent">Last Event</label> 
					<input type="text" readonly="readonly" disabled="disabled" data-value="lastEvent" class="form-control" id="lastEvent">
				</div>
				<div class="form-group">
					<label for="lastBeat">Last Heartbeat</label> 
					<input type="text" readonly="readonly" disabled="disabled" data-value="lastBeat" class="form-control" id="lastBeat">
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	renderModal = (function($){
		var toView = function(thing){
			$("#alive").prop("checked", thing.alive);
			$("#biDirectional").prop("checked", thing.biDirectional);
			$("#metrics-table").DataTable({
				searching: false,
				ordering: false,
				paging: false,
				info: false,
				data: thing.metrics,
				columns : [
					{data: "name"},
					{data: "abbreviation"},
					{data: "unit"},
					{data: "dataType"},
					{data: "description"},
				]
			});
		};
		return function(thing){
			toView(thing);
		};
		
	})(jQuery);
</script>