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
								<label for="thing-name">Name</label> 
								<input type="text"  name="name" class="form-control" id="thing-name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea name="properties"class="form-control" id="thing-properties" rows="8"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input type="text" name="description" class="form-control" id="thing-description">
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-gateway">Gateway</label>
								<select id="thing-gateway" name="gatewayId" class="form-control"></select> 
							</div>
						</td>

					</tr>
				</tbody>
			</table>
		</div>
		<div class="card-footer">
			<span class="pull-left">
				<a href="#" id="editSensorsButton" class="btn btn-secondary"> 
					<i class="fa fa-edit"></i> Edit Sensors
				</a>
				<a href="#" id="editActuatorsButton" class="btn btn-secondary"> 
					<i class="fa fa-edit"></i> Edit Actuators
				</a>
			</span>
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
		var toModel = function(thing){
			
		};
		var toView = function(thing){
			if(thing){
				$("#thing-name").val(thing.name);
				$("#thing-description").val(thing.description);
				$("#thing-properties").val(formatProperties(thing.properties));
				if(thing.gatewayId){
					var selectableOption = $("#thing-gateway option[value='"+thing.gatewayId+"']");
					if(selectableOption){
						selectableOption.prop('selected', true).change();
					}
				}
			}
		};
		var bindHandlers = function(){
			$("#cancelButton").click(function(){
				window.history.back();
			});
		};
	    return function(id) {
	    	bindHandlers();
		    gatewayService.findAllThin(function(gateways){
		    	$.each(gateways, function(index, gateway) {   
		    	     $('#thing-gateway').append("<option value='"+gateway.id+"'>"+gateway.name+"</option>"); 
		    	});
		    	if(id && 0 < id){
		    		$("#page-title").text("Edit Thing Details");
		    		thingService.findOne(id, function(data){
		    			thing = data;
		    			toView(thing);
		    		});
		    	}else{
		    		$("#page-title").text("Create New Thing");
		    		thing = {properties:{}};
		    		toView(thing);
		    	}
		    });
	    };
    })(jQuery);
</script>