var thingService = {
	findAll : function(success, error, always) {
		$.get(window.location.origin + "/api/v1/things")
		.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
	},
	findById : function(id, success, error, always) {
		$.get(window.location.origin + "/api/v1/things/" + id)
		.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
	},
	save: function(thing, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/things",
			type: thing.id && 0 < thing.id ? "PUT" : "POST",
			contentType:"application/json; charset=utf-8",
			data: JSON.stringify(thing),
		}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
	},
	deleteById : function(id, success, error, always){
		$.ajax({
			url: window.location.origin + "/api/v1/things/" + id,
			type: "DELETE",
		}).done(successCallback(success)).fail(error ? error : errorCallback).always(always);
	}
};

$.router.add("#things", function(){
    $("#template-container").loadTemplate("public/pages/things/list.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		$("#things").on("show-delete-thing-modal", function(event, thing, callback){
    			$.publish("show-confirmation-modal", [{
    	            message: "Are you sure you want to delete thing " + thing.name + " ?"
    	        }, function () {
    	            thingService.deleteById(thing.id, function () {
    	            	toast('Thing "'+thing.name+'" has been deleted successfully');
    	                $.modal.close();
    	                if(callback){
    	                	callback();
    	                }
    	            });
    	        }]);
    		});
    		thingService.findAll(function (things) {
    			render(things);
    		});
    	},
    	error: errorCallback
    });
});

$.subscribe("show-view-thing-modal", function(event, id){
	 $("#modal-container").loadTemplate("public/pages/things/view.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		thingService.findById(id, function(thing){
    			renderModal(thing);
    			$("#sensonr-table-container").loadTemplate("public/pages/things/sensors/view-list.jsp", null, {
        			beforeInsert: beforeTemplateInsert,
        			success: function(){
        				renderSensorDataTable(thing.sensors);
        			},
        			error: errorCallback
        		});
    			$("#actuator-table-container").loadTemplate("public/pages/things/actuators/view-list.jsp", null, {
        			beforeInsert: beforeTemplateInsert,
        			success: function(){
        				renderActuatorDataTable(thing.actuators);
        				$("#modal-container").modal();
        			},
        			error: errorCallback
        		});
    		});
    	},
    	error: errorCallback
    });
});

$.router.add("#things/view/:id", function(params){
	var id = arguments[0];
    $("#template-container").loadTemplate("public/pages/things/view.jsp", null, {
    	beforeInsert: beforeTemplateInsert,
    	success: function(){
    		thingService.findOne(id, function(thing){
    			render(thing);
    		});
    	},
    	error: errorCallback
    });
});

$.router.add("#things/edit/:id", function(params){
	var id = arguments[0];
	$("#template-container").loadTemplate("public/pages/things/edit.jsp", null, {
		beforeInsert: beforeTemplateInsert,
		success : function(){
			$("#thing-form").on("save-thing", function(event, thing, callback){
				thingService.save(thing, function(data){
					toast('Thing "'+data.name+'" has been saved successfully');
					if(callback){
						callback(data);
					}
				});
			});
			gatewayService.findAllThin(function(gateways){
				if(id && 0 < id){
					thingService.findOne(id, function(thing){
						render(thing, gateways);
					});
				}else{
					render(null, gateways);
				}
			});
		},
    	error: errorCallback
	});
});
