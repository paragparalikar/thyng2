var thingService = {
	findAll : function(success, error, always) {
		$.get(window.location.origin + "/api/v1/things")
		.done(successCallback(success)).fail(error ? error : errorCallback).always(always);
	},
	findOne : function(id, success, error, always) {
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
    $("#template-container").loadTemplate("list-things", null, {
    	success: function(){
    		render();
    	},
    	error: errorCallback
    });
});
$.router.add("#things/view/:id", function(params){
	var id = arguments[0];
    $("#template-container").loadTemplate("view-thing", null, {
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
	$("#template-container").loadTemplate("edit-thing", null, {
		success : function(){
			thingService.findOne(id, function(thing){
				render(thing);
			});
		},
    	error: errorCallback
	});
});

$.router.add("#things/create", function(params){
	var id = arguments[0];
	$("#template-container").loadTemplate("edit-thing", null, {
		success : function(){
			render({metrics:[]});
		},
    	error: errorCallback
	});
});

