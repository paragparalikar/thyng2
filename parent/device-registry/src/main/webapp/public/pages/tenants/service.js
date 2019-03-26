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
		}
		
};

$.router.add("#tenants", function(){
    $("#template-container").loadTemplate("public/pages/tenants/list.html", null, {
    	success: function(){
    		render();
    	},
    	error: errorCallback
    });
});

$.subscribe("show-tenant-view-modal", function(event, id){
	tenantService.findOne(id, function(data){
		$("#modal-container").loadTemplate("public/pages/tenants/view.html", data, {
			success: function(){
				$("#modal-container").modal();
				renderModal(data);
			}
		});
	});
});