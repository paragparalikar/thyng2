<div id="tenant-edit-modal" class="modal" style="display: block;">
	<div class="modal-content" style="width: 50em;">
	<form id="tenant-form">
		<div class="modal-header">
			<h4 class="modal-title" data-content="title" id="edit-tenant-title">Edit Tenant</h4>
		</div>
		<div class="modal-body">
			<input type="hidden" id="tenant-id" data-value="id">
			<table id="tenant-edit-table" class="form-table">
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-name">Name</label> 
							<input 	data-rule-required="true" 
									data-rule-minlength="3" 
									data-rule-maxlength="255" 
									maxlength="255"
									type="text" 
									data-value="name" 
									class="form-control" 
									id="tenant-name" 
									name="name" 
									placeholder="Tenant Name">
						</div>
					</td>
					<td>
						<div class="form-group">
							<input 	type="checkbox" 
									data-value="locked" 
									name="locked" 
									id="tenant-locked" 
									class="pull-right">
							<label for="tenant-locked" id="tenant-locked-label">Locked</label>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-description">Description</label> 
							<input 	data-rule-maxlength="255" 
									maxlength="255" 
									type="text" 
									data-value="description" 
									name="description"
									class="form-control" 
									id="tenant-description" 
									placeholder="Description for the Tenant">
						</div>
					</td>
					<td rowspan="3" id="tenant-properties-cell">
						<div class="form-group">
							<label for="tenant-properties">Properties</label> 
							<textarea 	data-rule-maxlength="255" 
										maxlength="255" 
										data-value="properties" 
										data-format="MapFormatter" 
										data-format-target="value" 
										name="properties"
										class="form-control" 
										id="tenant-properties" 
										placeholder="Tenant Properties" 
										rows="9">
							</textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-start">Start</label> 
							<div class="input-group date" id="tenant-start-datepicker">
								<input 	data-rule-required="true" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										data-value="start" 
										name="start" 
										class="form-control" 
										id="tenant-start" 
										placeholder="Tenant Start Date">
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="form-group">
							<label for="tenant-expiry">Expiry</label> 
							<div class="input-group date" id="tenant-expiry-datepicker">
								<input 	data-rule-required="true" 
										data-rule-maxlength="255" 
										maxlength="255" 
										type="text" 
										data-value="expiry" 
										name="expiry" 
										class="form-control" 
										id="tenant-expiry" 
										placeholder="Tenant Expiry Date">
								<span class="input-group-addon">
									<span class="fa fa-calendar"></span>
								</span>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<a id="tenant-edit-modal-cancel-button" class="btn btn-secondary"> 
				<i class="fa fa-trash"></i> Cancel
			</a>
			<button id="tenant-edit-modal-action-button" type="submit" class="btn btn-primary">
				<i class="fa fa-save"></i> Save
			</button>
		</div>
	</form>
	</div>
</div>
<script>
	renderModal = (function($){
		
		$("#tenant-start-datepicker").on("dp.change", function (e) {
            $('#tenant-expiry-datepicker').data("DateTimePicker").minDate(e.date);
        });
		
        $("#tenant-expiry-datepicker").on("dp.change", function (e) {
            $('#tenant-start-datepicker').data("DateTimePicker").maxDate(e.date);
        });
        
        $("#tenant-edit-modal-cancel-button").click(function(event){
        	$("#tenant-form").trigger("cancel-tenant-edit");
        });
        
        var tenantStartDatetimePicker = $("#tenant-start-datepicker").datetimepicker({
			format: 'L'
		});
		
		var tenantExpiryDatetimePicker = $("#tenant-expiry-datepicker").datetimepicker({
			useCurrent: false,
			format: 'L'
		});
		
		var save = function(){
			$("#tenant-form").trigger("save-tenant", toModel());
		}
		
		var toView = function(tenant){
			if(tenant){
				$("#tenant-id").val(tenant.id);
				$("#tenant-name").val(tenant.name);
				$("#tenant-description").val(tenant.description);
				$("#tenant-start").val(tenant.start ? new Date(tenant.start).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-expiry").val(tenant.expiry ? new Date(tenant.expiry).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-locked").prop("checked", tenant.locked);
				$("#tenant-properties").val(formatProperties(tenant.properties));
			}
		};
		
		var toModel = function(){
			return {
				id : $("#tenant-id").val(),
				name : $("#tenant-name").val(),
				description : $("#tenant-description").val(),
				start : new Date($("#tenant-start").val()).getTime(),
				expiry : new Date($("#tenant-expiry").val()).getTime(),
				locked : $("#tenant-locked").prop("checked"),
				properties : parseProperties($("#tenant-properties").val())
			};
		};
		
		$("#tenant-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			rules:{
				name: {
					remote : {
		                url : "/api/v1/tenants",
		                data : {
		                    id : function() {
			                    return $("#tenant-id").val();
		                    },
		                    name : function() {
			                    return $("#tenant-name").val();
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
		
		return function(tenant){
			toView(tenant);
			$("#edit-tenant-title").text(tenant && tenant.id && 0 < tenant.id ? "Edit Tenant Details" : "Create New Tenant");
			$("#tenant-name").focus();
		};
	})(jQuery);
</script>