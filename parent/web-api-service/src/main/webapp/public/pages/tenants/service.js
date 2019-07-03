tenantService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(id, success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants/" + id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save: function(tenant, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/tenants",
				type: tenant.id && 0 < tenant.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(tenant),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/tenants/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};

$.router.add("#tenants", function(){
    $("#template-container").loadTemplate("public/pages/tenants/list.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#tenants").on("show-tenant-delete-modal", function(event, tenant, callback){
    			 $.publish("show-confirmation-modal", [{
    		            message: "Are you sure you want to delete tenant " + tenant.name + " ?"
    		        }, function () {
    		            tenantService.deleteById(tenant.id, function () {
    		            	toast('Tenant "'+tenant.name+'" has been deleted successfully');
    		                $.modal.close();
    		                if(callback){
    							callback();
    						}
    		            });
    		        }]);
    		});
    		tenantService.findAll(function (tenants) {
    			render(tenants);
    		});
    	},
    	error: errorCallback
    });
});

$.subscribe("show-tenant-view-modal", function(event, id){
	 $("#modal-container").loadTemplate("public/pages/tenants/view.jsp", null, {
	    	beforeInsert: beforeTemplateInsert,
	    	success: function(){
	    		$("#modal-container").modal();
	    		tenantService.findById(id, function(tenant){
	    			renderModal(tenant);
	    		});
	    	},
	    	error: errorCallback
	    });
});

$.subscribe("show-tenant-edit-modal", function(event, id, callback){
    $("#modal-container").loadTemplate("public/pages/tenants/edit.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#modal-container").modal();
    		$("#tenant-form").on("cancel-tenant-edit", function(event){
    			$.modal.close();
    			$("#tenant-form").unbind(event);
    		});
    		$("#tenant-form").on("save-tenant", function(event, tenant){
    			tenantService.save(tenant, function(data){
    				$("#tenant-form").unbind(event);
					toast('Tenant "'+data.name+'" has been saved successfully');
					$.modal.close();
					if(callback){
						callback(data);
					}
				});
    		});
    		if(id && 0 < id){
    			tenantService.findById(id, function(tenant){
        			renderModal(tenant);
        		});
    		}else{
    			renderModal();
    		}
    	},
    	error: errorCallback
    });
});
