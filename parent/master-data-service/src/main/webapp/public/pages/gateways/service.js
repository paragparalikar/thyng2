gatewayService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findAllThin : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways?thin")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findOne : function(id, success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways/" + id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		save: function(gateway, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/gateways",
				type: gateway.id && 0 < gateway.id ? "PUT" : "POST",
				contentType:"application/json; charset=utf-8",
				data: JSON.stringify(gateway),
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		deleteById : function(id, success, error, always){
			$.ajax({
				url: window.location.origin + "/api/v1/gateways/" + id,
				type: "DELETE",
			}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		}
};
$.router.add("#gateways", function(){
    $("#template-container").loadTemplate("list-gateways", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		render();
    	},
    	error: errorCallback
    });
});
$.router.add("#gateways/view/:id", function(){
	var id = arguments[0];
    $("#template-container").loadTemplate("view-gateway", null, {
    	success: function(){
    		render(id);
    	},
    	error: errorCallback
    });
});

$.router.add("#gateways/edit/:id", function(){
	var id = arguments[0];
    $("#template-container").loadTemplate("edit-gateway", null, {
    	success: function(){
    		render(id);
    	},
    	error: errorCallback
    });
});