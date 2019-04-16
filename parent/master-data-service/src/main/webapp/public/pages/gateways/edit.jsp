<style>
#gateway-view-table tr td:LAST-CHILD {
	padding: 0 0 0 1em;
}
#gateway-view-table tr td:FIRST-CHILD {
	padding: 0 1em 0 0;
	width: 50%;
}
#gateway-properties-cell{
	vertical-align: top;
}
</style>
<div class="parent-center">
	<form class="card" id="gateway-form" style="width: 60em;">
		<div class="card-body">
			<input type="text" style="display: none;" id="gateway-id" data-value="id">
			<table id="gateway-view-table">
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-name">Name</label> 
							<input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255"
							type="text" data-value="name" class="form-control" id="gateway-name" name="name" placeholder="Gateway Name">
						</div>
					</td>
					<td rowspan="3" id="gateway-properties-cell">
						<div class="form-group">
							<label for="gateway-properties">Properties</label> 
							<textarea data-rule-maxlength="255" maxlength="255" data-value="properties" data-format="MapFormatter" data-format-target="value" name="properties"
							class="form-control" id="gateway-properties" placeholder="Gateway Properties" rows="9"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-description">Description</label> 
							<input data-rule-maxlength="255" maxlength="255"
							type="text" data-value="description" class="form-control" id="gateway-description" name="description" placeholder="Gateway Description">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-inactivityPeriod">Inactivity Period</label> 
							<input data-rule-required="true" data-rule-digits="true" data-rule-min="60"
							type="text" data-value="inactivityPeriod" class="form-control" id="gateway-inactivityPeriod" name="inactivityPeriod" placeholder="Inactivity Period">
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="card-footer">
			<a href="#" id="gateway-cancel-button" class="btn btn-secondary">
				<span class="fa fa-trash"></span> Cancel
			</a>
			<button type="submit" id="gateway-submit-button" class="btn btn-primary">
				<span class="fa fa-save"></span> Save
			</button>
		</div>
	</form>
</div>

<script>
render = (function($){
	var gateway = null;	
	var toView = function(gateway){
		if(gateway){
			$("#gateway-id").val(gateway.id);
			$("#gateway-name").val(gateway.name);
			$("#gateway-description").val(gateway.description);
			$("#gateway-inactivityPeriod").val(gateway.inactivityPeriod);
			$("#gateway-properties").val(formatProperties(gateway.properties));
		}
	};
	var toModel = function(gateway){
		gateway.id = $("#gateway-id").val();
		gateway.name = $("#gateway-name").val();
		gateway.description = $("#gateway-description").val();
		gateway.inactivityPeriod = $("#gateway-inactivityPeriod").val();
		gateway.properties = parseProperties($("#gateway-properties").val());
	};
	var bindEventHandlers = function(){
		$("#gateway-cancel-button").click(function (event) {
			window.history.back();
		});
	};
	$("#gateway-form").validate({
		errorPlacement: function(error, element) {
			$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
		},
		errorElement: "span",			
		rules:{
			name: {
				remote : {
	                url : "/api/v1/gateways",
	                data : {
	                    id : function() {
		                    return $("#gateway-id").val();
	                    },
	                    name : function() {
		                    return $("#gateway-name").val();
	                    }
	                }
	            }
			},
	        properties: {
	        	propertiesMap : true
	        }
		},			
		submitHandler: function(){
			toModel(gateway);
			gatewayService.save(gateway, function(data){
				gateway = data;
				toView(gateway);
				toast('Gateway has been saved successfully');
			});
		}	
	});
	return function(id){
		bindEventHandlers();
		if(id && 0 < id){
			$("#page-title").text("Edit Gateway Details");
			gatewayService.findOne(id, function(data){
				gateway = data;
				toView(gateway);
			});
		}else{
			$("#page-title").text("Create New Gateway");
			gateway = {properties:{}};
			toView(gateway);
		}
	};
})(jQuery);
</script>