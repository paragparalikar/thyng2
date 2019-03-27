<style>
#tenant-view-table{
	width: 100%;
}
#tenant-view-table tr td:LAST-CHILD {
	padding: 0 0 0 1em;
}
#tenant-view-table tr td:FIRST-CHILD {
	padding: 0 1em 0 0;
	width: 50%;
}
#tenant-locked-label{
	display: inline;
}
</style>

<form class="modal-content" id="tenant-form" style="width: 60em;">
	<div class="modal-header">
		<h4 class="modal-title">Tenant</h4>
	</div>
	<div class="modal-body">
		<input type="text" style="display: none;" id="tenant-id" data-value="id">
		<table id="tenant-view-table">
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-name">Name</label> 
						<input data-rule-required="true" data-rule-minlength="3" data-rule-maxlength="255" maxlength="255"
						type="text" data-value="name" class="form-control" id="tenant-name" name="name" placeholder="Tenant Name">
					</div>
				</td>
				<td>
					<div class="form-group">
						<input type="checkbox" data-value="locked" name="locked" id="tenant-locked" class="pull-right">
						<label for="tenant-locked" id="tenant-locked-label">Locked</label>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-description">Description</label> 
						<input data-rule-maxlength="255" maxlength="255" type="text" data-value="description" name="description"
						class="form-control" id="tenant-description" placeholder="Description for the Tenant">
					</div>
				</td>
				<td>
					<div class="form-group">
						<label for="tenant-tags">Tags</label> 
						<input data-rule-maxlength="255" maxlength="255" type="text" data-value="tags" name="tags"
						class="form-control" id="tenant-tags" placeholder="Tenant Tags">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-start">Start</label> 
						<div class="input-group date" id="tenant-start-datepicker">
							<input data-rule-required="true" data-rule-maxlength="255" maxlength="255" type="text" data-value="start" name="start" class="form-control" id="tenant-start" placeholder="Tenant Start Date">
							<span class="input-group-addon">
								<span class="fa fa-calendar"></span>
							</span>
						</div>
					</div>
				</td>
				<td rowspan="2">
					<div class="form-group">
						<label for="tenant-properties">Properties</label> 
						<textarea data-rule-maxlength="255" maxlength="255" data-value="properties" data-format="MapFormatter" data-format-target="value" name="properties"
						class="form-control" id="tenant-properties" placeholder="Tenant Properties" rows="5"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label for="tenant-expiry">Expiry</label> 
						<div class="input-group date" id="tenant-expiry-datepicker">
							<input data-rule-required="true" data-rule-maxlength="255" maxlength="255" type="text" data-value="expiry" name="expiry" class="form-control" id="tenant-expiry" placeholder="Tenant Expiry Date">
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
		<a href="#" id="tenant-cancel-button" class="btn btn-secondary">
			<span class="fa fa-trash"></span> Cancel
		</a>
		<button type="submit" id="tenant-submit-button" class="btn btn-primary">
			<span class="fa fa-save"></span> Save
		</button>
	</div>
</form>
<script>
	renderModal = (function($){
		var inputTenant = null;
		var inputTenants = null;
		var tenantCallback = null;
		var nameValidationArray = null;
		$("#tenant-name").focus();
		var tenantStartDatetimePicker = $("#tenant-start-datepicker").datetimepicker({
			format: 'L'
		});
		var tenantExpiryDatetimePicker = $("#tenant-expiry-datepicker").datetimepicker({
			useCurrent: false,
			format: 'L'
		});
		$("#tenant-start-datepicker").on("dp.change", function (e) {
            $('#tenant-expiry-datepicker').data("DateTimePicker").minDate(e.date);
        });
        $("#tenant-expiry-datepicker").on("dp.change", function (e) {
            $('#tenant-start-datepicker').data("DateTimePicker").maxDate(e.date);
        });
		$("#tenant-form").validate({
			errorPlacement: function(error, element) {
				$(element).closest("form").find( "label[for='"+element.attr( "id" ) + "']").append( error );
			},
			errorElement: "span",			
			messages:{
				name: {
					notInArray: "Another tenant exists with same name"
				}
			},			
			rules:{
				name: {
					notInArray : {
						provider: function(){
							return nameValidationArray;
						}
					}
				},
		        properties: {
		        	propertiesMap : true
		        }
				
			},			
			submitHandler: function(){
				toModel(inputTenant);
				tenantService.save(JSON.stringify(inputTenant), function(data){
					toast('Tenant has been saved successfully');
					$.modal.close();
					if(tenantCallback){
						tenantCallback(data);
				    }
				});
			}	
		});
		
		var bindEventHandlers = function(){
			$("#tenant-cancel-button").click(function (event) {
				event.preventDefault();
			    $.modal.close();
			});
		};
		
		var toView = function(tenant){
			if(tenant){
				$("#tenant-id").val(tenant.id);
				$("#tenant-name").val(tenant.name);
				$("#tenant-description").val(tenant.description);
				$("#tenant-start").val(tenant.start ? new Date(tenant.start).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-expiry").val(tenant.expiry ? new Date(tenant.expiry).toLocaleDateString() : new Date().toLocaleDateString());
				$("#tenant-locked").prop("checked", tenant.locked);
				 $("#tenant-tags").tagsinput({
			    	  trimValue: true,
			    	  maxChars: 255,
			    	  maxTags: 10,
			    	  confirmKeys: [13, 32, 44, 186, 188]
			    });
			    if(tenant.tags){
			    	tenant.tags.forEach(function(tag){
			    		$("#tenant-tags").tagsinput("add",tag);
			    	});
			    }
			    if(tenant.properties){
			    	$("#tenant-properties").val($.map(tenant.properties, function(val, key) {
						return key + "=" + val;
					}).join("\n"));
			    }
			}
		};
		
		var toModel = function(tenant){
			tenant.id = $("#tenant-id").val();
			tenant.name= $("#tenant-name").val();
			tenant.description= $("#tenant-description").val();
			tenant.start = new Date($("#tenant-start").val()).getTime();
			tenant.expiry = new Date($("#tenant-expiry").val()).getTime();
			tenant.locked = $("#tenant-locked").prop("checked");
			tenant.tags = $("#tenant-tags").val().split(",");
			tenant.properties = {};
		    $("#tenant-properties").val().split("\n").forEach(function(item) {
		        var pair = item.split("=");
		        tenant.properties[pair[0]] = pair[1];
	        });
		};
		
		return function(tenant, tenants, callback){
			inputTenant = tenant;
			inputTenants = tenants;
			tenantCallback = callback;
			toView(tenant);
			bindEventHandlers();
			
			nameValidationArray = inputTenants ? inputTenants.map(m => m.name) : [];
			var index = nameValidationArray.indexOf(inputTenant.name);
			if(index > -1){
				nameValidationArray.splice(index, 1);	
			}
		};
	})(jQuery);
</script>