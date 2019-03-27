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
                    <th>Tags</th>
                    <th>Description</th>
                    <% if(hasWriteAccess){ %>
                    <th>
                    	<%if(user.hasAuthority(Authority.TENANT_CREATE)){ %>
                        <button class="btn btn-primary btn-sm" id="newTenantButton">
                            <span class="fa fa-plus"></span> New Tenant
                        </button>
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
	
	var showEditTenantModal = function(event, element, originalEvent, id){
		element.blur();
		originalEvent.preventDefault();
		var row = tenantsDataTable.row("#" + id);
		var tenant = row.data();
		showEditTenantView(tenant);
	}
	
	var showEditTenantView = function(tenant){
		tenant = tenant ? tenant : {};
		$("#modal-container").loadTemplate("public/pages/tenants/edit.html", tenant, {
		    success : function() {
		    	$("#modal-container").modal();
		    	var data = tenantsDataTable.rows().data();
		    	renderModal(tenant, data, function(savedTenant){
		    		var isEdit = false;
		    		var editIndex = -1;
		    		for(var index = 0; index < data.length; index++){
		    			if(savedTenant.id === data[index].id){
		    				isEdit = true;
		    				editIndex = index;
		    				break;
		    			}
		    		}
		    		var clonedData = data.slice();
		    		if(isEdit){
		    			clonedData.splice(editIndex, 1);
		    		}else{
		    			clonedData.push(savedTenant);
		    		}
		    		tenantsDataTable.clear().rows.add(clonedData).draw();
			    });
		    },
	    	error: errorCallback
	    });
	}
	
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
							return user.hasAuthority("TENANT_CREATE") ? "<a href='#' onclick='this.blur(); event.preventDefault(); $.publish(\"show-tenant-view-modal\", "+row.id+");'>"+data+"</a>" : data;
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
	                { data: "tags" },
	                { data: "description" }
	                
	                <% if(hasWriteAccess){%>
	                ,{
	                    render: function (data, type, row, meta) {
	                    	var editHtml = user.hasAuthority("TENANT_UPDATE") ? 
	                    		'<button type="button" class="btn btn-warning btn-xs" onclick="$(\'#tenants\').trigger(\'edit-tenant\', [this, event,'+row.id+'])">' +
		                        	'<span class="fa fa-edit"></span> Edit' +
		                        '</button>' : "";
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
		$("#newTenantButton").click(function(event){
			showEditTenantView(null);
		});
		$("#tenants").on("edit-tenant", showEditTenantModal);
		$("#tenants").on("delete-tenant", showDeleteTenantConfirmationModal);
	}
})(jQuery);
</script>