<%@ page import="com.thyng.model.dto.UserDTO" %>
<%@ page import="com.thyng.model.enumeration.Authority" %>
<% 
	final UserDTO user = (UserDTO)session.getAttribute("user");
	final boolean hasWriteAccess = user.hasAuthority(Authority.TENANT_CREATE) || user.hasAuthority(Authority.TENANT_UPDATE) || user.hasAuthority(Authority.TENANT_DELETE);
%>
<div class="card">
    <div class="card-body">
        <table id="tenants" class="table table-bordered table-striped" style="width: 100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Start</th>
                    <th>Expiry</th>
                    <th>Locked</th>
                    <th>Description</th>
                    <% if(hasWriteAccess){ %>
                    <th>
                    	<%if(user.hasAuthority(Authority.TENANT_CREATE)){ %>
                        <a href="#tenants/create" class="btn btn-primary btn-sm">
                            <span class="fa fa-plus"></span> New Tenant
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
	var tenantsDataTable = null;
	
	var showDeleteTenantConfirmationModal = function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = tenantsDataTable.row("#" + id);
        $.publish("show-confirmation-modal", [{
            message: "Are you sure you want to delete tenant " + row.data().name + " ?"
        }, function () {
            tenantService.deleteById(id, function () {
            	toast('Tenant has been deleted successfully');
                row.remove().draw();
                $.modal.close();
            });
        }]);
	};
	
	var showTenantsDataTable = function(){
		tenantService.findAll(function (tenants) {
	        tenantsDataTable = $("#tenants").DataTable({
	            rowId: "id",
	            language: {
	                processing: "Loading ..."
	            },
	            processing: true,
	            columns: [
	                { data: "name",
						render: function(data, type, row){
							if(type === "sort" || type === "type"){
	                            return data;
	                        }
							return user.hasAuthority("TENANT_VIEW") ? "<a href='#tenants/view/"+row.id+"'>"+data+"</a>" : data;
						}	                	
                	},
	                { data: "start",
	                	render: function(data, type, row){
	                		if(type === "sort" || type === "type"){
	                            return data;
	                        }
	                		return new Date(data).toLocaleString();
	                	}
	                },
	                { data: "expiry",
	                	render: function(data, type, row){
	                		if(type === "sort" || type === "type"){
	                            return data;
	                        }
	                		return new Date(data).toLocaleString();
	                	}
	                },
	                { data: "locked",
						render: function(data, type, row){
							return data ? "Yes" : "No";
						}
	                },
	                { data: "description" }
	                
	                <% if(hasWriteAccess){%>
	                ,{
	                    render: function (data, type, row, meta) {
	                    	var editHtml = user.hasAuthority("TENANT_UPDATE") ? 
	                    		'<a class="btn btn-warning btn-xs" href="#tenants/edit/'+row.id+'">' +
		                        	'<span class="fa fa-edit"></span> Edit' +
		                        '</a>' : "";
		                    var deleteHtml = user.hasAuthority("TENANT_DELETE") ? 
		                    	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#tenants\').trigger(\'delete-tenant\', [this, event,'+row.id+'])">' +
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
	            data: tenants
	        });
	    });
	};
	
	return function(){
		$("#page-title").html("Tenants");
		showTenantsDataTable();
		$("#tenants").on("delete-tenant", showDeleteTenantConfirmationModal);
	}
})(jQuery);
</script>