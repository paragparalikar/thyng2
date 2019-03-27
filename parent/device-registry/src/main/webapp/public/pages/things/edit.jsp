<div class="modal-content" style="width: 60em;">
	<div class="modal-header">
		<h4 class="modal-title">Thing Details</h4>
	</div>
	<div class="modal-body">
		<table style="width:100%">
			<tr>
				<td width="50%" style="padding: 0 1em 0 0;">
					<div class="form-group">
						<label for="thingName">Name</label> 
						<input type="text" data-value="name" class="form-control" id="thingName">
					</div>
				</td>
				<td width="50%"  style="padding: 0 0 0 1em;">
					<span>
						<label>BiDirectional</label>
						<span style="float:right;">
							<input id="biDirectional" type="checkbox" data-value="biDirectional">
						</span>
					</span>
				</td>
			</tr>
			<tr>
				<td width="50%" style="padding: 0 1em 0 0;">
					<div class="form-group">
						<label for="thingDescription">Description</label> 
						<input type="text" data-value="description" class="form-control" id="thingDescription">
					</div>
				</td>
				<td width="50%"  style="padding: 0 0 0 1em;" rowspan="2">
					<div class="form-group">
						<label for="properties">Properties</label> 
						<textarea data-value="properties" data-format="MapFormatter" data-format-target="value" class="form-control" id="properties" rows="5"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td width="50%"  style="padding: 0 1em 0 0;">
					<div class="form-group">
						<label for="tags">Tags</label> 
						<input type="text" data-value="tags" class="form-control" id="tags">
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
<script>
	render = (function($){
		var toView = function(thing){
			$("#alive").prop("checked", thing.alive);
			$("#biDirectional").prop("checked", thing.biDirectional);
		};
		return function(thing){
			toView(thing);
		};
		
	})(jQuery);
</script>