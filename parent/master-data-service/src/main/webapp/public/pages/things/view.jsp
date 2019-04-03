<style>
#tenant-details-card{
	margin: 0 1em 1em 0; 
	width: 32em;
}
#thing-details-table{
	width: 100%;
}
#thing-details-table tbody tr td:FIRST-CHILD label{
	float: right;
	padding-right: 1em;
}
#thing-details-table tbody tr td:LAST-CHILD label{
	float: left;
}
</style>

<div class="card" style="" id="tenant-details-card">
	<div class="card-body">
		<table id="thing-details-table">
			<tbody>
				<tr>
					<td><label>Key : </label></td>
					<td id="thing-key"></td>
				</tr>
				<tr>
					<td><label>Name : </label></td>
					<td id="thing-name"></td>
				</tr>
				<tr>
					<td><label>Description : </label></td>
					<td id="thing-description"></td>
				</tr>
				<tr>
					<td><label>Tags : </label></td>
					<td id="thing-tags"></td>
				</tr>
				<tr>
					<td><label>Properties : </label></td>
					<td id="thing-properties"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

<div class="card" style="margin: 1em 0 0 0;">
	<div class="card-body">
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

<script>
	render = (function($) {
	    var toView = function(thing) {
		    $("#thing-key").text(thing.key);
		    $("#thing-name").text(thing.name);
		    $("#thing-description").text(thing.description);
		    $("#thing-tags").text(thing.tags);
		    $("#thing-properties").html($.map(thing.properties, function(val, key) {
				return key + "=" + val;
			}).join("<br>"));
		    $("#metrics-table").DataTable({
		        searching : false,
		        ordering : false,
		        paging : false,
		        info : false,
		        data : thing.metrics,
		        columns : [ {
			        data : "name"
		        }, {
			        data : "abbreviation"
		        }, {
			        data : "unit"
		        }, {
			        data : "dataType"
		        }, {
			        data : "description"
		        }, ]
		    });
	    };
	    return function(thing) {
		    $("#page-title").html("Thing Details");
		    toView(thing);
	    };
    })(jQuery);
</script>