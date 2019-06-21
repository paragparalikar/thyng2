<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 	final UserDTO user = (UserDTO)session.getAttribute("user"); %>
<style>
#thing-details-card{
	margin: 0 1em 1em 0; 
	width: 32em;
}
#thing-details-table{
	width: 100%;
}
#thing-details-table tbody tr td:FIRST-CHILD label{
	float: right;
	padding-right: 1em;
}
#thing-details-table tbody tr td:LAST-CHILD label{
	float: left;
}
</style>

<div class="card" id="thing-details-card">
	<div class="card-body">
		<table id="thing-details-table">
			<tbody>
				<tr>
					<td><label>Name : </label></td>
					<td id="thing-name"></td>
				</tr>
				<tr>
					<td><label>Description : </label></td>
					<td id="thing-description"></td>
				</tr>
				<tr>
					<td><label>Active : </label></td>
					<td id="thing-active"></td>
				</tr>
				<tr>
					<td><label>Gateway : </label></td>
					<td id="thing-gateway"></td>
				</tr>
				<tr>
					<td><label>Properties : </label></td>
					<td id="thing-properties"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<%if(user.hasAuthority(Authority.SENSOR_LIST)){ %>
<div class="card" style="margin: 1em 0 0 0;">
	<div class="card-header">
		<h5>Sensors</h5>
	</div>
	<div class="card-body">
		<table id="sensor-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Abbreviation</th>
					<th scope="col">Unit</th>
					<th scope="col">Data Type</th>
					<th scope="col">Active</th>
					<th scope="col">Inactivity Period</th>
					<th scope="col">Batch Size</th>
					<th scope="col">Description</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<%} %>
<%if(user.hasAuthority(Authority.ACTUATOR_LIST)){ %>
<div class="card" style="margin: 1em 0 0 0;">
	<div class="card-header">
		<h5>Actuators</h5>
	</div>
	<div class="card-body">
		<table id="actuator-table" class="table table-striped table-bordered table-sm" style="width: 100%;">
			<thead>
				<tr>
					<th scope="col">Name</th>
					<th scope="col">Unit</th>
					<th scope="col">Data Type</th>
					<th scope="col">Protocol</th>
					<th scope="col">Topic</th>
					<th scope="col">Host</th>
					<th scope="col">Port</th>
					<th scope="col">Path</th>
					<th scope="col">Description</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<%} %>
<script>
	render = (function($) {
	    var toView = function(thing) {
		    $("#thing-name").text(thing.name);
		    $("#thing-description").text(thing.description);
		    $("#thing-active").text(thing.active ? "Yes" : "No");
		    $("#thing-gateway").text(thing.gatewayName);
		    $("#thing-properties").html(formatProperties(thing.properties,"<br>"));
		    $("#sensor-table").DataTable({
		        searching : false,
		        ordering : false,
		        paging : false,
		        info : false,
		        data : thing.sensors,
		        columns : [ 
		           {
		        	   data : "name",
	                   render: function (data, type, row) {
	                       return user.hasAuthority("SENSOR_VIEW") ? '<a href="#things/view/'+row.id+'">' + data + '</a>' : data;
	                   }
		        	}, 
		           {data : "abbreviation" }, 
		           {data : "unit"}, 
		           {data : "dataType"}, 
		           {
		        	   data : "active",
		        	   render: function(data, type, row, meta){
		        		   return data ? "Yes" : "No";
		        	   }
		        	},
		           {data : "inactivityPeriod"},
		           {data : "batchSize"},
		           {data : "description"}
		        ]
		    });
		    $("#actuator-table").DataTable({
		        searching : false,
		        ordering : false,
		        paging : false,
		        info : false,
		        data : thing.actuators,
		        columns : [ 
		           {
		        	   data : "name",
	                   render: function (data, type, row) {
	                       return user.hasAuthority("ACTUATOR_VIEW") ? '<a href="#things/view/'+row.id+'">' + data + '</a>' : data;
	                   }
		        	}, 
		           {data : "unit"}, 
		           {data : "dataType"}, 
		           {data : "protocol"},
		           {data : "topic"},
		           {data : "host"},
		           {data : "port"},
		           {data : "path"},
		           {data : "description"}
		        ]
		    });
	    };
	    return function(thing) {
		    $("#page-title").html("Thing Details");
		    toView(thing);
	    };
    })(jQuery);
</script>