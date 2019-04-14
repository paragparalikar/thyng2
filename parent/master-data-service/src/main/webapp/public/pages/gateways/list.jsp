<%@ page import="com.thyng.model.dto.UserDTO" %>
<%@ page import="com.thyng.model.enumeration.Authority" %>
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
                        <a href="#gateways/create" class="btn btn-primary btn-sm">
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
	var gatetwaysDataTable = null;
	
	var showDeleteGatewayConfirmationModal = function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = gatetwaysDataTable.row("#" + id);
        $.publish("show-confirmation-modal", [{
            message: "Are you sure you want to delete gateway " + row.data().name + " ?"
        }, function () {
            gatewayService.deleteById(id, function () {
            	toast('Gateway has been deleted successfully');
                row.remove().draw();
                $.modal.close();
            });
        }]);
	};
	
	var showGatewaysDataTable = function(){
		gatewayService.findAll(function (gateways) {
	        gatewaysDataTable = $("#gateways").DataTable({
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
							return user.hasAuthority("GATEWAY_VIEW") ? "<a href='#gateways/view/"+row.id+"'>"+data+"</a>" : data;
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
	                    		'<a class="btn btn-warning btn-xs" href="#gateways/edit/'+row.id+'">' +
		                        	'<span class="fa fa-edit"></span> Edit' +
		                        '</a>' : "";
		                    var deleteHtml = user.hasAuthority("GATEWAY_DELETE") ? 
		                    	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#gateways\').trigger(\'delete-gateway\', [this, event,'+row.id+'])">' +
	                            	'<span class="fa fa-trash"></span> Delete' +
	                            '</button>' : "";
	                        return '<div class="btn-group" role="group">' + editHtml + deleteHtml + '</div>';
	                    }
	                }<% } %>],
	            <% if(hasWriteAccess){%>
	            columnDefs: [{
	                targets: -1,
	                orderable: false,
	                className: "text-center"
	            }],
	            <% } %>
	            data: gateways
	        });
	    });
	};
	
	return function(){
		$("#page-title").html("Gateways");
		showGatewaysDataTable();
		$("#gateways").on("delete-gateway", showDeleteGatewayConfirmationModal);
	}
})(jQuery);
</script>