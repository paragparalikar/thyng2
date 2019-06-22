<style>
#thing-details-card {
	width: 60em;
	margin-left: auto;
	margin-right: auto;
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
<form id="thing-form">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<input type="hidden" id="thing-id" name="id" data-value="id">
			<table id="thing-details-table">
				<tbody>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-name">Name</label> 
								<input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255" type="text" name="name" class="form-control" id="thing-name" placeholder="Thing name">
							</div>
						</td>
						<td rowspan="3">
							<div class="form-group">
								<label for="thing-properties">Properties</label>
								<textarea data-rule-maxlength="255" maxlength="255" name="properties"class="form-control" id="thing-properties" rows="8" placeholder="Thing properties"></textarea>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="form-group">
								<label for="thing-description">Description</label> 
								<input data-rule-maxlength="255" maxlength="255" type="text" name="description" class="form-control" id="thing-description" placeholder="Thing description">
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
			<a href="#" id="cancelButton" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button type="submit" href="#" id="saveButton" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
	</div>
</form>
<jsp:include page="sensors/list.jsp"/>
<jsp:include page="actuators/list.jsp"/>


<script>
	render = (function($) {
		var thing = null;
		var toModel = function(thing){
			thing.id = $("#thing-id").val();
			thing.name = $("#thing-name").val();
			thing.description = $("#thing-description").val();
			thing.gatewayId = $("#thing-gateway").val();
			thing.properties = parseProperties($("#thing-properties").val());
		};
		var toView = function(thing){
			if(thing){
				var isUpdate = thing.id && 0 < thing.id;
				$("#thing-id").val(thing.id);
				$("#thing-name").val(thing.name);
				$("#thing-description").val(thing.description);
				$("#thing-properties").val(formatProperties(thing.properties));
				if(thing.gatewayId){
					var selectableOption = $("#thing-gateway option[value='"+thing.gatewayId+"']");
					if(selectableOption){
						selectableOption.prop('selected', true).change();
					}
				}
				sensorsListView.data(thing.id, thing.sensors);
				sensorsListView.display(isUpdate);
				actuatorsListView.data(thing.id, thing.actuators);
				actuatorsListView.display(isUpdate);
			}
		};
		var bindHandlers = function(){
			$("#cancelButton").click(function(){
				window.history.back();
			});
		};
		$("#thing-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			rules:{
				name: {
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
			submitHandler: function(){
				toModel(thing);
				thingService.save(thing, function(data){
					thing = data;
					toView(thing);
					toast('Thing has been saved successfully');
				});
			}	
		});
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