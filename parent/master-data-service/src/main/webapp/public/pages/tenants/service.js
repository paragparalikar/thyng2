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
    $("#template-container").loadTemplate("list-tenants", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		tenantService.findAll(function (tenants) {
    			render(tenants);
    		});
    	},
    	error: errorCallback
    });
});

$.router.add("#tenants/view/:id", function(){
	var id = arguments[0];
    $("#template-container").loadTemplate("view-tenant", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		tenantService.findById(id, function(tenant){
    			render(tenant);
    		});
    	},
    	error: errorCallback
    });
});


$.subscribe("show-tenant-edit-modal", function(event, id, callback){
    $("#modal-container").loadTemplate("edit-tenant", null, {
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
					toast('Tenant has been saved successfully');
					$.modal.close();
					if(callback){
						callback(data);
					}
				});
    		});
    		tenantService.findById(id, function(tenant){
    			renderModal(tenant);
    		});
    	},
    	error: errorCallback
    });
});

$.router.add("#tenants/create", function(){
    $("#template-container").loadTemplate("edit-tenant", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		renderModal({});
    	},
    	error: errorCallback
    });
});


