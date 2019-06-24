<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
<% 
	final UserDTO user = (UserDTO)session.getAttribute("user");
	final boolean hasWriteAccess = user.hasAuthority(Authority.GATEWAY_CREATE) || user.hasAuthority(Authority.GATEWAY_UPDATE) || user.hasAuthority(Authority.GATEWAY_DELETE);
%>
<div class="card">
    <div class="card-body">
        <table id="gateways" class="table table-bordered table-striped" style="width: 100%">
            <thead>
                <tr>
                	<th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Active</th>
                    <th>Inactivity Period</th>
                    <th>Host</th>
                    <th>Port</th>
                    <% if(hasWriteAccess){ %>
                    <th>
                    	<%if(user.hasAuthority(Authority.GATEWAY_CREATE)){ %>
                        <a class="btn btn-primary btn-sm" id="new-gateway-button">
                            <span class="fa fa-plus"></span> New Gateway
                        </a>
                        <%} %>
                    </th>
                    <%} %>
                </tr>
            </thead>
        </table>
    </div>
</div>

<script>
render = (function($){

    var gatewaysDataTable = $("#gateways").DataTable({
        rowId: "id",
        language: {
            processing: "Loading ..."
        },
        processing: true,
        columns: [
            { data: "id" },
            { data: "name",
				render: function(data, type, row){
					if(type === "sort" || type === "type"){
                        return data;
                    }
					return user.hasAuthority("GATEWAY_VIEW") ? '<a href="" onclick="$(\'#gateways\').trigger(\'view-gateway\', [this, event,'+row.id+'])">'+data+"</a>" : data;
				}	                	
        	},
            { data: "description" },
            { data: "active" },
            { data: "inactivityPeriod" },
            { data: "host" },
            { data: "port" }
            <% if(hasWriteAccess){%>
            ,{
                render: function (data, type, row, meta) {
                	var editHtml = user.hasAuthority("GATEWAY_UPDATE") ? 
                		'<a class="btn btn-warning btn-xs" onclick="$(\'#gateways\').trigger(\'edit-gateway\', [this, event,'+row.id+'])">' +
                        	'<span class="fa fa-edit"></span> Edit' +
                        '</a>' : "";
                    var deleteHtml = user.hasAuthority("GATEWAY_DELETE") ? 
                    	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#gateways\').trigger(\'delete-gateway\', [this, event,'+row.id+'])">' +
                        	'<span class="fa fa-trash"></span> Delete' +
                        '</button>' : "";
                    return '<div class="btn-group" role="group">' + editHtml + deleteHtml + '</div>';
                }
            }<% } %>]
        <% if(hasWriteAccess){%>
        ,columnDefs: [{
            targets: -1,
            orderable: false,
            className: "text-center"
        }]
        <% } %>
    });
    
    if($("#new-gateway-button")){
    	$("#new-gateway-button").click(function(event){
    		$.publish("show-edit-gateway-modal", [null, function(gateway){
    			gatewaysDataTable.row.add(gateway).draw();
    		}]);
    	});
    }
    
    $("#gateways").on("view-gateway", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = gatewaysDataTable.row("#" + id);
		$.publish("show-view-gateway-modal", row.data().id);
	});
    
	$("#gateways").on("delete-gateway", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = gatewaysDataTable.row("#" + id);
		$("#gateways").trigger("show-delete-gateway-modal", [row.data(), function(){
			 row.remove().draw();
		}]);
	});
	
	$("#gateways").on("edit-gateway", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = gatewaysDataTable.row("#" + id);
		$.publish("show-edit-gateway-modal", [row.data().id, function(data){
			row.data(data).draw();
		}]);
	});
	
	return function(gateways){
		$("#page-title").html("Gateways");
		gatewaysDataTable.clear().rows.add(gateways).draw().columns.adjust();
	}
})(jQuery);
</script>