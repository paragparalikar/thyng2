tenantService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findOne : function(id, success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants/" + id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save: function(tenant, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/tenants",
				type: tenant.id && 0 < tenant.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: tenant,
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
    		render();
    	},
    	error: errorCallback
    });
});

$.router.add("#tenants/view/:id", function(){
	var id = arguments[0];
    $("#template-container").loadTemplate("view-tenant", null, {
    	success: function(){
    		tenantService.findOne(id, function(tenant){
    			render(tenant);
    		});
    	},
    	error: errorCallback
    });
});


$.router.add("#tenants/edit/:id", function(){
	var id = arguments[0];
    $("#template-container").loadTemplate("edit-tenant", null, {
    	success: function(){
    		tenantService.findOne(id, function(tenant){
    			render(tenant);
    		});
    	},
    	error: errorCallback
    });
});

$.router.add("#tenants/create", function(){
    $("#template-container").loadTemplate("edit-tenant", null, {
    	success: function(){
    		render({});
    	},
    	error: errorCallback
    });
});

