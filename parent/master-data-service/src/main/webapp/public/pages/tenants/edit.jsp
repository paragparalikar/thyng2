<style>
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

<div class="parent-center">
	<form class="card" id="tenant-form" style="width: 60em;">
		<div class="card-body">
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
		<div class="card-footer">
			<a href="#" id="tenant-cancel-button" class="btn btn-secondary">
				<span class="fa fa-trash"></span> Cancel
			</a>
			<button type="submit" id="tenant-submit-button" class="btn btn-primary">
				<span class="fa fa-save"></span> Save
			</button>
		</div>
	</form>
</div>

<script>
	render = (function($){
		var inputTenant = null;
		
		var tenantStartDatetimePicker = $("#tenant-start-datepicker").datetimepicker({
			format: 'L'
		});
		var tenantExpiryDatetimePicker = $("#tenant-expiry-datepicker").datetimepicker({
			useCurrent: false,
			format: 'L'
		});
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
			submitHandler: function(){
				toModel(inputTenant);
				tenantService.save(JSON.stringify(inputTenant), function(data){
					toast('Tenant has been saved successfully');
				});
			}	
		});
		
		var bindEventHandlers = function(){
			$("#tenant-cancel-button").click(function (event) {
				window.history.back();
			});
			$("#tenant-start-datepicker").on("dp.change", function (e) {
	            $('#tenant-expiry-datepicker').data("DateTimePicker").minDate(e.date);
	        });
	        $("#tenant-expiry-datepicker").on("dp.change", function (e) {
	            $('#tenant-start-datepicker').data("DateTimePicker").maxDate(e.date);
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
				$("#tenant-properties").val(formatProperties(tenant.properties));
				addTags($("#tenant-tags"), tenant.tags);
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
			tenant.properties = parseProperties($("#tenant-properties").val());
		};
		
		return function(tenant){
			inputTenant = tenant;
			toView(tenant);
			bindEventHandlers();
			$("#page-title").text(tenant && tenant.id ? "Edit Tenant Details" : "Create New Tenant");
			$("#tenant-name").focus();
		};
	})(jQuery);
</script>