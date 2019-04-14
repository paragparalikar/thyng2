gatewayService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways")
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