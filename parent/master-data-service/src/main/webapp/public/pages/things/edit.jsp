<style>
#thing-details-card {
	width: 60em;
	margin-left: auto;
	margin-right: auto;
}
</style>
<form id="thing-form">
	<div class="card" id="thing-details-card">
		<div class="card-body">
			<input type="hidden" id="thing-id" name="id" data-value="id">
			<table class="form-table">
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
		
		$("#cancelButton").click(function(){
			$("#thing-form").trigger("cancel-thing-edit");
		});
		
		var toModel = function(thing){
			return {
				id : $("#thing-id").val(),
				name : $("#thing-name").val(),
				description : $("#thing-description").val(),
				gatewayId : $("#thing-gateway").val(),
				properties : parseProperties($("#thing-properties").val())
			};
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
		
		var save = function(){
			$("#thing-form").trigger("save-thing", [toModel(), toView]);
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
			submitHandler: save
		});
	    return function(thing, gateways) {
	    	$("#page-title").text(thing && thing.id && 0 < thing.id ? "Edit Thing Details" : "Create New Thing");
	    	$('#thing-gateway').empty();
	    	$.each(gateways, function(index, gateway) {   
	    	     $('#thing-gateway').append("<option value='"+gateway.id+"'>"+gateway.name+"</option>"); 
	    	});
	    	toView(thing);
	    	$("#thing-name").focus();
	    };
    })(jQuery);
</script>