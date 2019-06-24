<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
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
                        <a class="btn btn-primary btn-sm" id="new-tenant-button">
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

    var tenantsDataTable = $("#tenants").DataTable({
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
					return user.hasAuthority("TENANT_VIEW") ? '<a href="" onclick="$(\'#tenants\').trigger(\'view-tenant\', [this, event,'+row.id+'])">'+data+'</a>' : data;
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
                		'<a class="btn btn-warning btn-xs" onclick="$(\'#tenants\').trigger(\'edit-tenant\', [this, event,'+row.id+'])">' +
                        	'<span class="fa fa-edit"></span> Edit' +
                        '</a>' : "";
                    var deleteHtml = user.hasAuthority("TENANT_DELETE") ? 
                    	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#tenants\').trigger(\'delete-tenant\', [this, event,'+row.id+'])">' +
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
    
    $("#tenants").on("view-tenant", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = tenantsDataTable.row("#" + id);
		$.publish("show-tenant-view-modal", [row.data().id]);
	});
    
    $("#new-tenant-button").click(function(event){
		$.publish("show-tenant-edit-modal", [null, function(data){
			tenantsDataTable.row.add(data).draw();
		}]);
	});
    
	$("#tenants").on("edit-tenant", function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		row = tenantsDataTable.row("#" + id);
		$.publish("show-tenant-edit-modal", [row.data().id, function(data){
			row.data(data).draw();
		}]);
	});
	
    $("#tenants").on("delete-tenant", function(event, element, originalEvent, id){
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
	});
    
	return function(tenants){
		$("#page-title").html("Tenants");
		tenantsDataTable.clear().rows.add(tenants).draw().columns.adjust();
	}
})(jQuery);
</script>