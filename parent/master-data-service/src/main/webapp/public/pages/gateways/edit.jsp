<div id="gateway-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="gateway-form">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title" id="edit-gateway-title">Edit Gateway</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" id="gateway-id" data-value="id">
			<table id="gateway-view-table" class="form-table">
				<tr>
					<td>
						<div class="form-group required">
							<label for="gateway-name">Name</label> 
							<input 	data-rule-required="true" 
									data-rule-minlength="3" 
									data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="name" 
									class="form-control" 
									id="gateway-name" 
									name="name" 
									placeholder="Gateway Name">
						</div>
					</td>
					<td rowspan="3" id="gateway-properties-cell">
						<div class="form-group">
							<label for="gateway-properties">Properties</label> 
							<textarea 	data-rule-maxlength="255" 
										maxlength="255" 
										data-value="properties" 
										data-format="MapFormatter" 
										data-format-target="value" 
										name="properties"
										class="form-control" 
										id="gateway-properties" 
										placeholder="Gateway Properties" 
										rows="9"></textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="gateway-description">Description</label> 
							<input 	data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="description" 
									class="form-control" 
									id="gateway-description" 
									name="description" 
									placeholder="Gateway Description">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group required">
							<label for="gateway-inactivityPeriod">Inactivity Period</label> 
							<input 	data-rule-required="true" 
									data-rule-digits="true" 
									data-rule-min="60"
									type="text" 
									data-value="inactivityPeriod" 
									class="form-control" 
									id="gateway-inactivityPeriod" 
									name="inactivityPeriod" 
									placeholder="Inactivity Period">
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<a id="gateway-edit-modal-cancel-button" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button id="gateway-edit-modal-action-button" type="submit" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
	</form>
	</div>
</div>
<script>
renderModal = (function($){
	
	 $("#gateway-edit-modal-cancel-button").click(function(event){
     	$("#gateway-form").trigger("cancel-gateway-edit");
     });

	var toView = function(gateway){
		if(gateway){
			$("#gateway-id").val(gateway.id);
			$("#gateway-name").val(gateway.name);
			$("#gateway-description").val(gateway.description);
			$("#gateway-inactivityPeriod").val(gateway.inactivityPeriod);
			$("#gateway-properties").val(formatProperties(gateway.properties));
		}
	};
	
	var toModel = function(){
		return {
			id : $("#gateway-id").val(),
			name : $("#gateway-name").val(),
			description : $("#gateway-description").val(),
			inactivityPeriod : $("#gateway-inactivityPeriod").val(),
			properties : parseProperties($("#gateway-properties").val())	
		};
	};
	
	var save = function(){
		$("#gateway-form").trigger("save-gateway", toModel());
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
		submitHandler: save
	});
	
	return function(gateway){
		$("#page-title").text(gateway && gateway.id && 0 < gateway.id ? "Edit Gateway Details" : "Create New Gateway");
		toView(gateway);
		$("#gateway-name").focus();
	};
})(jQuery);
</script>