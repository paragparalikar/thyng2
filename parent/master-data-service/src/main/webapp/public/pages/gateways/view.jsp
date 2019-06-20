<style>
#gateway-details-card{
	margin: 0 1em 1em 0; 
	width: 32em;
}
#gateway-details-table{
	width: 100%;
}
#gateway-details-table tbody tr td:FIRST-CHILD label{
	float: right;
	padding-right: 1em;
}
#gateway-details-table tbody tr td:LAST-CHILD label{
	float: left;
}
</style>

<div class="card" style="" id="gateway-details-card">
	<div class="card-body">
		<table id="gateway-details-table">
			<tbody>
				<tr>
					<td><label>ID : </label></td>
					<td id="gateway-id"></td>
				</tr>
				<tr>
					<td><label>Name : </label></td>
					<td id="gateway-name"></td>
				</tr>
				<tr>
					<td><label>Description : </label></td>
					<td id="gateway-description"></td>
				</tr>
				<tr>
					<td><label>Active : </label></td>
					<td id="gateway-active"></td>
				</tr>
				<tr>
					<td><label>Inactivity Period : </label></td>
					<td id="gateway-inactivityPeriod"></td>
				</tr>
				<tr>
					<td><label>Host : </label></td>
					<td id="gateway-host"></td>
				</tr>
				<tr>
					<td><label>Port : </label></td>
					<td id="gateway-port"></td>
				</tr>
				<tr>
					<td><label>Properties : </label></td>
					<td id="gateway-properties"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<script>
render = (function($){
	var toView = function(gateway){
		if(gateway){
			$("#gateway-id").text(gateway.id);
			$("#gateway-name").text(gateway.name);
			$("#gateway-description").text(gateway.description);
			$("#gateway-active").text(gateway.active ? "Yes" : "No");
			$("#gateway-inactivityPeriod").text(gateway.inactivityPeriod);
			$("#gateway-host").text(gateway.host);
			$("#gateway-port").text(gateway.port);
			$("#gateway-properties").html(formatProperties(gateway.properties, "<br>"));
		}
	};
	return function(id){
		 $("#page-title").text("Gateway Details");
		 gatewayService.findOne(id, toView);
	};
})(jQuery);
</script>