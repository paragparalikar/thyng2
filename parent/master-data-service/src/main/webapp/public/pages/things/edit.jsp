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
<form class="parent-center" id="thing-form">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<input type="hidden" id="thing-id" name="id" data-value="id">
			<table id="thing-details-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-name">Name</label> <input type="text" data-value="name" name="name" class="form-control" id="thing-name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea data-value="properties" name="properties" data-format="MapFormatter" data-format-target="value" class="form-control" id="thing-properties" rows="8"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input type="text" data-value="description" name="description" class="form-control" id="thing-description">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-tags">Tags</label> 
								<input type="text" data-value="tags" name="tags" class="form-control" id="thing-tags">
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
						<th scope="col">
							<button type="button" class="btn btn-primary btn-sm" formnovalidate="formnovalidate" id="newMetricBtn">
								<span class="fa fa-plus"></span> New Metric
							</button>
						</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="card-footer">
			<a href="#" id="cancelButton" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button type="submit" href="#" id="saveButton" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
	</div>
</form>
<script>
	render = (function($) {
		var thing = null;
		var metricsDataTable = null;
		 var showMatricEditorModal = function(event, source, originalEvent, id) {
		    source.blur();
		    originalEvent.preventDefault();
		    metric = metricsDataTable.row("#"+id).data();
		    showMetricEditView(metric);
	    };
	    var showMetricEditView = function(metric) {
	    	metric = metric ? metric : {dataType: "NUMBER"};
		    $("#modal-container").loadTemplate("public/pages/metrics/edit.html", metric, {
			    success : function() {
			    	$("#modal-container").modal();
			    	renderModal(metric, thing.metrics, function(){
				    	if(!metric.id){
				    		thing.metrics.push(metric);
				    	}
				    	metricsDataTable.clear().rows.add(thing.metrics).draw();
				    });
			    },
		    	error: errorCallback
		    });
	    };
	    var showMetricDeleteConfirmation = function(event, source, originalEvent, id) {
		    source.blur();
		    originalEvent.preventDefault();
		    var row = metricsDataTable.row("#"+id);
		    var metric = row.data();
		    $.publish("show-confirmation-modal", [ {
			    message : "Are you sure you want to delete metric " + metric.name + " ?"
		    }, function() {
		    	thing.metrics.splice(thing.metrics.indexOf(metric), 1);		    	
			    row.remove().draw();
			    $.modal.close();
		    } ]);
	    };
	    $("#thing-form").validate({
	    	errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" )+"']").append(error);
			},
			errorElement: "span",
	        rules : {
		        name : {
		            remote : {
		                url : "/api/v1/things",
		                data : {
		                    id : function() {
			                    return $("#thing-id").val();
		                    },
		                    name : function() {
			                    return $("#thing-name").val();
		                    }
		                }
		            }
		        },
		        properties: {
		        	propertiesMap : true
		        }
	        },
	        submitHandler : function() {
		        thingService.save(toModel(thing), function(data){
		        	toast('Thing has been saved successfully');
		        	thing = data;
		        	toView(thing);
		        });
	        }
	    });
	    var toModel = function(thing){
	    	thing.id = $("#thing-id").val();
	    	thing.name = $("#thing-name").val();
	    	thing.description = $("#thing-description").val();
	    	thing.tags = $("#thing-tags").val().split(",");
		    //template.inactivityPeriod = $("#thing-inactivity-period").val();
		    thing.properties = parseProperties($("#thing-properties").val());
	    	return thing;
	    };
		var bindHandlers = function(){
			$("#cancelButton").click(function(){
				window.history.back();
			});
			 $("#newMetricBtn").click(function() {
	 		    showMetricEditView(null);
	 	    });
			$("#metrics-table").on("edit-metric", showMatricEditorModal);
	    	$("#metrics-table").on("delete-metric", showMetricDeleteConfirmation);
		};
	    var toView = function(thing) {
		    if (thing) {
			    $("#thing-id").val(thing.id);
			    $("#thing-name").val(thing.name);
			    $("#thing-description").val(thing.description);
			    addTags($("#thing-tags"), thing.tags);
			    $("#thing-properties").val(formatProperties(thing.properties));
			    showMetricsDataTable(thing.metrics);
			    $("#thing-name").focus();
		    }
	    };
	    var showMetricsDataTable = function(metrics) {
	    	if(metricsDataTable){
	    		metricsDataTable.clear().rows.add(metrics).draw();
	    	}else{
	    		metricsDataTable = $("#metrics-table").DataTable({
	                rowId : "id",
	                searching : false,
	                ordering : false,
	                paging : false,
	                info : false,
	                data : metrics,
	                columnDefs : [ {
	                    targets : -1,
	                    className : "text-center"
	                } ],
	                columns : [
	                   {data : "name"},
	                   {data : "abbreviation"},
	                   {data : "unit"},
	                   {data : "dataType"},
	                   {data : "description"},
	                   {render : function(data, type, full, meta) {
	                     return '<div class="btn-group" role="group">'
	                             + '<button type="button" class="btn btn-warning btn-xs" onclick="$(\'#metrics-table\').trigger(\'edit-metric\', [this, event, ' + full.id + ']);">'
	                             + '<span class="fa fa-edit"></span> Edit' + '</button>'
	                             + '<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#metrics-table\').trigger(\'delete-metric\', [this, event, ' + full.id + ']);">'
	                             + '<span class="fa fa-trash"></span> Delete' + '</button>' + '</div>';
	                    }
	                   }]
	            });
	    	}
	    };
	    return function(inputThing) {
	    	thing = inputThing;
		    toView(thing);
		    bindHandlers();
		    $("#page-title").text(thing && thing.id ? "Edit Thing Details" : "Create New Thing");
	    };

    })(jQuery);
</script>