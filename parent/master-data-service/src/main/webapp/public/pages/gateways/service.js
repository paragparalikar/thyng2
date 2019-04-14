gatewayService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findOne : function(id, success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways/" + id)
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
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