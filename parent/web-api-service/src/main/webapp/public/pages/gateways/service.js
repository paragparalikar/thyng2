gatewayService = {
		findAll : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findAllThin : function(success, error, always) {
			$.get(window.location.origin + "/api/v1/gateways?thin")
			.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
		},
		findById : function(id, success, error, always) {
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
    $("#template-container").loadTemplate("public/pages/gateways/list.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#gateways").on("show-delete-gateway-modal", function(event, gateway, callback){
    			$.publish("show-confirmation-modal", [{
    	            message: "Are you sure you want to delete gateway " + gateway.name + " ?"
    	        }, function () {
    	            gatewayService.deleteById(gateway.id, function () {
    	            	toast('Gateway "'+gateway.name+'" has been deleted successfully');
    	                $.modal.close();
    	                if(callback){
    	                	callback();
    	                }
    	            });
    	        }]);
    		});
    		gatewayService.findAll(function (gateways) {
    			render(gateways);
    		});
    	},
    	error: errorCallback
    });
});

$.subscribe("show-edit-gateway-modal", function(event, id, callback){
	 $("#modal-container").loadTemplate("public/pages/gateways/edit.jsp", null, {
	 	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#modal-container").modal();
			var form = $("#gateway-form");
			form.on("cancel-gateway-edit", function(event){
				$.modal.close();
				form.unbind(event);
			});
			form.on("save-gateway", function(event, gateway){
				gatewayService.save(gateway, function(data){
					$.modal.close();
					form.unbind(event);
					toast("Gateway \""+data.name+"\" has been saved successfully");
					if(callback){
						callback(data);
					}
				});
			});
    		if(id && 0 < id){
    			gatewayService.findById(id, function(gateway){
        			renderModal(gateway);
        		});
    		}else{
    			renderModal();
    		}
    	},
    	error: errorCallback
    });
});

$.subscribe("show-view-gateway-modal", function(event, id){
	 $("#modal-container").loadTemplate("public/pages/gateways/view.jsp", null, {
		beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#modal-container").modal();
    		gatewayService.findById(id, function(gateway){
    			renderModal(gateway);
    		});
    	},
    	error: errorCallback
    });
});

