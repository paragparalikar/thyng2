tenantService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants")
			.done(success).fail(error ? error : errorCallback).always(always);
		},
		findOne : function(id, success, error, always) {
			$.get(window.location.origin + "/api/v1/tenants/" + id)
			.done(success).fail(error ? error : errorCallback).always(always);
		},
		save: function(tenant, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/tenants",
				type: "POST",
				contentType:"application/json; charset=utf-8",
				data: tenant,
			}).done(success).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/tenants/" + id,
				type: "DELETE",
			}).done(success).fail(error ? error : errorCallback).always(always);
		},
		formatProperties: function(props, delimiter){
			return props ? $.map(props, function(val, key) {
				return key + "=" + val;
			}).join(delimiter ? delimiter : "\n") : "";
		},
		parseProperties: function(text){
			var props = {};
			if(text){
				text.split("\n").forEach(function(item) {
			        var pair = item.split("=");
			        props[pair[0]] = pair[1];
		        });
			}
			return props;
		}
};

$.router.add("#tenants", function(){
    $("#template-container").loadTemplate("list-tenants", null, {
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


